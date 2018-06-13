package com.zhongtianli.overalls.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.zhongtianli.overalls.R;
import com.zhongtianli.overalls.activity.OverallsBaseActivity;
import com.zhongtianli.overalls.adapter.StoreAdapter;
import com.zhongtianli.overalls.bean.GetClothesBean;
import com.zhongtianli.overalls.interfaces.StoreRefreshListener;
import com.zhongtianli.overalls.maxwin.view.XListView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhu on 2016/12/26.
 */

public class StoreFragment extends Fragment implements XListView.IXListViewListener,View.OnClickListener
,OverallsBaseActivity.Overalls_GetIn_Listener {

    private XListView listView;
    private   static  Context context1;
    private List<String> list;
    private GetClothesBean getClothesBean = new GetClothesBean();
    private StoreAdapter storeAdapter;
    private String TAG = "zhu";
    private long mExitTime;
    private  static StoreRefreshListener storeRefreshListener1;

    public static StoreFragment newInstance(Context context, StoreRefreshListener storeRefreshListener) {
        context1 = context;
        storeRefreshListener1 = storeRefreshListener;
        StoreFragment f = new StoreFragment();
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.store_frag, container, false);
        initData ();
        initView (view);
        initEvent ();
        return view;
    }

    private void initData() {
        getClothesBean =  new GetClothesBean();
    }

    private void initEvent() {
         listView.setXListViewListener(this);
    }

    private void initView(View view) {
        listView = (XListView) view.findViewById(R.id.xListView_store);
        listView.setFootViewHide();
        listView.setPullLoadEnable(false);
        Log.e(TAG,"storeAdapter");
        storeAdapter = new StoreAdapter( context1 ,getClothesBean);
        listView.setAdapter(storeAdapter);
    }

    @Override
    public void onRefresh() {
        onLoad();
        if ((System.currentTimeMillis() - mExitTime) > 2000){//两次刷新间隔为 2秒,防止不停刷新
            mExitTime = System.currentTimeMillis();
            Log.e(TAG,"我被刷新了");//MVP经过主界面去拉取数据
            storeRefreshListener1.storeRefresh();
          } else {
        }
    }

    @Override
    public void onLoadMore() {

    }

    private void onLoad() {
        listView.stopRefresh();
        listView.setRefreshTime("刚刚");
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void overalls_GetIn( final GetClothesBean getClothesBean) {//一.	获取库存信息（Clothes_GetIn）
        Log.e(TAG,"overalls_GetIn");
        if (storeAdapter != null) {
            storeAdapter.setList(getClothesBean);
            storeAdapter.notifyDataSetChanged();
        } else {//网速好的话，一下子就会把数据加载出来，所以UI,initView方法还没有执行时，数据就来了，导致storeAdapter == null,没被初始化
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {//延时50毫秒，让UI代码先执行，
                    storeAdapter.setList(getClothesBean);
                    storeAdapter.notifyDataSetChanged();
                }
            },50);
        }
    }
}
