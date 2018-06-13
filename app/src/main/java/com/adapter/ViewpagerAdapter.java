package com.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by masskywcy on 2016-09-05.
 */
/*用于viewpager 的adapter加载*/
public class ViewpagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> list;

    public ViewpagerAdapter(FragmentManager fm,List<Fragment> list) {
        super(fm);
        // TODO Auto-generated constructor stub
        this.list=list;
    }

    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if(list!=null){
            return list.size();
        }
        return 0;

    }
}
