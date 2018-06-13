package com.massky.sraum;

import android.content.Intent;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.ClearEditText;
import com.Util.DialogUtil;
import com.Util.IntentUtil;
import com.Util.LimitCharLengthFilter;
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

/**
 * Created by masskywcy on 2016-09-27.
 */
//设置选择性别界面
public class SexActivity extends Basecactivity {
    @InjectView(R.id.man_id)
    RelativeLayout man_id;
    @InjectView(R.id.woman_id)
    RelativeLayout woman_id;
    @InjectView(R.id.backrela_id)
    RelativeLayout backrela_id;
    @InjectView(R.id.titlecen_id)
    TextView titlecen_id;
    @InjectView(R.id.newnamerela)
    RelativeLayout newnamerela;
    @InjectView(R.id.sexlinera)
    LinearLayout sexlinera;
    @InjectView(R.id.scenewname)
    ClearEditText scenewname;
    @InjectView(R.id.savebtn)
    Button savebtn;
    private String scename = "", boxNumber;
    private DialogUtil dialogUtil;

    @Override
    protected int viewId() {
        return R.layout.sex;
    }

    @Override
    protected void onView() {
        InputFilter[] filters = new InputFilter[]{new LimitCharLengthFilter(12)};
        scenewname.setFilters(filters);
        dialogUtil = new DialogUtil(SexActivity.this);
        boxNumber = (String) SharedPreferencesUtil.getData(SexActivity.this, "boxnumber", "");
        scename = IntentUtil.getIntentString(SexActivity.this, "scename");
        scenewname.setText(scename);
        if (!scename.equals("")) {
            newnamerela.setVisibility(View.VISIBLE);
            sexlinera.setVisibility(View.GONE);
        } else {
            newnamerela.setVisibility(View.GONE);
            sexlinera.setVisibility(View.VISIBLE);
        }
        titlecen_id.setText(R.string.gender);
        man_id.setOnClickListener(this);
        woman_id.setOnClickListener(this);
        backrela_id.setOnClickListener(this);
        savebtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.savebtn:
                sex_act();
                break;
            case R.id.man_id:
                Intent intent = new Intent();
                intent.putExtra("resultsex", "男");// 把数据塞入intent里面
                SexActivity.this.setResult(0, intent);// 跳转回原来的activity
                SexActivity.this.finish();// 一定要结束当前activity
                break;
            case R.id.woman_id:
                Intent intentt = new Intent();
                intentt.putExtra("resultsex", "女");// 把数据塞入intent里面
                SexActivity.this.setResult(0, intentt);// 跳转回原来的activity
                SexActivity.this.finish();// 一定要结束当前activity
                break;
            case R.id.backrela_id:
                SexActivity.this.finish();
                break;
        }
    }

    //save_btn
    private void sex_act() {
        String sceneNewName = scenewname.getText().toString().trim();
        if (sceneNewName.equals("")) {
            ToastUtil.showDelToast(SexActivity.this, "重命名不能为空");
        } else if (sceneNewName.equals(scename)) {
            ToastUtil.showDelToast(SexActivity.this, "场景名未修改");
        } else {
            dialogUtil.loadDialog();
            sraum_up_scene_name(sceneNewName);

        }
    }

    private void sraum_up_scene_name(final String sceneNewName) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(SexActivity.this));
        map.put("boxNumber", boxNumber);
        map.put("sceneOldName", scename);
        map.put("sceneNewName", sceneNewName);
        MyOkHttp.postMapObject(ApiHelper.sraum_updateSceneName, map, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {//获取togglen刷新数据
                sraum_up_scene_name(sceneNewName);
            }
        }, SexActivity.this, dialogUtil) {
            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                SexActivity.this.finish();
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }

}
