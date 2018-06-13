package com.massky.sraum;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.DialogUtil;
import com.Util.IntentUtil;
import com.Util.LogUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.SharedPreferencesUtil;
import com.Util.TokenUtil;
import com.adapter.MyfamilyAdapter;
import com.base.Basecactivity;
import com.data.User;
import com.xlistview.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;
import okhttp3.Call;

import static com.massky.sraum.R.id.myscneaddcircle_id;

/**
 * Created by masskywcy on 2016-09-21.
 */
public class MyfamilyActivity extends Basecactivity implements PullToRefreshLayout.OnRefreshListener {
    @InjectView(R.id.backrela_id)
    RelativeLayout backrela_id;
    @InjectView(R.id.titlecen_id)
    TextView titlecen_id;
    @InjectView(R.id.myfamlistview)
    ListView myfamlistview;
    @InjectView(R.id.refresh_view)
    PullToRefreshLayout refresh_view;
    @InjectView(R.id.addfamcircle)
    ImageView addfamcircle;

    private MyfamilyAdapter adapter;
    private DialogUtil dialogUtil;
    //进行判断是否进行创建刷新
    private boolean isFirstIn = true;
    private List<User.familylist> list = new ArrayList<>();
    private  PullToRefreshLayout pullToRefreshLayout;
    private String accountType;

    @Override
    protected int viewId() {
        return R.layout.myfamily;
    }

    @Override
    protected void onView() {
        dialogUtil = new DialogUtil(MyfamilyActivity.this);
        titlecen_id.setText(R.string.myfamily);
        backrela_id.setOnClickListener(this);
        refresh_view.setOnRefreshListener(this);
        addfamcircle.setOnClickListener(this);
        LogUtil.i("第二次查看");
        accountType = (String) SharedPreferencesUtil.getData(MyfamilyActivity.this, "accountType", "");
        //addfamcircle
        switch (accountType) {
            case "1":
                addfamcircle.setVisibility(View.VISIBLE);
                break;//    break;//主机，业主-写死
            case "2":
                addfamcircle.setVisibility(View.GONE);
                break;//从机，成员
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        refresh_view.autoRefresh();
    }

    //获取家庭成员
    private void getFamily(final PullToRefreshLayout pullToRefreshLayout) {
        sraum_get_famliy(pullToRefreshLayout);
    }

    private void sraum_get_famliy(final PullToRefreshLayout pullToRefreshLayout) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(MyfamilyActivity.this));
        MyOkHttp.postMapObject(ApiHelper.sraum_getFamily, map, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {//获取gogglen刷新数据
                sraum_get_famliy(pullToRefreshLayout);
            }
        }, MyfamilyActivity.this, dialogUtil) {
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
            }

            @Override
            public void pullDataError() {
                super.pullDataError();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
            }

            @Override
            public void wrongBoxnumber() {
                super.wrongBoxnumber();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void emptyResult() {
                super.emptyResult();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                list.clear();
                list.addAll(user.familyList);
                if (isFirstIn) {
                    adapter = new MyfamilyAdapter(MyfamilyActivity.this, list, TokenUtil.getToken(MyfamilyActivity.this), dialogUtil
                    ,accountType);
                    myfamlistview.setAdapter(adapter);
                    isFirstIn = false;
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backrela_id:
                MyfamilyActivity.this.finish();
                break;
            case R.id.addfamcircle:
                IntentUtil.startActivity(MyfamilyActivity.this, AddfamilyActivity.class);
                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        getFamily(pullToRefreshLayout);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

    }
}
