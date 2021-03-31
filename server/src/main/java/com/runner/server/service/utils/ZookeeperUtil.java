package com.runner.server.service.utils;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.alibaba.fastjson.JSON;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class ZookeeperUtil {
    private  static ZooKeeper zookeeper;

    // 注册中心信息缓存
    private static Map<String, RegistryConfig> registryConfigCache = new ConcurrentHashMap<>();

    // 各个业务方的ReferenceConfig缓存
    private static Map<String, ReferenceConfig> referenceCache = new ConcurrentHashMap<>();



    public static RegistryConfig getRegistryConfig(String zkAddress){
        RegistryConfig registryConfig=registryConfigCache.get(zkAddress);
        if(registryConfig!=null){
            return  registryConfig;
        }else{
            registryConfig = new RegistryConfig();
            registryConfig.setAddress("zookeeper://"+zkAddress);
            registryConfigCache.put(zkAddress,registryConfig);
            return registryConfig;
        }
    }



    public static ReferenceConfig<GenericService> getReference(String interfaceName, String zkAddress, String version, String application, String group){
        String key=zkAddress+interfaceName;
        ReferenceConfig<GenericService>  referenceConfig=referenceCache.get(key);
        if(referenceConfig!=null){
            return  referenceConfig;
        }else{
            ApplicationConfig app = new ApplicationConfig();
            referenceConfig = new ReferenceConfig<>();
            app.setName("apiRunner");
            referenceConfig.setApplication(app);
            referenceConfig.setVersion(version);
            referenceConfig.setInterface(interfaceName);
            if(group!=null && !group.equals("default") ){
                referenceConfig.setGroup(group);
            }
            referenceConfig.setGeneric(true);
            referenceConfig.setRegistry(getRegistryConfig(zkAddress));
            referenceCache.put(key,referenceConfig);
            return referenceConfig;
        }

    }


    /**
     * @description zk连接
     * @author 星空梦语
     * @date 2021/3/17 下午6:12
     */
    public static void connectZookeeper(String host) throws Exception{
        zookeeper = new ZooKeeper(host, 3000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) { }
        });

    }


    /**
     * @description 获取子节点
     * @author 星空梦语
     * @date 2021/3/17 下午6:12
     */
    public static List<String> getChildren(String path){
        try{
            return zookeeper.getChildren(path, false);
        }catch (Exception e){
            try {
                zookeeper.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return new ArrayList<>();
    }


    /**
     * @description 获取数据
     * @author 星空梦语
     * @date 2021/3/17 下午6:12
     */
    public static String getData(String path) throws Exception{
        byte[] data = zookeeper.getData(path, false, null);
        if (data == null) {
            return "";
        }
        return new String(data);
    }


}
