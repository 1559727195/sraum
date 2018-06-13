package com.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dialog.ToastUtils;

/**
 * Created by masskywcy on 2017-03-04.
 */
/*
 * 用于监听token是否过期*/
public class MytokenRecever extends BroadcastReceiver {
    private String action = "com.massky.jinruicenterpark.broadcast.wrongtoken";
    private Context context;

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (action.equals(intent.getAction())) {
            //获取token失败，退到登录界面

            boolean loginflag = (boolean) SharedPreferencesUtil.getData(context, "loginflag", false);
            if (loginflag) {
                ToastUtils.getInstances().showDialog("数据异常,请重新登录。");
            }
        }
    }
}
