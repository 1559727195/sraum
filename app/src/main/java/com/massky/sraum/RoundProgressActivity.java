package com.massky.sraum;

import android.view.View;

import com.Util.view.RoundProgressBar;
import com.base.Basecactivity;

/**
 * Created by zhu on 2017/10/26.
 */

public class RoundProgressActivity extends Basecactivity {
    private boolean is_index = true;
    private RoundProgressBar roundProgressBar2;

    @Override
    protected int viewId() {
        return R.layout.round_progress_act;
    }

    @Override
    protected void onView() {
        roundProgressBar2 = (RoundProgressBar) findViewById(R.id.roundProgressBar2);
            roundProgressBar2.setMax(255);
//        for (int i = 0; i < 100; i ++) {
//            try {
//                Thread.sleep(1000);
//                roundProgressBar2.setProgress(i);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        final int[] index = {255};
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (is_index) {
                    index[0]-- ;
                    try {
                        Thread.sleep(1000);
                        roundProgressBar2.setProgress(i++);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (index[0] < 0) {
                        is_index = false;
                    }
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {

    }
}
