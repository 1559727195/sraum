package com.massky.sraum;


import android.*;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.ClearEditText;
import com.Util.DbDevice;
import com.Util.DialogUtil;
import com.Util.EyeUtil;
import com.Util.IntentUtil;
import com.Util.LogUtil;
import com.Util.MD5Util;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.MycallbackNest;
import com.Util.OnTouchScrollView;
import com.Util.SharedPreferencesUtil;
import com.Util.Timeuti;
import com.Util.ToastUtil;
import com.Util.TokenUtil;
import com.base.Basecactivity;
import com.data.Allbox;
import com.data.User;
import com.google.gson.Gson;
import com.permissions.RxPermissions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;
import cn.jpush.android.api.JPushInterface;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;

//首次启动界面，用于登录界面
public class LoginActivity extends Basecactivity {
    @InjectView(R.id.scrollView)
    OnTouchScrollView scrollView;
    @InjectView(R.id.forgetrelative_id)
    RelativeLayout forgetrelative_id;
    @InjectView(R.id.register_id)
    Button register_id;
    @InjectView(R.id.enter_id)
    Button enter_id;
    @InjectView(R.id.usertext_id)
    ClearEditText usertext_id;
    @InjectView(R.id.phonepassword)
    ClearEditText phonepassword;
    @InjectView(R.id.eyeimageview_id)
    ImageView eyeimageview_id;
    private DialogUtil dialogUtil;
    private String loginPhone, token, boxnumber;

    private DbDevice dbdevice;
    private EyeUtil eyeUtil;
    private Handler h;
    private List<Allbox> allboxList = new ArrayList<Allbox>();

    @Override
    protected int viewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onView() {
        intiData();
        initPermission();
    }

    private void initPermission() {
        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(android.Manifest.permission.READ_PHONE_STATE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {

            }
        });
    }


    private void intiData() {
        JPushInterface.init(getApplicationContext());
        h = new Handler();
        dbdevice = new DbDevice(LoginActivity.this);
        loginPhone = (String) SharedPreferencesUtil.getData(LoginActivity.this, "loginPhone", "");
        dialogUtil = new DialogUtil(LoginActivity.this);
        register_id.setOnClickListener(this);
        enter_id.setOnClickListener(this);
        eyeimageview_id.setOnClickListener(this);
        forgetrelative_id.setOnClickListener(this);
        eyeUtil = new EyeUtil(LoginActivity.this, eyeimageview_id, phonepassword, true);
        usertext_id.setText(loginPhone);
        usertext_id.setSelection(usertext_id.getText().length());
        boolean flag = (boolean) SharedPreferencesUtil.getData(LoginActivity.this, "loginflag", false);
        //登录状态保存
        if (flag) {
            IntentUtil.startActivityAndFinishFirst(LoginActivity.this, MainfragmentActivity.class);
        }
        usertext_id.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeScrollView();
                return false;
            }
        });

        phonepassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeScrollView();
                return false;
            }
        });
    }

    /**
     * 使ScrollView指向底部
     */
    private void changeScrollView() {
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.smoothScrollTo(0, scrollView.getHeight() - 400);
            }
        }, 300);
    }

    private void getBox(Map<String, Object> mapbox) {
        MyOkHttp.postMapObject(ApiHelper.sraum_getAllBox, mapbox, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {//成功获取toglen后刷新数据
                Map<String, Object> mapbox = new HashMap<>();
                mapbox.put("token", TokenUtil.getToken(LoginActivity.this));
                getBox(mapbox);
            }
        }, LoginActivity.this, dialogUtil) {
            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                int boxtop = 1;
                allboxList.clear();
                for (User.box bo : user.boxList) {
                    Allbox allbox = new Allbox(bo.type, bo.number, bo.name, bo.status, bo.sign, boxtop);
                    allboxList.add(allbox);
                    boxtop++;
                }
                if (allboxList.size() == 0) {
                    SharedPreferencesUtil.saveData(LoginActivity.this, "boxnumber", "");
                } else {
                    for (Allbox ab : allboxList) {
                        if (ab.sign.equals("1")) {
                            SharedPreferencesUtil.saveData(LoginActivity.this, "boxstatus", ab.status);
                            SharedPreferencesUtil.saveData(LoginActivity.this, "boxnumber", ab.number);
                        }
                    }
                }
                IntentUtil.startActivityAndFinishFirst(LoginActivity.this, MainfragmentActivity.class);
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_id:
                String registerphone = usertext_id.getText().toString();
                IntentUtil.startActivity(LoginActivity.this, RegisterActivity.class, "registerphone", registerphone);
                break;
            case R.id.enter_id:
                String loginAccount = usertext_id.getText().toString();
                String pwd = phonepassword.getText().toString();
                if (loginAccount.equals("") || pwd.equals("")) {
                    ToastUtil.showDelToast(LoginActivity.this, "用户名或密码不能为空");
                } else {
                    String time = Timeuti.getTime();
                    Map<String, Object> maptwo = new HashMap<>();
                    maptwo.put("loginAccount", loginAccount);
                    maptwo.put("timeStamp", time);
                    maptwo.put("signature", MD5Util.md5(loginAccount + pwd + time));
                    LogUtil.eLength("传入时间戳", new Gson().toJson(maptwo) + "时间戳" + time);
                    dialogUtil.loadDialog();
                    MyOkHttp.postMapObjectnest(ApiHelper.sraum_getToken, maptwo, new MycallbackNest(new AddTogglenInterfacer() {
                        @Override
                        public void addTogglenInterfacer() {

                        }
                    }, LoginActivity.this, dialogUtil) {

                        @Override
                        public void onError(Call call, Exception e, int id) {
                            super.onError(call, e, id);
                            ToastUtil.showDelToast(LoginActivity.this, "网络连接超时");
                        }

                        @Override
                        public void onSuccess(User user) {
                            SharedPreferencesUtil.saveData(LoginActivity.this, "loginPassword", phonepassword.getText().toString());//保存密码
                            token = user.token;
                            SharedPreferencesUtil.saveData(LoginActivity.this, "tokenTime", true);
                            SharedPreferencesUtil.saveData(LoginActivity.this, "sraumtoken", user.token);
                            SharedPreferencesUtil.saveData(LoginActivity.this, "expires_in", user.expires_in);
                            SharedPreferencesUtil.saveData(LoginActivity.this, "logintime", (int) System.currentTimeMillis());
                            SharedPreferencesUtil.saveData(LoginActivity.this, "tagint", 0);
                            Map<String, Object> map = new HashMap<>();
                            TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                            if (ActivityCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            String szImei = TelephonyMgr.getDeviceId();
                            String regId = (String) SharedPreferencesUtil.getData(LoginActivity.this, "regId", "");
                            map.put("token", user.token);
                            map.put("regId", regId);
                            map.put("phoneId",szImei);
                            LogUtil.eLength("查看数据", new Gson().toJson(map));
                            MyOkHttp.postMapObjectnest(ApiHelper.sraum_login, map, new MycallbackNest(new AddTogglenInterfacer() {
                                @Override
                                public void addTogglenInterfacer() {

                                }
                            }, LoginActivity.this,dialogUtil){
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    super.onError(call, e, id);
                                    ToastUtil.showDelToast(LoginActivity.this, "网络连接超时");
                                }

                                @Override
                                public void onSuccess(User user) {
//                                    ToastUtil.showToast(LoginActivity.this,"登录成功");
                                    SharedPreferencesUtil.saveData(LoginActivity.this, "loginPhone", usertext_id.getText().toString());
                                    SharedPreferencesUtil.saveData(LoginActivity.this, "avatar", user.avatar);
                                    SharedPreferencesUtil.saveData(LoginActivity.this, "accountType", user.accountType);
                                    SharedPreferencesUtil.saveData(LoginActivity.this, "loginflag", true);
                                    if (user.userName != null && !user.userName.equals("")) {
                                        SharedPreferencesUtil.saveData(LoginActivity.this, "userName", user.userName);
                                    }
                                    Map<String, Object> mapbox = new HashMap<>();
                                    mapbox.put("token", TokenUtil.getToken(LoginActivity.this));
                                    getBox(mapbox);
                                }

                                @Override
                                public void wrongToken() {
                                    super.wrongToken();
                                    ToastUtil.showDelToast(LoginActivity.this, "登录失败");
                                }
                            });
                        }

                        @Override
                        public void wrongToken() {
                            super.wrongToken();//继承父类，实现父类的方法
                            ToastUtil.showDelToast(LoginActivity.this, "登录失败，账号未注册");
                        }
                    });
                }
                break;
            case R.id.forgetrelative_id:
                String findonepho = usertext_id.getText().toString();
                IntentUtil.startActivity(LoginActivity.this, FindPwdoneActivity.class, "findonepho", findonepho);
                break;
            case R.id.eyeimageview_id:
                eyeUtil.EyeStatus();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialogUtil.removeviewBottomDialog();
    }
}
