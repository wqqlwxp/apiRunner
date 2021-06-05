package com.runner.server.service.serviceImpl;


import com.alibaba.fastjson.JSON;
import com.runner.server.dao.entity.bo.MsgResponseObject;
import com.runner.server.dao.entity.bo.PageObject;
import com.runner.server.dao.entity.po.Cipher;
import com.runner.server.dao.entity.po.DatabaseConfig;
import com.runner.server.dao.mapper.DataBaseMapper;
import com.runner.server.dao.mapper.DynamicMapper;
import com.runner.server.service.utils.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.InvocationTargetException;
import java.util.*;


@Service
public class DataBaseService {


    @javax.annotation.Resource
    public DataBaseMapper dataBaseMapper;


    private MsgResponseObject msgReponseObject=new MsgResponseObject() ;


    /**
     * @description 查询数据
     * @author 星空梦语
     * @date 2020/4/11 11:37
     */
    public String query(PageObject pageObject) throws InvocationTargetException, IllegalAccessException {
        DatabaseConfig dataBaseConfig=new DatabaseConfig();
        BeanUtils.populate(dataBaseConfig, pageObject.getData());
        int offset=(pageObject.getOffset()-1)*pageObject.getLimit();
        List<DatabaseConfig> list=dataBaseMapper.queryPageData(offset,pageObject.getLimit(),dataBaseConfig);
        int total=dataBaseMapper.queryCountByExample(dataBaseConfig);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("total",total);
        map.put("rows",list);
        return JSON.toJSONString(map);
    }


    /**
     * @description 新增或修改数据
     * @author 星空梦语
     * @date 2020/4/11 12:14
     */
    public MsgResponseObject edit(PageObject pageObject) throws Exception {
        DatabaseConfig dataBaseConfig=new DatabaseConfig();
        BeanUtils.populate(dataBaseConfig, pageObject.getData());
        msgReponseObject.setCode(201);
        if(StringUtils.isBlank(dataBaseConfig.getModuleCode()) ||StringUtils.isBlank(dataBaseConfig.getProjectCode())  ){
            msgReponseObject.setMsg("编码或名称不能为空，请确认");
            return  msgReponseObject;
        }



        if(dataBaseConfig.getId()!=null){
            dataBaseConfig.setModuleName(null);
            dataBaseConfig.setProjectName(null);
            Example example=new Example(Cipher.class);
            example.createCriteria().andEqualTo("id",dataBaseConfig.getId());
            msgReponseObject.setMsg("修改失败");
            if(dataBaseMapper.updateByExampleSelective(dataBaseConfig,example)>0){
                msgReponseObject.setMsg("修改成功");
                msgReponseObject.setCode(200);
            }
        }else{
            dataBaseConfig.setCreateTime(DateUtil.formatToDayByDate3(new Date()));
            if(dataBaseMapper.insertSelective(dataBaseConfig)>0){
                msgReponseObject.setMsg("新增成功");
                msgReponseObject.setCode(200);
            }else{
                msgReponseObject.setMsg("新增失败");
            }
        }
        return  msgReponseObject;
    }

    
    /**
     * @description 
     * @author 星空梦语
     * @date 2021/2/23 上午10:10
     */
    public MsgResponseObject delete(int id) throws Exception {
        Example example=new Example(DatabaseConfig.class);
        example.createCriteria().andEqualTo("id",id);
        msgReponseObject.setMsg("删除失败");
        if(dataBaseMapper.deleteByExample(example)>0){
            msgReponseObject.setMsg("删除成功");
            msgReponseObject.setCode(200);
        }
        return msgReponseObject;
    }





    public SqlSessionFactory getSqlSessionFactory(String url,String user,String pwd) throws Exception {
        PooledDataSource dataSource = new PooledDataSource();
        dataSource.setDriver("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(pwd);
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        //配置mapper路径
        Resource[] resources = resolver.getResources("classpath:/mapperXml/DynamicMapper.xml");
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setMapperLocations(resources);
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setConfiguration(new Configuration());
        return sqlSessionFactoryBean.getObject();
    }


    public HashMap<String, Object> executeQuery(String sql,String url,String user,String pwd) throws Exception {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory(url,user,pwd);
        SqlSession sqlsession = sqlSessionFactory.openSession();
        DynamicMapper dynamicMapper = sqlsession.getMapper(DynamicMapper.class);
        HashMap<String, Object> result = dynamicMapper.query(sql).get(0);
        System.out.println("sql查询结果："+JSON.toJSONString(result));
        sqlsession.close();
        return  result;
    }


    /**
     * @description 根据字段查找值
     * @author 星空梦语
     * @date 2021/6/5 下午6:23
     */
    public String getFieldValue(HashMap<String, Object> map,String VerifField){
        Set<Map.Entry<String,Object>> entrySet = map.entrySet();
        for(Map.Entry<String,Object> e:entrySet) {
            if(e.getKey().equals(VerifField)){
                return e.getValue().toString();
            }
        }
        return null;
    }











    /**
     * @description  获取缓存
     * @author 星空梦语
     * @date 2020/4/19 11:37
     */
    public List<DatabaseConfig> getDatabaseCache(){
        return (List<DatabaseConfig>) CacheUtil.getCacheObject("database");
    }


    /**
     * @description  更新缓存
     * @author 星空梦语
     * @date 2020/4/19 11:41
     */
    public void updateDatabaseCache(){
        LogUtil.info("开始更新加解密缓存");
        List<DatabaseConfig> cipherList= dataBaseMapper.queryAll();
        CacheUtil.setCacheObject("database",cipherList,0L);
    }








}
