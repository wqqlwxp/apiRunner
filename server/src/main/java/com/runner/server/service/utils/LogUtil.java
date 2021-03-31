package com.runner.server.service.utils;


import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtil {

    /**
     * @description
     * @author 星空梦语
     * @date 2021/3/9 下午4:04
     */
     static PrintStream  myStream = new PrintStream(System.out) {
        @Override
        public  void println(String x) {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            super.println(sdf.format(date) + "---->" + x);
        }
    };


    static{System.setOut(myStream);}


    /**
     * @description
     * @author 星空梦语
     * @date 2021/3/9 下午4:04
     */
    public static void info(String str){
        System.out.println(str);
    }

    /**
     * @description
     * @author 星空梦语
     * @date 2021/3/9 下午4:04
     */
    public static void err(String str){
        System.err.println(str);
    }





}
