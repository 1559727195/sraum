package com.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.AppManager;
import com.Util.HomeWatcher;
import com.Util.IntentUtil;
import com.Util.LogUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.ScreenListener;
import com.Util.SharedPreferencesUtil;
import com.Util.TokenUtil;
import com.chenhongxin.autolayout.AutoLayoutActivity;
import com.data.Allbox;
import com.data.User;
import com.massky.sraum.FindPwdoneActivity;
import com.massky.sraum.FindPwdtwoActivity;
import com.massky.sraum.LoginActivity;
import com.massky.sraum.RegisterActivity;
import com.wujay.fund.GestureVerifyActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * Created by masskywcy on 2016-08-19.
 */
//用于不侧滑删除activity
public abstract class Basecactivity extends AutoLayoutActivity implements View.OnClickListener {
    private HomeWatcher mHomeWatcher;
    private ScreenListener screen;
    private boolean actflag = true;
    private String loginPhone = "";
    private SharedPreferences preferences;
    private List<Allbox> allboxList = new ArrayList<Allbox>();
    public static boolean isForegrounds = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(viewId());
        ButterKnife.inject(this);
        getGesture();
        onView();
        loginPhone = (String) SharedPreferencesUtil.getData(this, "loginPhone", "");
        preferences = getSharedPreferences("sraum" + loginPhone, Context.MODE_PRIVATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * 在登录，注册，忘记密码界面不进行手势密码的验证
     */
    private void getGesture() {
        if (this instanceof LoginActivity || this instanceof RegisterActivity ||
                this instanceof FindPwdoneActivity || this instanceof FindPwdtwoActivity) {
            actflag = false;
        }
        if (actflag) {
            AppManager.getAppManager().addActivity(this);//添加activity
            homeListener();
            screenListener();
        }
    }

    /**
     * 手势密码两种状态（点击home键和手机屏幕状态进行判定）
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        if (actflag) {
            boolean editFlag = preferences.getBoolean("editFlag", false);
            LogUtil.i("这是重新启动值" + editFlag);
            boolean gesflag = (boolean) SharedPreferencesUtil.getData(Basecactivity.this, "gesflag", false);
            boolean screenFlag = (boolean) SharedPreferencesUtil.getData(Basecactivity.this, "screenFlag", false);
            if (editFlag) {
                if (gesflag || screenFlag) {
                    IntentUtil.startActivity(Basecactivity.this, GestureVerifyActivity.class);
                }
            } else {
                //不管事黑屏再亮还是点击home键再次比较网关状态
                if (gesflag || screenFlag) {
                    getBoxstatus();
                }
            }
        }
    }

    /**
     * 用于监听手机屏幕状态
     */
    private void screenListener() {
        screen = new ScreenListener(this);
        screen.begin(new ScreenListener.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                SharedPreferencesUtil.saveData(Basecactivity.this, "screenFlag", true);
            }

            @Override
            public void onScreenOff() {
                SharedPreferencesUtil.saveData(Basecactivity.this, "screenFlag", false);
            }

            @Override
            public void onUserPresent() {
            }
        });

    }

    /**
     * 用于监听手机home键状态
     */
    private void homeListener() {
        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                SharedPreferencesUtil.saveData(Basecactivity.this, "gesflag", true);
            }

            @Override
            public void onHomeLongPressed() {
            }
        });
        mHomeWatcher.startWatch();
    }

    @Override
    protected void onPause() {
        isForegrounds = false;
        super.onPause();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    protected abstract int viewId();

    @Override
    protected void onResume() {
        isForegrounds = true;
        super.onResume();
        SharedPreferencesUtil.saveData(Basecactivity.this, "pagetag", "");
    }

    protected abstract void onView();
    /**
     * 取消广播状态
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (actflag) {
            screen.unregisterListener();
            mHomeWatcher.stopWatch();
            AppManager.getAppManager().finishActivity(this);
        }
    }

    //比较网关状态
    private void getBoxstatus() {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(Basecactivity.this));
        map.put("boxNumber", TokenUtil.getBoxnumber(Basecactivity.this));
        MyOkHttp.postMapObject(ApiHelper.sraum_getBoxStatus, map,
                new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {
                        getBoxstatus();
                    }
                }, Basecactivity.this, null) {
                    @Override
                    public void onSuccess(User user) {
                        super.onSuccess(user);
                        SharedPreferencesUtil.saveData(Basecactivity.this, "boxstatus", user.status);
                    }

                    @Override
                    public void wrongBoxnumber() {
                        super.wrongBoxnumber();
                        getBox();
                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                    }
                });
    }

    private void getBox() {
        Map<String, Object> mapbox = new HashMap<String, Object>();
        mapbox.put("token", TokenUtil.getToken(Basecactivity.this));
        MyOkHttp.postMapObject(ApiHelper.sraum_getAllBox, mapbox, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {
                getBox();
            }
        }, Basecactivity.this, null) {
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
                    SharedPreferencesUtil.saveData(Basecactivity.this, "boxnumber", "");
                } else {
                    for (Allbox ab : allboxList) {
                        if (ab.sign.equals("1")) {
                            SharedPreferencesUtil.saveData(Basecactivity.this, "boxstatus", ab.status);
                            SharedPreferencesUtil.saveData(Basecactivity.this, "boxnumber", ab.number);
                        } else {
                            SharedPreferencesUtil.saveData(Basecactivity.this, "boxnumber", "");
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

