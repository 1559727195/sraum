package com.massky.sraum;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.DialogUtil;
import com.Util.IntentUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.ToastUtil;
import com.Util.TokenUtil;
import com.base.Basecactivity;
import com.data.User;

import java.util.HashMap;
import java.util.Map;

import butterknife.InjectView;
import okhttp3.Call;

/**
 * Created by masskywcy on 2017-02-21.
 */

public class AddfamilyActivitytwo extends Basecactivity {
    @InjectView(R.id.familytwoedit)
    TextView familytwoedit;
    @InjectView(R.id.nametwoedit)
    TextView nametwoedit;
    @InjectView(R.id.famtwobtn_id)
    Button famtwobtn_id;
    @InjectView(R.id.backrela_id)
    RelativeLayout backrela_id;
    @InjectView(R.id.titlecen_id)
    TextView titlecen_id;
    private String mobilePhone, familyName;
    private DialogUtil dialogUtil;

    @Override
    protected int viewId() {
        return R.layout.addfamilytwo;
    }

    @Override
    protected void onView() {
        titlecen_id.setText("邀请家人");
        dialogUtil = new DialogUtil(AddfamilyActivitytwo.this);
        Bundle bundle = IntentUtil.getIntentBundle(AddfamilyActivitytwo.this);
        mobilePhone = bundle.getString("mobilePhone");
        familyName = bundle.getString("familyName");
        familytwoedit.setText(mobilePhone);
        nametwoedit.setText(familyName);
        backrela_id.setOnClickListener(this);
        famtwobtn_id.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backrela_id:
                AddfamilyActivitytwo.this.finish();
                break;
            case R.id.famtwobtn_id:
                add_family_act();
                break;
        }
    }

    private void add_family_act() {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(AddfamilyActivitytwo.this));
        map.put("mobilePhone", mobilePhone);
        map.put("familyName", familyName);
        dialogUtil.loadDialog();
        MyOkHttp.postMapObject(ApiHelper.sraum_addFamily, map, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {
                add_family_act();
            }
        }, AddfamilyActivitytwo.this, dialogUtil) {
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                dialogUtil.removeDialog();
                ToastUtil.showDelToast(AddfamilyActivitytwo.this, "网络连接超时");
            }

            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                if (user.result.equals("100")) {
                    IntentUtil.startActivity(AddfamilyActivitytwo.this, MyfamilyActivity.class);
                    AddfamilyActivitytwo.this.finish();
                } else {
                    ToastUtil.showDelToast(AddfamilyActivitytwo.this, "名字重复");
                }

            }

            @Override
            public void wrongBoxnumber() {
                ToastUtil.showDelToast(AddfamilyActivitytwo.this, "账号已经是您的家属");
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }
}
