package com.zhongtianli.overalls.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.zhongtianli.overalls.R;
import com.zhongtianli.overalls.adapter.TakeOutAdapter;
import com.zhongtianli.overalls.maxwin.view.XListView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhu on 2016/12/26.
 */

public class TakeOutFragment extends Fragment implements XListView.IXListViewListener,View.OnClickListener{

    private XListView listView;
    private   static Context context1;
    private List<String> list;
    private Button start_btn_take1;
    private Button stop_btn_take1;

    public static TakeOutFragment newInstance(Context context) {
        context1 = context;
        TakeOutFragment f = new TakeOutFragment();
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.take_out_frag, container, false);
        initData ();
        initView (view);
        initEvent ();
        return view;
    }

    private void initData() {
        list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list.add("取出测试数据:" + i);
        }
    }

    private void initEvent() {
        listView.setXListViewListener(this);
        start_btn_take1.setOnClickListener(this);//开始关联取衣
    }

    private void initView(View view) {
        //开始关联工作服
        start_btn_take1 = (Button) view.findViewById(R.id.start_btn_take);
        listView = (XListView) view.findViewById(R.id.xListView_take_out);
        listView.setFootViewHide();
        listView.setPullLoadEnable(false);
        listView.setAdapter(new TakeOutAdapter( context1 ,list));
    }

    @Override
    public void onRefresh() {
        onLoad();
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
        switch (v.getId()) {
            case R.id.start_btn_take://开始关联取出
                CustomDialogFragment newFragment = CustomDialogFragment.newInstance("title", "message", null);
                newFragment.show(getActivity().getSupportFragmentManager(), "dialog");
                break;
        }
    }
}
