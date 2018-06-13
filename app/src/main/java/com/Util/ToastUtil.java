package com.Util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by masskywcy on 2016-07-14.
 */
/*用于toast*/
public class ToastUtil {
    /*用于展示toast中部位置*/
    public static void showToast(Context context,String toastmsg){
        Toast toast = Toast.makeText(context,
                toastmsg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    /*用于展示toast默认位置*/
    public static void showDelToast(Context context,String toastmag){
        Toast.makeText(context, toastmag,
                Toast.LENGTH_SHORT).show();
    }
}
