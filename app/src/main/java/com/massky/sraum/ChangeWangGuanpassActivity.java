package com.massky.sraum;

import android.os.Bundle;
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
import com.Util.TokenDialog;
import com.Util.TokenUtil;
import com.base.Basecactivity;
import com.data.User;
import java.util.HashMap;
import java.util.Map;
import butterknife.InjectView;

/**
 * Created by masskywcy on 2016-09-26.
 */
/*重新设置密码界面*/
public class ChangeWangGuanpassActivity extends Basecactivity {
    @InjectView(R.id.backrela_id)
    RelativeLayout backrela_id;
    @InjectView(R.id.titlecen_id)
    TextView titlecen_id;
    @InjectView(R.id.completebtn_id)
    Button completebtn_id;
    @InjectView(R.id.originalpassword)
    ClearEditText originalpassword;
    @InjectView(R.id.newpassword)
    ClearEditText newpassword;
    @InjectView(R.id.againpassword)
    ClearEditText againpassword;
    @InjectView(R.id.eyeone)
    ImageView eyeone;
    @InjectView(R.id.eyetwo)
    ImageView eyetwo;
    @InjectView(R.id.eyethree)
    ImageView eyethree;

    private DialogUtil dialogUtil;
    private EyeUtil eyeUtilOne, eyeUtilTwo, eyeUtilThree;
    private Bundle bundle;
    private String boxname;
    private String boxnumber;

    @Override
    protected int viewId() {
        return R.layout.forgetpassword;
    }

    @Override
    protected void onView() {
        dialogUtil = new DialogUtil(ChangeWangGuanpassActivity.this);
        completebtn_id.setOnClickListener(this);
        backrela_id.setOnClickListener(this);
        eyeone.setOnClickListener(this);
        eyetwo.setOnClickListener(this);
        eyethree.setOnClickListener(this);
        eyeUtilOne = new EyeUtil(ChangeWangGuanpassActivity.this, eyeone, originalpassword, true);
        eyeUtilTwo = new EyeUtil(ChangeWangGuanpassActivity.this, eyetwo, newpassword, true);
        eyeUtilThree = new EyeUtil(ChangeWangGuanpassActivity.this, eyethree, againpassword, true);
        titlecen_id.setText(R.string.changepassword);
        dialogUtil = new DialogUtil(ChangeWangGuanpassActivity.this);
        bundle = IntentUtil.getIntentBundle(ChangeWangGuanpassActivity.this);
        boxname = bundle.getString("boxName", "");
        boxnumber = bundle.getString("boxnumber", "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backrela_id:
                ChangeWangGuanpassActivity.this.finish();
                break;
            case R.id.completebtn_id:
                String oldPwd = originalpassword.getText().toString();
                String newPwd = newpassword.getText().toString();
                String newPwdtwo = againpassword.getText().toString();
                if (!oldPwd.equals("") && !newPwd.equals("") && !newPwdtwo.equals("")) {
                    if (newPwd.equals(newPwdtwo)) {
                        accountmber_cahn(oldPwd, newPwd);

                    } else {
                        ToastUtil.showToast(ChangeWangGuanpassActivity.this, "新密码输入不一致");
                    }
                } else {
                    ToastUtil.showToast(ChangeWangGuanpassActivity.this, "信息不能为空");
                }
                break;
            case R.id.eyeone:
                eyeUtilOne.EyeStatus();
                break;
            case R.id.eyetwo:
                eyeUtilTwo.EyeStatus();
                break;
            case R.id.eyethree:
                eyeUtilThree.EyeStatus();
                break;
        }
    }

    private void accountmber_cahn(final String oldPwd,final String newPwd) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(ChangeWangGuanpassActivity.this));
        map.put("boxNumber", boxnumber);
        map.put("boxOldPwd", oldPwd);
        map.put("boxNewPwd", newPwd);
        dialogUtil.loadDialog();
        MyOkHttp.postMapObject(ApiHelper.sraum_setBoxPwd, map, new Mycallback
                (new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {
                        accountmber_cahn(oldPwd, newPwd);
                    }
                }, ChangeWangGuanpassActivity.this, dialogUtil) {
            @Override
            public void wrongBoxnumber() {
                ToastUtil.showDelToast(ChangeWangGuanpassActivity.this, "请输入正确的原密码");
            }

            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                switch (user.result) {
                    case "100":
                        ToastUtil.showDelToast(ChangeWangGuanpassActivity.this, "密码修改成功");
                        ChangeWangGuanpassActivity.this.finish();
                        break;
//                    case "103":
//                        ToastUtil.showDelToast(ChangeWangGuanpassActivity.this, "请输入正确的新密码");
//                        break;
//                    case "101":
//                        TokenDialog.getTokenDialog().showToDialog(ChangeWangGuanpassActivity.this);
//                        break;
//                    default:
//                        ToastUtil.showDelToast(ChangeWangGuanpassActivity.this, "程序无响应");
//                        break;
                }
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }

            @Override
            public void threeCode() {
                ToastUtil.showDelToast(ChangeWangGuanpassActivity.this, "原密码输入有误");
            }
        });
    }
}
