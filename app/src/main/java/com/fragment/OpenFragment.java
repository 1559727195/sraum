package com.fragment;

import android.view.View;
import android.widget.Button;

import com.Util.LogUtil;
import com.base.Basecfragment;
import com.massky.sraum.R;

import butterknife.InjectView;

/**
 * Created by masskywcy on 2016-09-08.
 */
//打开设备界面
public class OpenFragment extends Basecfragment {
    @InjectView(R.id.openbtn_id)
    Button openbtn_id;
    private boolean flag = true;

    @Override
    protected int viewId() {
        return R.layout.open;
    }

    @Override
    protected void onView() {
        LogUtil.i("这是第二个fragment", "onView: ");
        openbtn_id.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.openbtn_id:
                if (flag) {
                    openbtn_id.setBackgroundResource(R.drawable.closeh);
                    flag = false;
                } else {
                    openbtn_id.setBackgroundResource(R.drawable.openh);
                    flag = true;
                }
                break;
        }
    }
}
