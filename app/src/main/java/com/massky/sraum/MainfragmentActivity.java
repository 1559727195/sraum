package com.massky.sraum;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.DialogUtil;
import com.Util.IntentUtil;
import com.Util.LogUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.NetUtils;
import com.Util.SharedPreferencesUtil;
import com.Util.SlidingUtil;
import com.Util.ToastUtil;
import com.Util.TokenUtil;
import com.Util.UpdateManager;
import com.Util.VersionUtil;
import com.base.Basecfragmentactivity;
import com.data.User;
import com.dialog.CommonData;
import com.dialog.ToastUtils;
import com.fragment.AboutFragment;
import com.fragment.LeftFragment;
import com.fragment.MacdeviceFragment;
import com.fragment.Mainviewpager;
import com.fragment.MyRoomFragment;
import com.fragment.MygatewayFragment;
import com.fragment.MysceneFragment;
import com.fragment.MysetFragment;
import com.fragment.PanelFragment;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jpush.ExampleUtil;
import com.permissions.RxPermissions;
import java.util.HashMap;
import java.util.Map;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;

import static com.massky.sraum.R.id.addscene_id;
/**
 * Created by masskywcy on 2016-09-05.
 */
/*用于主界面设置*/
public class MainfragmentActivity extends Basecfragmentactivity implements Mainviewpager.MyInterface {
    //    @InjectView(R.id.sideslip_id)
//    RelativeLayout sideslip_id;
//    @InjectView(R.id.addrelative_id)
//    RelativeLayout addrelative_id;
//    @InjectView(R.id.addimage_id)
//    ImageView addimage_id;
//    @InjectView(R.id.cenimage_id)
//    ImageView cenimage_id;
//    @InjectView(R.id.centext_id)
//    TextView centext_id;
    public SlidingUtil slidingUtil;
    private Fragment mContent;
    public SlidingMenu menu;
    PopupWindow popupWindow;
//    private RelativeLayout addmac_id, addscene_id, addroom_id;
    //主要保存当前显示的是第几个fragment的索引值
    private Mainviewpager mainviewpager;
    private MygatewayFragment mygatewayFragment;
    private MacdeviceFragment macdeviceFragment;
    private MysceneFragment mysceneFragment;
    private MyRoomFragment myRoomFragment;
    private MysetFragment mysetFragment;
    private AboutFragment aboutFragment;
    private PanelFragment panelFragment;
    private LeftFragment leftFragment;
    private long exitTime = 0;
    private Button checkbutton_id, qxbutton_id;
    private TextView dtext_id, belowtext_id;
    private DialogUtil viewDialog;
    private String usertype, Version;
    private int versionCode;
    private DialogUtil dialogUtil;

    @Override
    protected int viewId() {
        return R.layout.main;
    }

    @Override
    protected void onView() {
        /*
        *  usertype = (String) SharedPreferencesUtil.getData(MainfragmentActivity.this, "usertype", "");
        if (usertype != null) {
            if (usertype.equals("2")) {
                setTabSelection(3);
            } else if (usertype.equals("1")) {
                setTabSelection(0);
            }
        }*/

        dialogUtil = new DialogUtil(this);

        initPermission();
        //在这里发送广播，expires_in是86400->24小时
        String expires_in = (String) SharedPreferencesUtil.getData(MainfragmentActivity.this, "expires_in", "");
        Intent broadcast = new Intent("com.massky.sraum.broadcast");
        broadcast.putExtra("expires_in", expires_in);
        broadcast.putExtra("timestamp", TokenUtil.getLogintime(MainfragmentActivity.this));
        sendBroadcast(broadcast);

//        addPopwinwow();
        versionCode = Integer.parseInt(VersionUtil.getVersionCode(MainfragmentActivity.this));
        slidingUtil = new SlidingUtil(MainfragmentActivity.this);
        menu = slidingUtil.initSlidingMenu();
        if (mContent == null) {
            usertype = IntentUtil.getIntentString(MainfragmentActivity.this, "addflag");
            LogUtil.eLength("这是数据", usertype + "s数据问题");
            if (usertype.equals("1")) {
                setTabSelection(3);
            } else {
                setTabSelection(0);
            }
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new LeftFragment()).commitAllowingStateLoss();
//        sideslip_id.setOnClickListener(this);
        getDialog();
        boolean netflag = NetUtils.isNetworkConnected(MainfragmentActivity.this);
        if (netflag) {
            updateApk();
        }
        registerMessageReceiver();
    }


    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public  static class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                    //说明别处已经登录
                    //先调用一下
//                    dialog.show();
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            MyService.stopNet();
////                            String result = null;
////                            try {
////                                result = jsonObject.getString("result");
////                                if ("ok".equals(result)) {
//////                ToastUtil.showToast(MainfragmentActivity.this,"智能家居退出成功");
////                                    Log.e("fei","result:" + result);
////                                }
////                            } catch (JSONException e) {
////                                e.printStackTrace();
////                            }
//                        }
//                    }).start();
                    boolean loginflag  = (boolean) SharedPreferencesUtil.getData(CommonData.mNowContext, "loginflag", false);
//                    ToastUtil.showToast(CommonData.mNowContext,"MainfragmentActivity-loginflag:" + loginflag);
                    if(loginflag)
                    ToastUtils.getInstances().showDialog("账号在其他地方登录，请重新登录。");
                }
            } catch (Exception e) {

            }
        }
    }

    //拦截侧滑
    public void gotoStop() {
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
    }

    //开始侧滑
    public void gotoStart() {
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
    }

    private void updateApk() {
//        boolean tokenflag = TokenUtil.getTokenflag(MainfragmentActivity.this);
//        if (tokenflag) {
//            sraum_get_version();
//        }
        sraum_get_version();
    }

    private void initPermission() {
        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
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

    private void sraum_get_version() {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(MainfragmentActivity.this));
        MyOkHttp.postMapObject(ApiHelper.sraum_getVersion, map,
                new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {
                        sraum_get_version();
                    }
                }, MainfragmentActivity.this, viewDialog) {
                    @Override
                    public void onSuccess(User user) {
                        super.onSuccess(user);
                        Version = user.version;
                        Log.e("fei","Version:"+Version);
                        int sracode = Integer.parseInt(user.versionCode);
                        if (versionCode < sracode) {
                            belowtext_id.setText("版本更新至" + Version);
                            viewDialog.loadViewdialog();
                        }
                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                    }
                });
    }

    //用于设置dialog展示
    private void getDialog() {
        View view = getLayoutInflater().inflate(R.layout.check, null);
        checkbutton_id = (Button) view.findViewById(R.id.checkbutton_id);
        qxbutton_id = (Button) view.findViewById(R.id.qxbutton_id);
        dtext_id = (TextView) view.findViewById(R.id.dtext_id);
        belowtext_id = (TextView) view.findViewById(R.id.belowtext_id);
        dtext_id.setText("发现新版本");
        checkbutton_id.setText("立即更新");
        qxbutton_id.setText("以后再说");
        viewDialog = new DialogUtil(MainfragmentActivity.this, view);
        checkbutton_id.setOnClickListener(this);
        qxbutton_id.setOnClickListener(this);
    }

    /**
     * 切换Fragment
     *
     * @param
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void setTabSelection(int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 0:
                // 如果MessageFragment为空，则创建一个并添加到界面上
//                addrelative_id.setVisibility(View.VISIBLE);
//                addimage_id.setVisibility(View.VISIBLE);
//                cenimage_id.setVisibility(View.VISIBLE);
//                centext_id.setVisibility(View.GONE);
                if (mainviewpager == null) {
                    mainviewpager = new Mainviewpager();
                    transaction.add(R.id.content_frame, mainviewpager);
                    LogUtil.i("数据", "setTabSelection: ");
                } else {
                    LogUtil.i("展示数据", "setTabSelection: ");
                    transaction.show(mainviewpager);
                }
                break;
            case 1:
//                addrelative_id.setVisibility(View.GONE);
//                addimage_id.setVisibility(View.GONE);
//                cenimage_id.setVisibility(View.GONE);
//                centext_id.setVisibility(View.GONE);
                if (mygatewayFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    mygatewayFragment = MygatewayFragment.newInstance(menu);
                    transaction.add(R.id.content_frame, mygatewayFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mygatewayFragment);
                }
                break;
            case 2:
//                centext_id.setVisibility(View.VISIBLE);
//                addrelative_id.setVisibility(View.GONE);
//                addimage_id.setVisibility(View.GONE);
//                cenimage_id.setVisibility(View.GONE);
//                centext_id.setText(R.string.mac);
                if (macdeviceFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    macdeviceFragment = MacdeviceFragment.newInstance(menu);
                    transaction.add(R.id.content_frame, macdeviceFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(macdeviceFragment);
                }

                macdeviceFragment.setBackToMainTitleListener(new MacdeviceFragment.BackToMainTitleListener() {
                    @Override
                    public void backToMainTitleLength(int length) {
//                        centext_id.setText("智能设备" + "(" + length + ")");
                    }
                });
                break;
            case 3:
//                centext_id.setVisibility(View.VISIBLE);
//                addrelative_id.setVisibility(View.GONE);
//                addimage_id.setVisibility(View.GONE);
//                cenimage_id.setVisibility(View.GONE);
//                centext_id.setText(R.string.scenecen);
                if (mysceneFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    mysceneFragment = MysceneFragment.newInstance(menu);
                    transaction.add(R.id.content_frame, mysceneFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mysceneFragment);
                }
                break;
            case 4:
                if (myRoomFragment == null) {
//                    centext_id.setVisibility(View.VISIBLE);
//                    addrelative_id.setVisibility(View.GONE);
//                    addimage_id.setVisibility(View.GONE);
//                    cenimage_id.setVisibility(View.GONE);
//                    centext_id.setText(R.string.myroom);
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    myRoomFragment = MyRoomFragment.newInstance(menu);
                    transaction.add(R.id.content_frame, myRoomFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(myRoomFragment);
                }
                break;
            case 5:
//                centext_id.setVisibility(View.VISIBLE);
//                addrelative_id.setVisibility(View.GONE);
//                addimage_id.setVisibility(View.GONE);
//                cenimage_id.setVisibility(View.GONE);
//                centext_id.setText(R.string.set);
                if (mysetFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    mysetFragment = MysetFragment.newInstance(menu);
                    transaction.add(R.id.content_frame, mysetFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mysetFragment);
                }
                break;
            case 6:
//                centext_id.setVisibility(View.VISIBLE);
//                addrelative_id.setVisibility(View.GONE);
//                addimage_id.setVisibility(View.GONE);
//                cenimage_id.setVisibility(View.GONE);
//                centext_id.setText(R.string.aboutmain);
                if (aboutFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    aboutFragment = AboutFragment.newInstance(menu);
                    transaction.add(R.id.content_frame, aboutFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(aboutFragment);
                }
                break;
            case 7:
//                centext_id.setVisibility(View.VISIBLE);
//                addrelative_id.setVisibility(View.GONE);
//                addimage_id.setVisibility(View.GONE);
//                cenimage_id.setVisibility(View.GONE);
//                centext_id.setText("我的面板");
                if (panelFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    panelFragment = PanelFragment.newInstance(menu);
                    transaction.add(R.id.content_frame, panelFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(panelFragment);
                }

                panelFragment.setBackToMainTitleListener(new PanelFragment.BackToMainTitleListener() {
                    @Override
                    public void backToMainTitleLength(int length) {
//                        centext_id.setText("我的面板"+"("+length+")");
                    }
                });
                break;
        }
        transaction.commitAllowingStateLoss();
        menu.showContent();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (mainviewpager != null) {
            transaction.hide(mainviewpager);
        }
        if (mygatewayFragment != null) {
            transaction.hide(mygatewayFragment);
        }
        if (macdeviceFragment != null) {
            transaction.hide(macdeviceFragment);
        }
        if (mysceneFragment != null) {
            transaction.hide(mysceneFragment);
        }
        if (myRoomFragment != null) {
            transaction.hide(myRoomFragment);
        }
        if (mysetFragment != null) {
            transaction.hide(mysetFragment);
        }
        if (aboutFragment != null) {
            transaction.hide(aboutFragment);
        }
        if (leftFragment != null) {
            transaction.hide(leftFragment);
        }

        if (panelFragment != null) {
            transaction.hide(panelFragment);
        }
    }

//    //加载主界面右上角popwindow
//    private void addPopwinwow() {
//        View v = LayoutInflater.from(MainfragmentActivity.this).inflate(R.layout.maindialog, null);
//        addmac_id = (RelativeLayout) v.findViewById(R.id.addmac_id);
//        addscene_id = (RelativeLayout) v.findViewById(addscene_id);
//        addroom_id = (RelativeLayout) v.findViewById(R.id.addroom_id);
//        popupWindow = new PopupWindow(v,
//                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
//        popupWindow.setTouchable(true);
//        popupWindow.setBackgroundDrawable(new ColorDrawable());
////        addrelative_id.setOnClickListener(this);
//        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                darkenBackgroud(1f);
//            }
//        });
//        addmac_id.setOnClickListener(this);
//        addroom_id.setOnClickListener(this);
//        addscene_id.setOnClickListener(this);
//    }

    //设置popwindow灰暗程度
    private void darkenBackgroud(Float bgcolor) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgcolor;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.sideslip_id:
//                menu.toggle();
//                break;
//            case addrelative_id:
//                darkenBackgroud(0.7f);
//                popupWindow.showAtLocation(v, Gravity.TOP | Gravity.RIGHT, 3, addrelative_id.getHeight() + 50);
//                break;
            case R.id.addmac_id:
                Intent intent = new Intent(MainfragmentActivity.this, MacdeviceActivity.class);
                intent.putExtra("name", "2");
                startActivity(intent);
                break;
            case R.id.addroom_id:
                Intent intentr = new Intent(MainfragmentActivity.this, MacdeviceActivity.class);
                intentr.putExtra("name", "1");
                startActivity(intentr);
                popupWindow.dismiss();
                break;
            case addscene_id:
                Intent intent1 = new Intent(MainfragmentActivity.this, MysceneActivity.class);
                startActivity(intent1);
                break;
            case R.id.checkbutton_id:
                viewDialog.removeviewDialog();
                String UpApkUrl = ApiHelper.UpdateApkUrl + "sraum" + Version + ".apk";
                Log.e("fei","UpApkUrl:" + UpApkUrl);
                UpdateManager manager = new UpdateManager(MainfragmentActivity.this, UpApkUrl);
                manager.showDownloadDialog();
                break;
            case R.id.qxbutton_id:
                viewDialog.removeviewDialog();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtil.showDelToast(MainfragmentActivity.this, "再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                MainfragmentActivity.this.finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public SlidingMenu getMenu() {
        return menu;
    }

    @Override
    protected void onResume() {
        isForegrounds = true;
        Log.e("zhu-","MainfragmentActivity:onResume():isForegrounds:" + isForegrounds);
//      String  extras_login = (String) SharedPreferencesUtil.getData(MainfragmentActivity.this,"extras_login","");
//        if (extras_login) {
//
//        }
//        后台唤醒的时候先掉下sc_islogin
//        返回100就继续能用返回别的就直接跳到登录
//        init_jlogin();
        init_jlogin();
        super.onResume();
    }

    private void init_jlogin() {
        String mobilePhone = (String) SharedPreferencesUtil.getData(MainfragmentActivity.this, "loginPhone", "");
        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String szImei = TelephonyMgr.getDeviceId();
        init_islogin(mobilePhone, szImei);
    }


    /**
     * 初始化登录
     * @param
     * @param mobilePhone
     * @param szImei
     */
    private void init_islogin(final String mobilePhone, final String szImei) {
        Map<String, Object> map = new HashMap<>();
        map.put("mobilePhone", mobilePhone);
        map.put("phoneId",szImei);
        LogUtil.eLength("查看数据", new Gson().toJson(map));
        MyOkHttp.postMapObject(ApiHelper.sraum_isLogin, map, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {
                init_islogin(mobilePhone, szImei);
            }
        }, MainfragmentActivity.this, dialogUtil) {
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                ToastUtil.showDelToast(MainfragmentActivity.this, "网络连接超时");
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
                ToastUtils.getInstances().showDialog("账号在其他地方登录，请重新登录。");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ToastUtil.showToast(MainfragmentActivity.this,"MainfragmentActivity:destroy");
    }
}
