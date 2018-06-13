package com.massky.sraum;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.Basecactivity;

import butterknife.InjectView;

import static android.R.id.list;

/**
 * Created by masskywcy on 2016-09-13.
 */
/*网关详情页面*/
public class GatedetailActivity extends Basecactivity {
    @InjectView(R.id.backrela_id)
    RelativeLayout backrela_id;
    @InjectView(R.id.titlecen_id)
    TextView titlecen_id;

    @Override
    protected int viewId() {
        return R.layout.details;
    }

    @Override
    protected void onView() {
        titlecen_id.setVisibility(View.GONE);
        backrela_id.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backrela_id:
                Intent intent = getIntent();
                setResult(RESULT_OK, intent);
                GatedetailActivity.this.finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        GatedetailActivity.this.finish();
    }
}
