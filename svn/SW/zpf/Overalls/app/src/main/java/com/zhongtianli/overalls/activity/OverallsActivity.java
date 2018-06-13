package com.zhongtianli.overalls.activity;

import android.support.v4.view.PagerAdapter;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.zhongtianli.overalls.R;
import com.zhongtianli.overalls.adapter.FragmentViewPagerAdapter;
import com.zhongtianli.overalls.bean.GetClothesBean;

/**
 * Created by zhu on 2016/12/27.
 */

public class OverallsActivity extends OverallsBaseActivity {
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_store:
                initStoreCard();
                //切换到存储fragment碎片
                mContentPager.setCurrentItem(0);
                break;//存储
            case R.id.txt_take_out:
                //切换到取出fragment碎片
                initTakeOutCard();
                mContentPager.setCurrentItem(1);
                break;//取出
        }
    }


    @Override
    protected PagerAdapter getPagerAdapter() {
        FragmentViewPagerAdapter fragmentViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(),
                mContentPager, mFragmentList);
        return fragmentViewPagerAdapter;
    }

    @Override
    public void overalls_clothes_getIn_view_sus(GetClothesBean getClothesBean) {//一.	获取库存信息（Clothes_GetIn）-成功
        handlerDialog.sendEmptyMessage(0);//关闭Dialog
        overalls_getIn_listener.overalls_GetIn(getClothesBean);
    }

    @Override
    public void overalls_clothes_getIn_view_fail() {//一.	获取库存信息（Clothes_GetIn）-失败
        handlerDialog.sendEmptyMessage(0);//关闭Dialog
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
       switch(v.getId()) {
           case R.id.pop_menu_lay://点击右上角popwindow菜单时
           switch (event.getAction()) {
               case MotionEvent.ACTION_DOWN://即按下时，让三个小按钮变成深蓝色
                    dotColor_blue();
                   break;
               case MotionEvent.ACTION_UP://手起来时，让三个小按钮变成白色
                    dotColor_white();
                   //在这里我要让它弹出popwindow
                   titlePopup.show(v);
                   break;
           }
           break;
       }
        return true;
    }

}
