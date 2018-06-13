package com.massky.sraum;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.ClearEditText;
import com.Util.DialogUtil;
import com.Util.EyeUtil;
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
 * Created by masskywcy on 2017-01-05.
 */

public class FindPwdtwoActivity extends Basecactivity {
    @InjectView(R.id.fintwopho_id)
    ClearEditText fintwopho_id;
    @InjectView(R.id.fintwoyz_id)
    ClearEditText fintwoyz_id;
    @InjectView(R.id.nexttwobtn_id)
    Button nexttwobtn_id;
    @InjectView(R.id.back_id)
    RelativeLayout back_id;
    @InjectView(R.id.centitle_id)
    TextView centitle_id;
    @InjectView(R.id.fineyeone)
    ImageView fineyeone;
    @InjectView(R.id.fineyetwo)
    ImageView fineyetwo;
    private String mobilePhone;
    private DialogUtil dialogUtil;
    private EyeUtil eyeUtilone, eyeUtiltwo;

    @Override
    protected int viewId() {
        return R.layout.findpwdtwo;
    }

    @Override
    protected void onView() {
        inti();
    }

    private void inti() {
        mobilePhone = IntentUtil.getIntentString(FindPwdtwoActivity.this, "finpho");
        back_id.setOnClickListener(this);
        nexttwobtn_id.setOnClickListener(this);
        fineyeone.setOnClickListener(this);
        fineyetwo.setOnClickListener(this);
        eyeUtilone = new EyeUtil(FindPwdtwoActivity.this, fineyeone, fintwopho_id, true);
        eyeUtiltwo = new EyeUtil(FindPwdtwoActivity.this, fineyetwo, fintwoyz_id, true);
        centitle_id.setText(R.string.findname);
        dialogUtil = new DialogUtil(FindPwdtwoActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_id:
                FindPwdtwoActivity.this.finish();
                break;
            case R.id.nexttwobtn_id:
                findPwdTwo();//findPwdTwo()
                break;
            case R.id.fineyeone:
                eyeUtilone.EyeStatus();
                break;
            case R.id.fineyetwo:
                eyeUtiltwo.EyeStatus();
                break;
        }
    }

    //findPwdTwo()
    private void findPwdTwo() {
        String newPwd = fintwopho_id.getText().toString();
        String newPwdtwo = fintwoyz_id.getText().toString();
        if (newPwd.equals("") || newPwdtwo.equals("")) {
            ToastUtil.showDelToast(FindPwdtwoActivity.this, "密码不能为空");
        } else {
            if (newPwd.equals(newPwdtwo)) {
                Map<String, String> map = new HashMap<>();
                map.put("mobilePhone", mobilePhone);
                map.put("newPwd", newPwd);
                dialogUtil.loadDialog();
                MyOkHttp.postMapString(ApiHelper.sraum_updatePwd, map,
                        new Mycallback(new AddTogglenInterfacer() {
                            @Override
                            public void addTogglenInterfacer() {//获取togglen成功后重新刷新数据
                                findPwdTwo();
                            }
                        }, FindPwdtwoActivity.this, dialogUtil) {
                            @Override
                            public void onSuccess(User user) {
                                super.onSuccess(user);
                                IntentUtil.startActivity(FindPwdtwoActivity.this, LoginActivity.class, "loginPhone", mobilePhone);
                            }

                            @Override
                            public void wrongToken() {
                                super.wrongToken();
                            }
                        });

            } else {
                ToastUtil.showDelToast(FindPwdtwoActivity.this, "密码输入不一致");
            }
        }
    }

}
