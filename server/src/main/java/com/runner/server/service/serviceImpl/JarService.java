package com.runner.server.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.runner.server.dao.entity.bo.AppRequest;
import com.runner.server.dao.entity.bo.KeyObject;
import com.runner.server.dao.entity.bo.Params;
import com.runner.server.dao.entity.po.ZkData;
import com.runner.server.service.utils.LogUtil;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang.ObjectUtils.NULL;


/**
 * @author 星空梦语
 * @desc
 * @date 2021/3/23 下午6:10
 */
@Service
public class JarService {
    private RedisTemplate redisTemplate;

    private final BigDecimal amount_0=new BigDecimal("0");

    private final List<ZkData> finalList=new ArrayList();

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
     * @description 
     * @author 星空梦语
     * @date 2021/3/23 下午6:28
     */
    public List<ZkData> paresJar(AppRequest appRequest, List<java.net.URL> urlList, Params params)  {
        LogUtil.info("开始解析jar方法，解析应用="+appRequest.getAppName());
        String redisResult= (String) redisTemplate.opsForValue().get(appRequest.getZkAddress());
        Map listMap= JSON.parseObject(redisResult);
        String data=JSON.toJSONString(listMap.get(appRequest.getAppName()));
        List<ZkData> zkDataList=JSON.parseArray(data,ZkData.class);
        ClassLoader cl = null;
        try {
            cl = new URLClassLoader(urlList.toArray(new URL[]{}));
        } catch (Exception e) {
            e.printStackTrace();
            return finalList;
        }
        List<ZkData> zkDataListNew=new ArrayList<>();
        for(ZkData  zkData:zkDataList){
            Class<?> c=null;
            Method[] methods=null;
            try{
                c = cl.loadClass(zkData.getInterfaceName());//从类加载器中加载类
                methods= c.getDeclaredMethods();//从类中加载方法
            }catch(Throwable t){
                LogUtil.err("加载类或方法异常 ："+zkData.getInterfaceName());
                continue;
            }
            for(Method method:methods){
                if(!zkData.getMethodName().equals(method.getName())){
                    continue;
                }
                List<Object> typeObject=new ArrayList<>();
                List<Object> paramObject=new ArrayList<>();
                Parameter[] parameters=method.getParameters();
                for(Parameter parameter:parameters){
                    Type type=parameter.getType();
                    Boolean result= null;
                    try {
                        result = isJavaClass(cl.loadClass(type.getTypeName()));
                        typeObject.add(type.getTypeName());
                        if(!result){
                            paramObject.add(parseClassField(cl.loadClass(type.getTypeName())));
                        }else{
                            paramObject.add(getData(type));
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        continue;
                    }
                }
                zkData.setParamTypes(JSON.toJSONString(typeObject));
                zkData.setParamDatas(JSON.toJSONString(paramObject, SerializerFeature.WriteMapNullValue));
                zkDataListNew.add(zkData);
            }
        }
        return  zkDataListNew;
    }


    /**
     * @description  判断是否是系统类还是用户自定义类
     * @author 星空梦语
     * @date 2021/3/24 下午2:26
     */
    public  boolean isJavaClass(Class<?> clz) {
        return clz != null && clz.getClassLoader() == null;
    }


    /**
     * @description 对象字段赋值
     * @author 星空梦语
     * @date 2021/3/24 上午10:44
     */
    public Object parseClassField(Class<?> c){
        Object obj=null;
        Map<String,Object> map=new HashMap<>();
        try {
            obj= c.newInstance();
            Field[] fields= c.getDeclaredFields();
            for(Field field:fields){
                field.setAccessible(true);
                if (Modifier.isFinal(field.getModifiers())) continue;
                field.set(obj,getData(field.getGenericType()));
                map.put(field.getName(),getData(field.getGenericType()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }


    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/24 下午3:18
     */
    public Object getData(Type type){
        if(type==String.class || type==char.class){
            return "";
        }else if(type==Boolean.class || type==boolean.class) {
            return  false;
        }else if(type==Integer.class || type==int.class || type==short.class ||
                type==Short.class || type==byte.class || type==Byte.class) {
            return 0;
        }else if(type==Long.class || type==long.class) {
            return 0L;
        }else if(type==Float.class|| type==float.class) {
                return  0.0f;
        }else if(type==Double.class|| type==double.class) {
            return  0.0d;
        }else if( type==java.math.BigDecimal.class ) {
            return  amount_0;
        }else{
            return  null;
        }
    }
}
