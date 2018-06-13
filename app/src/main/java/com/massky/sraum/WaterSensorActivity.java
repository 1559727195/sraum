package com.massky.sraum;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.base.Basecactivity;
import com.yanzhenjie.statusview.StatusUtils;
import com.yanzhenjie.statusview.StatusView;
import butterknife.InjectView;

/**
 * Created by zhu on 2018/1/16.
 */

public class WaterSensorActivity extends Basecactivity {
    @InjectView(R.id.back)
    ImageView back;
    private String deviceName;

//    @InjectView(R.id.project_select)
//    TextView project_select;
//    @InjectView(R.id.delete_rel)
//    RelativeLayout delete_rel;
//    @InjectView(R.id.wangguan_set_rel)
//    RelativeLayout wangguan_set_rel;
//    @InjectView(R.id.basic_information)
//    RelativeLayout basic_information;
    @InjectView(R.id.status_view)
    StatusView statusView;
//    @InjectView(R.id.device_list)
//    RelativeLayout device_list;
//    @InjectView(R.id.btn_cancel_wangguan)
//    Button btn_cancel_wangguan;
//    @InjectView(R.id.gujian_upgrade_rel)
//    RelativeLayout gujian_upgrade_rel;
//    @InjectView(R.id.scene_list_rel)
//    RelativeLayout scene_list_rel;


    @Override
    protected int viewId() {
        return R.layout.water_sensor_act;
    }

    @Override
    protected void onView() {
//        if (!StatusUtils.setStatusBarDarkFont(this, true)) {// Dark font for StatusBar.
//            statusView.setBackgroundColor(Color.BLACK);
//        }
        StatusUtils.setFullToStatusBar(this);  // StatusBar.
        back.setOnClickListener(this);
        if (!StatusUtils.setStatusBarDarkFont(this, true)) {// Dark font for StatusBar.
            statusView.setBackgroundColor(Color.BLACK);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                WaterSensorActivity.this.finish();
                break;
        }
    }

//    @Override
//    protected void onEvent() {
//        back.setOnClickListener(this);
//        delete_rel.setOnClickListener(this);
//        wangguan_set_rel.setOnClickListener(this);
//        basic_information.setOnClickListener(this);
//        device_list.setOnClickListener(this);
//        btn_cancel_wangguan.setOnClickListener(this);
//        gujian_upgrade_rel.setOnClickListener(this);
//        scene_list_rel.setOnClickListener(this);
//    }
//
//    @Override
//    protected void onData() {//"主卫水浸","防盗门门磁","防盗门猫眼","人体监测","防盗门门锁"
//        //,"漏水"
//        deviceName = (String) getIntent().getSerializableExtra("name");
//        project_select.setText(deviceName);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.back:
//                WangGuanDetailItemActivity.this.finish();//
//                break;
//            case R.id.delete_rel:
////                showCustomDialog();
//                break;
//            case R.id.wangguan_set_rel:
//                startActivity(new Intent(WangGuanDetailItemActivity.this
//                        , SettingWangGuanActivity.class));
//                break;//网关设置
//            case R.id.basic_information:
//                startActivity(new Intent(WangGuanDetailItemActivity.this
//                        , WangGuanBaseInformationActivity.class));
//                break;//网关的基本信息
//            case R.id.device_list:
//                startActivity(new Intent(WangGuanDetailItemActivity.this, DeviceListActivity.class));
//                break;//设备列表
//            case R.id.btn_cancel_wangguan:
//                showCustomDialog();
//                break;
//            case R.id.gujian_upgrade_rel:
//                startActivity(new Intent(WangGuanDetailItemActivity.this,GuJianWangGuanActivity.class));
//                break;
//            case R.id.scene_list_rel:
//                startActivity(new Intent(WangGuanDetailItemActivity.this,
//                        SceneListActivity.class));
//                break;
//        }
//    }

//    //自定义dialog
//    public void showCustomDialog() {
////        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
////        // 布局填充器
////        LayoutInflater inflater = LayoutInflater.from(getActivity());
////        View view = inflater.inflate(R.layout.user_name_dialog, null);
////        // 设置自定义的对话框界面
////        builder.setView(view);
////
////        cus_dialog = builder.create();
////        cus_dialog.show();
//
//
//        View view = LayoutInflater.from(WaterSensorActivity.this).inflate(R.layout.promat_dialog, null);
//        TextView confirm; //确定按钮
//        TextView cancel; //确定按钮
//        TextView tv_title;
////        final TextView content; //内容
//        cancel = (TextView) view.findViewById(R.id.call_cancel);
//        confirm = (TextView) view.findViewById(R.id.call_confirm);
//        tv_title = (TextView) view.findViewById(R.id.tv_title);
////        tv_title.setText("是否拨打119");
////        content.setText(message);
//        //显示数据
//        final Dialog dialog = new Dialog(this, R.style.BottomDialog);
//        dialog.setContentView(view);
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//
//        DisplayMetrics dm = getResources().getDisplayMetrics();
//        int displayWidth = dm.widthPixels;
//        int displayHeight = dm.heightPixels;
//        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); //获取对话框当前的参数值
//        p.width = (int) (displayWidth * 0.8); //宽度设置为屏幕的0.5
////        p.height = (int) (displayHeight * 0.5); //宽度设置为屏幕的0.5
////        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
//        dialog.getWindow().setAttributes(p);  //设置生效
//        dialog.show();
//
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        confirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//    }
}
