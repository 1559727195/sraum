package com.massky.sraum;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.ClearEditText;
import com.Util.DialogUtil;
import com.Util.IntentUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.ToastUtil;
import com.base.Basecactivity;
import com.data.User;

import java.util.HashMap;
import java.util.Map;

import butterknife.InjectView;

/**
 * Created by xufuchao on 2017-02-21.
 */
/*
* 添加成员界面*/
public class AddfamilyActivity extends Basecactivity {
    @InjectView(R.id.familyedit)
    ClearEditText familyedit;
    @InjectView(R.id.nameedit)
    ClearEditText nameedit;
    @InjectView(R.id.fambtn_id)
    Button fambtn_id;
    @InjectView(R.id.backrela_id)
    RelativeLayout backrela_id;
    @InjectView(R.id.titlecen_id)
    TextView titlecen_id;
    private DialogUtil dialogUtil;
    private Bundle bundle;

    @Override
    protected int viewId() {
        return R.layout.invitefamily;
    }

    @Override
    protected void onView() {
        bundle = new Bundle();
        titlecen_id.setText("邀请家人");
        dialogUtil = new DialogUtil(AddfamilyActivity.this);
        fambtn_id.setOnClickListener(this);
        backrela_id.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backrela_id:
                AddfamilyActivity.this.finish();
                break;
            case R.id.fambtn_id:
                String mobilePhone = familyedit.getText().toString();
                String familyName = nameedit.getText().toString();
                bundle.putString("mobilePhone", mobilePhone);
                bundle.putString("familyName", familyName);
                if (!mobilePhone.equals("") && !familyName.equals("")) {
                    if (mobilePhone.length() > 5) {
                        dialogUtil.loadDialog();
                        sraum_checkMobile(mobilePhone);
                    } else {
                        ToastUtil.showDelToast(AddfamilyActivity.this, "手机号码格式不正确");
                    }
                } else {
                    ToastUtil.showDelToast(AddfamilyActivity.this, "信息不能为空");
                }
                break;
        }
    }

    private void sraum_checkMobile(final String mobilePhone) {
        Map<String, Object> map = new HashMap<>();
        map.put("mobilePhone", mobilePhone);
        MyOkHttp.postMapObject(ApiHelper.sraum_checkMobilePhone, map,
                new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {
                        sraum_checkMobile(mobilePhone);
                    }
                }, AddfamilyActivity.this, dialogUtil) {
                    @Override
                    public void onSuccess(User user) {
                        super.onSuccess(user);
                        IntentUtil.startActivity(AddfamilyActivity.this, AddfamilyActivitytwo.class, bundle);
                    }

                    @Override
                    public void wrongToken() {
                        ToastUtil.showToast(AddfamilyActivity.this,"手机号已存在");
                    }
                });
    }
}
