package com.runner.server.service.serviceImpl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.JsonPath;
import com.runner.server.dao.entity.bo.*;
import com.runner.server.dao.entity.po.CaseLog;
import com.runner.server.dao.entity.po.Cipher;
import com.runner.server.dao.entity.po.TestCase;
import com.runner.server.dao.entity.po.TestPlan;
import com.runner.server.dao.mapper.CaseMapper;
import com.runner.server.dao.mapper.CipherMapper;
import com.runner.server.dao.mapper.TestPlanMapper;
import com.runner.server.service.utils.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class CipherService {


    @Resource
    public CipherMapper cipherMapper;



    private MsgResponseObject msgReponseObject=new MsgResponseObject() ;


    /**
     * @description 查询数据
     * @author 星空梦语
     * @date 2020/4/11 11:37
     */
    public String query(PageObject pageObject) throws InvocationTargetException, IllegalAccessException {
        Cipher cipher=new Cipher();
        BeanUtils.populate(cipher, pageObject.getData());
        int offset=(pageObject.getOffset()-1)*pageObject.getLimit();
        List<Cipher> list=cipherMapper.queryPageData(offset,pageObject.getLimit(),cipher);
        int total=cipherMapper.queryCountByExample(cipher);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("total",total);
        map.put("rows",list);
        return JSON.toJSONString(map);
    }


    /**
     * @description 新增或修改计划数据
     * @author 星空梦语
     * @date 2020/4/11 12:14
     */
    public MsgResponseObject edit(PageObject pageObject) throws Exception {
        Cipher cipher=new Cipher();
        BeanUtils.populate(cipher, pageObject.getData());
        msgReponseObject.setCode(201);
        if(StringUtils.isBlank(cipher.getModuleCode()) ||StringUtils.isBlank(cipher.getProjectCode())  ){
            msgReponseObject.setMsg("编码或名称不能为空，请确认");
            return  msgReponseObject;
        }



        if(cipher.getId()!=null){
            cipher.setModuleName(null);
            cipher.setProjectName(null);
            Example example=new Example(Cipher.class);
            example.createCriteria().andEqualTo("id",cipher.getId());
            msgReponseObject.setMsg("修改失败");
            if(cipherMapper.updateByExampleSelective(cipher,example)>0){
                msgReponseObject.setMsg("修改成功");
                msgReponseObject.setCode(200);
                updateCipherCache();
            }
        }else{
            cipher.setCreateTime(DateUtil.formatToDayByDate3(new Date()));
            if(cipherMapper.insertSelective(cipher)>0){
                msgReponseObject.setMsg("新增成功");
                msgReponseObject.setCode(200);
                updateCipherCache();
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
        Example example=new Example(Cipher.class);
        example.createCriteria().andEqualTo("id",id);
        msgReponseObject.setMsg("删除失败");
        if(cipherMapper.deleteByExample(example)>0){
            msgReponseObject.setMsg("删除成功");
            msgReponseObject.setCode(200);
            updateCipherCache();
        }
        return msgReponseObject;
    }



    /**
     * @description  获取缓存
     * @author 星空梦语
     * @date 2020/4/19 11:37
     */
    public List<Cipher> getCipherCache(){
        return (List<Cipher>) CacheUtil.getCacheObject("cipher");
    }


    /**
     * @description  更新缓存
     * @author 星空梦语
     * @date 2020/4/19 11:41
     */
    public void updateCipherCache(){
        LogUtil.info("开始更新加解密缓存");
        List<Cipher> cipherList= cipherMapper.queryAll();
        CacheUtil.setCacheObject("cipher",cipherList,0L);
    }




    /**
     * @description 解密
     * @author 星空梦语
     * @date 2021/2/28 下午2:20
     */
    public  String decrypt(String projectCode,String moduleCode,String text) throws Exception {
        LogUtil.info("用例接口响应解密开始：密文="+text);
        List<Cipher>cipherList=new ArrayList<Cipher>();
        for(Cipher cip:getCipherCache()){
            if(cip.getProjectCode().equals(projectCode) && cip.getModuleCode().equals(moduleCode) && cip.getStatus().equals("0")){
                cipherList.add(cip);
            }
        }
        String decryptText=parseCipherPathValue(cipherList,text); //

        //解密顺序和加密顺序相反
        cipherList=cipherList.stream().sorted(Comparator.comparing(Cipher::getExecuteOrder).reversed()).collect(Collectors.toList());
        try{
            for(Cipher cipher1:cipherList){
                if(!cipher1.getResponseDecrypt().equals("0")){
                    continue;      //响应配置不解密时，跳过
                }
                switch (cipher1.getType()){
                    case "MD5":
                        LogUtil.err("MD5无需进行解密");
                        break;
                    case "AES":
                        decryptText= AesUtil.decrypt(decryptText,cipher1.getKeyData(),cipher1.getIv());
                        break;
                    case "DES":
                        decryptText= DesUtil.decrypt(decryptText,cipher1.getKeyData());
                        break;
                    case "BASE64":
                        decryptText= Base64Util.decrypt(decryptText);
                        break;
                }
            }
        }catch(Exception e){
            return  "项目："+projectCode+" 模块："+moduleCode+",参数解密异常，异常原因："+e.getMessage();
        }
        LogUtil.info("用例接口响应解密结束："+decryptText);
        return  decryptText;
    }


    /**
     * @description 加密
     * @author 星空梦语
     * @date 2021/2/28 下午2:20
     */
    public  String encrypt(String projectCode,String moduleCode,String text) throws Exception {
        List<Cipher>cipherList=new ArrayList<Cipher>();
        for(Cipher cip:getCipherCache()){
            if(cip.getProjectCode().equals(projectCode) && cip.getModuleCode().equals(moduleCode) && cip.getStatus().equals("0")){
                cipherList.add(cip);
            }
        }
        String encryptText=text;
        //加密按照执行顺序从小到大执行
        cipherList=cipherList.stream().sorted(Comparator.comparing(Cipher::getExecuteOrder)).collect(Collectors.toList());
        try{
            for(Cipher cipher1:cipherList){
                if(!cipher1.getRequestEncrypt().equals("0")){   //请求配置不加密时，跳过
                    continue;
                }
                switch (cipher1.getType()){
                    case "MD5":
                        encryptText=Md5Util.sign(encryptText,cipher1.getKeyData());
                        break;
                    case "AES":
                        encryptText= AesUtil.encrypt(encryptText,cipher1.getKeyData(),cipher1.getIv());
                        break;
                    case "DES":
                        encryptText= DesUtil.encrypt(encryptText,cipher1.getKeyData());
                        break;
                    case "BASE64":
                        encryptText= Base64Util.encrypt(encryptText);
                        break;
                }
            }
        }catch(Exception e){
            return  "项目："+projectCode+" 模块："+moduleCode+",参数加密异常，异常原因："+e.getMessage();
        }

        return  encryptText;
    }



    /**
     * @description 解析响应体中的密文数据，例如响应体是json格式，加密数据是其中的某一个字段值
     * @author 星空梦语
     * @date 2021/2/28 下午8:41
     */
    public String parseCipherPathValue(List<Cipher> cipherList,String text){
        if(cipherList.size()==0){
            return  text;
        }
        return JsonUtil.jsonPathRead(text,cipherList.get(0).getCipherPath());
    }


}
