package com.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.Util.ToastUtil;
import com.Util.TokenUtil;
import com.adapter.MacdeviceAdapter;
import com.base.Basecfragment;
import com.data.Allbox;
import com.data.User;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.massky.sraum.MacdetailActivity;
import com.massky.sraum.MacdeviceActivity;
import com.massky.sraum.MainfragmentActivity;
import com.massky.sraum.R;
import com.xlistview.PullToRefreshLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.InjectView;
import okhttp3.Call;
import static com.fragment.Mainviewpager.getDeviceId;


/**
 * Created by masskywcy on 2016-09-23.
 */
//智能设备界面
public class MacdeviceFragment extends Basecfragment implements AdapterView.OnItemClickListener,
        PullToRefreshLayout.OnRefreshListener {
    @InjectView(R.id.maclistview_id)
    ListView maclistview_id;
    @InjectView(R.id.addcircle_id)
    ImageView addcircle_id;
    @InjectView(R.id.refresh_view)
    PullToRefreshLayout refresh_view;
    @InjectView(R.id.adderela_id)
    RelativeLayout adderela_id;
    @InjectView(R.id.addebtn_id)
    Button addebtn_id;
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
    @InjectView(R.id.macdevice_rel)
    RelativeLayout macdevice_rel;
    private MacdeviceAdapter adapter;
    private List<User.device> deviceList = new ArrayList<>();
    private String boxnumber;
    //进行判断是否进行创建刷新
    private boolean isFirstIn = true;
    private DialogUtil dialogUtil;
    private MainfragmentActivity mainfragmentActivity;
    private List<Allbox> allboxList = new ArrayList<Allbox>();
    private static SlidingMenu mySlidingMenu;
    private String accountType;

    public static MacdeviceFragment newInstance(SlidingMenu mySlidingMenu1) {
        MacdeviceFragment newFragment = new MacdeviceFragment();
        Bundle bundle = new Bundle();
        mySlidingMenu = mySlidingMenu1;
        newFragment.setArguments(bundle);
        return newFragment;

    }

    private void chageSlideMenu() {

        if (mySlidingMenu != null) {
            if (mySlidingMenu.isMenuShowing()) {
                mySlidingMenu.showContent();
            } else {
                mySlidingMenu.showMenu();
            }
        }
    }

    @Override
    protected int viewId() {
        return R.layout.macdevice;
    }

    @Override
    protected void onView() {

        cenimage_id.setVisibility(View.GONE);
        centext_id.setVisibility(View.VISIBLE);
        addimage_id.setVisibility(View.GONE);
        centext_id.setText("智能设备");

        registerMessageReceiver();
        mainfragmentActivity = (MainfragmentActivity) getActivity();
        dialogUtil = new DialogUtil(getActivity());
        boxnumber = (String) SharedPreferencesUtil.getData(getActivity(), "boxnumber", "");
        maclistview_id.setDividerHeight(0);
        maclistview_id.setOnItemClickListener(this);
        addcircle_id.setOnClickListener(this);
        refresh_view.setOnRefreshListener(this);
        addebtn_id.setOnClickListener(this);
        sideslip_id.setOnClickListener(this);

        accountType = (String) SharedPreferencesUtil.getData(getActivity(), "accountType", "");
        //panel_bottom_add_rel
        //异步发送数据
        switch (accountType) {
            case "1":
                macdevice_rel.setVisibility(View.VISIBLE);
                break;//    break;//主机，业主-写死
            case "2":
                macdevice_rel.setVisibility(View.GONE);
                break;//从机，成员
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        refresh_view.autoRefresh();
        LogUtil.i("查看onstart方法");
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sideslip_id:
                chageSlideMenu();
                break;
            case R.id.addcircle_id:
                sraum_setBox_accent();
//                Intent intent = new Intent(getActivity(), MacdeviceActivity.class);
//                intent.putExtra("name", "2");
//                startActivity(intent);
                break;
            case R.id.addebtn_id:
                Intent intentt = new Intent(getActivity(), MacdeviceActivity.class);
                intentt.putExtra("name", "2");
                startActivity(intentt);
                break;
        }
    }

    private void sraum_setBox_accent() {
        //在这里先调
        //设置网关模式-sraum-setBox
        Map map = new HashMap();
        String phoned = getDeviceId(getActivity());
        map.put("token", TokenUtil.getToken(getActivity()));
        String boxnumber = (String) SharedPreferencesUtil.getData(getActivity(), "boxnumber", "");
        map.put("boxNumber", boxnumber);
        map.put("phoneId", phoned);
        map.put("status", "1");//进入设置模式
        dialogUtil.loadDialog();
        MyOkHttp.postMapObject(ApiHelper.sraum_setBox, map, new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {
                        sraum_setBox_accent();
                    }
                }, getActivity(), dialogUtil) {
                    @Override
                    public void onSuccess(User user) {
                        Intent intent = new Intent(getActivity(), MacdeviceActivity.class);
                        intent.putExtra("name", "2");
                        startActivity(intent);
                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                    }

                    @Override
                    public void wrongBoxnumber() {
                        ToastUtil.showToast(getActivity(), "该网关不存在");
                    }
                }
        );
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        adapter.changeState(position);
        Bundle bundle = new Bundle();
        bundle.putString("type", deviceList.get(position).type);
        bundle.putString("name", deviceList.get(position).name);
        bundle.putString("number", deviceList.get(position).number);
        bundle.putString("name1", deviceList.get(position).name1);
        bundle.putString("name2", deviceList.get(position).name2);
        bundle.putString("panelName", deviceList.get(position).panelName);


        bundle.putString("status", (String) deviceList.get(position).status);
        bundle.putString("dimmer", (String) deviceList.get(position).dimmer);
        bundle.putString("mode", (String) deviceList.get(position).mode);
        IntentUtil.startActivity(getActivity(), MacdetailActivity.class, bundle);
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
//        if (TokenUtil.getTokenflag(getActivity())) {
//            if (TokenUtil.getBoxnumber(getActivity()).trim().equals("") || !TokenUtil.getBoxflag(getActivity())) {
//                deviceList.clear();
//                adapter = new MacdeviceAdapter(getActivity(), deviceList);
//                maclistview_id.setAdapter(adapter);
//                if (pullToRefreshLayout != null) {
//                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
//                }
//            } else {
//                upload(pullToRefreshLayout);
//            }
//        } else {
//            if (pullToRefreshLayout != null) {
//                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
//            }
//        }
        if (TokenUtil.getBoxnumber(getActivity()).trim().equals("") || !TokenUtil.getBoxflag(getActivity())) {
            deviceList.clear();
            adapter = new MacdeviceAdapter(getActivity(), deviceList);
            maclistview_id.setAdapter(adapter);
            if (pullToRefreshLayout != null) {
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }
        } else {
            upload(pullToRefreshLayout);
        }
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

    }

    //下拉刷新
    private void upload(final PullToRefreshLayout pullToRefreshLayout) {
        final Map<String, String> mapdevice = new HashMap<>();
        mapdevice.put("token", TokenUtil.getToken(getActivity()));
        mapdevice.put("boxNumber", TokenUtil.getBoxnumber(getActivity()));
        MyOkHttp.postMapString(ApiHelper.sraum_getBoxStatus, mapdevice, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {
                upload(pullToRefreshLayout);
            }
        }, getActivity(),
                dialogUtil) {
            @Override
            public void pullDataError() {
                super.pullDataError();
                if (pullToRefreshLayout != null) {
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                }
            }

            @Override
            public void wrongBoxnumber() {
                super.wrongBoxnumber();
                getBox_mac_dev(pullToRefreshLayout);
            }

            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                if (pullToRefreshLayout == null) {
                    downloadMac(mapdevice, null);
                } else {
                    downloadMac(mapdevice, pullToRefreshLayout);
                }
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });

    }

    private void getBox_mac_dev(PullToRefreshLayout pullToRefreshLayout) {
        Map<String, Object> mapbox = new HashMap<String, Object>();
        mapbox.put("token", TokenUtil.getToken(getActivity()));
        if (pullToRefreshLayout == null) {
            getBox(mapbox, null);
        } else {
            getBox(mapbox, pullToRefreshLayout);
        }
    }

    private void getBox(final Map<String, Object> mapbox, final PullToRefreshLayout pullToRefreshLayout) {
        MyOkHttp.postMapObject(ApiHelper.sraum_getAllBox, mapbox, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {
                getBox_mac_dev(pullToRefreshLayout);
            }
        }, getActivity(), dialogUtil) {
            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                int boxtop = 1;
                allboxList.clear();
                for (User.box bo : user.boxList) {
                    Allbox allbox = new Allbox(bo.type, bo.number, bo.name, bo.status, bo.sign, boxtop);
                    allboxList.add(allbox);
                    boxtop++;
                }
                if (allboxList.size() == 0) {
                    SharedPreferencesUtil.saveData(getActivity(), "boxnumber", "");
                } else {
                    for (Allbox ab : allboxList) {
                        if (ab.sign.equals("1")) {
                            SharedPreferencesUtil.saveData(getActivity(), "boxnumber", ab.number);
                        }
                    }
                }
                mac_device_pull(pullToRefreshLayout);
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }

    private void mac_device_pull(PullToRefreshLayout pullToRefreshLayout) {
        final Map<String, String> mapdevice = new HashMap<>();
        boxnumber = (String) SharedPreferencesUtil.getData(getActivity(), "boxnumber", "");
        mapdevice.put("token", TokenUtil.getToken(getActivity()));
        mapdevice.put("boxNumber", boxnumber);
        if (boxnumber.equals("")) {
            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        } else {
            downloadMac(mapdevice, pullToRefreshLayout);
        }
    }

    private void downloadMac(final Map<String, String> mapdevice, final PullToRefreshLayout pullToRefreshLayout) {
        MyOkHttp.postMapString(ApiHelper.sraum_getAllDevice, mapdevice, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {
                mac_device_pull(pullToRefreshLayout);
            }
        }, getActivity(), dialogUtil) {
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                if (pullToRefreshLayout != null) {
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                }
            }

            @Override
            public void pullDataError() {
                super.pullDataError();
                adderela_id.setVisibility(View.VISIBLE);
                if (pullToRefreshLayout != null) {
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                }
            }


            @Override
            public void wrongBoxnumber() {
                super.wrongBoxnumber();
                if (pullToRefreshLayout != null) {
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                }
            }

            @Override
            public void emptyResult() {
                super.emptyResult();
                adderela_id.setVisibility(View.VISIBLE);
                if (pullToRefreshLayout != null) {
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                }
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
                if (pullToRefreshLayout != null) {
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                }
            }

            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                if (pullToRefreshLayout != null) {
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                }
                deviceList.clear();
                deviceList.addAll(user.deviceList);
                if (deviceList.size() != 0) {
                    adderela_id.setVisibility(View.GONE);
                }
                centext_id.setText("智能设备(" + deviceList.size() + ")");
//                backToMainTitleListener.backToMainTitleLength(deviceList.size());
                adapter = new MacdeviceAdapter(getActivity(), deviceList);
                maclistview_id.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(mMessageReceiver);
    }

    public MessageReceiver mMessageReceiver;
    public static String ACTION_INTENT_RECEIVER = "com.massky.sraum.macdeviceceiver";

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
                if (messflag == 1 || messflag == 3 || messflag == 4 || messflag == 5) {
//                    if (TokenUtil.getTokenflag(getActivity())) {
//                        if (TokenUtil.getBoxnumber(getActivity()).trim().equals("")
//                                || !TokenUtil.getBoxflag(getActivity())) {
//                            deviceList.clear();
//                            backToMainTitleListener.backToMainTitleLength(deviceList.size());
//                            adapter = new MacdeviceAdapter(getActivity(), deviceList);
//                            maclistview_id.setAdapter(adapter);
//                        } else {
//                            upload(null);
//                        }
//                    }

                    if (TokenUtil.getBoxnumber(getActivity()).trim().equals("")
                            || !TokenUtil.getBoxflag(getActivity())) {
                        deviceList.clear();
                        backToMainTitleListener.backToMainTitleLength(deviceList.size());
                        adapter = new MacdeviceAdapter(getActivity(), deviceList);
                        maclistview_id.setAdapter(adapter);
                    } else {
                        upload(null);
                    }
                }
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            refresh_view.autoRefresh();
        }
        LogUtil.eLength("查看显示方法", hidden + "");
    }


    public void setBackToMainTitleListener(BackToMainTitleListener backToMainTitleListener) {
        this.backToMainTitleListener = backToMainTitleListener;
    }

    private BackToMainTitleListener backToMainTitleListener;

    public interface BackToMainTitleListener {
        void backToMainTitleLength(int length);
    }

    @Override
    public void onResume() {
        super.onResume();
        android.util.Log.e("peng", "MacdeviceFragment->onResume:name:");
    }
}
