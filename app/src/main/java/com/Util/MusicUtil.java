package com.Util;

import android.content.Context;
import android.content.Intent;

import com.massky.sraum.MyServcie;

/**
 * Created by masskywcy on 2016-08-31.
 */
//用于音乐播放器使用
public class MusicUtil {
    /*用于开启播放音乐*/
    public static void startMusic(Context context, int command) {
        Intent intent = new Intent(context, MyServcie.class);
        intent.putExtra("command", command);
        context.startService(intent);
    }

    /*用于音乐停止播放*/
    public static void stopMusic(Context context) {
        Intent intent = new Intent(context, MyServcie.class);
        context.stopService(intent);
    }
}
