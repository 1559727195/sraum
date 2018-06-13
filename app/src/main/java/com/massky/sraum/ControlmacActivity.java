package com.massky.sraum;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.Util.LogUtil;
import com.adapter.FragmentViewPagerAdapter;
import com.adapter.ViewpagerAdapter;
import com.base.Basecfragmentactivity;
import com.fragment.AirwindFragment;
import com.fragment.ControlFragment;
import com.fragment.OpenFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by masskywcy on 2016-09-08.
 */
/*用于点击控制开关界面*/
public class ControlmacActivity extends Basecfragmentactivity implements ViewPager.OnPageChangeListener {
    @InjectView(R.id.backrela_id)
    RelativeLayout backrela_id;
    @InjectView(R.id.viewpa_id)
    ViewPager viewpa_id;
    ViewpagerAdapter adapter;
    private FragmentManager fm;
    private AirwindFragment airfragment;
    private OpenFragment openFragment;
    private ControlFragment controlFragment;
    private List<Fragment> list = new ArrayList<Fragment>();

    @Override
    protected int viewId() {
        return R.layout.openlight;
    }

    @Override
    protected void onView() {
        fm = getSupportFragmentManager();
        backrela_id.setOnClickListener(this);
        addFragment();
    }

    private void addFragment() {
        airfragment = new AirwindFragment();
        openFragment = new OpenFragment();
        controlFragment = new ControlFragment();
        list.add(airfragment);
        list.add(controlFragment);
        list.add(openFragment);
        //adapter=new ViewpagerAdapter(fm,list);
        //viewpa_id.setAdapter(adapter);
        FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(fm, viewpa_id, list);
        viewpa_id.addOnPageChangeListener(this);
        adapter.setOnExtraPageChangeListener(new FragmentViewPagerAdapter.OnExtraPageChangeListener() {
            @Override
            public void onExtraPageSelected(int i) {
                LogUtil.i("Extra...i:", i + "onExtraPageSelected: ");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backrela_id:
                ControlmacActivity.this.finish();
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
