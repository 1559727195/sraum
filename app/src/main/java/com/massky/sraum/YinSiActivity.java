package com.massky.sraum;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.base.Basecactivity;

import butterknife.InjectView;

/**
 * Created by zhu on 2017/8/25.
 */

public class YinSiActivity extends Basecactivity{
    @InjectView(R.id.wvMain)
    WebView wvMain;
    @InjectView(R.id.pbMain)
    ProgressBar pbMain;
    @InjectView(R.id.back)
    ImageView back;
    private String _url = "http://app.sraum.com/sraumApp/yinsi/index.html";
    @Override
    protected int viewId() {
        return R.layout.yinsi_act;
    }

    @Override
    protected void onView() {
        wvMain.getSettings().setJavaScriptEnabled(true);
        wvMain.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
                pbMain.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);

                pbMain.setVisibility(View.GONE);

            }

        });
        wvMain.loadUrl(_url);
        back.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }
}
