package com.massky.sraum;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.percent.PercentRelativeLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.DialogUtil;
import com.Util.IntentUtil;
import com.Util.LogUtil;
import com.Util.MusicUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.MycallbackNest;
import com.Util.SharedPreferencesUtil;
import com.Util.ToastUtil;
import com.Util.TokenUtil;
import com.base.Basecactivity;
import com.data.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;
import okhttp3.Call;

import static com.fragment.MacFragment.ACTION_INTENT_RECEIVER_TO_SECOND_PAGE;
import static com.massky.sraum.R.id.open_kong_tiao;
import static okhttp3.Protocol.get;

/**
 * Created by masskywcy on 2016-11-17.
 */
//用于首页设置item灯光设置
public class LamplightActivity extends Basecactivity implements SeekBar.OnSeekBarChangeListener {
    @InjectView(R.id.titlecen_id)
    TextView titlecen_id;
    @InjectView(R.id.backrela_id)
    RelativeLayout backrela_id;
    @InjectView(R.id.mainairrea_id)
    RelativeLayout mainairrea_id;
    @InjectView(R.id.noairconditioned_id)
    RelativeLayout noairconditioned_id;
    @InjectView(R.id.tempmark_id)
    ImageView tempmark_id;
    @InjectView(R.id.windspeed_id)
    TextView windspeed_id;
    @InjectView(R.id.tempstate_id)
    TextView tempstate_id;
    @InjectView(R.id.windspeedrelative)
    RelativeLayout windspeedrelative;
    @InjectView(R.id.switchrelative)
    RelativeLayout switchrelative;
    @InjectView(R.id.patternrelative)
    RelativeLayout patternrelative;
    @InjectView(R.id.dimmerrelative)
    RelativeLayout dimmerrelative;
    @InjectView(R.id.id_seekBar)
    SeekBar id_seekBar;
    @InjectView(R.id.tempimage_id)
    TextView tempimage_id;
    @InjectView(R.id.statusopen)
    ImageView statusopen;
    @InjectView(R.id.dimmerimageview)
    ImageView dimmerimageview;
    @InjectView(R.id.curditextone_id)
    TextView curditextone_id;
    @InjectView(R.id.curditextonetwo_id)
    TextView curditextonetwo_id;
    @InjectView(R.id.curditextthree_id)
    TextView curditextthree_id;
    @InjectView(R.id.curopenrelativethree_id)
    RelativeLayout curopenrelativethree_id;
    @InjectView(R.id.curopenrelativetwo_id)
    RelativeLayout curopenrelativetwo_id;
    @InjectView(R.id.curopenrelative_id)
    RelativeLayout curopenrelative_id;
    @InjectView(R.id.curimage_id)
    ImageView curimage_id;
    @InjectView(R.id.curimagetwo_id)
    ImageView curimagetwo_id;
    @InjectView(R.id.curimagethree_id)
    ImageView curimagethree_id;
    @InjectView(R.id.curoffrelative_id)
    RelativeLayout curoffrelative_id;
    @InjectView(R.id.curoffrelativetwo_id)
    RelativeLayout curoffrelativetwo_id;
    @InjectView(R.id.curoffrelativethree_id)
    RelativeLayout curoffrelativethree_id;
    @InjectView(R.id.curoffimathree_id)
    ImageView curoffimathree_id;
    @InjectView(R.id.curoffimatwo_id)
    ImageView curoffimatwo_id;
    @InjectView(R.id.curoffima_id)
    ImageView curoffima_id;
    @InjectView(R.id.sucrela)
    RelativeLayout sucrela;
    @InjectView(R.id.sucrelaimage)
    ImageView sucrelaimage;
    @InjectView(R.id.windspeedtwo_id)
    TextView windspeedtwo_id;
    @InjectView(R.id.open_kong_tiao)
    PercentRelativeLayout    open_kong_tiao;
    @InjectView(R.id.openbtn_tiao_guang)
    ImageView openbtn_tiao_guang;
    @InjectView(R.id.open_tiaoguangdeng)
    PercentRelativeLayout open_tiaoguangdeng;

    //设备类型和设备编号，token，网关编号，状态值,调光值,空调模式，空调温度
    private String type, number, boxnumber, statusflag, dimmer, modeflag = "1",
            temperature, windflag = "1", loginPhone, name1, name2, name, curtain = "",
            flagone, flagtwo, flagthree;
    private DialogUtil dialogUtil;
    //whriteone,whritetwo,whritethree进行判断是否清空操作
    private boolean vibflag, musicflag, statusbo = true,
            mapflag, addflag = true, whriteone = true, whritetwo = true, whritethree = true;
    private MessageReceiver mMessageReceiver;

    @Override
    protected int viewId() {
        return R.layout.mainpageitemone;
    }

    @Override
    protected void onView() {
        registerMessageReceiver();
        loginPhone = (String) SharedPreferencesUtil.getData(LamplightActivity.this, "loginPhone", "");
        SharedPreferences preferences = getSharedPreferences("sraum" + loginPhone,
                Context.MODE_PRIVATE);
        vibflag = preferences.getBoolean("vibflag", false);
        musicflag = preferences.getBoolean("musicflag", false);
        LogUtil.i("查看值状态" + musicflag);
        boxnumber = (String) SharedPreferencesUtil.getData(LamplightActivity.this, "boxnumber", "");
        dialogUtil = new DialogUtil(LamplightActivity.this);
        backrela_id.setOnClickListener(this);
        windspeedrelative.setOnClickListener(this);
        patternrelative.setOnClickListener(this);
        id_seekBar.setOnSeekBarChangeListener(this);
        switchrelative.setOnClickListener(this);
        //调光的开关状态
        dimmerrelative.setOnClickListener(this);
        //窗帘全开点击
        curopenrelativethree_id.setOnClickListener(this);
        //窗帘全关点击
        curoffrelativethree_id.setOnClickListener(this);
        //窗帘1开关状态
        //点击窗帘1打开
        curopenrelative_id.setOnClickListener(this);
        //点击窗帘1关闭
        curoffrelative_id.setOnClickListener(this);
        //点击窗帘2打开
        curopenrelativetwo_id.setOnClickListener(this);
        //点击窗帘1关闭
        curoffrelativetwo_id.setOnClickListener(this);

        //调光控制
        openbtn_tiao_guang.setOnClickListener(this);
        //暂停
        sucrela.setOnClickListener(this);
        Bundle bundle = IntentUtil.getIntentBundle(LamplightActivity.this);
        type = bundle.getString("type");
        number = bundle.getString("number");
        name1 = bundle.getString("name1");
        name2 = bundle.getString("name2");
        name = bundle.getString("name");
        LogUtil.eLength("查看值", name1 + name2);
        curditextone_id.setText(name1);
        curditextonetwo_id.setText(name2);
        curditextthree_id.setText(name);
        titlecen_id.setText(name);
        //根据类型判断界面（除却类型为1的灯泡）
        switch (type) {
            //调光灯
            case "2":
                Log.e("peng","tiaoguangdeng");
                tempimage_id.setText("0");
                noairconditioned_id.setVisibility(View.VISIBLE);
                //调光的开关状态
                dimmerrelative.setVisibility(View.GONE);
                mainairrea_id.setVisibility(View.GONE);
                tempmark_id.setVisibility(View.GONE);
                windspeed_id.setVisibility(View.GONE);
                tempstate_id.setVisibility(View.GONE);
                windspeedrelative.setVisibility(View.GONE);
                switchrelative.setVisibility(View.GONE);
                patternrelative.setVisibility(View.GONE);
                open_kong_tiao.setVisibility(View.GONE);
                open_tiaoguangdeng.setVisibility(View.VISIBLE);
                break;
            //空调
            case "3":
                //判断展示值是否加16
                addflag = false;
                tempimage_id.setText("16");
                id_seekBar.setMax(14);
                noairconditioned_id.setVisibility(View.VISIBLE);
                mainairrea_id.setVisibility(View.GONE);
                break;
            //窗帘
            case "4":
                noairconditioned_id.setVisibility(View.GONE);
                mainairrea_id.setVisibility(View.VISIBLE);
                break;
            //新风设备
            case "5":
                addflag = false;
                tempimage_id.setText("16");
                id_seekBar.setMax(14);
                noairconditioned_id.setVisibility(View.VISIBLE);
                mainairrea_id.setVisibility(View.GONE);
                patternrelative.setVisibility(View.GONE);
                tempstate_id.setVisibility(View.GONE);
                windspeedtwo_id.setVisibility(View.VISIBLE);
                windspeed_id.setVisibility(View.GONE);
                break;
            //地暖
            case "6":
                addflag = false;
            tempimage_id.setText("16");//最低温度为16度 + 14 =30 度
            id_seekBar.setMax(14);
            noairconditioned_id.setVisibility(View.VISIBLE);
            mainairrea_id.setVisibility(View.GONE);
            patternrelative.setVisibility(View.GONE);
            tempstate_id.setVisibility(View.GONE);
            windspeedtwo_id.setVisibility(View.VISIBLE);
            windspeed_id.setVisibility(View.GONE);
            break;
        }
        id_seekBar.setOnTouchListener(new View.OnTouchListener() {//这个是根据网关状态在线情况，不在线的话，seekBar就不能滑动了，
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("zhu","id_seekBar->:" +statusbo);
                return !statusbo;
            }
        });
        //下载设备信息
        upload();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backrela_id:
                LamplightActivity.this.finish();
                break;
            //窗帘1打开
            case R.id.curopenrelative_id:
                curtain = "1";
                getMapdevice();
                break;
            //窗帘1关闭
            case R.id.curoffrelative_id:
                curtain = "2";
                getMapdevice();
                break;
            //窗帘2打开
            case R.id.curopenrelativetwo_id:
                curtain = "3";
                getMapdevice();
                break;
            //窗帘2关闭
            case R.id.curoffrelativetwo_id:
                curtain = "4";
                getMapdevice();
                break;
            //窗帘全开点击
            case R.id.curopenrelativethree_id:
                curtain = "5";
                getMapdevice();
                break;
            //窗帘全关点击
            case R.id.curoffrelativethree_id:
                curtain = "6";
                getMapdevice();
                break;
            //窗帘全关点击
            case R.id.sucrela:
                curtain = "7";
                getMapdevice();
                break;
            case R.id.windspeedrelative:
                mapflag = false;
                if (statusbo) {
                    //风速状态
                    switch (windflag) {
                        case "1":
                            windspeedtwo_id.setText("中风");
                            windspeed_id.setText("中风");
                            windflag = "2";
                            break;
                        case "2":
                            windspeedtwo_id.setText("高风");
                            windspeed_id.setText("高风");
                            windflag = "3";
                            break;
                        case "3":
                            windspeedtwo_id.setText("强力");
                            windspeed_id.setText("强力");
                            windflag = "4";
                            break;
                        case "4":
                            windspeedtwo_id.setText("送风");
                            windspeed_id.setText("送风");
                            windflag = "5";
                            break;
                        case "5":
                            windspeedtwo_id.setText("自动");
                            windspeed_id.setText("自动");
                            windflag = "6";
                            break;
                        case "6":
                            windspeedtwo_id.setText("低风");
                            windspeed_id.setText("低风");
                            windflag = "1";
                            break;
                        default:
                            break;
                    }
                    getMapdevice();
                }
                break;
            case R.id.patternrelative:
                mapflag = false;
                if (statusbo) {
                    setMode();
                    getMapdevice();
                }
                break;
            //空调开关状态
            case R.id.switchrelative:
                mapflag = true;
                getMapdevice();
                break;
            //调光的开关状态
            case R.id.openbtn_tiao_guang:
                mapflag = true;
                getMapdevice();
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.e("zhu","onProgressChanged: " + progress);
        if (addflag) {
            tempimage_id.setText(progress + "");
        } else {
            tempimage_id.setText((16 + progress) + "");
        }
        Log.e("zhu","tempimage_id.setText: " + tempimage_id.getText().toString());
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        LogUtil.i("开始滑动", "onStartTrackingTouch: ");
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        LogUtil.i("停止滑动", "onStopTrackingTouch: ");
        //停止滑动是的状态
        mapflag = false;
        if (statusbo) {
            getMapdevice();//控制
        }
    }

    //控制设备
    private void getMapdevice() {
        Map<String, Object> mapalldevice = new HashMap<String, Object>();
        List<Map<String, Object>> listob = new ArrayList<Map<String, Object>>();
        Map<String, Object> mapdevice = new HashMap<String, Object>();
        switch (type) {
            //调光灯
            case "2":
                dimmer = tempimage_id.getText().toString();
                temperature = modeflag = "";
                windflag = "";
                break;
            //空调
            case "3":
                temperature = tempimage_id.getText().toString();
                break;
            //窗帘
            case "4":
                break;
            //新风
            case "5":
                modeflag = dimmer = "";
                temperature = tempimage_id.getText().toString();
                break;
            //地暖
            case "6":
                modeflag = dimmer = "";
                temperature = tempimage_id.getText().toString();
                break;
            default:
                break;
        }
        mapdevice.put("type", type);
        mapdevice.put("number", number);
        //两个特别全开全关设置
        if (type.equals("4")) {
            String statusm = "";
            switch (curtain) {
                //窗帘1打开
                case "1":
                    statusm = "3";
                    break;
                //窗帘1关闭
                case "2":
                    statusm = "4";
                    break;
                //窗帘2打开
                case "3":
                    statusm = "5";
                    break;
                //窗帘2关闭
                case "4":
                    statusm = "6";
                    break;
                //5代表全开
                case "5":
                    statusm = "1";
                    break;
                //6代表全关
                case "6":
                    statusm = "0";
                    break;
                //7代表暂停
                case "7":
                    statusm = "2";
                    break;
                default:
                    break;
            }
            mapdevice.put("status", statusm);
        } else {
            if (mapflag) {
                mapdevice.put("status", statusflag);
            } else {
                mapdevice.put("status", "1");
            }
        }
        Log.e("zhu","dimmer:" + dimmer);
        mapdevice.put("dimmer", dimmer);
        mapdevice.put("mode", modeflag);
        mapdevice.put("temperature", temperature);
        mapdevice.put("speed", windflag);
        listob.add(mapdevice);
        mapalldevice.put("token", TokenUtil.getToken(LamplightActivity.this));
        mapalldevice.put("boxNumber", boxnumber);
        mapalldevice.put("deviceInfo", listob);
        LogUtil.eLength("真正传入", new Gson().toJson(mapalldevice));
        getBoxStatus(mapalldevice);
    }

    private void getBoxStatus(final Map<String, Object> mapdevice) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(LamplightActivity.this));
        map.put("boxNumber", boxnumber);
        dialogUtil.loadDialog();
        getBoxStatus_read(mapdevice, map);
    }

    /**
     * getBoxStatus_read
     * @param mapdevice
     * @param map
     */
    private void getBoxStatus_read(final Map<String, Object> mapdevice, final Map<String, Object> map) {
        MyOkHttp.postMapObjectnest(ApiHelper.sraum_getBoxStatus, map,new MycallbackNest(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {//刷新togglen获取的数据
                Map<String, Object> map = new HashMap<>();
                map.put("token", TokenUtil.getToken(LamplightActivity.this));
                map.put("boxNumber", boxnumber);
                getBoxStatus_read(mapdevice, map);
            }
        }, LamplightActivity.this, dialogUtil) {
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                ToastUtil.showDelToast(LamplightActivity.this, "网络连接超时");
            }

            @Override
            public void onSuccess(User user) {
                switch (user.status) {
                    case "1":
                        sraum_device_control(mapdevice);
                        break;
                    case "0":
                        //网关离线
                        ToastUtil.showDelToast(LamplightActivity.this, "网关处于离线状态");
                        break;
                    default:
                        break;
                }
            }

            private void sraum_device_control(Map<String, Object> mapdevice) {
                List<Map> list = (List<Map>) mapdevice.get("deviceInfo");
                Log.e("zhu", "mapdevice->diming:" + list.get(0).get("dimmer"));
                MyOkHttp.postMapObject(ApiHelper.sraum_deviceControl, mapdevice, new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {
                        Map<String, Object> map = new HashMap<>();
                        map.put("token", TokenUtil.getToken(LamplightActivity.this));
                        map.put("boxNumber", boxnumber);
                        sraum_device_control(map);

                    }
                }, LamplightActivity.this, dialogUtil) {
                    @Override
                    public void onSuccess(User user) {
                        super.onSuccess(user);
                        if (type.equals("4")) {
                            switch (curtain) {
                                //窗帘1打开
                                case "1":
                                    flagone = "1";
                                    flagtwo = "3";
                                    flagthree = "3";
                                    whriteone = true;
                                    whritetwo = false;
                                    whritethree = true;
                                    break;
                                //窗帘1关闭
                                case "2":
                                    flagone = "2";
                                    flagtwo = "3";
                                    flagthree = "3";
                                    whriteone = true;
                                    whritetwo = false;
                                    whritethree = true;
                                    break;
                                //窗帘2打开
                                case "3":
                                    flagone = "3";
                                    flagtwo = "1";
                                    flagthree = "3";
                                    whriteone = false;
                                    whritetwo = true;
                                    whritethree = true;
                                    break;
                                //窗帘2关闭
                                case "4":
                                    flagone = "3";
                                    flagtwo = "2";
                                    flagthree = "3";
                                    whriteone = false;
                                    whritetwo = true;
                                    whritethree = true;
                                    break;
                                //全开
                                case "5":
                                    flagone = "1";
                                    flagtwo = "1";
                                    flagthree = "1";
                                    whriteone = true;
                                    whritetwo = true;
                                    whritethree = true;
                                    break;
                                //全关
                                case "6":
                                    flagone = "2";
                                    flagtwo = "2";
                                    flagthree = "2";
                                    whriteone = true;
                                    whritetwo = true;
                                    whritethree = true;
                                    break;
                                //暂停
                                case "7":
                                    flagone = "3";
                                    flagtwo = "3";
                                    flagthree = "4";
                                    whriteone = true;
                                    whritetwo = true;
                                    whritethree = true;
                                    break;
                                default:
                                    break;
                            }
                            switchState(flagone, flagtwo, flagthree);
                        } else {
                            LogUtil.eLength("查看", mapflag + "");
                            if (mapflag) {
                                if (statusflag.equals("1")) {
                                    //调光灯开关状态
                                    LogUtil.eLength("方法走起", "是否走了");
//                                    dimmerrelative.setBackgroundResource(R.drawable.hsmall_black);
//                                    dimmerimageview.setImageResource(R.drawable.hairclose_word);
                                    openbtn_tiao_guang.setImageResource(R.drawable.guan_white_word);
                                    //空调开关状态
                                    switchrelative.setBackgroundResource(R.drawable.hsmall_black);
                                    statusopen.setImageResource(R.drawable.hairclose_word);
                                    statusflag = "0";
                                    statusbo = true;
                                } else {
                                    //调光灯开关状态
                                    LogUtil.eLength("确实走了", "是否走了");
//                                    dimmerrelative.setBackgroundResource(R.drawable.hsmall_circle);
//                                    dimmerimageview.setImageResource(R.drawable.hopen);
                                    openbtn_tiao_guang.setImageResource(R.drawable.open_black_word);
                                    //空调开关状态
                                    switchrelative.setBackgroundResource(R.drawable.hsmall_circle);
                                    statusopen.setImageResource(R.drawable.hopen);
                                    statusflag = "1";
                                    statusbo = false;
                                }
                            }
                        }
                        if (vibflag) {
                            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(200);
                        }
                        if (musicflag) {
                            MusicUtil.startMusic(LamplightActivity.this, 1);
                        } else {
                            MusicUtil.stopMusic(LamplightActivity.this);
                        }
                    }
                });
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }

    //下载设备信息并且比较状态（为了显示开关状态）
    private void upload() {
        Map<String, String> mapdevice = new HashMap<>();
        mapdevice.put("token", TokenUtil.getToken(LamplightActivity.this));
        mapdevice.put("boxNumber", boxnumber);
        dialogUtil.loadDialog();
        SharedPreferencesUtil.saveData(LamplightActivity.this, "boxnumber", boxnumber);
        MyOkHttp.postMapString(ApiHelper.sraum_getAllDevice, mapdevice, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {//获取togglen成功后重新刷新数据
                upload();
            }
        }, LamplightActivity.this, dialogUtil) {
            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                //拿到设备状态值
                for (User.device d : user.deviceList) {
                    if (d.number.equals(number)) {
                        //匹配状值设置当前状态
                        if (d.status != null) {
                            //进行判断是否为窗帘
                            statusflag = d.status;
                            LogUtil.eLength("下载数据", statusflag);
                            if (type.equals("4")) {
                                switch (d.status) {
                                    case "0":
                                        flagone = "2";// 2 -全关，1- 开 ， 3 -
                                        flagtwo = "2";
                                        flagthree = "2";
                                        break;
                                    case "1":
                                        flagone = "1";
                                        flagtwo = "1";
                                        flagthree = "1";
                                        break;
                                    //暂停
                                    case "2":
                                        flagone = "3";
                                        flagtwo = "3";
                                        flagthree = "4";
                                        break;
                                    //3-组 1 开组 2 关
                                    case "3":
                                        flagone = "1";
                                        flagtwo = "2";
                                        flagthree = "3";
                                        break;
                                    //4-组 1 开组 2 暂停
                                    case "4":
                                        flagone = "1";
                                        flagtwo = "3";
                                        flagthree = "3";
                                        break;
                                    //5-组 1 关组 2 开
                                    case "5":
                                        flagone = "2";
                                        flagtwo = "1";
                                        flagthree = "3";
                                        break;
                                    //6-组 1 关组 2 暂停
                                    case "6":
                                        flagone = "2";
                                        flagtwo = "3";
                                        flagthree = "3";
                                        break;
                                    //7-组 1 暂停 组 2 关
                                    case "7":
                                        flagone = "3";
                                        flagtwo = "2";
                                        flagthree = "3";
                                        break;
                                    //8-组 1 暂停组 2 开
                                    case "8":
                                        flagone = "3";
                                        flagtwo = "1";
                                        flagthree = "3";
                                        break;
                                }
                                switchState(flagone, flagtwo, flagthree);
                            } else {
                                //不为窗帘开关状态
                                dimmer = d.dimmer;
                                Log.e("zhu","d.dimmer:" + dimmer);
                                modeflag = d.mode;
                                temperature = d.temperature;
                                windflag = d.speed;
                                if (type.equals("3") || type.equals("5") || type.equals("6")) {
                                    if (dimmer != null && !dimmer.equals("")) {
                                        tempimage_id.setText(dimmer);
                                        id_seekBar.setProgress(Integer.parseInt(dimmer) - 16);
                                    }
                                    if (temperature != null && !temperature.equals("")) {
                                        tempimage_id.setText(temperature);
                                        id_seekBar.setProgress(Integer.parseInt(temperature) - 16);
                                    }
                                }
                                if (type.equals("2")) {
                                    if (dimmer != null && !dimmer.equals("")) {
                                        tempimage_id.setText(dimmer);
                                        id_seekBar.setProgress(Integer.parseInt(dimmer));
                                    }
                                }
                                setModetwo();
                                setSpeed();
                                LogUtil.eLength("查看是否进去", temperature + "进入方法" + dimmer);
                                if (d.status.equals("0")) {
                                    getBoxclose();
                                    LogUtil.eLength("没有进去", "进入方法");
                                } else {
                                    LogUtil.eLength("你看", "进入方法");
                                    getBoxopen();
                                }
                            }
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

    private void setSpeed() {
        String strwind = "";
        switch (windflag) {
            case "1":
                strwind = "低风";
                break;
            case "2":
                strwind = "中风";
                break;
            case "3":
                strwind = "高风";
                break;
            case "4":
                strwind = "强力";
                break;
            case "5":
                strwind = "送风";
                break;
            case "6":
                strwind = "自动";
                break;
            default:
                break;
        }
        windspeedtwo_id.setText(strwind);
        windspeed_id.setText(strwind);
    }

    private void setMode() {
        //模式状态
        switch (modeflag) {
            case "1":
                tempstate_id.setText("制热");
                modeflag = "2";
                break;
            case "2":
                tempstate_id.setText("除湿");
                modeflag = "3";
                break;
            case "3":
                tempstate_id.setText("自动");
                modeflag = "4";
                break;
            case "4":
                tempstate_id.setText("通风");
                modeflag = "5";
                break;
            case "5":
                tempstate_id.setText("制冷");
                modeflag = "1";
                break;
            default:
                break;
        }
    }

    private void setModetwo() {
        String strmode = "";
        switch (modeflag) {
            case "1":
                strmode = "制冷";
                break;
            case "2":
                strmode = "制热";
                break;
            case "3":
                strmode = "除湿";
                break;
            case "4":
                strmode = "自动";
                break;
            case "5":
                strmode = "通风";
                break;
            default:
                break;
        }
        tempstate_id.setText(strmode);
    }

    /*非窗帘全关状态设置*/
    private void getBoxclose() {
        //全部关闭状态
        //调光灯状态开关
//        dimmerrelative.setBackgroundResource(R.drawable.hsmall_circle);
//        dimmerimageview.setImageResource(R.drawable.hopen);
        openbtn_tiao_guang.setImageResource(R.drawable.open_black_word);
        //空调开关状态
        switchrelative.setBackgroundResource(R.drawable.hsmall_circle);
        statusopen.setImageResource(R.drawable.hopen);
        statusflag = "1";
        statusbo = false;
    }

    /*非窗帘全开状态设置*/
    private void getBoxopen() {
        //目前是全开的状态
        //调光灯状态开关
//        dimmerrelative.setBackgroundResource(R.drawable.hsmall_black);
//        dimmerimageview.setImageResource(R.drawable.hairclose_word);
        openbtn_tiao_guang.setImageResource(R.drawable.guan_white_word);
        //空调开关状态
        switchrelative.setBackgroundResource(R.drawable.hsmall_black);
        statusopen.setImageResource(R.drawable.hairclose_word);
        statusflag = "0";
        statusbo = true;
    }

    /*窗帘各个开关状态设置*/
    private void switchState(String flagone, String flagtwo, String flagthree) {
        //进行清空各个操作
        statusClear();
        if (flagone.equals("1")) {
            curopenrelative_id.setBackgroundResource(R.drawable.hsmall_black);
            curimage_id.setImageResource(R.drawable.hairopen_word_white);
        } else if (flagone.equals("2")) {
            curoffrelative_id.setBackgroundResource(R.drawable.hsmall_black);
            curoffima_id.setImageResource(R.drawable.hairclose_word);
        }
        if (flagtwo.equals("1")) {
            curopenrelativetwo_id.setBackgroundResource(R.drawable.hsmall_black);
            curimagetwo_id.setImageResource(R.drawable.hairopen_word_white);
        } else if (flagtwo.equals("2")) {
            curoffrelativetwo_id.setBackgroundResource(R.drawable.hsmall_black);
            curoffimatwo_id.setImageResource(R.drawable.hairclose_word);
        }
        if (flagthree.equals("1")) {
            curopenrelativethree_id.setBackgroundResource(R.drawable.hsmall_black);
            curimagethree_id.setImageResource(R.drawable.hairopen_word_white);
        } else if (flagthree.equals("2")) {
            curoffrelativethree_id.setBackgroundResource(R.drawable.hsmall_black);
            curoffimathree_id.setImageResource(R.drawable.hairclose_word);
        } else if (flagthree.equals("4")) {
            //暂停
            sucrela.setBackgroundResource(R.drawable.hsmall_black);
            sucrelaimage.setImageResource(R.drawable.hairpause_word_white);
        }
    }

    private void statusClear() {
        if (whriteone) {
            curopenrelative_id.setBackgroundResource(R.drawable.hairopen);
            curimage_id.setImageResource(R.drawable.hairopen_word);
            curoffrelative_id.setBackgroundResource(R.drawable.hairopen);
            curoffima_id.setImageResource(R.drawable.hairclose_word_black);
        }

        if (whritetwo) {
            curopenrelativetwo_id.setBackgroundResource(R.drawable.hairopen);
            curimagetwo_id.setImageResource(R.drawable.hairopen_word);
            curoffrelativetwo_id.setBackgroundResource(R.drawable.hairopen);
            curoffimatwo_id.setImageResource(R.drawable.hairclose_word_black);
        }

        if (whritethree) {
            curopenrelativethree_id.setBackgroundResource(R.drawable.hairopen);
            curimagethree_id.setImageResource(R.drawable.hairopen_word);
            curoffrelativethree_id.setBackgroundResource(R.drawable.hairopen);
            curoffimathree_id.setImageResource(R.drawable.hairclose_word_black);
            //暂停
            sucrela.setBackgroundResource(R.drawable.hairopen);
            sucrelaimage.setImageResource(R.drawable.hairpause_word);
        }
    }


    /**
     * 动态注册广播
     */
    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_INTENT_RECEIVER_TO_SECOND_PAGE);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals(ACTION_INTENT_RECEIVER_TO_SECOND_PAGE)) {
                Log.e("zhu","LamplightActivity:" + "LamplightActivity");
                //控制部分的二级页面进去要同步更新推送的信息显示 （推送的是消息）。
                upload();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMessageReceiver);
    }
}