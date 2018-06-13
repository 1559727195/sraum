package com.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Util.HorizontalListView;
import com.Util.LogUtil;
import com.adapter.HorizontalListViewAdapter;
import com.adapter.HorizontalListViewAdaptertwo;
import com.base.Basecfragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.massky.sraum.MacdeviceActivity;
import com.massky.sraum.MainfragmentActivity;
import com.massky.sraum.R;

import butterknife.InjectView;

import static com.massky.sraum.R.id.addimage_id;
import static com.massky.sraum.R.id.cenimage_id;
import static com.massky.sraum.R.id.centext_id;

/**
 * Created by masskywcy on 2016-09-18.
 */
/*我的房间fragment*/
public class MyRoomFragment extends Basecfragment {
    @InjectView(R.id.horizon_listview)
    HorizontalListView hListView;
    @InjectView(R.id.horizon_listviewtwo)
    HorizontalListView horizon_listviewtwo;
    @InjectView(R.id.rtext_id)
    TextView rtext_id;
    @InjectView(R.id.rimagetwo_id)
    ImageView rimagetwo_id;
    @InjectView(R.id.addcircle_id)
    ImageView addcircle_id;

    @InjectView(R.id.sideslip_id)
    RelativeLayout sideslip_id;
    @InjectView(R.id.addrelative_id)
    RelativeLayout addrelative_id;
    @InjectView(R.id.addimage_id)
    ImageView addimage_id;
    @InjectView(R.id.cenimage_id)
    ImageView cenimage_id;
    @InjectView(R.id.centext_id)
    TextView centext_id;

    HorizontalListViewAdapter hListViewAdapter;
    HorizontalListViewAdaptertwo hListViewAdaptertwo;
    private String[] titles = {"卧室", "厨房", "卫生间", "客厅", "餐厅",
            "阳台", "门厅", "儿童房", "书房", "车库", "化妆间",
            "娱乐室", "健身区"};
    private int[] icon = {R.drawable.bed, R.drawable.fridge, R.drawable.manandwoman,
            R.drawable.sofa, R.drawable.fork, R.drawable.balcony, R.drawable.tv,
            R.drawable.child, R.drawable.table, R.drawable.car, R.drawable.brush,
            R.drawable.mic, R.drawable.yaling};
    private int[] iconNameone = {R.string.rmone, R.string.rmtwo, R.string.rmthree, R.string.rmfour,
            R.string.rmfive, R.string.rmsix, R.string.rmseven, R.string.rmeight};
    private int[] giconone = {R.drawable.lamp, R.drawable.lamp, R.drawable.wendu, R.drawable.curtain,
            R.drawable.lamp, R.drawable.wendu, R.drawable.lamp, R.drawable.curtain};
    private MainfragmentActivity mainfragmentActivity;
    private static SlidingMenu mySlidingMenu;

    public static MyRoomFragment newInstance(SlidingMenu mySlidingMenu1) {
        MyRoomFragment newFragment = new MyRoomFragment();
        Bundle bundle = new Bundle();
        mySlidingMenu = mySlidingMenu1;
        newFragment.setArguments(bundle);
        return newFragment;
    }

    private void  chageSlideMenu () {
        if ( mySlidingMenu != null) {
            if ( mySlidingMenu.isMenuShowing()) {
                mySlidingMenu.showContent();
            } else {
                mySlidingMenu.showMenu();
            }
        }
    }

    @Override
    protected int viewId() {
        return R.layout.myroom;
    }

    @Override
    protected void onView() {
        cenimage_id.setVisibility(View.GONE);
        centext_id.setVisibility(View.VISIBLE);
        addimage_id.setVisibility(View.GONE);
        centext_id.setText("我的房间");


        mainfragmentActivity = (MainfragmentActivity) getActivity();
        addcircle_id.setOnClickListener(this);
        hListViewAdapter = new HorizontalListViewAdapter(getActivity(), titles, icon);
        hListViewAdaptertwo = new HorizontalListViewAdaptertwo(getActivity(), iconNameone, giconone);
        hListView.setAdapter(hListViewAdapter);
        horizon_listviewtwo.setAdapter(hListViewAdaptertwo);
        horizon_listviewtwo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hListViewAdaptertwo.setSelectIndex(position);
                hListViewAdaptertwo.notifyDataSetChanged();
                LogUtil.i(position + "这是position", "onItemClick: ");
                switch (position) {
                    case 0:
                        break;
                }
            }
        });
        hListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                rtext_id.setText(titles[position]);
                rimagetwo_id.setImageResource(icon[position]);
                hListViewAdapter.setSelectIndex(position);
                hListViewAdapter.notifyDataSetChanged();
            }
        });
        mainfragmentActivity.menu.addIgnoredView(hListView);
        mainfragmentActivity.menu.addIgnoredView(horizon_listviewtwo);
        sideslip_id.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addcircle_id:
                Intent intent = new Intent(getActivity(), MacdeviceActivity.class);
                intent.putExtra("name", "1");
                startActivity(intent);
                break;
            case R.id.sideslip_id:
                chageSlideMenu();
                break;
        }
    }

}
