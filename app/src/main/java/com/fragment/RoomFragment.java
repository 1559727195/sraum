package com.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Util.ApiHelper;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.SharedPreferencesUtil;
import com.Util.TokenUtil;
import com.adapter.SceneAdapter;
import com.base.Basecfragment;
import com.data.Allbox;
import com.data.User;
import com.massky.sraum.MacdeviceActivity;
import com.massky.sraum.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;

/**
 * Created by masskywcy on 2016-09-05.
 */
/*用于第二个添加房间fragment*/
public class RoomFragment extends Basecfragment {
    @InjectView(R.id.addroombtn_id)
    Button addroombtn_id;
    @InjectView(R.id.gritviewroom_id)
    GridView gritviewroom_id;
    @InjectView(R.id.addroomrela_id)
    RelativeLayout addroomrela_id;
    @InjectView(R.id.macstatus)
    TextView macstatus;
    private SceneAdapter adapter;
    private int[] icon = {R.drawable.bedroom, R.drawable.fridgeroom, R.drawable.manandwomanroom,
            R.drawable.sofaroom, R.drawable.forkroom, R.drawable.balconyroom, R.drawable.mobiletvroom,
            R.drawable.childroom, R.drawable.tableroom, R.drawable.carroom, R.drawable.brushroom,
            R.drawable.microom, R.drawable.yalingroom};
    private int[] iconName = {R.string.roomone, R.string.roomtwo, R.string.roomthree, R.string.roomfour,
            R.string.roomfive, R.string.roomsix, R.string.roomseven, R.string.roomeight, R.string.roomnine,
            R.string.roomten, R.string.roomeleven, R.string.roomtwelve, R.string.roomthirteen};
    private List<Allbox> allboxList = new ArrayList<Allbox>();

    @Override
    protected int viewId() {
        return R.layout.roomfragment;
    }

    @Override
    protected void onView() {
        registerMessageReceiver();
        adapter = new SceneAdapter(getActivity(), icon, iconName, icon);
        gritviewroom_id.setAdapter(adapter);
        addroombtn_id.setOnClickListener(this);
//        boxStatus(TokenUtil.getBoxflag(getActivity()));
    }

    @Override
    public void initData() {
//        boxStatus(TokenUtil.getBoxflag(getActivity()));
        SharedPreferencesUtil.saveData(getActivity(), "pagetag", "2");
    }

    private void boxStatus(boolean boxflag) {
        if (boxflag) {
            addroomrela_id.setVisibility(View.VISIBLE);
            gritviewroom_id.setVisibility(View.GONE);
            macstatus.setVisibility(View.GONE);
        } else {
            addroomrela_id.setVisibility(View.GONE);
            gritviewroom_id.setVisibility(View.GONE);
            macstatus.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addroombtn_id:
                Intent intent = new Intent(getActivity(), MacdeviceActivity.class);
                intent.putExtra("name", "1");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(mMessageReceiver);
    }

    public MessageReceiver mMessageReceiver;
    public static String ACTION_INTENT_RECEIVER = "com.massky.sraum.romceiver";

    /**
     * 动态注册广播
     */
    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_INTENT_RECEIVER);
        getActivity().registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals(ACTION_INTENT_RECEIVER)) {
                int messflag = intent.getIntExtra("notifactionId", 0);
                if (messflag == 3 || messflag == 4 || messflag == 5) {
//                    boxStatus(TokenUtil.getBoxflag(getActivity()));
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        android.util.Log.e("peng", "RoomFragment->onResume:name:");
    }

    @Override
    public void onStart() {
        super.onStart();
        android.util.Log.e("fei", "RoomFragment->onStart():name:");
    }
}
