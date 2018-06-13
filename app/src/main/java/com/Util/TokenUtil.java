package com.Util;

import android.content.Context;

/**
 * Created by masskywcy on 2017-03-06.
 */
/*
* 用于部分本地数据获取*/
public class TokenUtil {
    /*获取本地token*/
    public static String getToken(Context context) {
        String token = (String) SharedPreferencesUtil.getData(context, "sraumtoken", "");
        return token;
    }

    /*获取网关状态*/
    public static String getBoxstatus(Context context) {
        String boxstatus = (String) SharedPreferencesUtil.getData(context, "boxstatus", "");
        return boxstatus;
    }

    /*获取网关状态 true为在线  false为离线状态*/
    public static boolean getBoxflag(Context context) {
        String boxstatus = (String) SharedPreferencesUtil.getData(context, "boxstatus", "");
        boolean boxfalg;
        if (boxstatus.trim().equals("1")) {
            boxfalg = true;
        } else {
            boxfalg = false;
        }
        return boxfalg;
    }

    /*获取网关编号*/
    public static String getBoxnumber(Context context) {
        String boxnumber = (String) SharedPreferencesUtil.getData(context, "boxnumber", "");
        return boxnumber;
    }

    /*获取本地token是否过期 如果是true是处于token有效期内*/
    public static boolean getTokenflag(Context context) {
        boolean flag = (boolean) SharedPreferencesUtil.getData(context, "tokenTime", true);
        return flag;
    }

    /*获取本地是否再次开启广播*/
    public static int getTag(Context context) {
        int tagint = (int) SharedPreferencesUtil.getData(context, "tagint", 0);
        return tagint;
    }

    public static String getPagetag(Context context) {
        String pagetag = (String) SharedPreferencesUtil.getData(context, "pagetag", "");
        return pagetag;
    }

    /*获取本地是否再次开启广播*/
    public static int getLogintime(Context context) {
        int logintime = (int) SharedPreferencesUtil.getData(context, "logintime", 0);
        return logintime;
    }
}
