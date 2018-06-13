package com.massky.sraum;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.Util.LogUtil;

import java.io.IOException;

/*用于后台播放音乐*/
public class MyServcie extends Service {
    private MediaPlayer player = null;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        if (player == null) {
            player = MediaPlayer.create(MyServcie.this, R.raw.di);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        if (intent == null) {
            return super.onStartCommand(intent, flags, startId);
        }
        int command = intent.getIntExtra("command", 0);
        switch (command) {
            case 1:
                play();
                break;
            case 2:
                pause();
                break;
            case 3:
                stop();
                break;
            default:
                break;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        if (player != null) {
            player.stop();
            player.release();
        }
        super.onDestroy();

    }

    public void play() {
        if (player != null && !player.isPlaying()) {
            LogUtil.i("音乐正式响起");
            player.start();
        }
    }

    public void pause() {
        if (player != null && player.isPlaying()) {
            player.pause();
        }

    }

    public void stop() {
        if (player != null && player.isPlaying()) {
            player.stop();
            try {
                player.prepare();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
