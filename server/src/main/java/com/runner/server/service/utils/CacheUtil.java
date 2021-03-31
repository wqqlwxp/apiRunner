package com.runner.server.service.utils;


import java.util.concurrent.ConcurrentHashMap;

public class CacheUtil {

    private static  ConcurrentHashMap<String,String> cache=new ConcurrentHashMap<String,String> ();
    private static  ConcurrentHashMap<String,Object> cacheObject=new ConcurrentHashMap<String,Object> ();

    /**
     * 每个缓存生效时间12小时
     */
    public static final long CACHE_HOLD_TIME_12H =12 * 60 * 60 * 1000L;






    /**
     * @description 获取缓存key
     * @author 星空梦语
     * @date 2020/4/19 11:23
     */
    public static String getCache(String key){
        if(cache.get(key)!=null){
            return cache.get(key);
        }else{
            return "";
        }
    }






    /**
     * @description  获取缓存key
     * @author 星空梦语
     * @date 2020/4/19 11:23
     */
    public static Object getCacheObject(String key){
        LogUtil.info("查询缓存key="+key);
        return cacheObject.get(key);
    }






    /**
     * @description 移除缓存
     * @author 星空梦语
     * @date 2020/4/19 11:24
     */
    public static  void removeCache(String key){
        if(CacheUtil.getCache(key)!=null && !CacheUtil.getCache(key).equals("")){
            cache.remove(key);
        }
    }







    /**
     * @description 移除缓存
     * @author 星空梦语
     * @date 2020/4/19 11:24
     */
    public static  void removeCacheObject(String key){
        if(CacheUtil.getCache(key)!=null && !CacheUtil.getCache(key).equals("")){
            cacheObject.remove(key);
            cacheObject.remove( key + "_holdTime");
        }
    }






    /**
     * @description  设置缓存
     * @author 星空梦语
     * @date 2020/4/19 11:25
     */
    public static void setCacheObject(String key, Object obj,long holdTime)
    {
        removeCacheObject(key);
        cacheObject.put(key,obj);
        if(holdTime!=0L){
            cacheObject.put(key + "_holdTime", System.currentTimeMillis() + holdTime);//缓存失效时间
        }
    }

    /**
     * 检查缓存对象是否存在，
     * 若不存在，则返回false
     * 若存在，检查其是否已过有效期，如果已经过了则删除该缓存并返回false
     *
     * @param cacheName
     * @return
     */
    public static boolean checkCacheName(String cacheName) {
        Long cacheHoldTime = (Long) cacheObject.get(cacheName + "_holdTime");
        if (cacheHoldTime == null || cacheHoldTime == 0L) {
            return false;
        }
        if (cacheHoldTime < System.currentTimeMillis()) {
            removeCacheObject(cacheName);
            return false;
        }
        return true;
    }

}
