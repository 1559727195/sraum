package com.massky.sraum;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.ClearEditText;
import com.Util.DialogUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.SharedPreferencesUtil;
import com.Util.ToastUtil;
import com.Util.TokenUtil;
import com.base.Basecactivity;
import com.data.User;

import java.util.HashMap;
import java.util.Map;

import butterknife.InjectView;

import static android.R.id.list;
import static com.massky.sraum.R.id.editextdetail;
import static com.massky.sraum.R.id.gatedditext;
import static com.massky.sraum.R.id.gatedditexttwo;
import static com.massky.sraum.R.id.gateid;
import static com.massky.sraum.R.id.search_txt;

/**
 * Created by zhu on 2017/7/4.
 */

public class AddgateWayDetialActivity extends Basecactivity{
    @InjectView(R.id.backrela_id)
    RelativeLayout backrela_id;
    @InjectView(R.id.titlecen_id)
    TextView titlecen_id;
    @InjectView(R.id.gatedditexttwo_new)
    ClearEditText gatedditexttwo_new;
    private DialogUtil dialogUtil;
    @InjectView(R.id.gateid)
    TextView gateid;
    @InjectView(R.id.addsave_id)
    Button addsave_id;
    @Override
    protected int viewId() {
        return R.layout.addgatewaydetail;
    }

    @Override
    protected void onView() {
        Intent intent = getIntent();
        String gateWayMac =  intent.getStringExtra("gateWayMac");
        gateid.setText(gateWayMac);
        dialogUtil = new DialogUtil(AddgateWayDetialActivity.this);
        titlecen_id.setText("");
        backrela_id.setOnClickListener(this);
        addsave_id.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
         switch (v.getId()) {
             case R.id.backrela_id:
                 onBackPressed();
                 break;
             case R.id.addsave_id:
                 Intent intent = getIntent();
                 setResult(RESULT_OK, intent);
                 finish();
                 String boxPwd = gatedditexttwo_new.getText().toString();
                 if (boxPwd.equals("")) {
                     ToastUtil.showDelToast(AddgateWayDetialActivity.this, "网关密码不能为空");
                 } else {
                     addBox(gateid.getText().toString(), "", boxPwd);
                 }
                 break;
         }
    }

    private void addBox(final String boxNumber, String boxName, String boxPwd) {
        addgateway_act(boxNumber, boxName, boxPwd);
    }

    private void addgateway_act(final String boxNumber, final String boxName,final String boxPwd) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(AddgateWayDetialActivity.this));
        map.put("boxNumber", boxNumber);
        map.put("boxName", boxName);
        map.put("boxPwd", boxPwd);
        dialogUtil.loadDialog();
        MyOkHttp.postMapObject(ApiHelper.sraum_addBox, map, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {
                addgateway_act(boxNumber, boxName, boxPwd);
            }
        }, AddgateWayDetialActivity.this, dialogUtil) {
            @Override
            public void threeCode() {
                super.threeCode();
                ToastUtil.showDelToast(AddgateWayDetialActivity.this, "网关名称重复");
            }

            @Override
            public void fourCode() {
                super.fourCode();
                ToastUtil.showDelToast(AddgateWayDetialActivity.this, "网关密码错误");
            }

            @Override
            public void fiveCode() {
                super.fiveCode();
                ToastUtil.showDelToast(AddgateWayDetialActivity.this, "网关已存在");
            }

            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                SharedPreferencesUtil.saveData(AddgateWayDetialActivity.this, "boxnumber", boxNumber);
                Intent intent = getIntent();
                setResult(RESULT_OK, intent);
                finish();
//                AddgateWayDetialActivity.this.finish();
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }
}
