package com.massky.sraum;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.ClearEditText;
import com.Util.DialogUtil;
import com.Util.EyeUtil;
import com.Util.IntentUtil;
import com.Util.MyOkHttp;
import com.Util.MycallbackNest;
import com.Util.ToastUtil;
import com.base.Basecactivity;
import com.data.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.InjectView;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;

/**
 * Created by masskywcy on 2016-09-01.
 */
public class RegisterActivity extends Basecactivity {
    @InjectView(R.id.back_id)
    RelativeLayout back_id;
    @InjectView(R.id.timebutton_id)
    TextView timebutton_id;
    @InjectView(R.id.forgetpassword_id)
    RelativeLayout forgetpassword_id;
    @InjectView(R.id.password_id)
    ClearEditText password_id;
    @InjectView(R.id.confirm_id)
    ClearEditText confirm_id;
    @InjectView(R.id.phone_id)
    ClearEditText phone_id;
    @InjectView(R.id.checkcode_id)
    ClearEditText checkcode_id;
    @InjectView(R.id.registbtn_id)
    Button registbtn_id;
    @InjectView(R.id.regeyeone)
    ImageView regeyeone;
    @InjectView(R.id.regeyetwo)
    ImageView regeyetwo;

    private TimeCount time;
    private String des;
    private Handler handler;
    //mob短信的key
    protected static final String SmsKey = "1a65fe6cfd250";
    protected static final String SmsSecret = "991ff716e52088581327423bf04509b0";
    private Map<String, Object> mapcode = new HashMap<>();
    private DialogUtil dialogUtil;
    private EyeUtil eyeUtilone, eyeUtiltwo;

    @Override
    protected int viewId() {
        return R.layout.register;
    }

    @Override
    protected void onView() {
        intiData();
    }

    private void intiData() {
        phone_id.setText(IntentUtil.getIntentString(RegisterActivity.this, "registerphone"));
        phone_id.setSelection(phone_id.getText().length());
        dialogUtil = new DialogUtil(RegisterActivity.this);
        registbtn_id.setOnClickListener(this);
        back_id.setOnClickListener(this);
        forgetpassword_id.setOnClickListener(this);
        regeyeone.setOnClickListener(this);
        regeyetwo.setOnClickListener(this);
        eyeUtilone = new EyeUtil(RegisterActivity.this, regeyeone, password_id, true);
        eyeUtiltwo = new EyeUtil(RegisterActivity.this, regeyetwo, confirm_id, true);
        time = new TimeCount(60000, 1000);//构造CountDownTimer对象
        getSms();
    }

    private void getSms() {
        code();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        ToastUtil.showDelToast(RegisterActivity.this, "回调完成");
                        break;
                    case 2:
                        dialogUtil.loadDialog();
                        show_detail_togglen();
                        /*
                        * Intent intent = new Intent(RegisterActivity.this, ResetPassword.class);
                        intent.putExtra("phoneNumber", phoneNumber);
                        startActivity(intent); */
                        break;
                    case 3:
                        time.start();
                        ToastUtil.showDelToast(RegisterActivity.this, "验证码已发送，请查收");
                        break;
                    case 4:
                        ToastUtil.showDelToast(RegisterActivity.this, "返回支持发送国家验证码");
                        break;
                    case 5:
                        timebutton_id.setText("重新获取");
                        ToastUtil.showDelToast(RegisterActivity.this, des);
                        break;
                }
            }

            /**
             * ApiHelper.sraum_register
             */
            private void show_detail_togglen() {
                MyOkHttp.postMapObjectnest(ApiHelper.sraum_register, mapcode, new MycallbackNest(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {
                        show_detail_togglen();
                    }
                }, RegisterActivity.this, dialogUtil) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                        ToastUtil.showDelToast(RegisterActivity.this, "网络连接超时");
                    }

                    @Override
                    public void onSuccess(User user) {
                        switch (user.result) {
                            case "100":
                                RegisterActivity.this.finish();
                                break;
                            default:
                                ToastUtil.showDelToast(RegisterActivity.this, "注册失败");
                                break;
                        }
                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                    }
                });
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_id:
                RegisterActivity.this.finish();
                break;
            case R.id.forgetpassword_id:
                checkNum();
                break;
            case R.id.registbtn_id:
                checkRes();
                break;
            case R.id.regeyeone:
                eyeUtilone.EyeStatus();
                break;
            case R.id.regeyetwo:
                eyeUtiltwo.EyeStatus();
                break;
        }
    }

    //提交注册信息
    private void checkRes() {
        String mobilePhone = phone_id.getText().toString();
        String code = checkcode_id.getText().toString();
        String loginPwd = password_id.getText().toString();
        String loginPwdtwo = confirm_id.getText().toString();
        int pwdleng = loginPwd.length();
        if (mobilePhone.equals("") || code.equals("") || loginPwd.equals("") || loginPwdtwo.equals("")) {
            ToastUtil.showDelToast(RegisterActivity.this, "注册信息不能为空");
        } else {
            if (loginPwd.equals(loginPwdtwo)) {
                if (pwdleng >= 6 && pwdleng <= 20) {
                    mapcode.put("mobilePhone", mobilePhone);
                    mapcode.put("loginPwd", loginPwd);
                    SMSSDK.submitVerificationCode("86", mobilePhone, code);
                } else {
                    ToastUtil.showDelToast(RegisterActivity.this, "密码不能少于6位");
                }
            } else {
                ToastUtil.showDelToast(RegisterActivity.this, "两次密码输入不一致");
            }
        }

    }

    //验证手机号是否注册，发送验证码
    private void checkNum() {
        String mobilePhone = phone_id.getText().toString();
        if (mobilePhone.equals("")) {
            ToastUtil.showToast(RegisterActivity.this, "手机号不能为空");
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("mobilePhone", mobilePhone);
            dialogUtil.loadDialog();
            regist_togglen(map);
        }
    }

    /**
     * ApiHelper.sraum_checkMobilePhon
     * @param map
     */
    private void regist_togglen(final Map<String, Object> map) {
        MyOkHttp.postMapObjectnest(ApiHelper.sraum_checkMobilePhone, map, new MycallbackNest(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {
                regist_togglen(map);
            }
        }, RegisterActivity.this, dialogUtil){
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                ToastUtil.showDelToast(RegisterActivity.this, "网络连接超时");
            }

            @Override
            public void onSuccess(User user) {
                switch (user.result) {
                    case "100":
                        getCode();
                        break;
                    case "101":
                        ToastUtil.showDelToast(RegisterActivity.this, "手机号已注册");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }


    private void code() {
        SMSSDK.initSDK(this, SmsKey, SmsSecret);
        EventHandler eh = new EventHandler() {

            @Override
            public void afterEvent(int event, int result, Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        handler.sendEmptyMessage(2);
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                        handler.sendEmptyMessage(3);
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                        handler.sendEmptyMessage(4);
                    }
                } else {
                    ((Throwable) data).printStackTrace();
                    Log.i("异常", "afterEvent: ");
                    Throwable throwable = (Throwable) data;
                    int status = 0;
                    JSONObject object = null;
                    try {
                        object = new JSONObject(throwable.getMessage());
                        des = object.optString("detail");
                        status = object.optInt("status");
                        Log.i("这是返回错误", status + "afterEvent: ");
                        if (!TextUtils.isEmpty(des)) {
                            handler.sendEmptyMessage(5);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }

    private void getCode() {
        timebutton_id.setText("已发送...");
        SMSSDK.getVerificationCode("86", phone_id.getText().toString(), new OnSendMessageHandler() {
            @Override
            public boolean onSendMessage(String s, String s1) {
                return false;
            }
        });
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            timebutton_id.setText("重新获取");
            timebutton_id.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            forgetpassword_id.setClickable(false);
            timebutton_id.setText("已发送" + "(" + millisUntilFinished / 1000 + "秒" + ")");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
        time.cancel();
    }
}
