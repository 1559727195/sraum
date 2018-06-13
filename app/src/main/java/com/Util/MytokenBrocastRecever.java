package com.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.data.User;
import com.fragment.MacFragment;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by masskywcy on 2017-03-04.
 */
/*
 * 用于监听token是否过期*/
public class MytokenBrocastRecever extends BroadcastReceiver {
    private String action = "com.massky.sraum.broadcast";
    private int expires_in;
    private int datetime;
    private int currentime;
//    public  boolean threadFlag = true;//搞一个静态的全局的threadFlag
    private Context context;
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {

            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (action.equals(intent.getAction())) {
            this.context = context;
            expires_in = Integer.parseInt(intent.getStringExtra("expires_in")) - 5;
            datetime = intent.getIntExtra("timestamp", 0);
            Thread thread = new Thread(new Runnable() {

                private boolean threadFlag = true;

                @Override
                public void run() {
                    while (threadFlag) {
                        try {
                            Thread.sleep(1000);
                            /**
                             *要执行的操作
                             */
                            Log.e("robin","robin,定时服务在跑");
                            currentime = (int) System.currentTimeMillis();//ms-》毫秒级
                            int differ = (currentime - datetime) / 1000;
                            if (differ >= expires_in) {//如果从获取togglen开始计算时间时间超过24小时，即重新去获取toggle
                                threadFlag = false;
                                SharedPreferencesUtil.saveData(context, "tokenTime", false);//不保存tokenTime
                                getToken();
                                /*
                                *  if (NetUtils.isNetworkConnected(context)) {
                                }*/
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            thread.start();
        }
    }

    private void getToken() {
        String encryPass = (String) SharedPreferencesUtil.getData(context, "loginPassword", "");
        String password = DES.decryptDES(encryPass, "12345678");
        String loginPhone = (String) SharedPreferencesUtil.getData(context, "loginPhone", "");
        Map<String, Object> map = new HashMap<>();
        String time = Timeuti.getTime();
        map.put("loginAccount", loginPhone);
        map.put("timeStamp", time);
        map.put("signature", MD5Util.md5(loginPhone + encryPass+ time));
        LogUtil.eLength("重新传入数据", new Gson().toJson(map));
        MyOkHttp.postMapObjectnest(ApiHelper.sraum_getToken, map,new MycallbackNest(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {

            }
        }, context, null) {
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                String broexpires = (String) SharedPreferencesUtil.getData(context, "expires_in", "");
                int logintime = (int) System.currentTimeMillis();
                Intent broadcast = new Intent("com.massky.sraum.broadcast");
                broadcast.putExtra("expires_in", broexpires);
                broadcast.putExtra("timestamp", logintime);
                context.sendBroadcast(broadcast);
            }

            @Override
            public void onSuccess(User user) {
                switch (user.result) {
                    case "100":
                        expires_in = Integer.parseInt(user.expires_in) - 5;
                        int logintime = (int) System.currentTimeMillis();
                        SharedPreferencesUtil.saveData(context, "expires_in", user.expires_in);
                        SharedPreferencesUtil.saveData(context, "sraumtoken", user.token);
                        SharedPreferencesUtil.saveData(context, "tokenTime", true);
                        SharedPreferencesUtil.saveData(context, "logintime", logintime);
                        processCustomMessage();
                        Intent broadcast = new Intent("com.massky.sraum.broadcast");
                        broadcast.putExtra("expires_in", user.expires_in);
                        broadcast.putExtra("timestamp", logintime);
                        context.sendBroadcast(broadcast);
                }
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });

    }

    private void processCustomMessage() {
        Intent mIntent = new Intent(MacFragment.ACTION_INTENT_RECEIVER);
        mIntent.putExtra("notifactionId", 1);
        context.sendBroadcast(mIntent);
    }
}
