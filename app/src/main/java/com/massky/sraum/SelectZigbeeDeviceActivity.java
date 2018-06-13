package com.massky.sraum;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.DialogUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.SharedPreferencesUtil;
import com.Util.ToastUtil;
import com.Util.TokenUtil;
import com.adapter.SelectDevTypeAdapter;
import com.adapter.SelectWifiDevAdapter;
import com.andview.refreshview.XRefreshView;
import com.base.Basecactivity;
import com.data.User;
import com.yanzhenjie.statusview.StatusUtils;
import com.yanzhenjie.statusview.StatusView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;

import static com.fragment.Mainviewpager.getDeviceId;

/**
 * Created by zhu on 2018/1/3.
 */

public class SelectZigbeeDeviceActivity extends Basecactivity {
    @InjectView(R.id.status_view)
    StatusView statusView;
    //    @InjectView(R.id.maoyan_control)
//    LinearLayout maoyan_control;
//    @InjectView(R.id.shexiangtou_control)
//    LinearLayout shexiangtou_control;
//    @InjectView(R.id.yinyue_control)
//    LinearLayout yinyue_control;
//    @InjectView(R.id.back)
//    ImageView back;
    @InjectView(R.id.macfragritview_id)
    GridView macfragritview_id;
    @InjectView(R.id.refresh_view)
    XRefreshView refresh_view;

    private List<Map> dataSourceList = new ArrayList<>();
    private SelectDevTypeAdapter adapter;
    //    private AddSelectZigbeeDevAdapter addselectzigbeedevAdapter;
//
//    @InjectView(R.id.mineRoom_list)
//    GridView mineRoom_list;
    @InjectView(R.id.mac_wifi_dev_id)
    GridView mac_wifi_dev_id;
    @InjectView(R.id.back)
    ImageView back;
    //    private int[] icon = {R.drawable.marklamph, R.drawable.dimminglights,
//            R.drawable.home_26, R.drawable.home_curtain,
//            R.drawable.freshair, R.drawable.floorheating, R.drawable.magnetic_door_s,
//            R.drawable.human_induction_s, R.drawable.water_s, R.drawable.pm25_s,
//            R.drawable.emergency_button_s};
    private int[] icon = {R.drawable.icon_kaiguan_40, R.drawable.magnetic_door_s,
            R.drawable.human_induction_s, R.drawable.water_s, R.drawable.pm25_s,
            R.drawable.emergency_button_s, R.drawable.ic_launcher, R.drawable.ic_launcher,
            R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher
            , R.drawable.ic_launcher
    };

    //        //type：设备类型，1-灯，2-调光，3-空调，4-窗帘，5-新风，6-地暖,7-门磁，8-人体感应，9-水浸检测器，10-入墙PM2.5
    //11-紧急按钮，

    private int[] icon_wifi = {
            R.drawable.hongwai_s
    };
    private int[] iconNam_wifi = {R.string.hongwai};
    //


    //    private int[] iconName = {R.string.light, R.string.dimmer, R.string.kongtiao, R.string.curtain,
//            R.string.xinfeng, R.string.dinuan, R.string.menci, R.string.rentiganying, R.string.shuijin,
//            R.string.pm_ruqiang, R.string.jinjin_btn};
    private int[] iconName = {R.string.kaiguan, R.string.menci, R.string.rentiganying, R.string.shuijin,
            R.string.pm_ruqiang, R.string.jinjin_btn, R.string.jiuzuo, R.string.smoke_alarm, R.string.gas_alarm
            , R.string.pm25_in_wall, R.string.pm25_rubik, R.string.smart_lock
            , R.string.dc_mini
    };
    private SelectWifiDevAdapter adapter_wifi;
    private DialogUtil dialogUtil;


    @Override
    protected int viewId() {
        return R.layout.sel_dev_typ_act;
    }

    @Override
    protected void onView() {
        StatusUtils.setFullToStatusBar(this);  // StatusBar.
        back.setOnClickListener(this);
        if (!StatusUtils.setStatusBarDarkFont(this, true)) {// Dark font for StatusBar.
            statusView.setBackgroundColor(Color.BLACK);
        }
//        List<Map> list = new ArrayList<>();
//        for (int i = 0; i < icon.length; i++) {
//            Map map = new HashMap();
//
//            list.add(map);
//        }

        adapter = new SelectDevTypeAdapter(SelectZigbeeDeviceActivity.this, icon, iconName);
        macfragritview_id.setAdapter(adapter);
        refresh_view.setScrollBackDuration(300);
        refresh_view.setPinnedTime(1000);
        refresh_view.setPullLoadEnable(false);
        refresh_view.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
                refresh_view.stopRefresh();
            }
        });

        adapter_wifi = new SelectWifiDevAdapter(SelectZigbeeDeviceActivity.this, icon_wifi, iconNam_wifi);
        mac_wifi_dev_id.setAdapter(adapter_wifi);
//        refresh_view.setScrollBackDuration(300);
//        refresh_view.setPinnedTime(1000);
//        refresh_view.setPullLoadEnable(false);
//        refresh_view.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
//            @Override
//            public void onRefresh(boolean isPullDown) {
//                super.onRefresh(isPullDown);
//                refresh_view.stopRefresh();
//            }
//        });

        macfragritview_id.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
//                        img_show_zigbee.setImageResource(icon[0]);
//                        promat_zigbee_txt.setText(R.string.light_promat);
                        sraum_setBox_accent(position, "normal");
                        break;
                    case 1:
                    case 2:
                    case 3:
                    case 5:
//                        img_show_zigbee.setImageResource(icon[1]);
//                        promat_zigbee_txt.setText(R.string.zigbee_promat);
                    case 4:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:

//                        img_show_zigbee.setImageResource(icon[2]);
//                        promat_zigbee_txt.setText(R.string.zigbee_promat);
                        sraum_setBox_accent(position, "zigbee");
                        break;
                }
            }
        });

//        mac_wifi_dev_id.setAdapter(adapter);//Wi-Fi设备

        mac_wifi_dev_id.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//               ToastUtil.showToast(SelectZigbeeDeviceActivity.this,"click");
                sraum_setBox_accent(position, "wifi");
            }
        });

        dialogUtil = new DialogUtil(SelectZigbeeDeviceActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                SelectZigbeeDeviceActivity.this.finish();
                break;
        }
    }

    private void sraum_setBox_accent(final int position, final String type) {
        //在这里先调
        //设置网关模式-sraum-setBox
        Map map = new HashMap();
        String phoned = getDeviceId(SelectZigbeeDeviceActivity.this);
        map.put("token", TokenUtil.getToken(SelectZigbeeDeviceActivity.this));
        String boxnumber = (String) SharedPreferencesUtil.getData(SelectZigbeeDeviceActivity.this,
                "boxnumber", "");
        map.put("boxNumber", boxnumber);
        map.put("phoneId", phoned);
        switch (type) {
            case "normal":
                map.put("status", "1");//普通进入设置模式
                break;
            case "zigbee":
            case "wifi":
                map.put("status", "12");//zigbee进入设置模式
                break;
        }
        dialogUtil.loadDialog();
        MyOkHttp.postMapObject(ApiHelper.sraum_setBox, map, new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {
                        sraum_setBox_accent(position, type);
                    }
                }, SelectZigbeeDeviceActivity.this, dialogUtil) {
                    @Override
                    public void onSuccess(User user) {
                        Intent intent_position = null;
                        switch (type) {
                            case "normal":
                            case "zigbee":
                                intent_position = new Intent(SelectZigbeeDeviceActivity.this, AddZigbeeDevActivity.class);
                                intent_position.putExtra("position", position);
                                startActivity(intent_position);
                                break;
                            case "wifi":
                                intent_position = new Intent(SelectZigbeeDeviceActivity.this, AddWifiDevActivity.class);
                                intent_position.putExtra("position", position);
                                startActivity(intent_position);
                                break;
                        }

                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                    }

                    @Override
                    public void wrongBoxnumber() {
                        ToastUtil.showToast(SelectZigbeeDeviceActivity.this,
                                "该网关不存在");
                    }
                }
        );
    }

//
//    @Override
//    protected void onEvent() {
//        maoyan_control.setOnClickListener(this);
//        shexiangtou_control.setOnClickListener(this);
//        yinyue_control.setOnClickListener(this);
//        back.setOnClickListener(this);
//    }
//
//    @Override
//    protected void onData() {
//        HashMap<String, Object> itemHashMap = new HashMap<>();
//        itemHashMap.put("item_image", R.drawable.icon_kongtiao_active);
//        itemHashMap.put("device_name", "空调面板");
//        dataSourceList.add(itemHashMap);
//
//        itemHashMap = new HashMap<>();
//        itemHashMap.put("item_image", R.drawable.icon_pm25_active);
//        itemHashMap.put("device_name", "PM2.5");
//        dataSourceList.add(itemHashMap);
//
//        itemHashMap = new HashMap<>();
//        itemHashMap.put("item_image", R.drawable.icon_chuanglian_active);
//        itemHashMap.put("device_name", "窗帘面板");
//        dataSourceList.add(itemHashMap);
//
////        itemHashMap = new HashMap<>();
////        itemHashMap.put("item_image", R.drawable.icon_menci_active);
////        itemHashMap.put("device_name", "门磁");
////        dataSourceList.add(itemHashMap);
//
//        itemHashMap = new HashMap<>();
//        itemHashMap.put("item_image", R.drawable.icon_deng_active);
//        itemHashMap.put("device_name", "灯控面板");
//        dataSourceList.add(itemHashMap);
//
//        itemHashMap = new HashMap<>();
//        itemHashMap.put("item_image", R.drawable.icon_wangguan);
//        itemHashMap.put("device_name", "网关");
//        dataSourceList.add(itemHashMap);
//
//        addselectzigbeedevAdapter = new AddSelectZigbeeDevAdapter(this,dataSourceList);
//        mineRoom_list.setAdapter(addselectzigbeedevAdapter);
//
//    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.maoyan_control:
//            case R.id.shexiangtou_control:
//            case R.id.yinyue_control:
//                startActivity(new Intent(SelectZigbeeDeviceActivity.this,
//                        AddWifiDeviceActivity.class));
//                break;
//
//            case R.id.back:
//                SelectZigbeeDeviceActivity.this.finish();
//                break;
//        }
//    }
}
