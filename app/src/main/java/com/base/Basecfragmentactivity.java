package com.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.AppManager;
import com.Util.HomeWatcher;
import com.Util.IntentUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.ScreenListener;
import com.Util.SharedPreferencesUtil;
import com.Util.TokenUtil;
import com.chenhongxin.autolayout.AutoLayoutFragmentActivity;
import com.data.Allbox;
import com.data.User;
import com.massky.sraum.MainfragmentActivity;
import com.wujay.fund.GestureVerifyActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
/*用于非侧滑侧面fragmentactivity的基类*/

/**
 * Created by masskywcy on 2016-07-08.
 */
public abstract class Basecfragmentactivity extends AutoLayoutFragmentActivity implements View.OnClickListener {
    private HomeWatcher mHomeWatcher;
    private boolean mainEditFlag, mainGesFlag;
    private ScreenListener screen;
    private String loginPhone = "";
    private SharedPreferences preferences;
    private List<Allbox> allboxList = new ArrayList<Allbox>();
    public static boolean isForegrounds = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(viewId());
        ButterKnife.inject(this);
        // 添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);
        loginPhone = (String) SharedPreferencesUtil.getData(this, "loginPhone", "");
        preferences = getSharedPreferences("sraum" + loginPhone, Context.MODE_PRIVATE);
        homeListener();
        screenListener();
        onView();
    }

    //用于监听是否锁屏状态
    private void screenListener() {
        screen = new ScreenListener(this);
        screen.begin(new ScreenListener.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                SharedPreferencesUtil.saveData(Basecfragmentactivity.this, "screenFlag", true);
            }

            @Override
            public void onScreenOff() {
                SharedPreferencesUtil.saveData(Basecfragmentactivity.this, "screenFlag", false);
            }

            @Override
            public void onUserPresent() {
            }
        });

    }

    //监听home键
    private void homeListener() {
        //进行判断手势密码创建
        mainEditFlag = preferences.getBoolean("editFlag", false);
        mainGesFlag = (boolean) SharedPreferencesUtil.getData(Basecfragmentactivity.this, "gesflag", false);
        if ((this instanceof MainfragmentActivity) && mainEditFlag) {
            if (mainGesFlag) {
                IntentUtil.startActivity(Basecfragmentactivity.this, GestureVerifyActivity.class);
            }
        }
        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                SharedPreferencesUtil.saveData(Basecfragmentactivity.this, "gesflag", true);
            }

            @Override
            public void onHomeLongPressed() {
            }
        });
        mHomeWatcher.startWatch();
    }

    protected abstract int viewId();

    protected abstract void onView();

    @Override
    protected void onRestart() {
        super.onRestart();
        //进行判断手势密码验证
        boolean gesflag = (boolean) SharedPreferencesUtil.getData(Basecfragmentactivity.this, "gesflag", false);
        boolean editFlag = preferences.getBoolean("editFlag", false);
        boolean screenFlag = (boolean) SharedPreferencesUtil.getData(Basecfragmentactivity.this, "screenFlag", false);
        if (editFlag) {
            if (gesflag || screenFlag) {
                IntentUtil.startActivity(Basecfragmentactivity.this, GestureVerifyActivity.class);
            }
        } else {
            getBoxstatus();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 结束Activity&从堆栈中移除
        screen.unregisterListener();
        mHomeWatcher.stopWatch();
        AppManager.getAppManager().finishActivity(this);
    }

    //比较网关状态
    private void getBoxstatus() {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(Basecfragmentactivity.this));
        map.put("boxNumber", TokenUtil.getBoxnumber(Basecfragmentactivity.this));
        MyOkHttp.postMapObject(ApiHelper.sraum_getBoxStatus, map,
                new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {
                        getBoxstatus();
                    }
                }, Basecfragmentactivity.this, null) {
                    @Override
                    public void onSuccess(User user) {
                        super.onSuccess(user);
                        SharedPreferencesUtil.saveData(Basecfragmentactivity.this, "boxstatus", user.status);
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
        mapbox.put("token", TokenUtil.getToken(Basecfragmentactivity.this));
        MyOkHttp.postMapObject(ApiHelper.sraum_getAllBox, mapbox, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {
                getBox();
            }
        }, Basecfragmentactivity.this, null) {
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
                    SharedPreferencesUtil.saveData(Basecfragmentactivity.this, "boxnumber", "");
                } else {
                    for (Allbox ab : allboxList) {
                        if (ab.sign.equals("1")) {
                            SharedPreferencesUtil.saveData(Basecfragmentactivity.this, "boxstatus", ab.status);
                            SharedPreferencesUtil.saveData(Basecfragmentactivity.this, "boxnumber", ab.number);
                        } else {
                            SharedPreferencesUtil.saveData(Basecfragmentactivity.this, "boxnumber", "");
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

    @Override
    protected void onPause() {
        isForegrounds = false;
        super.onPause();
    }

    @Override
    protected void onResume() {
        isForegrounds = true;
        super.onResume();
    }

}
