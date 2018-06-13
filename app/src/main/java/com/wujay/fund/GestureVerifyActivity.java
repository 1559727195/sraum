package com.wujay.fund;


import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.AppManager;
import com.Util.BitmapUtil;
import com.Util.IntentUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.SharedPreferencesUtil;
import com.Util.TokenUtil;
import com.data.Allbox;
import com.data.User;
import com.massky.sraum.LoginActivity;
import com.massky.sraum.R;
import com.wujay.fund.widget.GestureContentView;
import com.wujay.fund.widget.GestureDrawline.GestureCallBack;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 手势绘制/校验界面
 */
public class GestureVerifyActivity extends Activity implements View.OnClickListener {
    /**
     * 手机号码
     */
    public static final String PARAM_PHONE_NUMBER = "PARAM_PHONE_NUMBER";
    /**
     * 意图
     */
    public static final String PARAM_INTENT_CODE = "PARAM_INTENT_CODE";
    private RelativeLayout mTopLayout;
    private TextView mTextTitle;
    private ImageView mImgUserLogo;
    private TextView mTextPhoneNumber;
    private TextView mTextTip;
    private FrameLayout mGestureContainer;
    private GestureContentView mGestureContentView;
    private TextView mTextForget;
    private TextView mTextOther;
    private LinearLayout geslinear;
    private String mParamPhoneNumber;
    private long mExitTime = 0;
    private int mParamIntentCode;
    String name;
    private String loginPhone;
    private List<Allbox> allboxList = new ArrayList<Allbox>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_verify);
        ObtainExtraData();
        setUpViews();
        setUpListeners();
        getBoxstatus();
    }

    private void ObtainExtraData() {
        name = (String) SharedPreferencesUtil.getData(GestureVerifyActivity.this, "mFirstPassword", "1235789");
        loginPhone = (String) SharedPreferencesUtil.getData(GestureVerifyActivity.this, "loginPhone", "");
        mParamPhoneNumber = getIntent().getStringExtra(PARAM_PHONE_NUMBER);
        mParamIntentCode = getIntent().getIntExtra(PARAM_INTENT_CODE, 0);
    }

    private void setUpViews() {
        mTopLayout = (RelativeLayout) findViewById(R.id.top_layout);
        mTextTitle = (TextView) findViewById(R.id.text_title);
        mImgUserLogo = (ImageView) findViewById(R.id.user_logo);
        mTextPhoneNumber = (TextView) findViewById(R.id.text_phone_number);
        mTextTip = (TextView) findViewById(R.id.text_tip);
        mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
        mTextForget = (TextView) findViewById(R.id.text_forget_gesture);
        mTextOther = (TextView) findViewById(R.id.text_other_account);
        mTextPhoneNumber.setText(getProtectedMobile(loginPhone));

        // 初始化一个显示各个点的viewGroup
        mGestureContentView = new GestureContentView(this, true, name,
                new GestureCallBack() {

                    @Override
                    public void onGestureCodeInput(String inputCode) {

                    }

                    @Override
                    public void checkedSuccess() {
                        mGestureContentView.clearDrawlineState(0L);
                        SharedPreferencesUtil.saveData(GestureVerifyActivity.this, "gesflag", false);
                        SharedPreferencesUtil.saveData(GestureVerifyActivity.this, "screenFlag", false);
                        GestureVerifyActivity.this.finish();
                    }

                    @Override
                    public void checkedFail() {
                        mGestureContentView.clearDrawlineState(1300L);
                        mTextTip.setVisibility(View.VISIBLE);
                        mTextTip.setText(Html
                                .fromHtml("<font color='#c70c1e'>密码错误</font>"));
                        // 左右移动动画
                        Animation shakeAnimation = AnimationUtils.loadAnimation(GestureVerifyActivity.this, R.anim.shake);
                        mTextTip.startAnimation(shakeAnimation);
                    }
                });
        // 设置手势解锁显示到哪个布局里面
        mGestureContentView.setParentView(mGestureContainer);
    }

    private void setUpListeners() {
        mImgUserLogo.setImageBitmap(BitmapUtil.stringtoBitmap((String) SharedPreferencesUtil.
                getData(GestureVerifyActivity.this, "avatar", "")));
        mTextForget.setOnClickListener(this);
        mTextOther.setOnClickListener(this);
    }

    private String getProtectedMobile(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(phoneNumber.subSequence(0, 3));
        builder.append("****");
        builder.append(phoneNumber.subSequence(7, phoneNumber.length()));
        return builder.toString();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_forget_gesture:
                SharedPreferences.Editor editor = getSharedPreferences("sraum" + loginPhone,
                        Context.MODE_PRIVATE).edit();
                editor.putBoolean("editFlag", false);
                editor.commit();
                SharedPreferencesUtil.saveData(GestureVerifyActivity.this, "loginflag", false);
                IntentUtil.startActivityAndFinishFirst(GestureVerifyActivity.this, LoginActivity.class);
                AppManager.getAppManager().finishAllActivity();
                break;
            default:
                break;
        }
    }

    //比较网关状态
    private void getBoxstatus() {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(GestureVerifyActivity.this));
        map.put("boxNumber", TokenUtil.getBoxnumber(GestureVerifyActivity.this));
        MyOkHttp.postMapObject(ApiHelper.sraum_getBoxStatus, map,
                new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {
                        getBoxstatus();
                    }
                }, GestureVerifyActivity.this, null) {
                    @Override
                    public void onSuccess(User user) {
                        super.onSuccess(user);
                        SharedPreferencesUtil.saveData(GestureVerifyActivity.this, "boxstatus", user.status);
                    }

                    @Override
                    public void wrongBoxnumber() {
                        super.wrongBoxnumber();
                        Map<String, Object> mapbox = new HashMap<String, Object>();
                        mapbox.put("token", TokenUtil.getToken(GestureVerifyActivity.this));
                        getBox(mapbox);
                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                    }
                });
    }

    private void getBox(Map<String, Object> mapbox) {
        MyOkHttp.postMapObject(ApiHelper.sraum_getAllBox, mapbox, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {
                Map<String, Object> mapbox = new HashMap<String, Object>();
                mapbox.put("token", TokenUtil.getToken(GestureVerifyActivity.this));
                getBox(mapbox);
            }
        }, GestureVerifyActivity.this, null) {
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
                    SharedPreferencesUtil.saveData(GestureVerifyActivity.this, "boxnumber", "");
                } else {
                    for (Allbox ab : allboxList) {
                        if (ab.sign.equals("1")) {
                            SharedPreferencesUtil.saveData(GestureVerifyActivity.this, "boxstatus", ab.status);
                            SharedPreferencesUtil.saveData(GestureVerifyActivity.this, "boxnumber", ab.number);
                        } else {
                            SharedPreferencesUtil.saveData(GestureVerifyActivity.this, "boxnumber", "");
                        }
                    }
                }
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }

}
