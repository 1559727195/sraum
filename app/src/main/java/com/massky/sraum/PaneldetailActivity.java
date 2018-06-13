package com.massky.sraum;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spannable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
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
import com.Util.ToastUtil;
import com.Util.TokenUtil;
import com.base.Basecactivity;
import com.data.User;

import java.util.HashMap;
import java.util.Map;

import butterknife.InjectView;
import okhttp3.Call;

import static com.massky.sraum.R.id.editextdetail;
import static com.massky.sraum.R.id.macdbtton_id;

/**
 * Created by masskywcy on 2017-05-12.
 */
//用于面板详情界面
public class PaneldetailActivity extends Basecactivity {
    @InjectView(R.id.backrela_id)
    RelativeLayout backrelaId;
    @InjectView(R.id.titlecen_id)
    TextView titlecenId;
    @InjectView(R.id.backimage)
    TextView backimage;
    @InjectView(R.id.backsave)
    RelativeLayout backsave;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.panelmac)
    TextView panelmac;
    @InjectView(R.id.paneltype)
    TextView paneltype;
    @InjectView(R.id.panelname)
    ClearEditText panelname;
    @InjectView(R.id.findpanel)
    ImageButton findpanel;

    private Bundle bundle;
    private String panelnumber = "";
    private String initname = "";
    private DialogUtil dialogUtil;

    @Override
    protected int viewId() {
        return R.layout.panelayout;
    }

    @Override
    protected void onView() {
        InputFilter[] filters = new InputFilter[]{new LimitCharLengthFilter(12)};
        panelname.setFilters(filters);
        titlecenId.setVisibility(View.GONE);
        backrelaId.setOnClickListener(this);
        findpanel.setOnClickListener(this);
        dialogUtil = new DialogUtil(PaneldetailActivity.this);
        bundle = IntentUtil.getIntentBundle(PaneldetailActivity.this);
        initname = bundle.getString("panelname");
        panelnumber = bundle.getString("panelnumber");
        panelmac.setText(bundle.getString("panelmac"));
        paneltype.setText(bundle.getString("paneltype"));
        panelname.setText(initname);
        //切换后将EditText光标置于末尾
        CharSequence charSequence = panelname.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
        panelname.setCursorVisible(false);//光标消失
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backrela_id:
                updatePanelname();
                break;
            case R.id.findpanel:
                clickanimation();
                setFindpanel();
                break;
        }
    }

    private void clickanimation() {
        Animation clickAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_animation_small);
        findpanel.startAnimation(clickAnimation);

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
                findpanel.clearAnimation();
                Animation clickAnimation = AnimationUtils.loadAnimation(PaneldetailActivity.this, R.anim.scale_animation_big);
                findpanel.startAnimation(clickAnimation);
            }
        });
    }

    private void setFindpanel() {
        dialogUtil.loadDialog();
        sraum_find_panel();
    }

    private void sraum_find_panel() {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(PaneldetailActivity.this));
        map.put("boxNumber", TokenUtil.getBoxnumber(PaneldetailActivity.this));
        map.put("panelNumber", panelnumber);
        MyOkHttp.postMapObject(ApiHelper.sraum_findPanel, map,
                new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {//刷新togglen获取新数据
                        sraum_find_panel();
                    }
                }, PaneldetailActivity.this, dialogUtil) {
                    @Override
                    public void onSuccess(User user) {
                        super.onSuccess(user);
                        ToastUtil.showToast(PaneldetailActivity.this, "操作完成，查看对应面板");
                    }

                    @Override
                    public void threeCode() {
                        super.threeCode();
                        ToastUtil.showToast(PaneldetailActivity.this, "面板未找到");
                    }

                    @Override
                    public void fourCode() {
                        super.fourCode();
                        ToastUtil.showToast(PaneldetailActivity.this, "面板未找到");
                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                    }
                });
    }

    private void updatePanelname() {
        String noinitname = panelname.getText().toString();
        if (noinitname.equals(initname)) {
            PaneldetailActivity.this.finish();
        } else {
            dialogUtil.loadDialog();
            sraum_update_panel_name(noinitname);
        }
    }

    private void sraum_update_panel_name(final String noinitname) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(PaneldetailActivity.this));
        map.put("boxNumber", TokenUtil.getBoxnumber(PaneldetailActivity.this));
        map.put("panelNumber", panelnumber);
        map.put("panelName", noinitname);
        MyOkHttp.postMapObject(ApiHelper.sraum_updatePanelName, map,
                new Mycallback(new AddTogglenInterfacer() {//刷新togglen获取新数据
                    @Override
                    public void addTogglenInterfacer() {
                        sraum_update_panel_name(noinitname);
                    }
                }, PaneldetailActivity.this, dialogUtil) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                        PaneldetailActivity.this.finish();
                    }

                    @Override
                    public void onSuccess(User user) {
                        super.onSuccess(user);
                        PaneldetailActivity.this.finish();
                        ToastUtil.showToast(PaneldetailActivity.this, "面板名字更新成功");
                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                    }

                    @Override
                    public void threeCode() {
                        super.threeCode();
                        PaneldetailActivity.this.finish();
                        ToastUtil.showToast(PaneldetailActivity.this, "面板编号不正确");
                    }

                    @Override
                    public void fourCode() {
                        super.fourCode();
                        PaneldetailActivity.this.finish();
                        ToastUtil.showToast(PaneldetailActivity.this, "面板名字已存在");
                    }

                });
    }
}
