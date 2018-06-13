package com.Util;

import android.app.Activity;
import android.content.Context;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.massky.sraum.R;

/**
 * Created by masskywcy on 2016-09-09.
 */
//用于侧滑菜单
public class SlidingUtil {
    public Context context;
    private SlidingMenu menu;

    public SlidingUtil(Context context) {
        this.context = context;
    }

    //用于侧滑出现菜单
    public com.jeremyfeinstein.slidingmenu.lib.SlidingMenu initSlidingMenu() {
        // 初始化SlidingMenu对象
        menu = new SlidingMenu(context);
        // 设置侧滑方式为左侧侧滑
        menu.setMode(SlidingMenu.LEFT);
        /*
         * 设置拖拽模式 SlidingMenu.TOUCHMODE_FULLSCREEN全屏触摸有效
         * SlidingMenu.TOUCHMODE_MARGIN 拖拽边缘有效 SlidingMenu.TOUCHMODE_NONE
         * 不响应触摸事件
         */
        // 控制是否slidingmenu可开有滑动手势。
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
//        menu.setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);
        // 设置阴影的宽度
        menu.setShadowWidthRes(R.dimen.shadow_width);
        // 设置sldingMenu的剩余大小---=内容显示页对应的dp大小
        menu.setBehindOffsetRes(R.dimen.d100);
        // 设置滑动时的渐变程度
        menu.setFadeDegree(0.35f);
        // 使SlidingMenu附加在Activity右边
        // SlidingMenu.SLIDING_CONTENT 将侧滑栏设置为在内容位置
        // SlidingMenu.SLIDING_WINDOW 将侧滑栏设置为在整个窗口呈现
        menu.attachToActivity((Activity) context, SlidingMenu.SLIDING_CONTENT);
        // 设置SlidingMenu关联的布局
        menu.setMenu(R.layout.slidingmenu);
        menu.setOffsetFadeDegree(0.4f);
        // 在SlidingMenu关联布局中查询控件
        return menu;
    }
}
