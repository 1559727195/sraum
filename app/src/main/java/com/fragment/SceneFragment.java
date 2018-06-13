package com.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.DialogUtil;
import com.Util.IntentUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.SharedPreferencesUtil;
import com.Util.ToastUtil;
import com.Util.TokenUtil;
import com.adapter.MyscefargmentAdapter;
import com.andview.refreshview.XRefreshView;
import com.base.Basecfragment;
import com.data.User;
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

/**
 * Created by masskywcy on 2016-09-05.
 */
/*用于加载第三个fragment 场景*/
public class SceneFragment extends Basecfragment implements
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    @InjectView(R.id.refresh_view)
    XRefreshView refresh_view;
    @InjectView(R.id.gritview_id)
    GridView gritview_id;
    @InjectView(R.id.myscneaddcircle_id)
    ImageView myscneaddcircle_id;
    @InjectView(R.id.macstatus)
    TextView macstatus;
    @InjectView(R.id.addmacrela_id)
    RelativeLayout addmacrela_id;
    @InjectView(R.id.addmacbtn_id)
    Button addmacbtn_id;
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
    private MainfragmentActivity mainfragmentActivity;
    private String boxNumber, scename, sceType, status = "";
    private List<User.scenelist> scenelist = new ArrayList<>();
    private List<User.devicesce> deviceListsce = new ArrayList<>();
    private List<Integer> listint = new ArrayList<>();
    private List<Integer> listintwo = new ArrayList<>();
    private List<String> listtype = new ArrayList();
    private TextView gitemtext, textitem_id;
    private ImageView gitemimage, imageitem_id;
    private int index_toggen;

    @Override
    protected int viewId() {
        return R.layout.myscene;
    }

    @Override
    protected void onView() {
        registerMessageReceiver();
        myscneaddcircle_id.setVisibility(View.GONE);
        boxNumber = (String) SharedPreferencesUtil.getData(getActivity(), "boxnumber", "");
        mainfragmentActivity = (MainfragmentActivity) getActivity();
        addDialog();
        gritview_id.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gritview_id.setOnItemClickListener(this);
        //gritview_id.setOnItemLongClickListener(this);
        addmacbtn_id.setOnClickListener(this);
        refresh_view.setScrollBackDuration(300);
        refresh_view.setPinnedTime(1000);
        refresh_view.setPullLoadEnable(false);

        adapter = new MyscefargmentAdapter(getActivity(), scenelist, listint, listintwo, false);
        gritview_id.setAdapter(adapter);

        refresh_view.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
                String boxnumberre = TokenUtil.getBoxnumber(getActivity());
                if (boxnumberre.equals("")) {
                    Log.e("zhu","没有网关");
                    scenelist.clear();
                    listint.clear();
                    listintwo.clear();
                    adapter = new MyscefargmentAdapter(getActivity(), scenelist, listint, listintwo, true);
                    gritview_id.setAdapter(adapter);
                    refresh_view.stopRefresh();
                    if (scenelist.size() == 0) {
                        addmacrela_id.setVisibility(View.VISIBLE);
                    } else {
                        addmacrela_id.setVisibility(View.GONE);
                    }
                } else {
                    getData(2);
                }
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
            }
        });
        getData(0);
    }

    private void boxStatus(boolean boxflag, int scneflag) {
        //true为在线状态  false为离线状态
        if (boxflag) {
            macstatus.setVisibility(View.GONE);
            if (scneflag == 0) {
                refresh_view.setVisibility(View.GONE);
                addmacrela_id.setVisibility(View.VISIBLE);
            } else {
                refresh_view.setVisibility(View.VISIBLE);
                addmacrela_id.setVisibility(View.GONE);
            }
        } else {
            addmacrela_id.setVisibility(View.GONE);
            refresh_view.setVisibility(View.GONE);
            macstatus.setVisibility(View.VISIBLE);
        }
    }

    private void getData(int dialogflag) {
        Map<String, Object> map = new HashMap<>();
        map.put("boxNumber", TokenUtil.getBoxnumber(getActivity()));
        map.put("token", TokenUtil.getToken(getActivity()));
        if (dialogflag != 2) {
            dialogUtil.loadDialog();
        }
        get_all_scene(map);
    }

    /**
     * 拿到所有的场景
     * @param map
     */
    private void get_all_scene(final Map<String, Object> map) {
        MyOkHttp.postMapObject(ApiHelper.sraum_getAllScene, map, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {
                getData(2);
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
                boxStatus(TokenUtil.getBoxflag(getActivity()), scenelist.size());
//                adapter = new MyscefargmentAdapter(getActivity(), scenelist, listint, listintwo, true);
//                gritview_id.setAdapter(adapter);
                adapter.setList_s(scenelist, listint, listintwo, true);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void wrongToken() {
                super.wrongToken();//Token是否过期
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
        mrelativeone.setOnClickListener(this);
        mrelativetwo.setOnClickListener(this);
        mrelativethree.setOnClickListener(this);
        mrelativefour.setOnClickListener(this);
    }

    @Override
    public void initData() {
        index_toggen = 0;
        SharedPreferencesUtil.saveData(getActivity(), "pagetag", "3");
//        boolean flag = TokenUtil.getTokenflag(getActivity());
        String boxnumberre = TokenUtil.getBoxnumber(getActivity());
//        if (flag) {
            if (boxnumberre.equals("")) {
                scenelist.clear();
                listint.clear();
                listintwo.clear();
                adapter = new MyscefargmentAdapter(getActivity(), scenelist, listint, listintwo, true);
                gritview_id.setAdapter(adapter);
                if (scenelist.size() == 0) {
                    addmacrela_id.setVisibility(View.VISIBLE);
                } else {
                    addmacrela_id.setVisibility(View.GONE);
                }
            } else {
                getData(1);
            }
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addmacbtn_id:
                IntentUtil.startActivity(getActivity(), MysceneActivity.class);
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
                dialogUtil.removeviewBottomDialog();
                dialogUtil.loadDialog();
                scene_fragment_show();
                break;
            case R.id.mrelativefour:
                Bundle bundle1 = new Bundle();
                bundle1.putString("scename", scename);
                bundle1.putString("sceType", sceType);
                dialogUtil.removeviewBottomDialog();
                IntentUtil.startActivity(getActivity(), AssociatedpanelActivity.class, bundle1);
                break;
        }
    }

    private void scene_fragment_show() {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(getActivity()));
        map.put("boxNumber", boxNumber);
        map.put("sceneName", scename);
        MyOkHttp.postMapObject(ApiHelper.sraum_deleteScene, map, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {
                scene_fragment_show();
            }
        }, getActivity(), dialogUtil) {
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
        scename = scenelist.get(position).name;
        gitemtext = (TextView) view.findViewById(R.id.gitemtext);
        textitem_id = (TextView) view.findViewById(R.id.textitem_id);
        gitemimage = (ImageView) view.findViewById(R.id.gitemimage);
        imageitem_id = (ImageView) view.findViewById(R.id.imageitem_id);
        if (listtype.get(position).equals("1")) {
            status = "0";
        } else {
            status = "1";
        }

        String boxstatus = TokenUtil.getBoxstatus(getActivity());
        if (!boxstatus.equals("0")) {
            Map<String, Object> map = new HashMap<>();
            map.put("token", TokenUtil.getToken(getActivity()));
            map.put("boxNumber", boxNumber);
            map.put("sceneName", scename);
            dialogUtil.loadDialog();
            sraum_sceneControl(position, map);
        }
    }

    /**
     * 场景控制
     * @param position
     * @param map
     */
    private void sraum_sceneControl(final int position, final Map<String, Object> map) {
        MyOkHttp.postMapObject(ApiHelper.sraum_sceneControl, map, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {
                Map<String, Object> map = new HashMap<>();
                map.put("token", TokenUtil.getToken(getActivity()));
                map.put("boxNumber", boxNumber);
                map.put("sceneName", scename);
                sraum_sceneControl(position, map);
            }
        }, getActivity(), dialogUtil) {
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
//                if (string.equals("1")) {
//                    gitemtext.setTextColor(Color.parseColor("#e2c896"));
//                    gitemimage.setImageResource(listintwo.get(position));
//                    textitem_id.setTextColor(Color.parseColor("#e2c896"));
//                    imageitem_id.setImageResource(listintwo.get(position));
//                } else {
//                    gitemtext.setTextColor(Color.parseColor("#303030"));
//                    gitemimage.setImageResource(listint.get(position));
//                    textitem_id.setTextColor(Color.parseColor("#303030"));
//                    imageitem_id.setImageResource(listint.get(position));
//                }


                //getData(null);
                //adapter.changeState(position);
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        dialogUtil.loadViewBottomdialog();
        scename = scenelist.get(position).name;
        sceType = scenelist.get(position).type;
        for (User.scenelist usce : scenelist) {
            if (scename.equals(usce.name)) {
                deviceListsce = usce.deviceList;
            }
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(mMessageReceiver);
    }

    public MessageReceiver mMessageReceiver;
    public static String ACTION_INTENT_RECEIVER = "com.massky.sraum.sceceiver";

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

    @Override
    public void onResume() {
        super.onResume();
        android.util.Log.e("peng", "SceneFragment->onResume:name:");
    }

    @Override
    public void onStart() {
        super.onStart();
        android.util.Log.e("fei", "SceneFragment->onStart():name:");
    }
}
