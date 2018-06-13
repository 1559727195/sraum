package com.massky.sraum;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spannable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
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
import com.Util.ToastUtil;
import com.Util.TokenUtil;
import com.adapter.MysceneactivityAdapter;
import com.base.Basecactivity;
import com.data.User;

import java.util.HashMap;
import java.util.Map;

import butterknife.InjectView;

import static com.massky.sraum.R.drawable.scename;

/**
 * Created by masskywcy on 2016-11-30.
 */
//用于展示添加场景界面
public class MysceneActivity extends Basecactivity implements AdapterView.OnItemClickListener {
    @InjectView(R.id.addscenegridview_id)
    GridView addscenegridview_id;
    @InjectView(R.id.backrela_id)
    RelativeLayout backrela_id;
    @InjectView(R.id.titlecen_id)
    TextView titlecen_id;
    @InjectView(R.id.scenename)
    ClearEditText scenename;
    @InjectView(R.id.nextbtn)
    Button nextbtn;
    private int[] icon = {R.drawable.add_scene_homein, R.drawable.add_scene_homein,
            R.drawable.add_scene_cup, R.drawable.add_scene_meeting,
            R.drawable.add_scene_homein, R.drawable.add_scene_homein, R.drawable.add_scene_homein,
            R.drawable.add_scene_nightlamp, R.drawable.add_scene_book, R.drawable.add_scene_noddle,
            R.drawable.add_scene_getup, R.drawable.add_scene_lampoff, R.drawable.add_scene_sleep,
            R.drawable.add_scene_homeout, R.drawable.add_scene_moive, R.drawable.add_scene_cycle,
            R.drawable.add_scene_lampon, R.drawable.defaultpic};
    private int[] icontwo = {R.drawable.add_scene_homein, R.drawable.add_scene_homein, R.drawable.add_scene_cup_checked,
            R.drawable.add_scene_meeting_checked, R.drawable.add_scene_homein, R.drawable.add_scene_homein,
            R.drawable.add_scene_homein_checked, R.drawable.add_scene_nightlamp_checked, R.drawable.add_scene_book_checked,
            R.drawable.add_scene_noddle_checked, R.drawable.add_scene_getup_checked, R.drawable.add_scene_lampoff_checked,
            R.drawable.add_scene_sleep_checked, R.drawable.add_scene_homeout_checked, R.drawable.add_scene_moive_checked,
            R.drawable.add_scene_cycle_checked, R.drawable.add_scene_lampon_checked, R.drawable.defaultpicheck};
    private String[] iconName = {"", "", "休息", "会客", "", "", "回家", "夜起", "学习",
            "用餐", "晨起", "全关", "睡眠", "离家", "影音", "运动", "全开", "自定义"};
    private MysceneactivityAdapter adapter;
    private Bundle bundle;
    private String sceneType = "6";
    private DialogUtil dialogUtil;
    private String scnename;

    @Override
    protected int viewId() {
        return R.layout.addmyscene;
    }

    @Override
    protected void onView() {
        dialogUtil = new DialogUtil(this);
        InputFilter[] filters = new InputFilter[]{new LimitCharLengthFilter(12)};
        scenename.setFilters(filters);
        bundle = new Bundle();
        adapter = new MysceneactivityAdapter(MysceneActivity.this, icon, icontwo);
        addscenegridview_id.setAdapter(adapter);
        adapter.setDefSelect(2);
        addscenegridview_id.setOnItemClickListener(this);
        backrela_id.setOnClickListener(this);
        nextbtn.setOnClickListener(this);
        titlecen_id.setText("添加场景");
        setEdit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backrela_id:
                MysceneActivity.this.finish();
                break;
            case R.id.nextbtn:
                scnename = scenename.getText().toString().trim();
                if (scnename.equals("")) {
                    ToastUtil.showDelToast(MysceneActivity.this, "场景输入名不能为空");
                } else {
                    verify_sceneName_isExist();
                }
                break;
        }
    }

    /**
     * 校验场景名称是否存在 , verify_sceneName_isExist ();//校验场景名称是否存在
     */
    private void verify_sceneName_isExist() {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(this));
        map.put("boxNumber", TokenUtil.getBoxnumber(this));
        map.put("sceneName", scnename);
        dialogUtil.loadDialog();
        MyOkHttp.postMapObject(ApiHelper.sraum_verifySceneName, map, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {
                verify_sceneName_isExist();
            }
        }, this, dialogUtil) {
            @Override
            public void threeCode() {
                super.threeCode();
                ToastUtil.showToast(MysceneActivity.this, "场景名称已存在");
            }

            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                bundle.putString("scnename", scnename);
                bundle.putString("sceneType", sceneType);
                IntentUtil.startActivity(MysceneActivity.this, AddsignsceneActivity.class, bundle);
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        adapter.setDefSelect(position);
        scenename.setText(iconName[position]);
        setEdit();
        switch (position) {
            case 2:
                sceneType = "6";
                break;
            case 3:
                sceneType = "9";
                break;
            case 6:
                sceneType = "1";
                break;
            case 7:
                sceneType = "4";
                break;
            case 8:
                sceneType = "7";
                break;
            case 9:
                sceneType = "11";
                break;
            case 10:
                sceneType = "5";
                break;
            case 11:
                sceneType = "13";
                break;
            case 12:
                sceneType = "3";
                break;
            case 13:
                sceneType = "2";
                break;
            case 14:
                sceneType = "8";
                break;
            case 15:
                sceneType = "10";
                break;
            case 16:
                sceneType = "12";
                break;
            case 17:
                sceneType = "14";
                break;
            default:
                sceneType = "14";
                break;
        }
        LogUtil.i("这是position", position + "onItemClick: ");
    }

    private void setEdit() {
        //切换后将EditText光标置于末尾
        CharSequence charSequence = scenename.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
    }
}
