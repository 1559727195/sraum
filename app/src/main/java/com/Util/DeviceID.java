package com.Util;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by masskywcy on 2016-09-06.
 */
//获取android手机设备编号
public class DeviceID {
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }
}
