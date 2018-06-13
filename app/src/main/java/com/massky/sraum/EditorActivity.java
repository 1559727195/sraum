package com.massky.sraum;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.Basecactivity;

import butterknife.InjectView;

/**
 * Created by xfc on 2017-03-22.
 */
//编辑场景
public class EditorActivity extends Basecactivity {
    @InjectView(R.id.backrela_id)
    RelativeLayout backrelaId;
    @InjectView(R.id.titlecen_id)
    TextView titlecenId;
    @InjectView(R.id.editorlist)
    ListView editorlist;
    @InjectView(R.id.editorbtn)
    Button editorbtn;

    @Override
    protected int viewId() {
        return R.layout.editorlay;
    }

    @Override
    protected void onView() {
        titlecenId.setText("编辑场景");
        backrelaId.setOnClickListener(this);
        editorbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backrela_id:
                EditorActivity.this.finish();
                break;
        }
    }

}
