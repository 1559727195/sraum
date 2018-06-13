package com.zhongtianli.overalls.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/5/19.
 */
public class FinishUtil {
    public static List<Activity> acticitys=new ArrayList<Activity>();

    public static void addActivity(Activity activity){
        acticitys.add(activity);
    }
    public static void deleteActivity(Activity activity){
        acticitys.remove(activity);
    }
    public static void finishActivity(){
        for (Activity activity : acticitys) {
            if(activity != null){
                activity.finish();
            }
        }
    }
}
