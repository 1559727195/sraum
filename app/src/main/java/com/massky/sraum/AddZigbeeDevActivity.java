package com.massky.sraum;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.DialogUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.SharedPreferencesUtil;
import com.Util.ToastUtil;
import com.Util.TokenUtil;
import com.Util.view.RoundProgressBar;
import com.base.Basecactivity;
import com.data.User;
import com.fragment.ConfigDialogFragment;
import com.fragment.ConfigZigbeeConnDialogFragment;
import com.yanzhenjie.statusview.StatusUtils;
import com.yanzhenjie.statusview.StatusView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;
import okhttp3.Call;

import static com.fragment.Mainviewpager.getDeviceId;

/**
 * Created by zhu on 2018/5/30.
 */

public class AddZigbeeDevActivity extends Basecactivity {
    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.status_view)
    StatusView statusView;
    @InjectView(R.id.next_step_id)
    Button next_step_id;
    private ConfigZigbeeConnDialogFragment newFragment;

//    private int[] icon = {R.drawable.icon_type_switch, R.drawable.menci_big,
//            R.drawable.human_ganying_big, R.drawable.water, R.drawable.pm25,
//            R.drawable.emergency_button};

    private int[] icon = {R.drawable.pic_zigbee_kaiguan, R.drawable.pic_zigbee,
            R.drawable.pic_zigbee_pm25};
    @InjectView(R.id.img_show_zigbee)
    ImageView img_show_zigbee;
    @InjectView(R.id.promat_zigbee_txt)
    TextView promat_zigbee_txt;
    @InjectView(R.id.roundProgressBar2)
    RoundProgressBar roundProgressBar2;
    @InjectView(R.id.txt_remain_time)
    TextView txt_remain_time;
    private boolean is_index;
    private int position;//灯控，zigbee设备
    public static String ACTION_SRAUM_SETBOX = "ACTION_SRAUM_SETBOX";//notifactionId = 8 ->设置网关模式，sraum_setBox
    private MessageReceiver mMessageReceiver;
    private DialogUtil dialogUtil;

    private String panelType;
    private String panelName;
    private String panelNumber;
    private String deviceNumber;
    private String panelMAC;
    private List<User.device> deviceList = new ArrayList<>();
    private List<User.panellist> panelList = new ArrayList<>();

    @Override
    protected int viewId() {
        return R.layout.add_zigbee_dev_act;
    }

    @Override
    protected void onView() {
        registerMessageReceiver();
        back.setOnClickListener(this);
        next_step_id.setOnClickListener(this);
        dialogUtil = new DialogUtil(AddZigbeeDevActivity.this);
        StatusUtils.setFullToStatusBar(this);  // StatusBar.
        back.setOnClickListener(this);
        if (!StatusUtils.setStatusBarDarkFont(this, true)) {// Dark font for StatusBar.
            statusView.setBackgroundColor(Color.BLACK);
        }

        initDialog();
        position = (int) getIntent().getSerializableExtra("position");
        switch (position) {
            case 0:
                img_show_zigbee.setImageResource(icon[0]);
                promat_zigbee_txt.setText(R.string.light_promat);
                break;
            case 1:
            case 2:
            case 3:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
                img_show_zigbee.setImageResource(icon[1]);
                promat_zigbee_txt.setText(R.string.zigbee_promat);
                break;
            case 4:
                img_show_zigbee.setImageResource(icon[2]);
                promat_zigbee_txt.setText(R.string.zigbee_promat);
                break;
        }

        init_status_bar();
    }

    private void init_status_bar() {
        roundProgressBar2.setMax(100);
        final int[] index = {15};
        double c = (double) 100 / 15;//c = (10.0/3) = 3.333333
        final float process = (float) c; //剩余30秒
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                is_index = true;
                while (is_index) {
                    try {
                        Thread.sleep(1000);
                        roundProgressBar2.setProgress(process * (i++));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                progress_loading_linear.setVisibility(View.GONE);
//                                loading_error_linear.setVisibility(View.VISIBLE);
                                if (index[0] >= 0) {
                                    txt_remain_time.setText("剩余" + index[0] + "秒");
                                } else {
                                    txt_remain_time.setText("剩余" + 0 + "秒");
                                }
                                index[0]--;
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (index[0] < 0) {
                        sraum_setBox_quit("", position);
                        is_index = false;
//                        if(getActivity() != null)
//                        txt_remain_time.setText("剩余" + 0 + "秒");
                        AddZigbeeDevActivity.this.finish();
                    }
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                sraum_setBox_quit("", position);
                is_index = false;
                AddZigbeeDevActivity.this.finish();

                break;
            case R.id.next_step_id:

//                //在这里弹出dialogFragment对话框
//                if (!newFragment.isAdded()) {//DialogFragment.show()内部调用了FragmentTransaction.add()方法，所以调用DialogFragment.show()方法时候也可能
//                    FragmentManager manager = getFragmentManager();
//                    FragmentTransaction ft = manager.beginTransaction();
//                    ft.add(newFragment, "dialog");
//                    ft.commit();
//                }
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        connWifiInterfacer.conn_wifi_interface();
//                    }
//                }, 30);
//                break;
                sraum_setBox_quit("", position);
                is_index = false;
                AddZigbeeDevActivity.this.finish();
        }
    }

    /**
     * 初始化dialog
     */
    private void initDialog() {
        // TODO Auto-generated method stub
        newFragment = ConfigZigbeeConnDialogFragment.newInstance(AddZigbeeDevActivity.this, "", "", new ConfigZigbeeConnDialogFragment.DialogClickListener() {

            @Override
            public void doRadioWifi() {//wifi快速配置

            }

            @Override
            public void doRadioScanDevice() {

            }

            @Override
            public void dialogDismiss() {

            }

        });//初始化快配和搜索设备dialogFragment

        connWifiInterfacer = (ConnWifiInterfacer) newFragment;
    }

    private ConnWifiInterfacer connWifiInterfacer;

    public interface ConnWifiInterfacer {
        void conn_wifi_interface();
    }


    private void sraum_setBox_quit(final String pannelid, int postion) {
        //在这里先调
        //设置网关模式-sraum-setBox
        Map map = new HashMap();
        String phoned = getDeviceId(this);
        map.put("token", TokenUtil.getToken(this));
        String boxnumber = (String) SharedPreferencesUtil.getData(this, "boxnumber", "");
        map.put("boxNumber", boxnumber);
        map.put("phoneId", phoned);
        switch (position) {
            case 0:
                map.put("status", "0");//进入设置模式
                break;
            case 1:
            case 2:
            case 3:
            case 5:
            case 4:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
                map.put("status", "13");//进入设置模式
                break;
        }
//        dialogUtil.loadDialog();
        MyOkHttp.postMapObject(ApiHelper.sraum_setBox, map, new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {
                        sraum_setBox_quit(pannelid, position);
                    }
                }, AddZigbeeDevActivity.this, null) {
                    @Override
                    public void onSuccess(User user) {
                        //退出设置网关模式成功，后，
//                        if (!panelid.equals("")) {
//                            Intent intent = new Intent(MacdeviceActivity.this,
//                                    ChangePanelAndDeviceActivity.class);
//                            intent.putExtra("panelid", panelid);
//
//                            startActivity(intent);
//                            MacdeviceActivity.this.finish();
//                        }

//
//                        deviceList.addAll(user.deviceList);
//
//                        //面板的详细信息
//                        panelType = user.panelType;
//                        panelName = user.panelName;
//                        panelMAC = user.panelMAC;
                        Intent intent = null;
                        if (!pannelid.equals("")) {
                            switch (panelType) {
                                case "A201"://一灯控
                                case "A202"://二灯控
                                case "A203"://三灯控
                                case "A204"://四灯控
                                case "A301"://一键调光，3键灯控  设备4个
                                case "A302"://两键调光，2键灯控
                                case "A303"://三键调光，一键灯控
                                case "A401"://设备2个
                                case "A511"://空调-设备1个
                                case "A611"://新风
                                case "A711"://地暖
                                    intent = new Intent(AddZigbeeDevActivity.this,
                                            ChangePanelAndDeviceActivity.class);
                                    break;
                                case "A801":
                                    //门磁
                                case "A901":
                                    //人体感应
                                case "A902":
                                    //TOA久坐报警器
                                case "AB01":
                                    //烟雾报警器
                                case "AB04":
                                    //天然气报警器
                                case "AC01":
                                    //水浸检测器
                                case "AD01":
                                    //PM2.5入墙
                                case "AD02":
                                    //PM2.5魔方
                                case "B001":
                                    //紧急按钮
                                case "B201":
                                    //智能门锁
                                case "B301"://直流电阀机械手
                                    intent = new Intent(AddZigbeeDevActivity.this,
                                            AddZigbeeDeviceScucessActivity.class);
                                    break;//直流电阀机械手
                            }
                            intent.putExtra("panelid", pannelid);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("deviceList", (Serializable) deviceList);
//                            intent.putExtra("deviceList", (Serializable) deviceList);
                            intent.putExtra("panelType", panelType);
                            intent.putExtra("panelName", panelName);
                            intent.putExtra("panelMAC", panelMAC);
                            intent.putExtra("bundle_panel", bundle);
                            intent.putExtra("findpaneltype", "wangguan_status");
                            startActivity(intent);
                            AddZigbeeDevActivity.this.finish();
                        }
                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                    }

                    @Override
                    public void wrongBoxnumber() {
                        ToastUtil.showToast(AddZigbeeDevActivity.this, "该网关不存在");
                    }
                }
        );
    }

    /**
     * 动态注册广播
     */
    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_SRAUM_SETBOX);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals(ACTION_SRAUM_SETBOX)) {
                int messflag = intent.getIntExtra("notifactionId", 0);
                String panelid = intent.getStringExtra("panelid");
                if (messflag == 8) {//notifactionId = 8 ->设置网关模式，sraum_setBox
                    //收到服务器端设置网关成功以后，跳转到修改面板名称，以及该面板下设备列表名称

                    //在网关转圈界面，下去拉设备，判断设备类型，不是我们的。网关不关，是我们的设备类型；在关网关。
                    //然后跳转到显示设备列表界面。
//                    ToastUtil.showToast(MacdeviceActivity.this,"messflag:" + messflag);
                    getPanel_devices(panelid);
                }
            }
        }
    }

    /**
     * 添加面板下的设备信息
     *
     * @param panelid
     */
    private void getPanel_devices(final String panelid) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(AddZigbeeDevActivity.this));
        map.put("boxNumber", TokenUtil.getBoxnumber(AddZigbeeDevActivity.this));
        map.put("panelNumber", panelid);
        MyOkHttp.postMapObject(ApiHelper.sraum_getPanelDevices, map,
                new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {
                        getPanel_devices(panelid);
                    }
                }, AddZigbeeDevActivity.this, dialogUtil) {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                    }

                    @Override
                    public void onSuccess(User user) {
                        super.onSuccess(user);
                        panelList.clear();
                        deviceList.clear();
                        deviceList.addAll(user.deviceList);

                        //面板的详细信息
                        panelType = user.panelType;
                        panelName = user.panelName;
                        panelMAC = user.panelMAC;
//                        panelmac.setText(panelMAC);
//                        panelname.setText(panelName);
//                        paneltype.setText(panelType);
//                        Log.e("robin debug"," panelType:" +  panelType + ",panelName:" + panelName);
//                        show_device_from_panel(panelType);
//                        //在这里显示设备列表信息
//                        switch (deviceList.size()) {
//                            case 1:
//                                onekey_device_txt.setText(deviceList.get(0).deviceName);
//                                break;
//                            case 2:
//                                onekey_device_txt.setText(deviceList.get(0).deviceName);
//                                twokey_device_txt.setText(deviceList.get(1).deviceName);
//                                break;
//                            case 3:
//                                onekey_device_txt.setText(deviceList.get(0).deviceName);
//                                twokey_device_txt.setText(deviceList.get(1).deviceName);
//                                threekey_device_txt.setText(deviceList.get(2).deviceName);
//                                break;
//                            case 4:
//                                onekey_device_txt.setText(deviceList.get(0).deviceName);
//                                twokey_device_txt.setText(deviceList.get(1).deviceName);
//                                threekey_device_txt.setText(deviceList.get(2).deviceName);
//                                fourkey_device_txt.setText(deviceList.get(3).deviceName);
//                                break;
//                        }
//                    }


//                        switch (panelType) {
//                            case "A201"://一灯控
//                            case "A202"://二灯控
//                            case "A203"://三灯控
//                            case "A204"://四灯控
//                            case "A301"://一键调光，3键灯控  设备4个
//                            case "A302"://两键调光，2键灯控
//                            case "A303"://三键调光，一键灯控
//                            case "A401"://设备2个
//                            case "A501"://空调-设备1个
//                            case "A601"://新风
//                            case "A701"://地暖
                        sraum_setBox_quit(panelid, position);
//                                break;
//                            default://其他的面板类型的面，界面不跳转并且，网关不关闭
//
//                                break;
                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                    }
                });
    }

}
