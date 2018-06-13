package com.Util;

import android.net.ParseException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by masskywcy on 2016-12-29.
 */

public class Timeuti {

    public static String getTime() {
        SimpleDateFormat foo = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        System.out.println("foo:" + foo.format(new Date()));

        Calendar gc = GregorianCalendar.getInstance();
        System.out.println("gc.getTime():" + gc.getTime());
        System.out.println("gc.getTimeInMillis():" + new Date(gc.getTimeInMillis()));

        //当前系统默认时区的时间：
        Calendar calendar = new GregorianCalendar();
        System.out.print("时区：" + calendar.getTimeZone().getID() + "  ");
        System.out.println("时间：" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
        //美国洛杉矶时区
        TimeZone tz = TimeZone.getTimeZone("Beijing");
        //时区转换
        calendar.setTimeZone(tz);
        System.out.print("时区：" + calendar.getTimeZone().getID() + "  ");
        System.out.println("时间：" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
        Date time = new Date();

        //1、取得本地时间：
        java.util.Calendar cal = java.util.Calendar.getInstance();

        //2、取得时间偏移量：
        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);

        //3、取得夏令时差：
        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);

        //4、从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));

        //之后调用cal.get(int x)或cal.getTimeInMillis()方法所取得的时间即是UTC标准时间。
        System.out.println("UTC:" + new Date(cal.getTimeInMillis()));

        Calendar calendar1 = Calendar.getInstance();
        TimeZone tztz = TimeZone.getTimeZone("GMT");
        calendar1.setTimeZone(tztz);
        System.out.println(calendar.getTime());
        System.out.println(calendar.getTimeInMillis());
        return calendar.getTimeInMillis() + "";
    }

    /**
     * 获取UTC时间
     *
     * @return
     */
    public static String getUTCTimeStr() {
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd/hh/mm/ss");
        StringBuffer UTCTimeBuffer = new StringBuffer();
        // 1、取得本地时间：
        Calendar cal = Calendar.getInstance();
        // 2、取得时间偏移量：
        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
        // 3、取得夏令时差：
        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
        // 4、从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        UTCTimeBuffer.append(year).append("/").append(month).append("/")
                .append(day);
        UTCTimeBuffer.append("/").append(hour).append("/").append(minute)
                .append("/").append(second);
        try {
            format.parse(UTCTimeBuffer.toString());
            return UTCTimeBuffer.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取时间戳
     *
     * @param dateCur
     * @return
     */
    public static long GetTicks(String dateCur) {
        String[] ds = dateCur.split("/");
        // start of the ticks time
        Calendar calStart = Calendar.getInstance();

        /**
         * 此处的参数很重要，原则上都是1，日所以为2，是因为之前的日期没减掉1 第三个参数为1：日期多了2天，为2则日期多1天
         * **/
        //上传失败时这里总会出现混乱的情况，需要找到源头解决
        calStart.set(1, 1, 3, 0, 0, 0);

        // the target time
        Calendar calEnd = Calendar.getInstance();
        calEnd.set(Integer.parseInt(ds[0]), Integer.parseInt(ds[1]),
                Integer.parseInt(ds[2]), Integer.parseInt(ds[3]),
                Integer.parseInt(ds[4]), Integer.parseInt(ds[5]));

        // epoch time of the ticks-start time
        long epochStart = calStart.getTime().getTime();
        // epoch time of the target time
        long epochEnd = calEnd.getTime().getTime();


        // get the sum of epoch time, from the target time to the ticks-start
        // time
        long all = epochEnd - epochStart;
        // convert epoch time to ticks time
        long ticks = ((all / 1000) * 1000000) * 10;

        return ticks;
    }
}
