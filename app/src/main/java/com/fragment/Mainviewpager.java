package com.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.DialogUtil;
import com.Util.LogUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.SharedPreferencesUtil;
import com.Util.ToastUtil;
import com.Util.TokenUtil;
import com.adapter.FragmentViewPagerAdapter;
import com.adapter.MygatewayAdapter;
import com.base.Basecfragment;
import com.data.Allbox;
import com.data.User;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.massky.sraum.LoginActivity;
import com.massky.sraum.MacdeviceActivity;
import com.massky.sraum.MainfragmentActivity;
import com.massky.sraum.MysceneActivity;
import com.massky.sraum.R;
import com.massky.sraum.SelectZigbeeDeviceActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;
import butterknife.OnClick;

import static cn.ciaapp.sdk.CIAService.context;
import static com.Util.SharedPreferencesUtil.getData;
import static com.Util.TokenUtil.getBoxnumber;
import static com.massky.sraum.R.id.belowtext_id;
import static com.massky.sraum.R.id.checkbutton_id;
import static com.massky.sraum.R.id.checklinear_id;
import static com.massky.sraum.R.id.dtext_id;
import static com.massky.sraum.R.id.mygatecir;
import static com.massky.sraum.R.id.qxbutton_id;
import static com.massky.sraum.R.id.swibtnone;


/**
 * Created by masskywcy on 2016-09-09.
 */
//用于主界面首页设置
public class Mainviewpager extends Basecfragment implements ViewPager.OnPageChangeListener {
    @InjectView(R.id.viewpager_id)
    ViewPager viewpager_id;
    @InjectView(R.id.macrelative_id)
    RelativeLayout macrelative_id;
    @InjectView(R.id.roomrelative_id)
    RelativeLayout roomrelative_id;
    @InjectView(R.id.scenerelative_id)
    RelativeLayout scenerelative_id;
    @InjectView(R.id.viewone)
    View viewone;
    @InjectView(R.id.viewtwo)
    View viewtwo;
    @InjectView(R.id.viewthree)
    View viewthree;
    @InjectView(R.id.mactext_id)
    TextView mactext_id;
    @InjectView(R.id.roomtext_id)
    TextView roomtext_id;
    @InjectView(R.id.scene_id)
    TextView scene_id;

    @InjectView(R.id.sideslip_id)
    RelativeLayout sideslip_id;
    @InjectView(R.id.addrelative_id)
    RelativeLayout addrelative_id;
    @InjectView(R.id.addimage_id)
    ImageView addimage_id;
    @InjectView(R.id.cenimage_id)
    ImageView cenimage_id;
    @InjectView(R.id.centext_id)
    TextView centext_id;

    private FragmentManager fm;
    private List<Fragment> list = new ArrayList<Fragment>();
    private MacFragment macFragment;
    private RoomFragment roomFragment;
    private SceneFragment sceneFragment;
    private int golden = Color.parseColor("#e0c48e");
    private int black = Color.parseColor("#6f6f6f");
    private FragmentViewPagerAdapter adapter;
    private MainfragmentActivity mainfragmentActivity;
    private SlidingMenu menu;
    private MyInterface myInterface;
    private boolean flag = false;
    private RelativeLayout addmac_id, addscene_id, addroom_id;
    private PopupWindow popupWindow;
    private Dialog dialog;
    private List<Allbox> allboxList = new ArrayList<Allbox>();
    private DialogUtil dialogUtil;
    private RelativeLayout select_dev_type_id;


    @Override
    protected int viewId() {
        return R.layout.mainviewpager;
    }

    @Override
    protected void onView() {
        dialogUtil = new DialogUtil(getActivity());
        addPopwinwow();
        LogUtil.i("这是oncreate方法", "onView: ");
        fm = getChildFragmentManager();
        addFragment();
        onclick();
        startState();
        menu = myInterface.getMenu();
        deleteDialog();
        //成员，业主accountType->addrelative_id
        String accountType = (String) SharedPreferencesUtil.getData(getActivity(), "accountType", "");
        switch (accountType) {
            case "1":
                addrelative_id.setVisibility(View.VISIBLE);
                addimage_id.setVisibility(View.VISIBLE);
                break;//业主
            case "2":
                addrelative_id.setVisibility(View.GONE);
                addimage_id.setVisibility(View.GONE);
                break;//家庭成员

        }
    }

    public interface MyInterface {
        SlidingMenu getMenu();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        myInterface = (MyInterface) activity;
    }

    //添加fragment
    private void addFragment() {
        macFragment = new MacFragment();
        roomFragment = new RoomFragment();
        sceneFragment = new SceneFragment();
        list.add(macFragment);
        list.add(sceneFragment);
        list.add(roomFragment);
        adapter = new FragmentViewPagerAdapter(fm, viewpager_id, list);
        adapter.setOnExtraPageChangeListener(new FragmentViewPagerAdapter.OnExtraPageChangeListener() {
            @Override
            public void onExtraPageSelected(int i) {
                LogUtil.i("Extra...i:", i + "onExtraPageSelected: ");
            }
        });
        viewpager_id.addOnPageChangeListener(this);
        mainfragmentActivity = (MainfragmentActivity) getActivity();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            list.clear();
            macFragment = new MacFragment();
            roomFragment = new RoomFragment();
            sceneFragment = new SceneFragment();
            list.add(macFragment);
            list.add(sceneFragment);
            list.add(roomFragment);
            adapter = new FragmentViewPagerAdapter(fm, viewpager_id, list);
            startState();
        }
    }

    private void onclick() {
        MyOnclick on = new MyOnclick();
        macrelative_id.setOnClickListener(on);
        roomrelative_id.setOnClickListener(on);
        scenerelative_id.setOnClickListener(on);
    }

    /**
     * 设置一个监听类
     */
    class MyOnclick implements View.OnClickListener {

        @Override
        public void onClick(View arg0) {
            clear();
            viewchange(arg0.getId());
        }
    }

    private void clear() {
        mactext_id.setTextColor(black);
        roomtext_id.setTextColor(black);
        scene_id.setTextColor(black);
        viewone.setVisibility(View.GONE);
        viewtwo.setVisibility(View.GONE);
        viewthree.setVisibility(View.GONE);
    }

    // 进行匹配设置一个方法，判断是否被点击
    private void viewchange(int num) {
        switch (num) {
            case R.id.macrelative_id:
            case 0:
                mactext_id.setTextColor(golden);
                viewone.setVisibility(View.VISIBLE);
                viewpager_id.setCurrentItem(0);
                break;
            case R.id.roomrelative_id:
            case 1:
                roomtext_id.setTextColor(golden);
                viewtwo.setVisibility(View.VISIBLE);
                viewpager_id.setCurrentItem(1);
                break;
            case R.id.scenerelative_id:
            case 2:
                scene_id.setTextColor(golden);
                viewthree.setVisibility(View.VISIBLE);
                viewpager_id.setCurrentItem(2);
                break;
            default:
                break;
        }
    }

    private void startState() {
        clear();
        viewchange(1);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position == 0) {
            menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
            Log.e("robin debug", "ok");
        } else {
            menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
            Log.e("robin debug", "no");
        }
    }

    @Override
    public void onPageSelected(int position) {
        LogUtil.eLength("这是滚动", position + "");
        if (flag) {
            Basecfragment fagmentbase = (Basecfragment) list.get(position);
            fagmentbase.initData();
        }
        flag = true;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 2) {
            clear();
            viewchange(viewpager_id.getCurrentItem());
        }
    }

    @OnClick(R.id.addrelative_id)
    void addrelative_id() {
        //成员不能进行增删该
//        ToastUtil.showToast(getActivity(),"addrelative_id");
        darkenBackgroud(0.7f);
        popupWindow.showAtLocation(addrelative_id, Gravity.TOP | Gravity.RIGHT, 3, addrelative_id.getHeight() + 50);
    }

    @OnClick(R.id.sideslip_id)
    void change_slide_menu() {
        menu.toggle();
    }

    //设置popwindow灰暗程度
    private void darkenBackgroud(Float bgcolor) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgcolor;
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getActivity().getWindow().setAttributes(lp);
    }

    //加载主界面右上角popwindow
    private void addPopwinwow() {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.maindialog, null);
        //选择设备类型,select_dev_type_id

        select_dev_type_id = (RelativeLayout) v.findViewById(R.id.select_dev_type_id);
        select_dev_type_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_dev_type();//选择设备类型
            }
        });


        addmac_id = (RelativeLayout) v.findViewById(R.id.addmac_id);
        addmac_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sraum_setBox_accent();
            }
        });
        addscene_id = (RelativeLayout) v.findViewById(R.id.addscene_id);
        addscene_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), MysceneActivity.class);
                startActivity(intent1);
            }
        });
        addroom_id = (RelativeLayout) v.findViewById(R.id.addroom_id);
        addroom_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentr = new Intent(getActivity(), MacdeviceActivity.class);
                intentr.putExtra("name", "1");
                startActivity(intentr);
                popupWindow.dismiss();
            }
        });
        popupWindow = new PopupWindow(v,
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
//        addrelative_id.setOnClickListener(this);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                darkenBackgroud(1f);
            }
        });
//        addmac_id.setOnClickListener(this);
//        addroom_id.setOnClickListener(this);
//        addscene_id.setOnClickListener(this);
    }

    /**
     * 选择设备类型
     */
    private void select_dev_type() {
        startActivity(new Intent(getActivity(), SelectZigbeeDeviceActivity.class));
        popupWindow.dismiss();//智能设备 ，进入到列表
    }

    private void sraum_setBox_accent() {
        //在这里先调
        //设置网关模式-sraum-setBox
        Map map = new HashMap();
        String phoned = getDeviceId(getActivity());
        map.put("token", TokenUtil.getToken(getActivity()));
        String boxnumber = (String) SharedPreferencesUtil.getData(getActivity(), "boxnumber", "");
        map.put("boxNumber", boxnumber);
        map.put("phoneId", phoned);
        map.put("status", "1");//进入设置模式
        dialogUtil.loadDialog();
        MyOkHttp.postMapObject(ApiHelper.sraum_setBox, map, new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {
                        sraum_setBox_accent();
                    }
                }, getActivity(), dialogUtil) {
                    @Override
                    public void onSuccess(User user) {
                        Intent intent = new Intent(getActivity(), MacdeviceActivity.class);
                        intent.putExtra("name", "2");
                        startActivity(intent);
                        popupWindow.dismiss();//智能设备 ，进入到列表
                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                    }

                    @Override
                    public void wrongBoxnumber() {
                        ToastUtil.showToast(getActivity(), "该网关不存在");
                    }
                }
        );
    }


    @Override
    public void onStart() {//onStart方法是屏幕唤醒时弹执行该方法。
        super.onStart();
        //
        getAllBox();
    }

    /**
     * 获取手机唯一标示码
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        String id;
        //android.telephony.TelephonyManager
        TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephony.getDeviceId() != null) {
            id = mTelephony.getDeviceId();
        } else {
            //android.provider.Settings;
            id = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return id;
    }

    //获取全部网关信息
    private void getAllBox() {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(getActivity()));
//        dialogUtil.loadDialog();
        MyOkHttp.postMapObject(ApiHelper.sraum_getAllBox, map, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {
                getAllBox();
            }
        }, getActivity(), null) {
            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                allboxList = new ArrayList<Allbox>();
                for (User.box bo : user.boxList) {
                    Allbox allbox = new Allbox(bo.type, bo.number, bo.name, bo.status, bo.sign);
                    allboxList.add(allbox);
                }
                if (allboxList.size() == 0) {
                    Log.e("zhu", "no-gateway_allboxlist");
//                    mySlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
//                    mySlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    SharedPreferencesUtil.saveData(getActivity(), "boxnumber", "");
//                    SharedPreferencesUtil.saveData(getActivity(), "sraumboxNumber", "");
                } else {
//                    mySlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//                    mySlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    for (Allbox ab : allboxList) {
                        if (ab.sign.equals("1")) {
                            SharedPreferencesUtil.saveData(getActivity(), "boxstatus", ab.status);
                            SharedPreferencesUtil.saveData(getActivity(), "boxnumber", ab.number);
                            LogUtil.eLength("存储数据", ab.number);
                        }
                    }
                }
                //更新本地的网关
                String boxnumberre = (String) getData(getActivity(), "boxnumber", "");
                if (boxnumberre.equals("")) {
                    Log.e("zhu", "没有网关,boxnumberre:" + boxnumberre);
                    //弹出没有网关，去添加网关
                    dialog.show();
                }
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }

    //删除dialog提示框
    private void deleteDialog() {
        View view = getActivity().getLayoutInflater().inflate(R.layout.check_is_status, null);
        Button checkbutton_id = (Button) view.findViewById(R.id.checkbutton_id);
        checkbutton_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Log.e("zhu", "dialog-close");
                //dialog ->跳转到gatewayFragment界面
                Intent intent = new Intent();
                intent.setAction("UPSTATUS");
                intent.putExtra("type", "no_gateway");
                getActivity().sendBroadcast(intent);
            }
        });
//        dialogBack = new DialogUtil(getActivity(), view);

        dialog = new Dialog(getActivity());
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
//        confirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
        DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();
        int displayWidth = dm.widthPixels;
        int displayHeight = dm.heightPixels;
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); //获取对话框当前的参数值
//        p.width = (int) (displayWidth * 0.55); //宽度设置为屏幕的0.5
//        p.height = (int) (displayHeight * 0.5); //宽度设置为屏幕的0.5
//        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        dialog.getWindow().setAttributes(p);  //设置生效
    }
}
