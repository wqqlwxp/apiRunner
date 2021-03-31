package com.runner.server.service.serviceImpl;

import com.alibaba.dubbo.common.URL;
import com.alibaba.fastjson.JSON;
import com.runner.server.dao.entity.bo.AppRequest;
import com.runner.server.dao.entity.bo.MsgResponseObject;
import com.runner.server.dao.entity.bo.PageObject;
import com.runner.server.dao.entity.bo.Params;
import com.runner.server.dao.entity.po.ZkConfig;
import com.runner.server.dao.entity.po.ZkData;
import com.runner.server.dao.mapper.ZkDataMapper;
import com.runner.server.dao.mapper.ZkMapper;
import com.runner.server.service.utils.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ZkService implements Watcher {

    private RedisTemplate redisTemplate;

    @Resource
    private ZkMapper zkMapper;

    @Resource
    private ZkDataMapper zkDataMapper;

    private MsgResponseObject msgReponseObject = new MsgResponseObject();

    private ZooKeeper zookeeper;

    @Value("${nexus-address}")
    private String repository;

    @Value("${nexus-target}")
    private String target;

    @Value("${open_ssh}")
    private  boolean openSSH;

    @Resource
    private JarService jarService;


    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {   //防止key出现乱码
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        this.redisTemplate = redisTemplate;
    }


    /**
     * @description 查询配置数据
     * @author 星空梦语
     * @date 2020/4/11 11:37
     */
    public String query(PageObject pageObject) throws InvocationTargetException, IllegalAccessException {
        ZkConfig zkConfig = new ZkConfig();
        BeanUtils.populate(zkConfig, pageObject.getData());
        int offset = (pageObject.getOffset() - 1) * pageObject.getLimit();
        List<ZkConfig> list = zkMapper.queryPageData(offset, pageObject.getLimit(), zkConfig);
        int total = zkMapper.queryCountByExample(zkConfig);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", total);
        map.put("rows", list);
        return JSON.toJSONString(map);
    }


    /**
     * @description 查询服务数据
     * @author 星空梦语
     * @date 2020/4/11 11:37
     */
    public String queryData(PageObject pageObject) throws InvocationTargetException, IllegalAccessException {
        ZkData zkData = new ZkData();
        BeanUtils.populate(zkData, pageObject.getData());
        int offset = (pageObject.getOffset() - 1) * pageObject.getLimit();
        List<ZkData> list = zkDataMapper.queryPageData(offset, pageObject.getLimit(), zkData);
        int total = zkDataMapper.queryCountByExample(zkData);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", total);
        map.put("rows", list);
        return JSON.toJSONString(map);
    }


    /**
     * @description 新增或修改配置数据
     * @author 星空梦语
     * @date 2020/4/11 12:14
     */
    public MsgResponseObject edit(PageObject pageObject) throws Exception {
        ZkConfig zkConfig = new ZkConfig();
        BeanUtils.populate(zkConfig, pageObject.getData());
        msgReponseObject.setCode(201);
        if (StringUtils.isBlank(zkConfig.getZkIp()) || StringUtils.isBlank(zkConfig.getZkAlias())) {
            msgReponseObject.setMsg("数据不能为空，请确认");
            return msgReponseObject;
        }

        Example example = new Example(ZkConfig.class);
        example.createCriteria().andEqualTo("zkIp", zkConfig.getZkIp());
        int total = zkMapper.selectByExample(example).size();
        if (total > 0) {
            msgReponseObject.setMsg("zk地址已经存在，请不要重复添加");
            return msgReponseObject;
        }
        if (zkConfig.getId() == null) {
            zkConfig.setCreateTime(DateUtil.formatToDayByDate3(new Date()));
            if (zkMapper.insertSelective(zkConfig) > 0) {
                Example example1 = new Example(ZkConfig.class);
                example1.createCriteria().andEqualTo("zkIp", zkConfig.getZkIp());
                zkConfig = zkMapper.selectByExample(example1).get(0);
                msgReponseObject = initZkServiceData(zkConfig.getZkIp(), zkConfig.getId());
            } else {
                msgReponseObject.setMsg("新增失败");
            }
        }
        return msgReponseObject;
    }


    /**
     * @description
     * @author 星空梦语
     * @date 2021/2/23 上午10:10
     */
    public MsgResponseObject delete(int id) throws Exception {
        Example example = new Example(ZkConfig.class);
        example.createCriteria().andEqualTo("id", id);
        msgReponseObject.setMsg("删除失败");
        if (zkMapper.deleteByExample(example) > 0) {
            //删除对应明细数据
            Example example1 = new Example(ZkData.class);
            example1.createCriteria().andEqualTo("zkId", id);
            zkDataMapper.deleteByExample(example1);
            msgReponseObject.setMsg("删除成功");
            msgReponseObject.setCode(200);
        }
        return msgReponseObject;
    }


    /**
     * @description 获取redis数据
     * @author 星空梦语
     * @date 2021/3/23 上午10:55
     */
    public String getAppsByZk(String zkAddress) {
        return (String) redisTemplate.opsForValue().get(zkAddress + "-apps");
    }



    /**
     * @description  根据zk查询应用
     * @author 星空梦语
     * @date 2021/3/29 上午9:43
     */
    public String queryAllAppByZkAddress(int zkId){
        ZkData zkData=new ZkData();
        zkData.setZkId(zkId);
        List<ZkData> zkDataList=zkDataMapper.queryPageData(0,1000,zkData);
        List<String> datas=zkDataList.stream().map(ZkData::getAppName).collect(Collectors.toList());
        datas= datas.stream().distinct().collect(Collectors.toList());
        return JSON.toJSONString(datas);
    }


    /**
     * @description 根据应用查询接口
     * @author 星空梦语
     * @date 2021/3/29 上午9:43
     */
    public String queryAllInterfaceByZkApp(ZkData zkData){
        List<ZkData> zkDataList=zkDataMapper.queryPageData(0,1000,zkData);
        List<String> datas=zkDataList.stream().map(ZkData::getInterfaceName).collect(Collectors.toList());
        datas= datas.stream().distinct().collect(Collectors.toList());
        return JSON.toJSONString(datas);
    }





    /**
     * @description 根据接口查询方法
     * @author 星空梦语
     * @date 2021/3/29 上午9:43
     */
    public String queryAllMethodByInterface(ZkData zkData){
        List<ZkData> zkDataList=zkDataMapper.queryPageData(0,1000,zkData);
        List<String> datas=zkDataList.stream().map(ZkData::getMethodName).collect(Collectors.toList());
        return JSON.toJSONString(datas);
    }


    /**
     * @description 根据方法查询明细
     * @author 星空梦语
     * @date 2021/3/29 上午9:43
     */
    public String queryMethodDetail(ZkData zkData){
        ZkData zkData1=zkDataMapper.queryPageData(0,1000,zkData).get(0);
        return JSON.toJSONString(zkData1);
    }



    /**
     * @description 创建本地服务
     * @author 星空梦语
     * @date 2021/3/23 下午3:35
     */
    public MsgResponseObject createApp(AppRequest appRequest)  {
        MsgResponseObject msgResponseObject = new MsgResponseObject();
        msgResponseObject.setCode(200);
        Map<String, String> map = XmlUtil.parseDependencyXml(appRequest.getDependency());
        if(map==null){
            msgResponseObject.setCode(201);
            msgResponseObject.setMsg("maven依赖不能为空");
            return msgResponseObject;
        }
        Params params = new Params(map.get("groupId"), map.get("artifactId"), map.get("version"));
        String repo = repository;
        if (map.get("version").endsWith("SNAPSHOT")) {
            repo = repo + "maven-snapshots";
        } else {
            repo = repo + "maven-releases";
        }
        params.setRepository(repo);
        params.setTarget(target + File.separator + appRequest.getAppName());
        try {
            DependencyUtil.downLoadMavenJar(params);
        } catch (Exception e) {
            msgResponseObject.setCode(201);
            msgResponseObject.setMsg("下载依赖maven jar异常，" + e.getMessage());
            e.printStackTrace();
            return msgResponseObject;
        }
        String[] group = params.getGroupId().split("\\.");
        String groupPath = "";
        for (String data : group) {
            groupPath = groupPath + data + File.separator;
        }
        String jarPath = target + appRequest.getAppName() + File.separator ;
        File jarFile=new File(jarPath);
        List<java.net.URL> urlList = new ArrayList<>();
        urlList=getAllChildFile(jarFile,urlList);
        List<ZkData> zkDataList = jarService.paresJar(appRequest, urlList, params);
        ZkData zkData = new ZkData();
        zkData.setZkId(appRequest.getZkId());
        zkData.setAppName(appRequest.getAppName());
        zkDataMapper.delete(zkData);
        if(zkDataList.size()>0){
            zkDataMapper.insertBatchZkData(zkDataList);
        }
        msgResponseObject.setMsg("本地服务创建成功");
        return msgResponseObject;
    }


    /**
     * @description 刷新服务提供者数据
     * @author 星空梦语
     * @date 2021/3/26 下午5:28
     */
    public MsgResponseObject refresh(AppRequest appRequest){
        MsgResponseObject msgResponseObject = new MsgResponseObject();
        msgResponseObject.setCode(200);
        msgResponseObject.setMsg("zk服务刷新成功");
        initZkServiceData(appRequest.getZkAddress(),appRequest.getZkId());
        return msgResponseObject;
    }





    /**
     * @description 创建zk后初始化数据
     * @author 星空梦语
     * @date 2021/3/22 下午6:13
     */
    public MsgResponseObject initZkServiceData(String zkAddress, int zkId) {
        LogUtil.info("开始初始化zk服务数据，初始化zk："+zkAddress);
        String pre = "/dubbo";
        msgReponseObject.setMsg("zk数据初始化成功");
        msgReponseObject.setCode(200);
        try {
            ZookeeperUtil.connectZookeeper(zkAddress);
            List<String> interfaces = ZookeeperUtil.getChildren(pre);
            List<ZkData> list = new ArrayList<>();
            for (String interfaceName : interfaces) {
                List<String> childrens = ZookeeperUtil.getChildren(pre + File.separator + interfaceName + "/providers");
                if (childrens.size() == 0) {
                    continue;
                }
                String providers = URL.decode(childrens.get(0));
                URL dubboUrl = URL.valueOf(providers);
                String app = dubboUrl.getParameter("application");
                String host = dubboUrl.getHost();
                int port = dubboUrl.getPort();
                String addr = host + ":" + port;
                String version = dubboUrl.getParameter("version");
                String methods = dubboUrl.getParameter("methods");
                String group = dubboUrl.getParameter("group");
                String[] methodArray = methods.split(",");
                for (String method : methodArray) {
                    ZkData zkData = new ZkData();
                    zkData.setInterfaceName(interfaceName);
                    zkData.setZkId(zkId);
                    zkData.setGroup(group);
                    zkData.setAddress(addr);
                    zkData.setMethodName(method);
                    zkData.setVersion(version);
                    zkData.setAppName(app);
                    zkData.setCreateTime(DateUtil.formatToDayByDate3(new Date()));
                    list.add(zkData);
                }
            }
            Map<String, List<ZkData>> data = list.stream().collect(Collectors.groupingBy(ZkData::getAppName));
            redisTemplate.opsForValue().set(zkAddress, JSON.toJSONString(data));
            Set<String> keys = data.keySet();
            redisTemplate.opsForValue().set(zkAddress + "-apps", JSON.toJSONString(keys));
            LogUtil.info("设置zk服务redis缓存成功");
        } catch (Exception e) {
            e.printStackTrace();
            msgReponseObject.setCode(201);
            msgReponseObject.setMsg("zk数据初始化失败：");
        }
        return msgReponseObject;
    }

    /**
     * @description  服务重启时，判断缓存是否存在
     * @author 星空梦语
     * @date 2021/3/26 下午2:51
     */
    public  void initCheck(){
        LogUtil.info("初始化检查zk服务数据是否存在->>>>>");
        List<ZkConfig>  zkConfigList=zkMapper.queryAll();
        SSHChannelUtil.connectSSHForward(openSSH);    //代理连接
        zkConfigList.forEach(item->{
            String zk= (String) redisTemplate.opsForValue().get(item.getZkIp());
            String app= (String) redisTemplate.opsForValue().get(item.getZkIp()+ "-apps");
            if(StringUtils.isBlank(zk) || StringUtils.isBlank(app)){
                initZkServiceData(item.getZkIp(),item.getId());
            }
        });
    }



    /**
     * @description 递归查询应用下载目录下的所有子文件
     * @author 星空梦语
     * @date 2021/3/26 下午11:02
     */
    public  List<java.net.URL> getAllChildFile(File filePath,List<java.net.URL> urlList){
        File[] files = filePath.listFiles();
        if(files == null){
            return null;
        }
        for(File f:files){
            if(f.isDirectory()){
                getAllChildFile(f,urlList);
            }else{
                if(!f.getPath().endsWith("jar")){
                    continue;
                }
                try {
                    urlList.add(f.toURI().toURL());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
        return urlList;
    }


    @Override
    public void process(WatchedEvent watchedEvent) {

    }

}
