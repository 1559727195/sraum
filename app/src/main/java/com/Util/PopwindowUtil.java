package com.Util;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Created by masskywcy on 2016-09-27.
 */
/*用于popwindow设置工具*/
public class PopwindowUtil {
    PopupWindow popupWindow;
    //popwindow中加载的布局
    private View view;
    private Context context;
    //x,y为偏移量
    private int x;
    private int y;
    //设置在某个布局的下方
    private View anchor;

    public PopwindowUtil(View anchor, View view, Context context, int x, int y) {
        this.anchor = anchor;
        this.view = view;
        this.context = context;
        this.x = x;
        this.y = y;
    }

    //加载popwindow
    public PopupWindow loadPopupwindow() {
        if (popupWindow == null) {
            popupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT, true);
        }
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.showAsDropDown(anchor, x, y);
        return popupWindow;
    }

    //移除popwindow
    public void removePopwindow() {
        if (popupWindow != null) {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        }
    }
}
