package com.Util;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.data.User;
import com.fragment.MacFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zhy.http.okhttp.callback.StringCallback;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Call;

/**
 * Created by masskywcy on 2017-03-02.
 */

public class MycallbackNest extends StringCallback implements ApiResult{

    /**
     * 上下文对象
     *
     * @param Context context
     */
    private Context context;
    /**
     * 加载数据动画展示
     *
     * @param DialogUtil dialogUtil
     */
    private DialogUtil dialogUtil;
    private int wrongtoken_index = 2;
//    private boolean threadFlag;
   private  AddTogglenInterfacer addTogglenInterfacer;
    public MycallbackNest(AddTogglenInterfacer addTogglenInterfacer, Context context, DialogUtil dialogUtil) {

        this.context = context;
        this.dialogUtil = dialogUtil;
        this.addTogglenInterfacer = addTogglenInterfacer;
    }
    @Override
    public void onError(Call call, Exception e, int id) {
        remove();
        LogUtil.i("这是异常error", e.getMessage() + "");
    }

    @Override
    public void onResponse(String response, int id) {

        LogUtil.eLength("这是返回数据", response + "");
        remove();
        if (TextUtils.isEmpty(response)) {
            emptyResult();
        } else {
            User user = new GsonBuilder().registerTypeAdapterFactory(
                    new NullStringToEmptyAdapterFactory()).create().fromJson(response, User.class);
            switch (user.result) {
                case "100":
                    onSuccess(user);
                    break;
                case "101":
                    wrongToken();
                    break;
                case "102":
                    wrongBoxnumber();
                    break;
                case "103":
                    threeCode();
                    break;
                case "104":
                    fourCode();
                    break;
                case "105":
                    fiveCode();
                    break;
                case "106":
                    sixCode();
                    break;
                case "107":
                    sevenCode();
                    break;
                //解析失败
                case "1":
                    pullDataError();
                    break;
                default:
                    defaultCode();
                    ToastUtil.showToast(context, "操作失败");
                    break;
            }
        }

        LogUtil.i("这是返回数据", response + "");
    }

    @Override
    public void onSuccess(User user) {

    }

    @Override
    public void wrongToken() {
        //这里是是基类，把获取错误的Token写在这里
        if (wrongtoken_index > 0)
            getToken();
    }

    @Override
    public void wrongBoxnumber() {

    }

    @Override
    public void pullDataError() {

    }

    @Override
    public void emptyResult() {

    }

    @Override
    public void threeCode() {

    }

    @Override
    public void fourCode() {

    }

    @Override
    public void fiveCode() {

    }

    @Override
    public void sixCode() {

    }

    @Override
    public void sevenCode() {

    }

    @Override
    public void defaultCode() {

    }

    //移除dialog动画加载
    private void remove() {
        if (dialogUtil != null) {
            dialogUtil.removeDialog();
        }
    }

    private void getToken() {
        wrongtoken_index --;
        String encryPass = (String) SharedPreferencesUtil.getData(context, "loginPassword", "");
        String password = DES.decryptDES(encryPass, "12345678");
        String loginPhone = (String) SharedPreferencesUtil.getData(context, "loginPhone", "");
        Map<String, Object> map = new HashMap<>();
        String time = Timeuti.getTime();
        map.put("loginAccount", loginPhone);
        map.put("timeStamp", time);
        map.put("signature", MD5Util.md5(loginPhone + encryPass + time));
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
                        wrongtoken_index = 2;
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
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(100);
                                    addTogglenInterfacer.addTogglenInterfacer();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                }
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
                if (wrongtoken_index <= 0) {
                    //弹出退出到登录，的对话框
                    Intent broadcast = new Intent("com.massky.jinruicenterpark.broadcast.wrongtoken");
                    context.sendBroadcast(broadcast);
                }
            }
        });

    }

    private void processCustomMessage() {
        Intent mIntent = new Intent(MacFragment.ACTION_INTENT_RECEIVER);
        mIntent.putExtra("notifactionId", 1);
        context.sendBroadcast(mIntent);
    }
}
