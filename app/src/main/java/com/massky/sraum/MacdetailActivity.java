package com.massky.sraum;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spannable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.ClearEditText;
import com.Util.DialogUtil;
import com.Util.IntentUtil;
import com.Util.LimitCharLengthFilter;
import com.Util.LogUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.SelectionEnd;
import com.Util.SharedPreferencesUtil;
import com.Util.ToastUtil;
import com.Util.TokenUtil;
import com.base.Basecactivity;
import com.data.User;

import java.util.HashMap;
import java.util.Map;

import butterknife.InjectView;
import okhttp3.Call;

/**
 * Created by masskywcy on 2016-09-23.
 */
//设备详情界面
public class MacdetailActivity extends Basecactivity {
    @InjectView(R.id.backrela_id)
    RelativeLayout backrela_id;
    @InjectView(R.id.titlecen_id)
    TextView titlecen_id;
    @InjectView(R.id.macdethreetexttwo)
    TextView macdethreetexttwo;
    @InjectView(R.id.macdetwotexttwo)
    TextView macdetwotexttwo;
    @InjectView(R.id.macdetentext)
    TextView macdetentext;
    @InjectView(R.id.macdeleventext)
    TextView macdeleventext;
    @InjectView(R.id.editexthree)
    ClearEditText editexthree;
    @InjectView(R.id.editextfour)
    ClearEditText editextfour;
    @InjectView(R.id.editextone)
    ClearEditText editextone;
    @InjectView(R.id.macdbtton_id)
    Button macdbtton_id;
    private String name, type, boxnumber, number, name1, name2, panelName = "";
    private Bundle bundle;
    private DialogUtil dialogUtil;
    @InjectView(R.id.other_device_all_rel)
    RelativeLayout other_device_all_rel;
    @InjectView(R.id.device_status_txt)
    TextView device_status_txt;
    //device_dimmer_txt
    @InjectView(R.id.device_dimmer_txt)
    TextView device_dimmer_txt;
    @InjectView(R.id.device_mode_txt)
    TextView device_mode_txt;
    private String mode;
    private String status;
    private String dimmer;
    @InjectView(R.id.macdetwotext)
    TextView macdetwotext;

    @Override
    protected int viewId() {
        return R.layout.macdetail;
    }

    @Override
    protected void onView() {
        InputFilter[] filters = new InputFilter[]{new LimitCharLengthFilter(12)};
        editextone.setFilters(filters);
        editexthree.setFilters(filters);
        editextfour.setFilters(filters);
        dialogUtil = new DialogUtil(MacdetailActivity.this);
        boxnumber = (String) SharedPreferencesUtil.getData(MacdetailActivity.this, "boxnumber", "");
        LogUtil.e("这是数据", boxnumber);
        backrela_id.setOnClickListener(this);
        macdbtton_id.setOnClickListener(this);
        bundle = IntentUtil.getIntentBundle(MacdetailActivity.this);
        name = bundle.getString("name");
        type = bundle.getString("type");
        mode = bundle.getString("mode");
        status = bundle.getString("status");
        dimmer = bundle.getString("dimmer");
        switch (type) {
            case "7":
            case "8":
            case "9":
            case "10":
            case "11":
                other_device_all_rel.setVisibility(View.VISIBLE);
                device_mode_txt.setText(mode);
                device_dimmer_txt.setText(dimmer);
                device_status_txt.setText(status);
                macdetwotexttwo.setVisibility(View.GONE);
                macdetwotext.setVisibility(View.GONE);
                break;
            default:
                other_device_all_rel.setVisibility(View.GONE);
                break;
        }

        LogUtil.eLength("数据查看", type);
        number = bundle.getString("number");
        name1 = bundle.getString("name1");
        name2 = bundle.getString("name2");
        panelName = bundle.getString("panelName");
        macdetwotexttwo.setText(panelName);
        editexthree.setText(name1);
        editextfour.setText(name2);
        SelectionEnd.setSelectionEnd(editexthree);
        SelectionEnd.setSelectionEnd(editextfour);
        iniState();
    }

    private void iniState() {
        titlecen_id.setText(name);
        editextone.setText(name);
        editextone.setCursorVisible(false);//光标消失
        //切换后将EditText光标置于末尾
        CharSequence charSequence = editextone.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
        if (!type.equals("4")) {
            macdetentext.setVisibility(View.GONE);
            macdeleventext.setVisibility(View.GONE);
            editexthree.setVisibility(View.GONE);
            editextfour.setVisibility(View.GONE);
        }
        switch (type) {
            case "1":
                macdethreetexttwo.setText("灯");
                break;
            case "2":
                macdethreetexttwo.setText("调光");
                break;
            case "3":
                macdethreetexttwo.setText("空调");
                break;
            case "4":
                macdethreetexttwo.setText("窗帘");
                break;
            case "5":
                macdethreetexttwo.setText("新风");
                break;
            case "6":
                macdethreetexttwo.setText("地暖");
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backrela_id:
                String boxname = editextone.getText().toString();
                if (type.equals("4")) {
                    String name3 = editexthree.getText().toString();
                    String name4 = editextfour.getText().toString();
                    if (boxname.equals(name) && name3.equals(name1) && name4.equals(name2)) {
                        MacdetailActivity.this.finish();
                    } else {
                        updateDeviceInfo(name3, name4);
                    }
                } else {
                    if (!name.equals(boxname)) {
                        if (boxname.equals("")) {
                            ToastUtil.showToast(MacdetailActivity.this, "更改设备信息不能为空");
                        } else {
                            updateDeviceInfo("", "");
                        }
                    } else {
                        MacdetailActivity.this.finish();
                    }
                }
                break;
            case R.id.macdbtton_id:
                clickanimation ();
                refresh_mac_detail();// Refresh_MAC
                break;
        }
    }

    private void clickanimation() {
        Animation clickAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_animation_small);
        macdbtton_id.startAnimation(clickAnimation);

        //如果你想要点下去然后弹上来就这样
        clickAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //动画执行完后的动作
                macdbtton_id.clearAnimation();
                Animation clickAnimation = AnimationUtils.loadAnimation(MacdetailActivity.this, R.anim.scale_animation_big);
                macdbtton_id.startAnimation(clickAnimation);
            }
        });
    }

    /**
     * Refresh_MAC
     */
    private void refresh_mac_detail() {
        dialogUtil.loadDialog();
        Mac_details_1();
    }

    private void Mac_details_1() {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(MacdetailActivity.this));
        map.put("boxNumber", boxnumber);
        map.put("deviceNumber", number);
        MyOkHttp.postMapObject(ApiHelper.sraum_findDevice, map, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {//获取gogglen刷新数据
                Mac_details_1();
            }
        }, MacdetailActivity.this, dialogUtil) {
            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                switch (user.result) {
                    case "100":
                        ToastUtil.showDelToast(MacdetailActivity.this, "操作完成，查看对应设备");
                        break;
                    case "103":
                        ToastUtil.showDelToast(MacdetailActivity.this, "设备编号不存在");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }

    private void updateDeviceInfo(String strone, String strtwo) {
        sraum_update_s(strone, strtwo);

    }

    private void sraum_update_s(final String strone,final String strtwo) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(MacdetailActivity.this));
        map.put("boxNumber", boxnumber);
        map.put("deviceNumber", number);
        map.put("customName", editextone.getText().toString());
        if (type.equals("4")) {
            map.put("name1", strone);
            map.put("name2", strtwo);
        }
        dialogUtil.loadDialog();
        MacdetailActivity.this.finish();
        MyOkHttp.postMapObject(ApiHelper.sraum_updateDeviceInfo, map, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {
                sraum_update_s(strone, strtwo);
            }
        }, MacdetailActivity.this, dialogUtil) {
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
            }

            @Override
            public void wrongBoxnumber() {
                super.wrongBoxnumber();
                ToastUtil.showDelToast(MacdetailActivity.this, "网关不正确");
            }

            @Override
            public void threeCode() {
                super.threeCode();
                ToastUtil.showDelToast(MacdetailActivity.this, "设备编号不正确");
            }

            @Override
            public void fourCode() {
                super.fourCode();
                ToastUtil.showDelToast(MacdetailActivity.this, "自定义名称重复");
            }

            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                ToastUtil.showDelToast(MacdetailActivity.this, "上传成功");
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }

    @Override
    protected void onPause() {
        if (dialogUtil != null)
            dialogUtil.removeDialog();
        super.onPause();
    }
}
