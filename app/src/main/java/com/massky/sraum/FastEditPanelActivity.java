package com.massky.sraum;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.DialogUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.TokenUtil;
import com.base.Basecactivity;
import com.data.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;
import okhttp3.Call;

import static com.massky.sraum.R.id.addroomrelative_id;
import static com.massky.sraum.R.id.sraum_rela_act;

/**
 * Created by zhu on 2017/11/9.
 */

public class FastEditPanelActivity extends Basecactivity{
    public static String ACTION_SRAUM_FAST_EDIT = "ACTION_SRAUM_FAST_EDIT";//notifactionId = 8 ->设置网关模式，sraum_setBox
    @InjectView(R.id.backrela_id)
    RelativeLayout backrela_id;
    @InjectView(R.id.titlecen_id)
    TextView titlecen_id;
    //    @InjectView(R.id.addscroll)
//    ScrollView addscroll;
    @InjectView(R.id.search_image)
    ImageView search_image;
    @InjectView(R.id.search_image_rel)
    RelativeLayout search_image_rel;
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
        return R.layout.fast_edit_panel_act;
    }

    @Override
    protected void onView() {
        titlecen_id.setText("快捷编辑");
        backrela_id.setOnClickListener(this);
//                addscroll.setVisibility(View.GONE);
        registerMessageReceiver();
    }

    /**
     * 动态注册广播
     */
    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_SRAUM_FAST_EDIT);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals(ACTION_SRAUM_FAST_EDIT)) {
                int messflag = intent.getIntExtra("notifactionId", 0);
                String panelid  = intent.getStringExtra("panelid");
                if (messflag == 1) {//快捷编辑。在我的面板添加快捷编辑按钮。
                    //在这个界面时，type=1.找面板下的设备列表。在编辑面板的界面。找面板按钮，找到面板后，
                    getPanel_devices(panelid);
                }
            }
        }
    }


    /**
     * 添加面板下的设备信息
     * @param panelid
     */
    private void getPanel_devices(final String panelid) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(FastEditPanelActivity.this));
        map.put("boxNumber", TokenUtil.getBoxnumber(FastEditPanelActivity.this));
        map.put("panelNumber", panelid);
        MyOkHttp.postMapObject(ApiHelper.sraum_getPanelDevices, map,
                new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {
                        getPanel_devices(panelid);
                    }
                }, FastEditPanelActivity.this, dialogUtil) {

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


                        switch (panelType) {
                            case "A201"://一灯控
                            case "A202"://二灯控
                            case "A203"://三灯控
                            case "A204"://四灯控
                            case "A301"://一键调光，3键灯控  设备4个
                            case "A302"://两键调光，2键灯控
                            case "A303"://三键调光，一键灯控
                            case "A401"://设备2个
                            case "A501"://空调-设备1个
                            case "A601"://新风
                            case "A701"://地暖
//                                sraum_setBox_quit(panelid);
                                if (!panelid.equals("")) {
                                    Intent intent = new Intent(FastEditPanelActivity.this,
                                            ChangePanelAndDeviceActivity.class);
                                    intent.putExtra("panelid", panelid);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("deviceList", (Serializable) deviceList);
//                            intent.putExtra("deviceList", (Serializable) deviceList);
                                    intent.putExtra("panelType", panelType);
                                    intent.putExtra("panelName", panelName);
                                    intent.putExtra("panelMAC", panelMAC);
                                    intent.putExtra("bundle_panel", bundle);
                                    intent.putExtra("findpaneltype","fastedit");
                                    startActivity(intent);
//                                    FastEditPanelActivity.this.finish();
                                }
                                break;
                            default://其他的面板类型的面，界面不跳转并且，网关不关闭

                                break;
                        }
                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backrela_id:
                FastEditPanelActivity.this.finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMessageReceiver);
    }
}
