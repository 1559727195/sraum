package com.massky.sraum;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.ClearEditText;
import com.Util.DialogUtil;
import com.Util.LimitCharLengthFilter;
import com.Util.LogUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.SharedPreferencesUtil;
import com.Util.ToastUtil;
import com.Util.TokenUtil;
import com.base.Basecactivity;
import com.data.User;
import com.j256.ormlite.field.types.IntegerObjectType;
import com.karics.library.zxing.android.CaptureActivity;
import com.permissions.RxPermissions;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.util.HashMap;
import java.util.Map;

import butterknife.InjectView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.Util.AES.Decrypt;

/**
 * Created by masskywcy on 2016-09-12.
 */
//用于添加网关界面
public class AddgatewayActivity extends Basecactivity {
    @InjectView(R.id.backrela_id)
    RelativeLayout backrela_id;
    @InjectView(R.id.titlecen_id)
    TextView titlecen_id;
    @InjectView(R.id.locanet_id)
    RelativeLayout locanet_id;
    @InjectView(R.id.qrcode_id)
    RelativeLayout qrcode_id;
    @InjectView(R.id.detairela)
    RelativeLayout detairela;
    @InjectView(R.id.scanlinear)
    LinearLayout scanlinear;
    @InjectView(R.id.gateid)
    TextView gateid;
    @InjectView(R.id.addsave_id)
    Button addsave_id;
    @InjectView(R.id.gatedditext)
    ClearEditText gatedditext;
    @InjectView(R.id.gatedditexttwo)
    ClearEditText gatedditexttwo;

    private static final int REQUEST_CODE_SCAN = 0x0000;
    private static final int REQUECT_CODE_SDCARD = 2;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private int sdkversion;
    private DialogUtil dialogUtil;
    private int REQUEST_SEARCHGATE_SCAN = 0x008;
    private String act_flag = "act_flag";//activity标志

    @Override
    protected int viewId() {
        return R.layout.addgateway;
    }

    @Override
    protected void onView() {
        initPermission();
        InputFilter[] filters = new InputFilter[]{new LimitCharLengthFilter(12)};
        gatedditext.setFilters(filters);
        gatedditexttwo.setFilters(filters);
        dialogUtil = new DialogUtil(AddgatewayActivity.this);
        sdkversion = Integer.parseInt(Build.VERSION.SDK);
        titlecen_id.setVisibility(View.GONE);
        backrela_id.setOnClickListener(this);
        locanet_id.setOnClickListener(this);
        qrcode_id.setOnClickListener(this);
        addsave_id.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backrela_id:
                switch (act_flag) {
                    case "Search":
                        Intent intent = new Intent(AddgatewayActivity.this, SearchgatewayActivity.class);
                        startActivityForResult(intent, REQUEST_SEARCHGATE_SCAN);
                        overridePendingTransition(R.anim.push_right_in,
                                R.anim.push_right_out);
                        break;//说明是SearchgatewayActivity过来的
                    case "goBack":
                        AddgatewayActivity.this.finish();
                        break;
                    case "scan_flag":
                        act_flag = "goBack";
                        scanlinear.setVisibility(View.VISIBLE);
                        detairela.setVisibility(View.GONE);
                        break;
                    case "act_flag":
                        AddgatewayActivity.this.finish();
                        break;
                }
                break;
            case R.id.locanet_id:
                Intent intent = new Intent(AddgatewayActivity.this, SearchgatewayActivity.class);
                startActivityForResult(intent, REQUEST_SEARCHGATE_SCAN);
                break;
            case R.id.qrcode_id:
                    Intent intentc = new Intent(AddgatewayActivity.this,
                            CaptureActivity.class);
                    startActivityForResult(intentc, REQUEST_CODE_SCAN);
                break;
            case R.id.addsave_id:
                String boxName = gatedditext.getText().toString();
                String boxPwd = gatedditexttwo.getText().toString();
                if (boxPwd.equals("")) {
                    ToastUtil.showDelToast(AddgatewayActivity.this, "网关密码不能为空");
                } else {
                    addBox(gateid.getText().toString(), "123", boxPwd);
                }
                break;
        }
    }

    private void initPermission() {
        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.CAMERA).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void addBox(final String boxNumber, String boxName, String boxPwd) {
        addgateway_act(boxNumber, boxName, boxPwd);
    }

    private void addgateway_act(final String boxNumber, final String boxName,final String boxPwd) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(AddgatewayActivity.this));
        map.put("boxNumber", boxNumber);
        map.put("boxName", boxName);
        map.put("boxPwd", boxPwd);
        dialogUtil.loadDialog();
        MyOkHttp.postMapObject(ApiHelper.sraum_addBox, map, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {
                addgateway_act(boxNumber, boxName, boxPwd);
            }
        }, AddgatewayActivity.this, dialogUtil) {
            @Override
            public void threeCode() {
                super.threeCode();
                ToastUtil.showDelToast(AddgatewayActivity.this, "网关名称重复");
            }

            @Override
            public void fourCode() {
                super.fourCode();
                ToastUtil.showDelToast(AddgatewayActivity.this, "网关密码错误");
            }

            @Override
            public void fiveCode() {
                super.fiveCode();
                ToastUtil.showDelToast(AddgatewayActivity.this, "网关已存在");
            }

            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                SharedPreferencesUtil.saveData(AddgatewayActivity.this, "boxnumber", boxNumber);
                AddgatewayActivity.this.finish();
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(DECODED_CONTENT_KEY);
//                String subcon = content.substring(0, 11);
                act_flag = "scan_flag";
//                LogUtil.eLength("截取后的数据", subcon);//masskysraum61a001ff1002singlechipgateway
//                if (subcon.equals("masskysraum")) {
//                    String subtent = content.substring(11, 23);
//                    LogUtil.eLength("截取后的数据", subtent);
//                    //Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);
//                    scanlinear.setVisibility(View.GONE);
//                    detairela.setVisibility(View.VISIBLE);
//                    gateid.setText(subtent);
//                    gatedditexttwo.setText("");
//                } else {
//                    ToastUtil.showToast(AddgatewayActivity.this, "此二维码不是网关二维码");
//                    scanlinear.setVisibility(View.VISIBLE);
//                    detairela.setVisibility(View.GONE);
//                }

                Log.e("robin debug","content:" + content);
                if(TextUtils.isEmpty(content))
                    return;
                //在这里解析二维码，变成房号
                // 密钥
                String key = "ztlmassky6206ztl";
                // 解密
                String DeString = null;
                try {
//                    content = "0a4ab23ad13aac565069283aac3882e5";
                    DeString = Decrypt(content, key);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(DeString == null){
                    ToastUtil.showToast(AddgatewayActivity.this, "此二维码不是网关二维码");
                    scanlinear.setVisibility(View.VISIBLE);
                    detairela.setVisibility(View.GONE);
                } else {
                    scanlinear.setVisibility(View.GONE);
                    detairela.setVisibility(View.VISIBLE);
                    gateid.setText(DeString);
                    gatedditexttwo.setText("");
                }
            }
        } else if (requestCode == REQUEST_SEARCHGATE_SCAN && resultCode == RESULT_OK) {//REQUEST_SEARCHGATE_SCAN
            if (data != null) {
                String content = data.getStringExtra("gateWayMac");
                if (content != null) {
                    act_flag = data.getStringExtra("act_flag");
                    scanlinear.setVisibility(View.GONE);
                    detairela.setVisibility(View.VISIBLE);
                    gateid.setText(content);
                    gatedditexttwo.setText("");
                } else {
                    act_flag = "goBack";//goBack
                    scanlinear.setVisibility(View.VISIBLE);
                    detairela.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        switch (act_flag) {
            case "Search":
                Intent intent = new Intent(AddgatewayActivity.this, SearchgatewayActivity.class);
                startActivityForResult(intent, REQUEST_SEARCHGATE_SCAN);
                overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
                break;//说明是SearchgatewayActivity过来的
            case "goBack":
                AddgatewayActivity.this.finish();
                break;
            case "scan_flag":
                act_flag = "goBack";
                scanlinear.setVisibility(View.VISIBLE);
                detairela.setVisibility(View.GONE);
                break;
            case "act_flag":
                AddgatewayActivity.this.finish();
                break;
        }
    }
}
