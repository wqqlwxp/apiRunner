package com.runner.server.service.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {
 
    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:07
     */
    public static Date tranStrToDateByYYYYMMDDHHMMSS(String dataStr) throws Exception {
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return format.parse(dataStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:07
     */

    public static Date formatToDateByCalendar(Calendar date) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        return format.parse(date.getTime().toString());
    }


  
    
    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:07
     */
    public static Date tranStrToDateByYYYYMMDD(String dataStr) throws Exception {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(dataStr);
    }

    
    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:07
     */
    public static Date tranStrToDateByYYYYMMDD2(String dataStr) throws Exception {
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.parse(dataStr);
    }

    
    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:07
     */
    public static Date tranStrToDateByYYYYMMDD3(String dataStr) throws Exception {
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        return format.parse(dataStr);
    }



    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:07
     */
    public static String getTimeToNanos(String dateTime) throws Exception {
        Calendar c = Calendar.getInstance();
        c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime));
        Long curNanos = Math.abs(System.nanoTime());
        String nanos = curNanos.toString();
        return c.getTimeInMillis() + nanos.substring(nanos.length() - 6);
    }

   
    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:07
     */
    public static String getDateStrByTimeMillis(long dateTime) throws Exception {
        Date date = new Date(dateTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }


   
    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:07
     */
    public static String dealJiraDateFormat(String oldDateStr) throws Exception {
        if (oldDateStr != null && !oldDateStr.equals("null")) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = df.parse(oldDateStr);
            DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
            return df2.format(date);
        } else {
            return "";
        }
    }


  
    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:07
     */
    public static String formatToDayByDate(Date date) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }





    
    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:07
     */
    public static String formatToDayByDate2(Date date) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(date);
    }


    
    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:07
     */
    public static String formatToDayByDate3(Date date) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }



    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:08
     */
    public static String FormatDateByPayTime(String payTime) {
        return payTime.substring(0, 10);
    }


 
    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:08
     */
    public static int daysBetween(Date smdate, Date bdate) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        long between_days = (bdate.getTime() - smdate.getTime()) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }


   
    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:08
     */
    public static String getCurDate() throws Exception {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }


  
    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:08
     */
    public static String getCurTime() throws Exception {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:08
     */
    public static Date getCurTimeDate() throws Exception {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return new Date();
    }


    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:08
     */
    public static long getDiffTimeZoneRawOffset(String timeZoneId, long time) throws Exception {
        long diffTime = TimeZone.getDefault().getRawOffset() - TimeZone.getTimeZone(timeZoneId).getRawOffset();
        long curTime = System.currentTimeMillis();
        return time - curTime + diffTime;
    }

  
    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:08
     */
    public static Long getEndTime() throws Exception {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.AM_PM, 0);
        todayEnd.set(Calendar.HOUR, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime().getTime();
    }

  
    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:08
     */
    public static String addDays(String dateTime, int days) throws Exception {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar date = Calendar.getInstance();
            date.setTime(sdf.parse(dateTime));
            date.set(Calendar.DATE, date.get(Calendar.DATE) + days);
            return sdf.format(date.getTime());
        } catch (Exception e) {
            return dateTime;
        }
    }

 
    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:08
     */
    public static String addSecondes(String dateTime, int secondes) throws Exception {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar date = Calendar.getInstance();
            date.setTime(sdf.parse(dateTime));
            date.set(Calendar.SECOND, date.get(Calendar.SECOND) + secondes);
            return sdf.format(date.getTime());
        } catch (Exception e) {
            return dateTime;
        }
    }




    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:08
     */
    public static String timestampFormat(String time) {
        String yyyy = time.substring(0, 4);
        String mm = time.substring(4, 6);
        String dd = time.substring(6, 8);
        String hh = time.substring(8, 10);
        String min = time.substring(10, 12);
        String ss = time.substring(12, 14);
        return yyyy + "-" + mm + "-" + dd + " " + hh + ":" + min + ":" + ss;
    }

   
    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:08
     */
    public static Date getDateDaysBefore(Date d, int day) throws Exception {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.add(Calendar.DATE, -day);
        return now.getTime();
    }

  
    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:08
     */
    public static Date getDateDaysAfter(Date d, int day) throws Exception {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.add(Calendar.DATE, day);
        return now.getTime();
    }

 
    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:09
     */
    public static Date getDateMonthsBefore(Date d, int month) throws Exception {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.add(Calendar.MONTH, -month);
        return now.getTime();
    }

  
    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:09
     */
    public static Date getDateMonthsAfter(Date d, int month) throws Exception {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.add(Calendar.MONTH, month);
        return now.getTime();
    }


   
    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:09
     */
    public static Date addMonth(Date date, int month) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, month);// 增加一个月
        return c.getTime();
    }


  
    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:09
     */
    public static Date getFirstDayByWeek() throws Exception {
        LocalDate today = LocalDate.now();
        return tranStrToDateByYYYYMMDD(today.with(DayOfWeek.MONDAY).toString());
    }


   
    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:09
     */
    public static String DateFormatStr(String date) throws ParseException {
        SimpleDateFormat formatter=new SimpleDateFormat("yyyyMMdd");
        Date newDate= formatter.parse(date);
        formatter=new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(newDate);
    }






 
    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:09
     */
    public static String dateToString(Date time) {
        try {
            SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return formater.format(time);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    
    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:09
     */
    public static String getWebsiteDatetime(){
        try {
            URL url = new URL("http://www.ntsc.ac.cn/");// 取得资源对象
            URLConnection uc = url.openConnection();// 生成连接对象
            uc.connect();// 发出连接
            long ld = uc.getDate();// 读取网站日期时间
            Date date = new Date(ld);// 转换为标准时间对象
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);// 输出北京时间
            return sdf.format(date);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


  
    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:09
     */
    public static int dateDifference(String startDate, String endDate) {
        Date d1 = conversionToDate(startDate, "yyyy-MM-dd HH:mm:ss");
        Date d2 = conversionToDate(endDate, "yyyy-MM-dd HH:mm:ss");
        long dateDifference = d2.getTime() - d1.getTime();
        long nd = 1000;
        return (int) (dateDifference / nd);
    }

   
    /**
     * @description 
     * @author 星空梦语
     * @date 2021/3/9 下午4:12
     */
    public static Date conversionToDate(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date d = null;
        try {
            d = sdf.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }







}