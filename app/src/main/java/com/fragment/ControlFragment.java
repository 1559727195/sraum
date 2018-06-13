package com.fragment;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Util.LogUtil;
import com.base.Basecfragment;
import com.massky.sraum.R;

import butterknife.InjectView;

/**
 * Created by masskywcy on 2016-09-08.
 */
//用于控制设备温度或者灯光界面
public class ControlFragment extends Basecfragment {
    @InjectView(R.id.addrela_id)
    RelativeLayout addrela_id;
    @InjectView(R.id.reduce_id)
    RelativeLayout reduce_id;
    @InjectView(R.id.temptext_id)
    TextView temptext_id;
    private int x = 16;

    @Override
    protected int viewId() {
        return R.layout.temperature;
    }

    @Override
    protected void onView() {
        LogUtil.i("这是第三个fragment", "onView: ");
        addrela_id.setOnClickListener(this);
        reduce_id.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addrela_id:
                if (x < 30) {
                    x++;
                }
                temptext_id.setText(x + "");
                break;
            case R.id.reduce_id:
                if (x > 16) {
                    x--;
                }
                temptext_id.setText(x + "");
                break;
        }
    }
}
