package com.massky.sraum;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.base.Basecactivity;
import com.yanzhenjie.statusview.StatusUtils;
import com.yanzhenjie.statusview.StatusView;

import butterknife.InjectView;

/**
 * Created by zhu on 2018/5/30.
 */

public class AddWifiDevActivity extends Basecactivity {
    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.status_view)
    StatusView statusView;
    @InjectView(R.id.next_step_id)
    Button next_step_id;
    @InjectView(R.id.img_show_zigbee)
    ImageView img_show_zigbee;
    private int[] icon_wifi = {
            R.drawable.pic_hongwai
    };

    @Override
    protected int viewId() {
        return R.layout.add_wifi_dev_act;
    }

    @Override
    protected void onView() {
        back.setOnClickListener(this);
        next_step_id.setOnClickListener(this);
        StatusUtils.setFullToStatusBar(this);  // StatusBar.
        back.setOnClickListener(this);
        if (!StatusUtils.setStatusBarDarkFont(this, true)) {// Dark font for StatusBar.
            statusView.setBackgroundColor(Color.BLACK);
        }

        int position = (int) getIntent().getSerializableExtra("position");
        switch (position) {
//            case 0:
//
//                break;
//            case 1:
//
//                break;
//            case 2:
//
//                break;
//            case 3:
//
//                break;
//            case 4:
//
//                break;
//            case 5:
//
//                break;

        }
        img_show_zigbee.setImageResource(icon_wifi[position]);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                AddWifiDevActivity.this.finish();
                break;
            case R.id.next_step_id:
                startActivity(new Intent(AddWifiDevActivity.this, ConnWifiActivity.class));
                break;
        }
    }
}
