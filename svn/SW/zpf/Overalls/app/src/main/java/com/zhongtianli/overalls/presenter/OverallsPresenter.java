package com.zhongtianli.overalls.presenter;

import android.content.Context;

import com.zhongtianli.overalls.bean.GetClothesBean;
import com.zhongtianli.overalls.model.Get_overalls_clothes_getIn;
import com.zhongtianli.overalls.model.Overalls_clothes_getIn;
import com.zhongtianli.overalls.model.Overalls_clothes_getIn_listener;
import com.zhongtianli.overalls.view.Overalls_clothes_getIn_view;

/**
 * Created by zhu on 2016/12/28.
 */

public class OverallsPresenter {
    //	获取库存信息（Clothes_GetIn）
    private  Overalls_clothes_getIn_view overalls_clothes_getIn_view;//展示获取库存信息展示View
    private Get_overalls_clothes_getIn get_overalls_clothes_getIn;
    public  OverallsPresenter ( Overalls_clothes_getIn_view overalls_clothes_getIn_view) {
        this.overalls_clothes_getIn_view = overalls_clothes_getIn_view;
        this.get_overalls_clothes_getIn  = new Overalls_clothes_getIn();
    }

    //具体由activity传进来的参数,获取库存信息
    public void  Overalls_clothes_getInLists(Context context, String ipString){
         get_overalls_clothes_getIn.get_overalls_clothes_getIn(context,ipString,new Overalls_clothes_getIn_listener() {
             @Override
             public void overalls_clothes_getIn_listener_sus(GetClothesBean getClothesBean){
                   overalls_clothes_getIn_view.overalls_clothes_getIn_view_sus(getClothesBean);
             }

             @Override
             public void overalls_clothes_getIn_listener_fail() {
                 overalls_clothes_getIn_view.overalls_clothes_getIn_view_fail();
             }
         });
    }
}
