package com.runner.server.service.utils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author 星空梦语
 * @desc
 * @date 2021/3/19 下午3:17
 */
public class SSHChannelUtil {
    private static Session sessionRedis,seeesionZk; //zk代理
    private static Channel channelRedis,channelZk;  //redis代理


    public static void connectSSHForward(boolean openSSH) {
        if(!openSSH){
            return;  //不走代理
        }
        if(seeesionZk==null && channelRedis==null){
            try {
                JSch jsch = new JSch();
                //登陆跳板机
                seeesionZk = jsch.getSession("xxxx", "xx.xx.xx.xx", 22);
                seeesionZk.setPassword("xxxxx");
                seeesionZk.setConfig("StrictHostKeyChecking", "no");
                seeesionZk.connect();
                //建立通道
                channelZk = seeesionZk.openChannel("session");
                channelZk.connect();
                //通过ssh连接到远程机器
                seeesionZk.setPortForwardingL("127.0.0.1",2181, "xx.xx.xx.xx", 2181);
                LogUtil.info("ssh跳板机连接zk成功.......->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(sessionRedis==null && sessionRedis==null){
            try {
                JSch jsch = new JSch();
                //登陆跳板机
                sessionRedis = jsch.getSession("xxxx", "xx.xx.xx.xx", 22);
                sessionRedis.setPassword("xxx");
                sessionRedis.setConfig("StrictHostKeyChecking", "no");
                sessionRedis.connect();
                //建立通道
                channelRedis = sessionRedis.openChannel("session");
                channelRedis.connect();
                //通过ssh连接到远程机器
                sessionRedis.setPortForwardingL("127.0.0.1",6379, "127.0.0.1", 6379);
                LogUtil.info("ssh跳板机连接redis成功.......->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
