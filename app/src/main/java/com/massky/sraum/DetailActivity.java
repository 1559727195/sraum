package com.massky.sraum;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spannable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.ClearDetailEditText;
import com.Util.ClearEditText;
import com.Util.DialogUtil;
import com.Util.IntentUtil;
import com.Util.LimitCharLengthFilter;
import com.Util.LogUtil;
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
 * Created by masskywcy on 2016-09-14.
 */
//网关详情页面
public class DetailActivity extends Basecactivity {
    @InjectView(R.id.backrela_id)
    RelativeLayout backrela_id;
    @InjectView(R.id.titlecen_id)
    TextView titlecen_id;
    @InjectView(R.id.gatewthree)
    TextView gatewthree;
    @InjectView(R.id.gatewfive)
    TextView gatewfive;
    @InjectView(R.id.gatewseven)
    TextView gatewseven;
    @InjectView(R.id.gatewnine)
    TextView gatewnine;
    @InjectView(R.id.editextdetail)
    ClearDetailEditText editextdetail;

    private String boxname, boxnumber;
    private DialogUtil dialogUtil;
    private Bundle bundle;
    private String boxStatus;

    @Override
    protected int viewId() {
        return R.layout.gatedetail;
    }


    @Override
    protected void onView() {

    }

    //网关详情
    private void getBoxInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(DetailActivity.this));
        map.put("boxNumber", boxnumber);
        dialogUtil.loadDialog();
        LogUtil.i("看看数据", "数据");
        MyOkHttp.postMapObject(ApiHelper.sraum_getBoxInfo, map, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {//获取togglen成功后重新刷新数据
                getBoxInfo();
            }
        }, DetailActivity.this, dialogUtil) {
            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                User.boxinfor boxinfor = user.boxInfo;
//                status：状态，1-在线，0-离线*/
                gatewthree.setText(boxinfor.mac);
                gatewfive.setText(boxinfor.type);
                gatewseven.setText(boxinfor.firmware);
                gatewnine.setText(boxinfor.hardware);
                editextdetail.setText(boxinfor.name.trim());
                //切换后将EditText光标置于末尾
                CharSequence charSequence = editextdetail.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
                if (boxStatus.equals("0")) {//网关离线
                    editextdetail.setEnabled(false);
//            editextdetail.drawdelete(false);//让差号不可见
                    editextdetail.setClearDetailShow(false);
                } else {//网关在线
                    editextdetail.setEnabled(true);
//            editextdetail.drawdelete(false);//让差号不可见
                    if (editextdetail.getText().toString().equals("")) {
                        editextdetail.setClearDetailShow(false);
                    } else {
                        editextdetail.setClearDetailShow(true);
                    }
                }
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backrela_id:
                if (boxname.equals(editextdetail.getText().toString())) {
                    DetailActivity.this.finish();
                } else {
                    UpdateBoxInfo();
                }
                break;
        }
    }

    private void UpdateBoxInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(DetailActivity.this));
        map.put("boxNumber", boxnumber);
        map.put("boxName", editextdetail.getText().toString());
        dialogUtil.loadDialog();
        MyOkHttp.postMapObject(ApiHelper.sraum_UpdateBoxInfo, map, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {//获取togglen成功后重新刷新数据
                UpdateBoxInfo();
            }
        }, DetailActivity.this, dialogUtil) {
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                DetailActivity.this.finish();
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
                DetailActivity.this.finish();
            }

            @Override
            public void wrongBoxnumber() {
                super.wrongBoxnumber();
                DetailActivity.this.finish();
            }

            @Override
            public void threeCode() {
                super.threeCode();
                DetailActivity.this.finish();
            }

            @Override
            public void fourCode() {
                super.fourCode();
                DetailActivity.this.finish();
            }

            @Override
            public void fiveCode() {
                super.fiveCode();
                DetailActivity.this.finish();
            }

            @Override
            public void sixCode() {
                super.sixCode();
                DetailActivity.this.finish();
            }

            @Override
            public void sevenCode() {
                super.sevenCode();
                DetailActivity.this.finish();
            }

            @Override
            public void emptyResult() {
                super.emptyResult();
                DetailActivity.this.finish();
            }

            @Override
            public void pullDataError() {
                super.pullDataError();
                DetailActivity.this.finish();
            }

            @Override
            public void defaultCode() {
                super.defaultCode();
                DetailActivity.this.finish();
            }

            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                DetailActivity.this.finish();
                ToastUtil.showDelToast(DetailActivity.this, "信息更新成功");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InputFilter[] filters = new InputFilter[]{new LimitCharLengthFilter(12)};
        editextdetail.setFilters(filters);
        dialogUtil = new DialogUtil(DetailActivity.this);
        bundle = IntentUtil.getIntentBundle(DetailActivity.this);
        boxname = bundle.getString("boxName", "");
        boxnumber = bundle.getString("boxnumber", "");
//        bundle.putString("boxStatus", allboxList.get(posit).status);//status-网关状态
        boxStatus = bundle.getString("boxStatus", "");
        backrela_id.setOnClickListener(this);
        titlecen_id.setVisibility(View.GONE);
        if (boxStatus.equals("0")) {//网关离线
            editextdetail.setEnabled(false);
//            editextdetail.drawdelete(false);//让差号不可见
            editextdetail.setClearDetailShow(false);
        } else {//网关在线
            editextdetail.setEnabled(true);
//            editextdetail.drawdelete(false);//让差号不可见
              if (editextdetail.getText().toString().equals("")) {
                  editextdetail.setClearDetailShow(false);
              } else {
                  editextdetail.setClearDetailShow(true);
              }
        }
        editextdetail.setCursorVisible(false);
        getBoxInfo();
    }
}
