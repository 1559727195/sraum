package com.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.DbDevice;
import com.Util.DialogUtil;
import com.Util.IntentUtil;
import com.Util.LogUtil;
import com.Util.MusicUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.SharedPreferencesUtil;
import com.Util.Timeuti;
import com.Util.ToastUtil;
import com.Util.TokenUtil;
import com.adapter.MacFragAdapter;
import com.andview.refreshview.XRefreshView;
import com.base.Basecfragment;
import com.data.Allbox;
import com.data.User;
import com.massky.sraum.AirControlActivity;
import com.massky.sraum.LamplightActivity;
import com.massky.sraum.MacdetailActivity;
import com.massky.sraum.MacdeviceActivity;
import com.massky.sraum.Pm25Activity;
import com.massky.sraum.R;
import com.massky.sraum.TVShowActivity;
import com.massky.sraum.WaterSensorActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.InjectView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;

import static android.R.attr.action;
import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.start;
import static cn.ciaapp.sdk.CIAService.context;
import static cn.jiguang.c.a.n;

/**
 * Created by masskywcy on 2016-09-05.
 */
/*用于第一个fragment主界面*/
public class MacFragment extends Basecfragment implements
        AdapterView.OnItemClickListener {
    @InjectView(R.id.bottomimage_id)
    ImageView bottomimage_id;
    @InjectView(R.id.addmacbtn_id)
    Button addmacbtn_id;
    @InjectView(R.id.addmacrela_id)
    RelativeLayout addmacrela_id;
    @InjectView(R.id.macfragritview_id)
    GridView macfragritview_id;
    @InjectView(R.id.refresh_view)
    XRefreshView refresh_view;
    @InjectView(R.id.macstatus)
    TextView macstatus;
    private DialogUtil dialogUtil;
    private MacFragAdapter adapter;
    private DbDevice dbDevice;
    //网关编号,token
    private String loginPhone, status = "";
    //震动和音乐的判断值
    private boolean vibflag, musicflag;
    private ImageView lightimage_id, airimage_id, curimage_id;
    private List<User.device> list = new ArrayList<>();
    //进行判断是否进行创建刷新
    private boolean isFirstIn = true;
    private RelativeLayout itemrela_id;
    private List<String> listtype = new ArrayList();
    private List<Allbox> allboxList = new ArrayList<Allbox>();
    private int index_toggen;
    private List<Map<String, Object>> listob;
    private boolean isjpush;

    @Override
    protected int viewId() {
        return R.layout.macfragment;
    }

    @Override
    public void onStart() {//onStart()-这个方法在屏幕唤醒时调用。
        super.onStart();
//        boolean tokenflag = TokenUtil.getTokenflag(getActivity());
//        if (tokenflag) {
//            if (!TokenUtil.getBoxnumber(getActivity()).equals("")) {
//                LogUtil.eLength("查看方法是否走起", "方法走动");
//                upload(true);
//                LogUtil.i("这是设备长度" + list.size());
//                for (User.device ud : list) {
//                    listtype.add(ud.status);
//                }
//            }
//        }
        android.util.Log.e("fei", "MacFragment->onStart():name:");
        if (!TokenUtil.getBoxnumber(getActivity()).equals("")) {
            LogUtil.eLength("查看方法是否走起", "方法走动");
            upload(true);
            LogUtil.i("这是设备长度" + list.size());
            for (User.device ud : list) {
                listtype.add(ud.status);
            }
        }
    }

    //判断网关是否在线
    private void boxStatus(boolean boxFlag, int sceflag) {
        if (boxFlag) {
            if (sceflag == 0) {
                addmacrela_id.setVisibility(View.VISIBLE);
                refresh_view.setVisibility(View.GONE);
            } else {
                addmacrela_id.setVisibility(View.GONE);
                refresh_view.setVisibility(View.VISIBLE);
            }
            macstatus.setVisibility(View.GONE);
            macfragritview_id.setVisibility(View.VISIBLE);
        } else {
            macstatus.setVisibility(View.VISIBLE);
            addmacrela_id.setVisibility(View.GONE);
            macfragritview_id.setVisibility(View.GONE);
        }
    }

    @Override

    protected void onView() {
        registerMessageReceiver();
        dbDevice = new DbDevice(getActivity());
        loginPhone = (String) SharedPreferencesUtil.getData(getActivity(), "loginPhone", "");
        SharedPreferences preferences = getActivity().getSharedPreferences("sraum" + loginPhone,
                Context.MODE_PRIVATE);
        vibflag = preferences.getBoolean("vibflag", false);
        musicflag = preferences.getBoolean("musicflag", false);
        LogUtil.i("这是震动二" + musicflag);
        View bottomview = getActivity().getLayoutInflater().inflate(R.layout.macfbottom, null);
        lightimage_id = (ImageView) bottomview.findViewById(R.id.lightimage_id);
        airimage_id = (ImageView) bottomview.findViewById(R.id.airimage_id);
        curimage_id = (ImageView) bottomview.findViewById(R.id.curimage_id);
        dialogUtil = new DialogUtil(getActivity(), bottomview, 1);
        bottomimage_id.setOnClickListener(this);
        addmacbtn_id.setOnClickListener(this);
        lightimage_id.setOnClickListener(this);
        airimage_id.setOnClickListener(this);
        curimage_id.setOnClickListener(this);
        refresh_view.setScrollBackDuration(300);
        refresh_view.setPinnedTime(1000);
        refresh_view.setPullLoadEnable(false);

        adapter = new MacFragAdapter(getActivity(), list);
        macfragritview_id.setAdapter(adapter);

        refresh_view.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
//                if (TokenUtil.getTokenflag(getActivity())) {
//                    if (TokenUtil.getBoxnumber(getActivity()).equals("")) {
//                        list.clear();
//                        adapter = new MacFragAdapter(getActivity(), list);
//                        macfragritview_id.setAdapter(adapter);
//                        refresh_view.stopRefresh();
//                    } else {
//                        upload(false);
//                    }
//                } else {
//                    refresh_view.stopRefresh();
//                }

                if (TokenUtil.getBoxnumber(getActivity()).equals("")) {
                    list.clear();
                    adapter = new MacFragAdapter(getActivity(), list);
                    macfragritview_id.setAdapter(adapter);
                    refresh_view.stopRefresh();
                } else {
                    upload(false);
                }
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
            }
        });
        macfragritview_id.setOnItemClickListener(this);
    }

    @Override
    public void initData() {
        SharedPreferencesUtil.saveData(getActivity(), "pagetag", "1");
//        boolean flag = TokenUtil.getTokenflag(getActivity());
//        if (flag) {
//            if (!TokenUtil.getBoxnumber(getActivity()).equals("")) {
//                upload(true);
//            } else {
//                list.clear();
//                adapter = new MacFragAdapter(getActivity(), list);
//                macfragritview_id.setAdapter(adapter);
//            }
//        }

        if (!TokenUtil.getBoxnumber(getActivity()).equals("")) {
            upload(true);
        } else {
            list.clear();
            adapter = new MacFragAdapter(getActivity(), list);
            macfragritview_id.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottomimage_id:
                dialogUtil.loadViewBottomdialog();
                break;
            case R.id.addmacbtn_id:
                Intent intent = new Intent(getActivity(), MacdeviceActivity.class);
                intent.putExtra("name", "2");
                startActivity(intent);
                break;
            //进行分类   灯光，空调，窗帘
            case R.id.lightimage_id:
                getList("2", "1");
                break;
            case R.id.airimage_id:
                getList("3", "0");
                break;
            case R.id.curimage_id:
                getList("4", "0");
                break;
        }
    }

    private void getList(String type, String typetwo) {
        List<User.device> listud = new ArrayList<>();
        if (typetwo.equals("1")) {
            for (User.device d : list) {
                if (d.type.equals("2") || d.type.equals("1")) {
                    listud.add(d);
                }
            }
        } else {
            for (User.device d : list) {
                if (d.type.equals(type)) {
                    listud.add(d);
                }
            }
        }
        LogUtil.e("查看长度", listud.size() + "");
        adapter = new MacFragAdapter(getActivity(), listud);
        macfragritview_id.setAdapter(adapter);
        dialogUtil.removeviewBottomDialog();
    }

    //下拉刷新
    private void upload(final boolean flag) {
        Map<String, String> mapdevice = new HashMap<>();
        mapdevice.put("token", TokenUtil.getToken(getActivity()));
        mapdevice.put("boxNumber", TokenUtil.getBoxnumber(getActivity()));
        if (flag) {
            dialogUtil.loadDialog();
        }
        uploader_refresh(mapdevice);

}

    private void uploader_refresh(final Map<String, String> mapdevice) {
        MyOkHttp.postMapString(ApiHelper.sraum_getAllDevice, mapdevice, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {//刷新togglen数据
                Map<String, String> mapdevice = new HashMap<>();
                mapdevice.put("token", TokenUtil.getToken(getActivity()));
                mapdevice.put("boxNumber", TokenUtil.getBoxnumber(getActivity()));
                uploader_refresh(mapdevice);
            }
        }, getActivity(), dialogUtil) {
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                refresh_view.stopRefresh();
            }

            @Override
            public void pullDataError() {
                super.pullDataError();
                refresh_view.stopRefresh();
            }

            @Override
            public void emptyResult() {
                super.emptyResult();
                refresh_view.stopRefresh();
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
                refresh_view.stopRefresh();
                //重新去获取togglen,这里是因为没有拉到数据所以需要重新获取togglen

            }

            @Override
            public void wrongBoxnumber() {
                super.wrongBoxnumber();
                refresh_view.stopRefresh();
            }

            @Override
            public void onSuccess(final User user) {
                Observable.create(new ObservableOnSubscribe<User>() {
                    @Override
                    public void subscribe(ObservableEmitter<User> emitter) throws Exception {
                        emitter.onNext(user);//耗时动作
                    }
                }).timeout(5, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<User>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(User user) {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                super.onSuccess(user);
                refresh_view.stopRefresh();
                list.clear();
                listtype.clear();
                list.addAll(user.deviceList);
                for (User.device ud : list) {
                    listtype.add(ud.status);
                }
                LogUtil.i("这是设备长度2", "" + list.size());
                macstatus.setVisibility(View.GONE);
                boxStatus(TokenUtil.getBoxflag(getActivity()), list.size());
//                adapter = new MacFragAdapter(getActivity(), list);
//                macfragritview_id.setAdapter(adapter);
                adapter.clear();
                adapter.addAll(list);//不要new adapter
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        View v = parent.getChildAt(position - macfragritview_id.getFirstVisiblePosition());
        itemrela_id = (RelativeLayout) v.findViewById(R.id.itemrela_id);
        Map<String, Object> mapalldevice = new HashMap<String, Object>();
        listob = new ArrayList<Map<String, Object>>();
        if (listtype.get(position).equals("1")) {
            status = "0";
        } else {
            status = "1";
        }
        Map<String, Object> mapdevice = new HashMap<String, Object>();
        mapdevice.put("type", list.get(position).type);
        mapdevice.put("number", list.get(position).number);
        mapdevice.put("status", status);
        mapdevice.put("dimmer", list.get(position).dimmer);
        mapdevice.put("mode", list.get(position).mode);
        mapdevice.put("temperature", list.get(position).temperature);
        mapdevice.put("speed", list.get(position).speed);

        mapdevice.put("name", list.get(position).name);
        mapdevice.put("panelName", list.get(position).panelName);

        listob.add(mapdevice);
        mapalldevice.put("token", TokenUtil.getToken(getActivity()));
        mapalldevice.put("boxNumber", TokenUtil.getBoxnumber(getActivity()));
        mapalldevice.put("deviceInfo", listob);

        switch (list.get(position).type) {
            case "7":
            case "8":
            case "9":
            case "10":
            case "11":
              //special_type_device(mapdevice);
                //test_pm25();
               // test_tv();
                //test_air_control();
                water_sensor();
                break;
            default:
                curtains_and_light(position, mapalldevice);
                break;

        }
    }

    /**
     * 测试水浸传感器
     */
    private void water_sensor() {

        startActivity(new Intent(getActivity(),WaterSensorActivity.class));
    }

    /**
     * 测试空调
     */
    private void test_air_control() {
        startActivity(new Intent(getActivity(),AirControlActivity.class));
    }

    /**
     * 测试电视
     */
    private void test_tv() {
        startActivity(new Intent(getActivity(),TVShowActivity.class));
    }

    /**
     * 测试pm2.5
     */
    private void test_pm25() {
        startActivity(new Intent(getActivity(), Pm25Activity.class));
    }

    /**
     * 门磁，水浸，人体感应，入墙PM2.5
     *
     * @param mapalldevice
     */
    private void special_type_device(Map<String, Object> mapalldevice) {
        Bundle bundle = new Bundle();
        bundle.putString("type", (String) mapalldevice.get("type"));
        bundle.putString("name", (String) mapalldevice.get("name"));
        bundle.putString("number", (String) mapalldevice.get("number"));
        bundle.putString("name1", (String) mapalldevice.get("name1"));
        bundle.putString("name2", (String) mapalldevice.get("name2"));
        bundle.putString("panelName", (String) mapalldevice.get("panelName"));

        bundle.putString("status", (String) mapalldevice.get("status"));
        bundle.putString("dimmer", (String) mapalldevice.get("dimmer"));
        bundle.putString("mode", (String) mapalldevice.get("mode"));
        IntentUtil.startActivity(getActivity(), MacdetailActivity.class, bundle);
    }

    /**
     * curtains and light,窗帘与灯
     *
     * @param position
     * @param mapalldevice
     */
    private void curtains_and_light(int position, Map<String, Object> mapalldevice) {
        if (list.get(position).type.equals("1")) {
            String boxstatus = TokenUtil.getBoxstatus(getActivity());
            if (!boxstatus.equals("0")) {
                getBoxStatus(mapalldevice, position);
            }
        } else {
            /*窗帘所需要的属性值*/
            Log.e("zhu", "chuanglian:" + "窗帘所需要的属性值");
            Bundle bundle = new Bundle();
            bundle.putString("type", list.get(position).type);
            bundle.putString("number", list.get(position).number);
            bundle.putString("name1", list.get(position).name1);
            bundle.putString("name2", list.get(position).name2);
            bundle.putString("name", list.get(position).name);
            LogUtil.eLength("名字", list.get(position).name1 + list.get(position).name2);
            IntentUtil.startActivity(getActivity(), LamplightActivity.class, bundle);
        }
    }

    private void getBoxStatus(final Map<String, Object> mapdevice, final int position) {
        Map<String, String> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(getActivity()));
        map.put("boxNumber", TokenUtil.getBoxnumber(getActivity()));
        dialogUtil.loadDialog();
        get_mac_fragment(mapdevice, position, map);
    }

    private void get_mac_fragment(final Map<String, Object> mapdevice, final int position, final Map<String, String> map) {
        MyOkHttp.postMapString(ApiHelper.sraum_getBoxStatus, map, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {//这个是获取togglen来刷新数据
                getBoxStatus(mapdevice, position);
            }
        }, getActivity(), dialogUtil) {
            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                switch (user.status) {
                    case "1":
                        sraum_device_control(mapdevice);
                        break;
                    case "0":
                        //网关离线
                        ToastUtil.showDelToast(getActivity(), "网关处于离线状态");
                        break;
                    default:
                        break;
                }
            }

            /**
             * sraum_device_control
             * @param
             */
            private void sraum_device_control(Map<String, Object> mapdevice) {
                MyOkHttp.postMapObject(ApiHelper.sraum_deviceControl, mapdevice, new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {
                        Map<String, Object> mapalldevice = new HashMap<String, Object>();
                        mapalldevice.put("token", TokenUtil.getToken(getActivity()));
                        mapalldevice.put("boxNumber", TokenUtil.getBoxnumber(getActivity()));
                        mapalldevice.put("deviceInfo", listob);
                        sraum_device_control(mapalldevice);
                    }
                }, getActivity(), dialogUtil) {
                    @Override
                    public void fourCode() {
                        super.fourCode();
                        ToastUtil.showToast(getActivity(), "操作失败");
                    }

                    @Override
                    public void onSuccess(User user) {
                        super.onSuccess(user);
                        ToastUtil.showToast(getActivity(), "操作成功");
                        if (vibflag) {
                            Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(200);
                        }
                        if (musicflag) {
                            LogUtil.i("铃声响起");
                            MusicUtil.startMusic(getActivity(), 1);
                        } else {
                            MusicUtil.stopMusic(getActivity());
                        }
                        listtype.set(position, status);
                        String string = listtype.get(position);
                        if (string.equals("1")) {
                            itemrela_id.setBackgroundResource(R.drawable.markstarh);
                        } else {
                            itemrela_id.setBackgroundResource(R.drawable.markh);
                        }
                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                    }
                });
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    public MessageReceiver mMessageReceiver;
    public static String ACTION_INTENT_RECEIVER = "com.massky.jr.treceiver";

    public static String ACTION_INTENT_RECEIVER_TO_SECOND_PAGE = "com.massky.secondpage.treceiver";

    /**
     * 动态注册广播
     */
    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_INTENT_RECEIVER);
        getActivity().registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals(ACTION_INTENT_RECEIVER)) {
                int messflag = intent.getIntExtra("notifactionId", 0);
                if (messflag == 1 || messflag == 3 || messflag == 4 || messflag == 5) {
                    upload(false);//控制部分，推送刷新；主动推送刷新。
                    Log.e("zhu", "upload(false):" + "upload(false)" + "messflag:" + messflag);
                    //控制部分的二级页面进去要同步更新推送的信息显示 （推送的是消息）。
                    sendBroad();
                    //推送过来的
//                    ToastUtil.showToast(getActivity(), "我控制的设备时推送过来的" + ",messflag:" + messflag);
                }
            }
        }
    }

    private void sendBroad() {
        Intent mIntent = new Intent(ACTION_INTENT_RECEIVER_TO_SECOND_PAGE);
        getActivity().sendBroadcast(mIntent);
    }

    @Override
    public void onResume() {
        super.onResume();
        android.util.Log.e("peng", "MacFragment->onResume:name:");
    }
}
