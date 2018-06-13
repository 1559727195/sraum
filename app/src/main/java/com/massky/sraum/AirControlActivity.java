package com.massky.sraum;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.base.Basecactivity;
import com.yanzhenjie.statusview.StatusUtils;
import com.yanzhenjie.statusview.StatusView;
import butterknife.InjectView;

/**
 * Created by zhu on 2018/6/6.
 */

public class AirControlActivity extends Basecactivity{
    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.status_view)
    StatusView statusView;
    @InjectView(R.id.air_text)
    TextView air_text;
    @InjectView(R.id.text_clond)
    TextView text_clond;
    @InjectView(R.id.text_model)
    TextView text_model;
    @InjectView(R.id.wendu_txt)
    TextView wendu_txt;
    @Override
    protected int viewId() {
        return R.layout.air_control_act;
    }

    @Override
    protected void onView() {
        StatusUtils.setFullToStatusBar(this);  // StatusBar.
        back.setOnClickListener(this);
        if (!StatusUtils.setStatusBarDarkFont(this, true)) {// Dark font for StatusBar.
            statusView.setBackgroundColor(Color.BLACK);
        }

        word_set();
    }

    /**
     * 字体设置
     */
    private void word_set() {
//得到AssetManager
        AssetManager mgr=getAssets();

//根据路径得到Typeface
        Typeface tf=Typeface.createFromAsset(mgr, "fonts/BlackSimInFasion.ttf");

//设置字体
        //textView.setTypeface(tf);
        air_text.setTypeface(tf);
        text_model.setTypeface(tf);
        text_clond.setTypeface(tf);
        wendu_txt.setTypeface(tf);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                AirControlActivity.this.finish();
                break;
        }
    }
}
