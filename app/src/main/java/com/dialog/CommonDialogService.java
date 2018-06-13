package com.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.App;
import com.Util.AppManager;
import com.Util.LogUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.SharedPreferencesUtil;
import com.Util.ToastUtil;
import com.data.User;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.massky.sraum.LoginActivity;
import com.massky.sraum.R;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Call;


/**
 * Created by zhu on 2016/9/8.
 *
 * @version ${VERSION}
 * @decpter
 */
public class CommonDialogService extends Service implements CommonDialogListener {

    private static Dialog dialog;
    private static View view;
    private TextView belowtext_id;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtils.getInstances().setListener(this);//绑定

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(dialog!=null&&dialog.isShowing()){
            dialog.cancel();
            dialog=null;
        }
    }

    private void showDialog(String str){
        if(dialog!=null){
            dialog.cancel();
            dialog=null;
        }
        if(dialog==null&&CommonData.mNowContext!=null){
            dialog = new Dialog(CommonData.mNowContext);
            view = LayoutInflater.from(this).inflate(R.layout.check_is_status,null,false);
            //belowtext_id
            Button checkbutton_id= (Button) view.findViewById(R.id.checkbutton_id);
            belowtext_id = (TextView) view.findViewById(R.id.belowtext_id);
            belowtext_id.setText(str);
            checkbutton_id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Activity nowactivty = (Activity) CommonData.mNowContext;
//                    App.getInstance().removeActivity_but_activity(nowactivty);
                    AppManager.getAppManager().removeActivity_but_activity(nowactivty);
                    SharedPreferencesUtil.saveData(CommonData.mNowContext, "loginflag", false);
                    Intent intent = new Intent(CommonData.mNowContext, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    nowactivty.finish();
                }
            });
            dialog.setContentView(view);
            dialog.show();
            WindowManager.LayoutParams lp = dialog.getWindow()
                    .getAttributes();
//            if(CommonData.ScreenWidth!=0)
//            lp.width =  CommonData.ScreenWidth/ 3;
//
//            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(false);
//        confirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
            WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); //获取对话框当前的参数值
//        p.width = (int) (displayWidth * 0.55); //宽度设置为屏幕的0.5
//        p.height = (int) (displayHeight * 0.5); //宽度设置为屏幕的0.5
//        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
            dialog.getWindow().setAttributes(p);  //设置生效

        }else {
//            Toast.makeText(CommonData.applicationContext,"有误", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void show(String str) {
       showDialog(str);
    }

    @Override
    public void cancel() {
         if(dialog!=null){
                dialog.dismiss();
                dialog=null;
         }
    }

    @Override
    public void show_isfourbackground(String str) {
        //延时100ms发广播 --MESSAGE_RECEIVED_ACTION

        //发送Action为com.example.communication.RECEIVER的广播
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                intent.putExtra("progress", "");
//                sendBroadcast(intent);
//            }
//        },100);
        init_jlogin(str);
    }


    private void init_jlogin(String str) {
        String mobilePhone = (String) SharedPreferencesUtil.getData(CommonData.mNowContext, "loginPhone", "");
        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String szImei = TelephonyMgr.getDeviceId();
        init_islogin(mobilePhone, szImei,str);
    }


    /**
     * 初始化登录
     * @param
     * @param mobilePhone
     * @param szImei
     * @param str
     */
    private void init_islogin(final String mobilePhone, final String szImei, final String str) {
        Map<String, Object> map = new HashMap<>();
        map.put("mobilePhone", mobilePhone);
        map.put("phoneId",szImei);
        LogUtil.eLength("查看数据", new Gson().toJson(map));
        MyOkHttp.postMapObject(ApiHelper.sraum_isLogin, map, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {
                init_islogin(mobilePhone, szImei, str);
            }
        }, CommonData.mNowContext, null) {
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                ToastUtil.showDelToast(CommonData.mNowContext, "网络连接超时");
            }

            @Override
            public void onSuccess(User user) {

            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }

            @Override
            public void threeCode() {
                //103已经登录，需要退出app
//                dialog.show();
                if (CommonData.mNowContext != null) {
                    boolean loginflag = (boolean) SharedPreferencesUtil.getData(CommonData.mNowContext, "loginflag", false);
                    if (loginflag) {
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                MyService.stopNet();
////                                String result = null;
////                                try {
////                                    result = jsonObject.getString("result");
////                                    if ("ok".equals(result)) {
//////                ToastUtil.showToast(MainfragmentActivity.this,"智能家居退出成功");
////                                        Log.e("fei", "result:" + result);
////                                    }
////                                } catch (JSONException e) {
////                                    e.printStackTrace();
////                                }
//                            }
//                        }).start();
                        ToastUtils.getInstances().showDialog(str);
//                        showDialog(str);
                    }
                }
            }
        });
    }
}
