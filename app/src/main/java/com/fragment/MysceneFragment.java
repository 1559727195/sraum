package com.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.DialogUtil;
import com.Util.IntentUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;;
import com.Util.SharedPreferencesUtil;
import com.Util.ToastUtil;
import com.Util.TokenUtil;
import com.adapter.MyscefargmentAdapter;
import com.andview.refreshview.XRefreshView;
import com.base.Basecfragment;
import com.data.Allbox;
import com.data.User;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.massky.sraum.AddsignsceneActivity;
import com.massky.sraum.AssociatedpanelActivity;
import com.massky.sraum.MainfragmentActivity;
import com.massky.sraum.MysceneActivity;
import com.massky.sraum.R;
import com.massky.sraum.SexActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;

import static android.R.attr.accountType;
import static com.massky.sraum.R.id.addimage_id;
import static com.massky.sraum.R.id.cenimage_id;
import static com.massky.sraum.R.id.centext_id;
import static com.massky.sraum.R.id.panel_bottom_add_rel;

/**
 * Created by masskywcy on 2016-09-14.
 */
//用于我的场景
public class MysceneFragment extends Basecfragment implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {
    @InjectView(R.id.gritview_id)
    GridView gritview_id;
    @InjectView(R.id.refresh_view)
    XRefreshView refresh_view;
    @InjectView(R.id.myscneaddcircle_id)
    ImageView myscneaddcircle_id;

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

    RelativeLayout mrelativeone, mrelativetwo, mrelativethree, mrelativefour;
    private MyscefargmentAdapter adapter;
    private DialogUtil dialogUtil;
    private int[] icon = {R.drawable.gohome, R.drawable.sleep, R.drawable.getup_night,
            R.drawable.leavehome, R.drawable.rest, R.drawable.study, R.drawable.movie,
            R.drawable.meeting, R.drawable.dinner, R.drawable.sport, R.drawable.getup_morning,
            R.drawable.open_all, R.drawable.close_all};
    private int[] icontwo = {R.drawable.gohome2, R.drawable.sleep2, R.drawable.getup_night2,
            R.drawable.leavehome2, R.drawable.rest2, R.drawable.study2, R.drawable.movie2,
            R.drawable.meeting2, R.drawable.dinner2, R.drawable.sport2, R.drawable.getup_morning2,
            R.drawable.open_all2, R.drawable.close_all2};
    private int[] iconName = {R.string.sceneone, R.string.scenetwo, R.string.scenethree, R.string.scenefour,
            R.string.scenefive, R.string.scenesix, R.string.sceneseven, R.string.sceneight, R.string.scenenine,
            R.string.sceneten, R.string.sceneleven, R.string.scenetwle, R.string.scenethirteen};
    private MainfragmentActivity mainfragmentActivity;
    private String scename, sceType, status = "", gatewayid = "",
            panelType = "", panelNumber, buttonNumber;
    private List<User.scenelist> scenelist = new ArrayList<>();
    private List<User.devicesce> deviceListsce = new ArrayList<>();
    private List<Integer> listint = new ArrayList<>();
    private List<Integer> listintwo = new ArrayList<>();
    private List<String> listtype = new ArrayList();
    private TextView gitemtext;
    private ImageView gitemimage;
    private List<Allbox> allboxList = new ArrayList<Allbox>();
    private static SlidingMenu mySlidingMenu;
    private String accountType;
    private DialogUtil dialogUtil1;

    public static MysceneFragment newInstance(SlidingMenu mySlidingMenu1) {
        MysceneFragment newFragment = new MysceneFragment();
        Bundle bundle = new Bundle();
        mySlidingMenu = mySlidingMenu1;
        newFragment.setArguments(bundle);
        return newFragment;
    }
    @Override
    protected int viewId() {
        return R.layout.myscene_slide;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
//            if (TokenUtil.getTokenflag(getActivity())) {
//                if (!TokenUtil.getBoxnumber(getActivity()).equals("")) {
//                    getData(1);
//                } else {
//                    scenelist.clear();
//                    listint.clear();
//                    listintwo.clear();
//                    adapter = new MyscefargmentAdapter(getActivity(), scenelist, listint, listintwo, false);
//                    gritview_id.setAdapter(adapter);
//                }
//            }
            if (!TokenUtil.getBoxnumber(getActivity()).equals("")) {
                getData(1);
            } else {
                scenelist.clear();
                listint.clear();
                listintwo.clear();
                adapter = new MyscefargmentAdapter(getActivity(), scenelist, listint, listintwo, false);
                gritview_id.setAdapter(adapter);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        if (TokenUtil.getTokenflag(getActivity())) {
//            if (!TokenUtil.getBoxnumber(getActivity()).equals("")) {
//                getData(1);
//            } else {
//                scenelist.clear();
//                listint.clear();
//                listintwo.clear();
//                adapter = new MyscefargmentAdapter(getActivity(), scenelist, listint, listintwo, false);
//                gritview_id.setAdapter(adapter);
//            }
//        }
        if (!TokenUtil.getBoxnumber(getActivity()).equals("")) {
            getData(1);
        } else {
            scenelist.clear();
            listint.clear();
            listintwo.clear();
            adapter = new MyscefargmentAdapter(getActivity(), scenelist, listint, listintwo, false);
            gritview_id.setAdapter(adapter);
        }
    }

    @Override
    protected void onView() {
        cenimage_id.setVisibility(View.GONE);
        centext_id.setVisibility(View.VISIBLE);
        addimage_id.setVisibility(View.GONE);
        centext_id.setText("我的场景");

        registerMessageReceiver();
        mainfragmentActivity = (MainfragmentActivity) getActivity();
        addDialog();
        gritview_id.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gritview_id.setOnItemClickListener(this);
        gritview_id.setOnItemLongClickListener(this);
        myscneaddcircle_id.setOnClickListener(this);
        refresh_view.setScrollBackDuration(300);
        refresh_view.setPinnedTime(1000);
        refresh_view.setPullLoadEnable(false);
        getData(2);
        refresh_view.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
//                if (TokenUtil.getTokenflag(getActivity())) {
//                    getData(2);
//                } else {
//                    refresh_view.stopRefresh();
//                }
                getData(2);
            }
        });
        sideslip_id.setOnClickListener(this);

        accountType = (String) SharedPreferencesUtil.getData(getActivity(), "accountType", "");
        //panel_bottom_add_rel
        //异步发送数据
        switch (accountType) {
            case "1":
                myscneaddcircle_id.setVisibility(View.VISIBLE);
                break;//    break;//主机，业主-写死
            case "2":
                myscneaddcircle_id.setVisibility(View.GONE);
                break;//从机，成员
        }
    }

    private void getData(int dialogflag) {
        final Map<String, Object> map = new HashMap<>();
        map.put("boxNumber", TokenUtil.getBoxnumber(getActivity()));
        map.put("token", TokenUtil.getToken(getActivity()));
        if (dialogflag != 2) {
            dialogUtil.loadDialog();
        }
        MyOkHttp.postMapObject(ApiHelper.sraum_getBoxStatus, map, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {//刷新场景
                getData(2);
            }
        }, getActivity(),
                dialogUtil) {
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                refresh_view.stopRefresh(false);
            }

            @Override
            public void pullDataError() {
                super.pullDataError();
                refresh_view.stopRefresh();
            }

            @Override
            public void wrongBoxnumber() {
                super.wrongBoxnumber();
                Map<String, Object> mapbox = new HashMap<String, Object>();
                mapbox.put("token", TokenUtil.getToken(getActivity()));
                getBox(mapbox);
            }

            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                downloadScene(map);
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }

    private void getBox(final Map<String, Object> mapbox) {
        MyOkHttp.postMapObject(ApiHelper.sraum_getAllBox, mapbox, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {
                Map<String, Object> mapbox = new HashMap<String, Object>();
                mapbox.put("token", TokenUtil.getToken(getActivity()));
                getBox(mapbox);
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
                            SharedPreferencesUtil.saveData(getActivity(), "boxstatus", ab.status);
                            SharedPreferencesUtil.saveData(getActivity(), "boxnumber", ab.number);
                        }
                    }
                }
                final Map<String, Object> mapdevice = new HashMap<>();
                String boxNumber = (String) SharedPreferencesUtil.getData(getActivity(), "boxnumber", "");
                mapdevice.put("token", TokenUtil.getToken(getActivity()));
                mapdevice.put("boxNumber", boxNumber);
                if (boxNumber.equals("")) {
                    refresh_view.stopRefresh();
                } else {
                    downloadScene(mapdevice);
                }
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }

    private void downloadScene(final Map<String, Object> map) {
        MyOkHttp.postMapObject(ApiHelper.sraum_getAllScene, map, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {
                final Map<String, Object> mapdevice = new HashMap<>();
                String boxNumber = (String) SharedPreferencesUtil.getData(getActivity(), "boxnumber", "");
                mapdevice.put("token", TokenUtil.getToken(getActivity()));
                mapdevice.put("boxNumber", boxNumber);
                downloadScene(mapdevice);
            }
        }, getActivity(), dialogUtil) {
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                refresh_view.stopRefresh();
            }

            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                refresh_view.stopRefresh();
                scenelist = user.sceneList;
                for (User.scenelist ud : user.sceneList) {
                    listtype.add(ud.sceneStatus);
                }
                for (User.scenelist us : scenelist) {
                    setPicture(us.type);
                }
                adapter = new MyscefargmentAdapter(getActivity(), scenelist, listint, listintwo, false);
                gritview_id.setAdapter(adapter);
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }

    private void setPicture(String type) {
        switch (type) {
            case "1":
                listint.add(R.drawable.add_scene_homein);
                listintwo.add(R.drawable.gohome2);
                break;
            case "2":
                listint.add(R.drawable.add_scene_homeout);
                listintwo.add(R.drawable.leavehome2);
                break;
            case "3":
                listint.add(R.drawable.add_scene_sleep);
                listintwo.add(R.drawable.sleep2);
                break;
            case "4":
                listint.add(R.drawable.add_scene_nightlamp);
                listintwo.add(R.drawable.getup_night2);
                break;
            case "5":
                listint.add(R.drawable.add_scene_getup);
                listintwo.add(R.drawable.getup_morning2);
                break;
            case "6":
                listint.add(R.drawable.add_scene_cup);
                listintwo.add(R.drawable.rest2);
                break;
            case "7":
                listint.add(R.drawable.add_scene_book);
                listintwo.add(R.drawable.study2);
                break;
            case "8":
                listint.add(R.drawable.add_scene_moive);
                listintwo.add(R.drawable.movie2);
                break;
            case "9":
                listint.add(R.drawable.add_scene_meeting);
                listintwo.add(R.drawable.meeting2);
                break;
            case "10":
                listint.add(R.drawable.add_scene_cycle);
                listintwo.add(R.drawable.sport2);
                break;
            case "11":
                listint.add(R.drawable.add_scene_noddle);
                listintwo.add(R.drawable.dinner2);
                break;
            case "12":
                listint.add(R.drawable.add_scene_lampon);
                listintwo.add(R.drawable.open_all2);
                break;
            case "13":
                listint.add(R.drawable.add_scene_lampoff);
                listintwo.add(R.drawable.close_all2);
                break;
            case "14":
                listint.add(R.drawable.defaultpic);
                listintwo.add(R.drawable.defaultpicheck);
                break;
            default:
                listint.add(R.drawable.defaultpic);
                listintwo.add(R.drawable.defaultpicheck);
                break;
        }
    }

    private void addDialog() {
        View viewbottom = LayoutInflater.from(getActivity()).inflate(R.layout.myscenedialog, null);
        mrelativeone = ButterKnife.findById(viewbottom, R.id.mrelativeone);
        mrelativetwo = ButterKnife.findById(viewbottom, R.id.mrelativetwo);
        mrelativethree = ButterKnife.findById(viewbottom, R.id.mrelativethree);
        mrelativefour = ButterKnife.findById(viewbottom, R.id.mrelativefour);
        dialogUtil = new DialogUtil(getActivity(), viewbottom, 2);
        dialogUtil1 = new DialogUtil(getActivity());
        mrelativeone.setOnClickListener(this);
        mrelativetwo.setOnClickListener(this);
        mrelativethree.setOnClickListener(this);
        mrelativefour.setOnClickListener(this);
    }

    @Override
    public void initData() {

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sideslip_id:
                chageSlideMenu();
                break;
            case R.id.myscneaddcircle_id:
                //
                IntentUtil.startActivity(getActivity(), MysceneActivity.class);
                //添加在场景侧栏页，判断该场景名称是否存在接口，存在就提示，不存在就添加。
                break;
            case R.id.mrelativeone:
                dialogUtil.removeviewBottomDialog();
                IntentUtil.startActivity(getActivity(), SexActivity.class, "scename", scename);
                break;
            case R.id.mrelativetwo:
                dialogUtil.removeviewBottomDialog();
                Bundle bundle = new Bundle();
                bundle.putString("scnename", scename);
                bundle.putBoolean("actflag", true);
                bundle.putSerializable("deviLceListsce", (Serializable) deviceListsce);
                IntentUtil.startActivity(getActivity(), AddsignsceneActivity.class, bundle);
                break;
            case R.id.mrelativethree:
                mrelativethree_();
                break;
            case R.id.mrelativefour:
                Bundle bundle1 = new Bundle();
                bundle1.putString("scename", scename);
                bundle1.putString("sceType", sceType);
                bundle1.putString("gatewayid", gatewayid);
                bundle1.putString("panelType", panelType);
                bundle1.putString("panelNumber", panelNumber);
                bundle1.putString("buttonNumber", buttonNumber);
                dialogUtil.removeviewBottomDialog();
                IntentUtil.startActivity(getActivity(), AssociatedpanelActivity.class, bundle1);
                break;
        }
    }


    //mrelativethree
    private void mrelativethree_() {
        dialogUtil.removeviewBottomDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(getActivity()));
        map.put("boxNumber", TokenUtil.getBoxnumber(getActivity()));
        map.put("sceneName", scename);
        dialogUtil.loadDialog();
        MyOkHttp.postMapObject(ApiHelper.sraum_deleteScene, map, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {
                mrelativethree_();
            }
        }, getActivity(), dialogUtil) {
            @Override
            public void threeCode() {
                super.threeCode();
                ToastUtil.showToast(getActivity(), "场景名字不正确");
            }

            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                getData(1);
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        get_my_scene(view, position);//响应点击某个场景的事件
    }

    /**
     * 响应点击某个场景的事件
     * @param view
     * @param position
     */
    private void get_my_scene(final View view, final int position) {
        scename = scenelist.get(position).name;
        gitemtext = (TextView) view.findViewById(R.id.gitemtext);
        gitemimage = (ImageView) view.findViewById(R.id.gitemimage);
        if (listtype.get(position).equals("1")) {
            status = "0";
        } else {
            status = "1";
        }
        String boxstatus = TokenUtil.getBoxstatus(getActivity());
        if (!boxstatus.equals("0")) {
            Map<String, Object> map = new HashMap<>();
            map.put("token", TokenUtil.getToken(getActivity()));
            map.put("boxNumber", TokenUtil.getBoxnumber(getActivity()));
            map.put("sceneName", scename);
            dialogUtil.loadDialog();
            my_secene_list(position, map);
        }
    }

    private void my_secene_list(final int position, Map<String, Object> map) {
        MyOkHttp.postMapObject(ApiHelper.sraum_sceneControl, map, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {//刷新togglen
                Map<String, Object> map = new HashMap<>();
                map.put("token", TokenUtil.getToken(getActivity()));
                map.put("boxNumber", TokenUtil.getBoxnumber(getActivity()));
                map.put("sceneName", scename);
                my_secene_list(position, map);

            }
        }, getActivity(),
                dialogUtil) {
            @Override
            public void fourCode() {
                super.fourCode();
                ToastUtil.showToast(getActivity(), "操作失败");
            }

            @Override
            public void threeCode() {
                super.threeCode();
                ToastUtil.showToast(getActivity(), "操作失败");
            }

            @Override
            public void fiveCode() {
                super.fiveCode();
                ToastUtil.showToast(getActivity(), "操作失败");
            }

            @Override
            public void sixCode() {
                super.sixCode();
                ToastUtil.showToast(getActivity(), "操作失败");
            }

            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                ToastUtil.showToast(getActivity(), "操作成功");
                listtype.set(position, status);
                String string = listtype.get(position);
                if (string.equals("1")) {
                    gitemtext.setTextColor(Color.parseColor("#e2c896"));
                    gitemimage.setImageResource(listintwo.get(position));
                } else {
                    gitemtext.setTextColor(Color.parseColor("#303030"));
                    gitemimage.setImageResource(listint.get(position));
                }
                //adapter.changeState(position);
                //getData(null);
            }
        });
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//        ToastUtil.showToast(getActivity(),"accountType:" + accountType);
        switch (accountType) {
            case "1":
                dialogUtil.loadViewBottomdialog();
                scename = scenelist.get(position).name;
                sceType = scenelist.get(position).type;
                gatewayid = scenelist.get(position).gatewayid;
                panelType = scenelist.get(position).panelType;
                panelNumber = scenelist.get(position).panelNumber;
                buttonNumber = scenelist.get(position).buttonNumber;
                for (User.scenelist usce : scenelist) {//场景下的设备列表
                    if (scename.equals(usce.name)) {
                        deviceListsce = usce.deviceList;//当前网关下的所有设备列表
                    }
                }
                break;//    break;//主机，业主-写死
            case "2":

                break;//从机，成员
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(mMessageReceiver);
    }

    public MessageReceiver mMessageReceiver;
    public static String ACTION_INTENT_RECEIVER = "com.massky.sraum.myscenesceceiver";

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
                if (messflag == 2 || messflag == 3 || messflag == 4 || messflag == 5) {
                    getData(2);
                }
            }
        }
    }
}
