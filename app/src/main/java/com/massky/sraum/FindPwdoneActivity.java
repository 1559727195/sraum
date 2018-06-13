package com.massky.sraum;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.ClearEditText;
import com.Util.DialogUtil;
import com.Util.IntentUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
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

/**
 * Created by masskywcy on 2017-01-05.
 */
//用于找回密码验证码验证界面
public class FindPwdoneActivity extends Basecactivity {
    @InjectView(R.id.finpho_id)
    ClearEditText finpho_id;
    @InjectView(R.id.finyz_id)
    ClearEditText finyz_id;
    @InjectView(R.id.back_id)
    RelativeLayout back_id;
    @InjectView(R.id.centitle_id)
    TextView centitle_id;
    @InjectView(R.id.timebutton_id)
    Button timebutton_id;
    @InjectView(R.id.nextbtn_id)
    Button nextbtn_id;

    private TimeCount time;
    private String des;
    //mob短信的key
    protected static final String SmsKey = "1a65fe6cfd250";
    protected static final String SmsSecret = "991ff716e52088581327423bf04509b0";
    private Handler handler;
    private DialogUtil dialogUtil;

    @Override
    protected int viewId() {
        return R.layout.findpwdone;
    }

    @Override
    protected void onView() {
        inti();
    }

    private void inti() {
        finpho_id.setText(IntentUtil.getIntentString(FindPwdoneActivity.this, "findonepho"));
        finpho_id.setSelection(finpho_id.getText().length());
        dialogUtil = new DialogUtil(FindPwdoneActivity.this);
        time = new TimeCount(60000, 1000);//构造CountDownTimer对象
        timebutton_id.setOnClickListener(this);
        centitle_id.setText(R.string.findname);
        back_id.setOnClickListener(this);
        nextbtn_id.setOnClickListener(this);
        getSms();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_id:
                FindPwdoneActivity.this.finish();
                break;
            case R.id.timebutton_id:
                checkNum();
                break;
            case R.id.nextbtn_id:
                String mobilePhone = finpho_id.getText().toString();
                String code = finyz_id.getText().toString();
                if (mobilePhone.equals("") || code.equals("")) {
                    ToastUtil.showDelToast(FindPwdoneActivity.this, "手机号或验证码不能为空");
                } else {
                    SMSSDK.submitVerificationCode("86", mobilePhone, code);
                }
                break;
        }
    }

    //验证手机号是否注册，发送验证码
    private void checkNum() {
        String mobilePhone = finpho_id.getText().toString();
        if (mobilePhone.equals("")) {
            ToastUtil.showToast(FindPwdoneActivity.this, "手机号不能为空");
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("mobilePhone", mobilePhone);
            dialogUtil.loadDialog();
            MyOkHttp.postMapString(ApiHelper.sraum_checkMobilePhone, map, new Mycallback(new AddTogglenInterfacer() {
                @Override
                public void addTogglenInterfacer() {//获取togglen刷新数据
                    checkNum();
                }
            }, FindPwdoneActivity.this, dialogUtil) {
                @Override
                public void onSuccess(User user) {
                    super.onSuccess(user);
                    ToastUtil.showDelToast(FindPwdoneActivity.this, "账号未注册，请注册");
                }

                @Override
                public void wrongBoxnumber() {
                    getCode();
                }
            });
        }
    }

    private void getCode() {
        timebutton_id.setText("已发送...");
        SMSSDK.getVerificationCode("86", finpho_id.getText().toString(), new OnSendMessageHandler() {
            @Override
            public boolean onSendMessage(String s, String s1) {
                return false;
            }
        });
    }

    private void getSms() {
        code();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        ToastUtil.showDelToast(FindPwdoneActivity.this, "回调完成");
                        break;
                    case 2:
                        String finpho = finpho_id.getText().toString();
                        IntentUtil.startActivity(FindPwdoneActivity.this, FindPwdtwoActivity.class, "finpho", finpho);
                        break;
                    case 3:
                        time.start();
                        ToastUtil.showDelToast(FindPwdoneActivity.this, "验证码已发送，请查收");
                        break;
                    case 4:
                        ToastUtil.showDelToast(FindPwdoneActivity.this, "返回支持发送国家验证码");
                        break;
                    case 5:
                        timebutton_id.setText("重新获取");
                        ToastUtil.showDelToast(FindPwdoneActivity.this, des);
                        break;
                }
            }
        };
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
            timebutton_id.setClickable(false);
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
