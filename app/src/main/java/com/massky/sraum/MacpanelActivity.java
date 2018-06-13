package com.massky.sraum;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.base.Basecactivity;

import butterknife.InjectView;

/**
 * Created by masskywcy on 2016-11-30.
 */

public class MacpanelActivity extends Basecactivity {
    @InjectView(R.id.backrela_id)
    RelativeLayout backrela_id;
    @InjectView(R.id.addroomrelative_id)
    RelativeLayout addroomrelative_id;
    @InjectView(R.id.titlecen_id)
    TextView titlecen_id;
    @InjectView(R.id.addscroll)
    ScrollView addscroll;
    @InjectView(R.id.addmacimageview)
    ImageView addmacimageview;
    @InjectView(R.id.search_image)
    ImageView search_image;
    @InjectView(R.id.search_image_rel)
    RelativeLayout search_image_rel;

    @Override
    protected int viewId() {
        return R.layout.findmacpanel;
    }

    @Override
    protected void onView() {
        search_image.setVisibility(View.VISIBLE);
        search_image_rel.setVisibility(View.VISIBLE);
        search_image_rel.setOnClickListener(this);
        startState();
    }

    private void startState() {
        backrela_id.setOnClickListener(this);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        switch (name) {
            case "1":
                titlecen_id.setText(R.string.addroom);
                addroomrelative_id.setVisibility(View.VISIBLE);
                addscroll.setVisibility(View.GONE);
                break;
            case "2":
                titlecen_id.setText(R.string.addpanel);
                addroomrelative_id.setVisibility(View.GONE);
                addscroll.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backrela_id:
                MacpanelActivity.this.finish();
                break;
            case R.id. search_image_rel:
                Intent intent = new Intent();
                intent.setAction("UPSTATUS");
                intent.putExtra("type","panel");
                sendBroadcast(intent);
                MacpanelActivity.this.finish();
                //跳转到侧滑，智能设备列表
                break;
        }
    }
}
