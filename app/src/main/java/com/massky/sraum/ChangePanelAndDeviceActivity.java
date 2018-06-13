package com.massky.sraum;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.AppManager;
import com.Util.ClearEditText;
import com.Util.DialogUtil;
import com.Util.IntentUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.ToastUtil;
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

import static com.massky.sraum.R.id.findpanel;


/**
 * Created by zhu on 2017/10/27.
 */

public class ChangePanelAndDeviceActivity extends Basecactivity{

    @InjectView(R.id.onekey_device)
    TextView onekey_device;
    @InjectView(R.id.twokey_device)
    TextView twokey_device;
    @InjectView(R.id.threekey_device)
    TextView threekey_device;
    @InjectView(R.id.fourkey_device)
    TextView fourkey_device;

    @InjectView(R.id.onekey_device_txt)
    ClearEditText onekey_device_txt;
    @InjectView(R.id.twokey_device_txt)
    ClearEditText twokey_device_txt;
    @InjectView(R.id.threekey_device_txt)
    ClearEditText threekey_device_txt;
    @InjectView(R.id.fourkey_device_txt)
    ClearEditText fourkey_device_txt;

    @InjectView(R.id.panelmac)
    TextView panelmac;
    @InjectView(R.id.paneltype)
    TextView paneltype;
    @InjectView(R.id.panelname)
    ClearEditText panelname;
    @InjectView(R.id.titlecen_id)
    TextView titlecen_id;//面板标题
    @InjectView(R.id.save_panel)
    TextView save_panel;

    @InjectView(R.id.backrela_id)
    RelativeLayout backrela_id;

    @InjectView(R.id.findpanel)
    ImageButton findpanel;
    private List<User.panellist> panelList = new ArrayList<>();
    private DialogUtil dialogUtil;
    private List<User.device> deviceList = new ArrayList<>();
    private String panelType;
    private String panelName;
    private String panelNumber;
    private String deviceNumber;
    private String panelMAC;
    private String onekey_device_txt_str;
    private String twokey_device_txt_str;
    private String threekey_device_txt_str;
    private String fourkey_device_txt_str;
    private int device_index;
    private boolean isPanelAndDeviceSame;
    private String findpaneltype;
    private boolean upgradete;

    @Override
    protected int viewId() {
        return R.layout.changepanel_and_device;
    }

    @Override
    protected void onView() {
        dialogUtil = new DialogUtil(this);
        //根据面板类型，显示不同的设备列表UI
        get_panel_detail();
        //调取面板下的设备信息
//        getPanel_devices();
        show_device_from_panel(panelType);
        panel_and_device_information();
        save_panel.setOnClickListener(this);
        backrela_id.setOnClickListener(this);
        panelname.setClearIconVisible(false);
        onekey_device_txt.setClearIconVisible(false);
        twokey_device_txt.setClearIconVisible(false);
        threekey_device_txt.setClearIconVisible(false);
        fourkey_device_txt.setClearIconVisible(false);
        findpanel.setOnClickListener(this);
    }

    /**
     * 显示面板和设备内容信息
     */
    private void panel_and_device_information() {
        switch (panelType) {
            case "A201"://一灯控
                onekey_device_txt.setText(deviceList.get(0).name);
                break;
            case "A202"://二灯控
                onekey_device_txt.setText(deviceList.get(0).name);
                twokey_device_txt.setText(deviceList.get(1).name);

                break;
            case "A203"://三灯控
                onekey_device_txt.setText(deviceList.get(0).name);
                twokey_device_txt.setText(deviceList.get(1).name);
                threekey_device_txt.setText(deviceList.get(2).name);
                break;
            case "A204"://四灯控
                onekey_device_txt.setText(deviceList.get(0).name);
                twokey_device_txt.setText(deviceList.get(1).name);
                threekey_device_txt.setText(deviceList.get(2).name);
                fourkey_device_txt.setText(deviceList.get(3).name);
                break;

            case "A301"://一键调光，3键灯控  设备4个
            case "A302"://两键调光，2键灯控
            case "A303"://三键调光，一键灯控
//            case "A304"://四键调光
                onekey_device_txt.setText(deviceList.get(0).name);
                twokey_device_txt.setText(deviceList.get(1).name);
                threekey_device_txt.setText(deviceList.get(2).name);
                fourkey_device_txt.setText(deviceList.get(3).name);
                break;//一键-4键调光

            case "A401"://设备2个
                onekey_device_txt.setText(deviceList.get(0).name);
                twokey_device_txt.setText(deviceList.get(0).name1);
                threekey_device_txt.setText(deviceList.get(0).name2);
                fourkey_device_txt.setText(deviceList.get(1).name);
                //窗帘前3个搞定，最后一个按钮为八键灯控名称修改
                break;//窗帘 ，窗帘第八个按钮为八键灯控名称修改

            case "A501"://空调-设备1个
            case "A601"://新风
            case "A701"://地暖
                onekey_device_txt.setText(deviceList.get(0).name);
                break;
            default:

                break;
            //updateDeviceInfo();
        }
    }

    /**
     * 得到面板信息
     */
    private void get_panel_detail() {
        panelNumber = getIntent().getStringExtra("panelid");
        //根据panelid去查找相关面板信心
        //根据panelid去遍历所有面板
//        getData();
//     deviceList = getIntent().getExtras("deviceList");


//        intent.putExtra("panelType", panelType);
//        intent.putExtra("panelName", panelName);
//        intent.putExtra("panelMAC", panelMAC);
        Bundle bundle = getIntent().getBundleExtra("bundle_panel");
        deviceList = (List<User.device>) bundle.getSerializable("deviceList");
        panelType = getIntent().getStringExtra("panelType");
        panelName = getIntent().getStringExtra("panelName");
        panelMAC = getIntent().getStringExtra("panelMAC");
        findpaneltype = getIntent().getStringExtra("findpaneltype");
        switch (findpaneltype) {
            case "fastedit"://快速编辑
                findpanel.setVisibility(View.VISIBLE);
                break;
            case "wangguan_status":
                findpanel.setVisibility(View.GONE);
                break;
        }
        panelmac.setText(panelMAC);
        panelname.setText(panelName);
        paneltype.setText(panelType);
    }

    /**
     * 根据面板类型显示相应设备信息
     * @param type
     */
    private void show_device_from_panel(String type) {
        switch (type) {
            case "A201"://一灯控
                show_one_item();
                onekey_device.setText("一路灯控名称");
                break;
            case "A202"://二灯控
                show_two_item();
                onekey_device.setText("一路灯控名称");
                twokey_device.setText("二路灯控名称");
                break;
            case "A203"://三灯控
                show_three_item();
                onekey_device.setText("一路灯控名称");
                twokey_device.setText("二路灯控名称");
                threekey_device.setText("三路灯控名称");
                break;
            case "A204"://四灯控
                four_all_show();
                onekey_device.setText("一路灯控名称");
                twokey_device.setText("二路灯控名称");
                threekey_device.setText("三路灯控名称");
                fourkey_device.setText("四路灯控名称");
                break;//一键-到4键灯控

            case "A301"://一键调光，3键灯控
                four_all_show();
                onekey_device.setText("一路调光名称");
                twokey_device.setText("二路灯控名称");
                threekey_device.setText("三路灯控名称");
                fourkey_device.setText("四路灯控名称");
                break;

            case "A302"://两键调光，2键灯控
                four_all_show();
                onekey_device.setText("一路调光名称");
                twokey_device.setText("二路调光名称");
                threekey_device.setText("三路灯控名称");
                fourkey_device.setText("四路灯控名称");
                break;

            case "A303"://三键调光，一键灯控
                four_all_show();
                onekey_device.setText("一路调光名称");
                twokey_device.setText("二路调光名称");
                threekey_device.setText("三路调光名称");
                fourkey_device.setText("四路灯控名称");
                break;

//            case "A304"://四键调光
//                four_all_show();
//                onekey_device.setText("一路调光");
//                twokey_device.setText("二路调光");
//                threekey_device.setText("三路调光");
//                fourkey_device.setText("四路调光");
//                break;//一键-4键调光

            case "A401":
                four_all_show();
                onekey_device.setText("窗帘名称");
                twokey_device.setText("一路窗帘名称");
                threekey_device.setText("二路窗帘名称");
                fourkey_device.setText("八路灯控名称");
                break;//窗帘 ，窗帘第八个按钮为八键灯控名称修改

            case "A501"://空调
                show_one_item();
                onekey_device.setText("空调名称");
                break;
            case "A601"://新风
                show_one_item();
                onekey_device.setText("新风名称");
                break;
            case "A701"://地暖
                show_one_item();
                onekey_device.setText("地暖名称");
                break;
            default:
                break;
        }
    }

    /**
     * 添加面板下的设备信息
     */
    private void getPanel_devices() {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(ChangePanelAndDeviceActivity.this));
        map.put("boxNumber", TokenUtil.getBoxnumber(ChangePanelAndDeviceActivity.this));
        map.put("panelNumber", panelNumber);
        MyOkHttp.postMapObject(ApiHelper.sraum_getPanelDevices, map,
                new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {
                        getPanel_devices();
                    }
                }, ChangePanelAndDeviceActivity.this, dialogUtil) {

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
                        panelmac.setText(panelMAC);
                        panelname.setText(panelName);
                        paneltype.setText(panelType);
                        Log.e("robin debug"," panelType:" +  panelType + ",panelName:" + panelName);
                        show_device_from_panel(panelType);
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


                        panel_and_device_information();
                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                    }
                });
    }

//    private void getData() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("token", TokenUtil.getToken(ChangePanelAndDeviceActivity.this));
//        map.put("boxNumber", TokenUtil.getBoxnumber(ChangePanelAndDeviceActivity.this));
//
//        MyOkHttp.postMapObject(ApiHelper.sraum_getAllPanel, map,
//                new Mycallback(new AddTogglenInterfacer() {
//                    @Override
//                    public void addTogglenInterfacer() {
//                        getData();
//                    }
//                }, ChangePanelAndDeviceActivity.this, dialogUtil) {
//
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        super.onError(call, e, id);
//                    }
//
//                    @Override
//                    public void onSuccess(User user) {
//                        super.onSuccess(user);
//                        panelList.clear();
//                        panelList = user.panelList;
//
//                        for (int i = 0; i < panelList.size(); i++) {
//                            if (panelid.equals(panelList.get(i).id)) {//找到该面板
//                                //
//
//                                //显示面板信息
//
////                                @InjectView(R.id.panelmac)
////                                TextView panelmac;
////                                @InjectView(R.id.paneltype)
////                                TextView paneltype;
////                                @InjectView(R.id.panelname)
////                                ClearEditText panelname;
//                                panel_position = i;
//                                panelmac.setText(panelList.get(i).mac);
//                                paneltype.setText(panelList.get(i).type);
//                                panelname.setText(panelList.get(i).name);
//
//                                show_device_from_panel(panelList.get(i).type);
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void wrongToken() {
//                        super.wrongToken();
//                    }
//                });
//    }

    /**
     * 显示三个
     */
    private void show_three_item() {
        onekey_device.setVisibility(View.VISIBLE);
        twokey_device.setVisibility(View.VISIBLE);
        threekey_device.setVisibility(View.VISIBLE);
        fourkey_device.setVisibility(View.GONE);

        onekey_device_txt.setVisibility(View.VISIBLE);
        twokey_device_txt.setVisibility(View.VISIBLE);
        threekey_device_txt.setVisibility(View.VISIBLE);
        fourkey_device_txt.setVisibility(View.GONE);
    }

    /**
     * 显示两个
     */
    private void show_two_item() {
        onekey_device.setVisibility(View.VISIBLE);
        twokey_device.setVisibility(View.VISIBLE);
        threekey_device.setVisibility(View.GONE);
        fourkey_device.setVisibility(View.GONE);

        onekey_device_txt.setVisibility(View.VISIBLE);
        twokey_device_txt.setVisibility(View.VISIBLE);
        threekey_device_txt.setVisibility(View.GONE);
        fourkey_device_txt.setVisibility(View.GONE);
    }

    /**
     * 显示第一个
     */
    private void show_one_item() {
        onekey_device.setVisibility(View.VISIBLE);
        twokey_device.setVisibility(View.GONE);
        threekey_device.setVisibility(View.GONE);
        fourkey_device.setVisibility(View.GONE);

        onekey_device_txt.setVisibility(View.VISIBLE);
        twokey_device_txt.setVisibility(View.GONE);
        threekey_device_txt.setVisibility(View.GONE);
        fourkey_device_txt.setVisibility(View.GONE);
    }

    /**
     * 四个全部显示
     */
    private void four_all_show() {
        onekey_device.setVisibility(View.VISIBLE);
        twokey_device.setVisibility(View.VISIBLE);
        threekey_device.setVisibility(View.VISIBLE);
        fourkey_device.setVisibility(View.VISIBLE);

        onekey_device_txt.setVisibility(View.VISIBLE);
        twokey_device_txt.setVisibility(View.VISIBLE);
        threekey_device_txt.setVisibility(View.VISIBLE);
        fourkey_device_txt.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_panel://保存面板信息
                save_panel();
                break;
            case R.id.backrela_id://返回
                ChangePanelAndDeviceActivity.this.finish();
                break;
            case R.id.findpanel://查找面板
                clickanimation();
                setFindpanel();
                break;
        }
    }

    private void setFindpanel() {
        dialogUtil.loadDialog();
        sraum_find_panel();
    }

    private void sraum_find_panel() {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(ChangePanelAndDeviceActivity.this));
        map.put("boxNumber", TokenUtil.getBoxnumber(ChangePanelAndDeviceActivity.this));
        map.put("panelNumber", panelNumber);
        MyOkHttp.postMapObject(ApiHelper.sraum_findPanel, map,
                new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {//刷新togglen获取新数据
                        sraum_find_panel();
                    }
                }, ChangePanelAndDeviceActivity.this, dialogUtil) {
                    @Override
                    public void onSuccess(User user) {
                        super.onSuccess(user);
                        ToastUtil.showToast(ChangePanelAndDeviceActivity.this, "操作完成，查看对应面板");
                    }

                    @Override
                    public void threeCode() {
                        super.threeCode();
                        ToastUtil.showToast(ChangePanelAndDeviceActivity.this, "面板未找到");
                    }

                    @Override
                    public void fourCode() {
                        super.fourCode();
                        ToastUtil.showToast(ChangePanelAndDeviceActivity.this, "面板未找到");
                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                    }
                });
    }

    /**
     * 保存面板
     */
    private void save_panel() {
        //                panelmac.setText(panelList.get(i).mac);
//                paneltype.setText(panelList.get(i).type);
//                panelname.setText(panelList.get(i).name);
        String panelName = panelname.getText().toString().trim();
        int count_device = deviceList.size();
        //判断面板和设备名称是否相同，相同就不提交

        onekey_device_txt_str = onekey_device_txt.getText().toString().trim() == null
                ||onekey_device_txt.getText().toString().trim() == ""? "":onekey_device_txt.getText().toString().trim();
        twokey_device_txt_str = twokey_device_txt.getText().toString().trim() == null
                ||twokey_device_txt.getText().toString().trim() == ""? "":twokey_device_txt.getText().toString().trim();
        threekey_device_txt_str = threekey_device_txt.getText().toString().trim() == null
                ||threekey_device_txt.getText().toString().trim() == ""? "":threekey_device_txt.getText().toString().trim();
        fourkey_device_txt_str = fourkey_device_txt.getText().toString().trim() == null
                ||fourkey_device_txt.getText().toString().trim() == ""? "":fourkey_device_txt.getText().toString().trim();
        List<String> list = new ArrayList<>();
        list.add(panelName);
        list.add(onekey_device_txt_str);
        list.add(twokey_device_txt_str);
        list.add(threekey_device_txt_str);
        list.add(fourkey_device_txt_str);

//                switch (count_device) {
//                    case 1:
//
//                        break;
//                    case 2:
//
//                        break;
//                    case 3:
//
//                        break;
//                    case 4:
//
//                        break;
//                }

        //遍历面板和设备名称有相同的吗
        isPanelAndDeviceSame = false;
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = i + 1; j < list.size(); j ++) {
                if ((list.get(i).equals(list.get(j)) && !list.get(i).equals("")
                && !list.get(j).equals(""))) {
                    isPanelAndDeviceSame = true;
                    break;
                }
            }
        }

        if (!isPanelAndDeviceSame) {
            for (int i = 0; i < count_device + 1; i ++ ) {
                if (list.get(i).equals("")) {
                    isPanelAndDeviceSame = true;
                }
            }

            if (isPanelAndDeviceSame) {
                ToastUtil.showToast(ChangePanelAndDeviceActivity.this,"输入框不能为空");
            } else {
                dialogUtil.loadDialog();
                sraum_update_panel_name(panelName, panelNumber);//更新面板信息
                //更新面板下的设备列表信息
//                int count_device = deviceList.size();
                //updateDeviceInfo();//更新设备信息
            }
        } else  {
            ToastUtil.showToast(ChangePanelAndDeviceActivity.this,"所输入内容重复");
        }
    }

    /**
     * 更新设备信息
     */
    private void updateDeviceInfo() {
        switch (panelType) {
            case "A201"://一灯控
                onekey_device_txt_str = onekey_device_txt.getText().toString().trim();
                device_index = 0;
                control_device_name_change_one(onekey_device_txt_str,0);
                break;
            case "A202"://二灯控
                onekey_device_txt_str = onekey_device_txt.getText().toString().trim();
                twokey_device_txt_str = twokey_device_txt.getText().toString().trim();
                device_index = 1;
                control_device_name_change_one(onekey_device_txt_str,0);//从0 -1 开始
                break;
            case "A203"://三灯控
                onekey_device_txt_str = onekey_device_txt.getText().toString().trim();
                twokey_device_txt_str = twokey_device_txt.getText().toString().trim();
                threekey_device_txt_str = threekey_device_txt.getText().toString().trim();
                device_index = 2;
                control_device_name_change_one(onekey_device_txt_str,0);//从0 - 2开始
                break;
            case "A204"://四灯控
                onekey_device_txt_str = onekey_device_txt.getText().toString().trim();
                twokey_device_txt_str = twokey_device_txt.getText().toString().trim();
                threekey_device_txt_str = threekey_device_txt.getText().toString().trim();
                fourkey_device_txt_str = fourkey_device_txt.getText().toString().trim();
                device_index = 3;
                control_device_name_change_one(onekey_device_txt_str,0);//从0-3开始
                break;
            case "A301"://一键调光，3键灯控  设备4个
            case "A302"://两键调光，2键灯控
            case "A303"://三键调光，一键灯控
//            case "A304"://四键调光
//                updateDeviceInfo(onekey_device_txt.getText().toString().trim(), "", "",
//                        deviceList.get(0).number, "");
//                updateDeviceInfo(twokey_device_txt.getText().toString().trim(), "", "",
//                        deviceList.get(1).number, "");
//                updateDeviceInfo(threekey_device_txt.getText().toString().trim(), "", "",
//                        deviceList.get(2).number, "");
//                updateDeviceInfo(fourkey_device_txt.getText().toString().trim(), "", "",
//                        deviceList.get(3).number, "");
                onekey_device_txt_str = onekey_device_txt.getText().toString().trim();
                twokey_device_txt_str = twokey_device_txt.getText().toString().trim();
                threekey_device_txt_str = threekey_device_txt.getText().toString().trim();
                fourkey_device_txt_str = fourkey_device_txt.getText().toString().trim();
                device_index = 3;
                control_device_name_change_one(onekey_device_txt_str,0);//从0-3开始
//                control_device_name_change(twokey_device_txt_str,1);
//                control_device_name_change(threekey_device_txt_str,2);
//                control_device_name_change(fourkey_device_txt_str,3);

                break;//一键-4键调光

            case "A401"://设备2个

                final String customName = onekey_device_txt.getText().toString().trim();
                final String name1 = twokey_device_txt.getText().toString().trim();
                final String name2 = threekey_device_txt.getText().toString().trim();
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        String deviceNumber = deviceList.get(0).number;
                        updateDeviceInfo(customName, name1, name2, deviceNumber, "窗帘前3", 0);
                    }
                }).start();


                //窗帘前3个搞定，最后一个按钮为八键灯控名称修改
                break;//窗帘 ，窗帘第八个按钮为八键灯控名称修改

            case "A501"://空调-设备1个
                updateDeviceInfo(onekey_device_txt.getText().toString().trim(), "", "",
                        deviceList.get(0).number, "",0);
                break;
            case "A601"://新风
                updateDeviceInfo(onekey_device_txt.getText().toString().trim(), "", "",
                        deviceList.get(0).number, "", 0);
                break;
            case "A701"://地暖
                updateDeviceInfo(onekey_device_txt.getText().toString().trim(), "", "",
                        deviceList.get(0).number, "", 0);
                break;
            default:
                break;
//                updateDeviceInfo();
        }
    }

    /**
     * 窗帘第八键设备控制
     * @param chuanglian
     */
    private void select_window_bajian(final String chuanglian) {
        deviceNumber = deviceList.get(1).number; //
        final String customName_window = fourkey_device_txt.getText().toString().trim();
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateDeviceInfo(customName_window, "", "", deviceNumber, chuanglian, 0);
            }
        }).start();
    }

    /**
     * 第一个设备控制
     * @param device_name
     * @param index
     */
    private void control_device_name_change_one(final String  device_name,final int index) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (index <= deviceList.size() - 1) //
                updateDeviceInfo(device_name, "", "",
                        deviceList.get(index).number, "",index);
            }
        }).start();
    }

    /**
     * 第三个设备控制
     * @param device_name
     * @param index
     */
    private void control_device_name_change_three(final String  device_name,final int index) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                    if (index <= deviceList.size() - 1) //
                updateDeviceInfo(device_name, "", "",
                        deviceList.get(index).number, "", index);
            }
        }).start();
    }

    /**
     * 第二个设备控制
     * @param device_name
     * @param index
     */
    private void control_device_name_change_two(final String  device_name,final int index) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (index <= deviceList.size() - 1) //
                updateDeviceInfo(device_name, "", "",
                        deviceList.get(index).number, "", index);
            }
        }).start();
    }

    /**
     * 第四个设备控制
     * @param device_name
     * @param index
     */
    private void control_device_name_change_four(final String  device_name, final int index) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (index <= deviceList.size() - 1) //
                updateDeviceInfo(device_name, "", "",
                        deviceList.get(index).number, "", index);
            }
        }).start();
    }

    private void updateDeviceInfo(String customName, String name1, String name2, String deviceNumber, String chuanglian, int index) {
        sraum_update_s(customName, name1,name2,deviceNumber,chuanglian,index);
    }

    private void sraum_update_s(final String customName, final String name1, final String name2, final String deviceNumber, final String chuanglian, final int index) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(ChangePanelAndDeviceActivity.this));
        map.put("boxNumber", TokenUtil.getBoxnumber(ChangePanelAndDeviceActivity.this));
        map.put("deviceNumber", deviceNumber);
        map.put("customName", customName);
        if (chuanglian.equals("窗帘前3")) {
            map.put("name1", name1);
            map.put("name2", name2);
        }

        MyOkHttp.postMapObject(ApiHelper.sraum_updateDeviceInfo, map, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {
                sraum_update_s(customName, name1, name2, deviceNumber, chuanglian, index);
            }
        }, ChangePanelAndDeviceActivity.this, dialogUtil) {
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
            }

            @Override
            public void wrongBoxnumber() {
                super.wrongBoxnumber();
                select_device_byway("网关不正确");
            }

            @Override
            public void threeCode() {
                super.threeCode();
//                ToastUtil.showDelToast(ChangePanelAndDeviceActivity.this, "设备编号不正确");
                select_device_byway("设备编号不正确");
            }

            @Override
            public void fourCode() {
                super.fourCode();
//                ToastUtil.showDelToast(ChangePanelAndDeviceActivity.this, "自定义名称重复");
                select_device_byway("自定义名称重复");
            }

            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
//                ToastUtil.showDelToast(ChangePanelAndDeviceActivity.this, "上传成功");
                select_device_byway("上传成功");
            }

            /**
             * 选设备4-1，依次执行
             * @param content
             */
            private void select_device_byway(String content) {//失败就提示添加失败名称，和失败类型
                if(chuanglian.equals("窗帘前3")) {
                    if (content.equals("上传成功")) {//当前不成功，就不继续加下去了
                        select_window_bajian("窗帘第八键");//控制窗帘第八键
                    }  else {
                        ToastUtil.showDelToast(ChangePanelAndDeviceActivity.this, customName +":"+content);
                    }
                    return;
                }

                //窗帘第八键
                if(chuanglian.equals("窗帘第八键")) {
                    if (content.equals("上传成功")) {//当前不成功，就不继续加下去了
                            //
                        ToastUtil.showToast(ChangePanelAndDeviceActivity.this,"更新成功");
                        switch (findpaneltype) {
                            case "fastedit"://快速编辑
                                    AppManager.getAppManager().finishActivity_current(FastEditPanelActivity.class);
                                break;
                            case "wangguan_status":
                                break;
                        }
                        ChangePanelAndDeviceActivity.this.finish();//修改完毕
                    }  else {
                        ToastUtil.showDelToast(ChangePanelAndDeviceActivity.this, customName +":"+content);
                    }
                    return;
                }

                int index_now = index;
                index_now ++;
                if (content.equals("上传成功")) {//当前不成功，就不继续加下去了
                    switch (index_now) {
                        case 0://调1
                            control_device_name_change_one(onekey_device_txt_str, 0);
                            //control_device_name_change_one(onekey_device_txt_str,3);
                            break;
                        case 1:
                            control_device_name_change_two(twokey_device_txt_str, 1);
                            break;
                        case 2:
                            control_device_name_change_three(threekey_device_txt_str, 2);
                            break;
                        case 3:
                            control_device_name_change_four(fourkey_device_txt_str, 3);
                            break;
                    }

                    if (index_now >  device_index) {
                        ToastUtil.showToast(ChangePanelAndDeviceActivity.this,"更新成功");
                        switch (findpaneltype) {
                            case "fastedit"://快速编辑
                                    AppManager.getAppManager().finishActivity_current(FastEditPanelActivity.class);
                                break;
                            case "wangguan_status":
                                break;
                        }
                        ChangePanelAndDeviceActivity.this.finish();//修改完毕
                        return;
                    }
                } else {
                    ToastUtil.showDelToast(ChangePanelAndDeviceActivity.this, customName +":"+content);
                }
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }

    private void sraum_update_panel_name(final String panelName, final String panelNumber) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(ChangePanelAndDeviceActivity.this));
        map.put("boxNumber", TokenUtil.getBoxnumber(ChangePanelAndDeviceActivity.this));
        map.put("panelNumber", panelNumber);
        map.put("panelName", panelName);
        MyOkHttp.postMapObject(ApiHelper.sraum_updatePanelName, map,
                new Mycallback(new AddTogglenInterfacer() {//刷新togglen获取新数据
                    @Override
                    public void addTogglenInterfacer() {
                        sraum_update_panel_name(panelName, panelNumber);
                    }
                }, ChangePanelAndDeviceActivity.this, dialogUtil) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                        ChangePanelAndDeviceActivity.this.finish();
                    }

                    @Override
                    public void onSuccess(User user) {
                        super.onSuccess(user);
//                        ChangePanelAndDeviceActivity.this.finish();
//                        ToastUtil.showToast(ChangePanelAndDeviceActivity.this, panelName+":"+"面板名字更新成功");
                        updateDeviceInfo();//更新设备信息
                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                    }

                    @Override
                    public void threeCode() {
                        super.threeCode();
                        ToastUtil.showToast(ChangePanelAndDeviceActivity.this,panelName+":"+"面板编号不正确");
                    }

                    @Override
                    public void fourCode() {
                        super.fourCode();
                        ToastUtil.showToast(ChangePanelAndDeviceActivity.this,panelName+":"+"面板名字已存在");
                    }
                });
    }

    private void clickanimation() {
        Animation clickAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_animation_small);
        findpanel.startAnimation(clickAnimation);

        //如果你想要点下去然后弹上来就这样
        clickAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //动画执行完后的动作
                findpanel.clearAnimation();
                Animation clickAnimation = AnimationUtils.loadAnimation(ChangePanelAndDeviceActivity.this, R.anim.scale_animation_big);
                findpanel.startAnimation(clickAnimation);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
