package com.massky.sraum;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Util.LogUtil;
import com.base.Basecactivity;
import com.yanzhenjie.statusview.StatusUtils;
import com.yanzhenjie.statusview.StatusView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.InjectView;

/**
 * Created by zhu on 2017/12/29.
 */

public class SleepModeActivity extends Basecactivity {
    @InjectView(R.id.status_view)
    StatusView statusView;
    @InjectView(R.id.back)
    ImageView back;
    //    private TimePickerView pvCustomTime;
    @InjectView(R.id.sleep_time_rel)
    RelativeLayout sleep_time_rel;
    @InjectView(R.id.get_up_rel)
    RelativeLayout get_up_rel;
    @InjectView(R.id.sleep_time_txt)
    TextView sleep_time_txt;
    @InjectView(R.id.get_up_time_txt)
    TextView get_up_time_txt;

    private final int SLEEP = 4;
    private final int GET_UP = 5;

    @Override
    protected int viewId() {
        return R.layout.sleep_mode_act;
    }

    @Override
    protected void onView() {
        if (!StatusUtils.setStatusBarDarkFont(this, true)) {// Dark font for StatusBar.
            statusView.setBackgroundColor(Color.BLACK);
        }
        StatusUtils.setFullToStatusBar(this);  // StatusBar.
        back.setOnClickListener(this);
        sleep_time_rel.setOnClickListener(this);
        get_up_rel.setOnClickListener(this);
    }
//
//    @Override
//    protected void onEvent() {
//        back.setOnClickListener(this);
//        sleep_time_rel.setOnClickListener(this);
//        get_up_rel.setOnClickListener(this);
//    }
//
//    @Override
//    protected void onData() {
//
//    }

//    private void init_permissions() {
//
//        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
//        RxPermissions permissions = new RxPermissions(this);
//        permissions.request(Manifest.permission.CAMERA).subscribe(new Observer<Boolean>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(Boolean aBoolean) {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });
//    }


    @Override
    public void onClick(View v) {
        Intent intent1birthday = new Intent(SleepModeActivity.this, DeviceModelTimeSelectActivity.class);
        intent1birthday.putExtra("type", "select_time");
        switch (v.getId()) {
            case R.id.back:
                SleepModeActivity.this.finish();
                break;
            case R.id.sleep_time_rel://睡觉

                startActivityForResult(intent1birthday, SLEEP);
                break;
            case R.id.get_up_rel://起床

                startActivityForResult(intent1birthday, GET_UP);
                break;
        }
    }


    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case SLEEP:
                if (intent != null) {
                    if (intent.getStringExtra("birthactivity") != null) {
                        String birthactivity = intent.getStringExtra("birthactivity");// 取出传回来的数据
                        LogUtil.i("", birthactivity + "onActivityResult: " + "这是设置");
//                        updateAccountInfo(nickNameaaccount.getText().toString(), sextext_id.getText().toString(),
//                                birthactivity, mobilePho.getText().toString());
                        sleep_time_txt.setText(birthactivity);


                    }
                }
                break;
            case GET_UP:
                if (intent != null) {
                    if (intent.getStringExtra("birthactivity") != null) {
                        String birthactivity = intent.getStringExtra("birthactivity");// 取出传回来的数据
                        LogUtil.i("", birthactivity + "onActivityResult: " + "这是设置");
//                        updateAccountInfo(nickNameaaccount.getText().toString(), sextext_id.getText().toString(),
//                                birthactivity, mobilePho.getText().toString());
                        get_up_time_txt.setText(birthactivity);


                    }
                }
                break;
        }
    }
}
