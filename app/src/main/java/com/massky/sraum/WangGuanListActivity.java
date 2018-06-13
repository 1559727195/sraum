package com.massky.sraum;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adapter.ShowUdpServerAdapter;
import com.adapter.ShowUdpServerDetailAdapter;
import com.base.Basecactivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import butterknife.InjectView;

import static com.massky.sraum.R.id.item_touch_helper_previous_elevation;
import static com.massky.sraum.R.id.list_show_rev_item;

/**
 * Created by zhu on 2017/7/4.
 */

public  class WangGuanListActivity extends Basecactivity{
    @InjectView(R.id.list_show_rev_item_detail)
    ListView list_show_rev_item_detail;
    @InjectView(R.id.backrela_id)
    RelativeLayout backrela_id;
    @InjectView(R.id.titlecen_id)
    TextView titlecen_id;

    private List<String> list = new ArrayList<>();
    private ShowUdpServerDetailAdapter showUdpServerAdapter;

    @Override
    protected int viewId() {
        return R.layout.wanggaun_list;
    }

    @Override
    protected void onView() {
        Intent intent = getIntent();
       list = intent.getStringArrayListExtra("list");
        showUdpServerAdapter = new ShowUdpServerDetailAdapter(WangGuanListActivity.this
                ,list);
        list_show_rev_item_detail.setAdapter(showUdpServerAdapter);
        list_show_rev_item_detail.setOnItemClickListener(new MyOnItemCLickListener());
        backrela_id.setOnClickListener(this);
        titlecen_id.setText("网关列表");
    }

    private  class  MyOnItemCLickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //销毁activity返回到AddGateWayActivity
//            Intent intent = getIntent();
//            intent.putExtra("gateWayMac",list.get(position).toString());
//            setResult(RESULT_OK, intent);
//            finish();
            Intent intent = new Intent(WangGuanListActivity.this,AddgateWayDetialActivity.class);
            intent.putExtra("gateWayMac",list.get(position).toString());
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backrela_id:
                Intent intent = getIntent();
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        WangGuanListActivity.this.finish();
    }
}
