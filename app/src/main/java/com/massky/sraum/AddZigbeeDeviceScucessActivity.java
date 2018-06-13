package com.massky.sraum;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.ClearEditText;
import com.Util.DialogUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.ToastUtil;
import com.Util.TokenUtil;
import com.base.Basecactivity;
import com.data.User;
import com.yanzhenjie.statusview.StatusUtils;
import com.yanzhenjie.statusview.StatusView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;
import okhttp3.Call;

import static com.Util.ClearEditText.dip2px;

/**
 * Created by zhu on 2018/1/8.
 */

public class AddZigbeeDeviceScucessActivity extends Basecactivity {
    @InjectView(R.id.next_step_txt)
    ImageView next_step_txt;
    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.status_view)
    StatusView statusView;
    private PopupWindow popupWindow;
    private String device_name;
    private List<User.panellist> panelList = new ArrayList<>();
    private DialogUtil dialogUtil;
    private List<User.device> deviceList = new ArrayList<>();
    private String panelType;
    private String panelName;
    private String panelNumber;
    private String deviceNumber;
    private String panelMAC;
    @InjectView(R.id.dev_name)
    ClearEditText dev_name;
    @InjectView(R.id.btn_login_gateway)
    Button btn_login_gateway;


    @Override
    protected int viewId() {
        return R.layout.add_zigbee_deivice_scucess;
    }

    @Override
    protected void onView() {
        if (!StatusUtils.setStatusBarDarkFont(this, true)) {// Dark font for StatusBar.
            statusView.setBackgroundColor(Color.BLACK);
        }
        back.setOnClickListener(this);
        btn_login_gateway.setOnClickListener(this);
        dialogUtil = new DialogUtil(this);
        next_step_txt.setOnClickListener(this);
        StatusUtils.setFullToStatusBar(this);  // StatusBar.
//        device_name = (String) getIntent().getSerializableExtra("device_name");
//        switch (device_name){
//            case "空调面板":
//
//                break;
//            case "PM2.5":
//
//                break;
//            case "窗帘面板":
//                window_linear.setVisibility(View.VISIBLE);
//                break;
//            case "灯控面板":
//                light_control_panel.setVisibility(View.VISIBLE);
//                break;
//            case "网关":
//
//                break;
//            case "wifi":
//
//                break;
//        }
        get_panel_detail();
    }

//    @Override
//    protected void onEvent() {
//        back.setOnClickListener(this);
//        btn_login_gateway.setOnClickListener(this);
//    }
//
//    @Override
//    protected void onData() {
//        next_step_txt.setOnClickListener(this);
//        Intent intent = getIntent();
////        if (intent == null) return;
////        String excute = (String) intent.getSerializableExtra("excute");
////        switch (excute) {
////            case "auto"://自动
////                rel_scene_set.setVisibility(View.GONE);
////                break;
////            default:
////                rel_scene_set.setVisibility(View.VISIBLE);
////                break;
////        }
//    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.next_step_txt:
////                GuanLianSceneBtnActivity.this.finish();
//                showPopWindow();
//                break;
//            case R.id.back:
//                AddDeviceScucessActivity.this.finish();
//                break;
//
//            case R.id.btn_login_gateway:
//                ApplicationContext.getInstance().finishActivity(ConRouInforActivity.class);
//                ApplicationContext.getInstance().finishActivity(AddWifiDeviceActivity.class);
//                ApplicationContext.getInstance().finishActivity(SelectZigbeeDeviceActivity.class);
//                AddDeviceScucessActivity.this.finish();
//                startActivity(new Intent(AddDeviceScucessActivity.this,SelectRoomActivity.class));
//                break;
//        }
        switch (v.getId()) {
            case R.id.back:
                AddZigbeeDeviceScucessActivity.this.finish();
                break;
            case R.id.next_step_txt:
                showPopWindow();
                break;
            case R.id.btn_login_gateway:
                save_panel();
                break;
        }
    }

    /**
     * Android popupwindow在指定控件正下方滑动弹出效果
     */
    private void showPopWindow() {
        try {
            View view = LayoutInflater.from(AddZigbeeDeviceScucessActivity.this).inflate(
                    R.layout.add_devsucesspopupwindow, null);
            popupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            ColorDrawable cd = new ColorDrawable(0x00ffffff);// 背景颜色全透明
            popupWindow.setBackgroundDrawable(cd);
            int[] location = new int[2];
            next_step_txt.getLocationOnScreen(location);//获得textview的location位置信息，绝对位置
            popupWindow.setAnimationStyle(R.style.style_pop_animation);// 动画效果必须放在showAsDropDown()方法上边，否则无效
            backgroundAlpha(0.5f);// 设置背景半透明 ,0.0f->1.0f为不透明到透明变化。
            int xoff = dip2px(AddZigbeeDeviceScucessActivity.this, 20);
            popupWindow.showAsDropDown(next_step_txt, 0, dip2px(AddZigbeeDeviceScucessActivity.this, 10));
//            popupWindow.showAtLocation(tv_pop, Gravity.NO_GRAVITY, location[0]+tv_pop.getWidth(),location[1]);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    popupWindow = null;// 当点击屏幕时，使popupWindow消失
                    backgroundAlpha(1.0f);// 当点击屏幕时，使半透明效果取消，1.0f为透明
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 设置popupWindow背景半透明
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;// 0.0-1.0
        getWindow().setAttributes(lp);
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
        dev_name.setText(panelName);
    }

    /**
     * 更新面板名称
     *
     * @param panelName
     * @param panelNumber
     */
    private void sraum_update_panel_name(final String panelName, final String panelNumber) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(AddZigbeeDeviceScucessActivity.this));
        map.put("boxNumber", TokenUtil.getBoxnumber(AddZigbeeDeviceScucessActivity.this));
        map.put("panelNumber", panelNumber);
        map.put("panelName", panelName);
        MyOkHttp.postMapObject(ApiHelper.sraum_updatePanelName, map,
                new Mycallback(new AddTogglenInterfacer() {//刷新togglen获取新数据
                    @Override
                    public void addTogglenInterfacer() {
                        sraum_update_panel_name(panelName, panelNumber);
                    }
                }, AddZigbeeDeviceScucessActivity.this, dialogUtil) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                        AddZigbeeDeviceScucessActivity.this.finish();
                    }

                    @Override
                    public void onSuccess(User user) {
                        super.onSuccess(user);
                        //
                        AddZigbeeDeviceScucessActivity.this.finish();
                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                    }

                    @Override
                    public void threeCode() {
                        super.threeCode();
                        ToastUtil.showToast(AddZigbeeDeviceScucessActivity.this, panelName + ":" + "面板编号不正确");
                    }

                    @Override
                    public void fourCode() {
                        super.fourCode();
                        ToastUtil.showToast(AddZigbeeDeviceScucessActivity.this, panelName + ":" + "面板名字已存在");
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
        String panelName = dev_name.getText().toString().trim();
        sraum_update_panel_name(panelName, panelNumber);
    }

}
