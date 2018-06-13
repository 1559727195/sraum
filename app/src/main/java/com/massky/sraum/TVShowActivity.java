package com.massky.sraum;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.base.Basecactivity;
import com.yanzhenjie.statusview.StatusUtils;
import com.yanzhenjie.statusview.StatusView;

import butterknife.InjectView;

/**
 * Created by zhu on 2018/6/6.
 */

public class TVShowActivity extends Basecactivity{
    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.status_view)
    StatusView statusView;
    @Override
    protected int viewId() {
        return R.layout.tv_show_act;
    }

    @Override
    protected void onView() {
        back.setOnClickListener(this);
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
                TVShowActivity.this.finish();
                break;
        }
    }
}
