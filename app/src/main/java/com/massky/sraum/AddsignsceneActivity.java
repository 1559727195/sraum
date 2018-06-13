package com.massky.sraum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.AppManager;
import com.Util.DialogUtil;
import com.Util.IntentUtil;
import com.Util.LogUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.SerializableMap;
import com.Util.SharedPreferencesUtil;
import com.Util.ToastUtil;
import com.Util.TokenUtil;
import com.adapter.AddsignAdapter;
import com.base.Basecactivity;
import com.data.User;
import com.xlistview.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;
import okhttp3.Call;

import static android.media.CamcorderProfile.get;
import static com.Util.ApiHelper.sraum_updateScene;

/**
 * Created by masskywcy on 2017-03-20.
 */
//用于添加场景后选择智能设备信息
public class AddsignsceneActivity extends Basecactivity implements
        PullToRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {
    @InjectView(R.id.backrela_id)
    RelativeLayout backrela_id;
    @InjectView(R.id.titlecen_id)
    TextView titlecen_id;
    @InjectView(R.id.savebtn)
    Button savebtn;
    @InjectView(R.id.assobtn)
    Button assobtn;
    @InjectView(R.id.refresh_view)
    PullToRefreshLayout refresh_view;
    @InjectView(R.id.maclistview_id)
    ListView maclistview_id;
    @InjectView(R.id.editorbtn)
    Button editorbtn;
    @InjectView(R.id.addsilinear)
    LinearLayout addsilinear;

    private DialogUtil dialogUtil;
    private boolean isFirstIn = true, actflag = false, mapflag = true;
    private List<User.device> deviceList = new ArrayList<>();
    private AddsignAdapter adapter;
    private String sceneName, sceneType, panelNumber = "", buttonNumber = "", boxnumber;
    private CheckBox cb;
    private List<Map<String, Object>> listob = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> listob_new = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> listobtwo = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> listobtwo_new = new ArrayList<Map<String, Object>>();
    private List<User.devicesce> deviceListsce = new ArrayList<>();
    private List<Boolean> list = new ArrayList<>();
    public static final int REQUEST_CAPTURE = 100;
    private List<SerializableMap> listmap = new ArrayList<>();
    private String status;


    @Override
    protected int viewId() {
        return R.layout.addsign;
    }

    @Override
    protected void onView() {
        Bundle bundle = IntentUtil.getIntentBundle(AddsignsceneActivity.this);
        sceneName = bundle.getString("scnename");
        sceneType = bundle.getString("sceneType");
        actflag = bundle.getBoolean("actflag");
        if (actflag) {
            deviceListsce = (List<User.devicesce>) bundle.getSerializable("deviLceListsce");//actflag是场景和添加智能设备的标志
            titlecen_id.setText("编辑场景");
            editorbtn.setVisibility(View.VISIBLE);
            addsilinear.setVisibility(View.GONE);
        } else {
            titlecen_id.setText("添加智能设备");
            editorbtn.setVisibility(View.GONE);
            addsilinear.setVisibility(View.VISIBLE);
        }
        boxnumber = (String) SharedPreferencesUtil.getData(AddsignsceneActivity.this, "boxnumber", "");
        dialogUtil = new DialogUtil(AddsignsceneActivity.this);
        refresh_view.setOnRefreshListener(this);
        maclistview_id.setOnItemClickListener(this);
        backrela_id.setOnClickListener(this);
        savebtn.setOnClickListener(this);
        assobtn.setOnClickListener(this);
        editorbtn.setOnClickListener(this);
        refresh_view.autoRefresh();
        assobtn.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.assobtn:
                Bundle bundle1 = new Bundle();
                bundle1.putString("scename", sceneName);
                bundle1.putString("sceType", sceneType);
                IntentUtil.startActivity(AddsignsceneActivity.this, AssociatedpanelActivity.class, bundle1);
                break;
            case R.id.editorbtn:
                Map<String, Object> mapedi = new HashMap<>();
                mapedi.put("token", TokenUtil.getToken(AddsignsceneActivity.this));
                mapedi.put("boxNumber", boxnumber);
                mapedi.put("sceneName", sceneName);
                mapedi.put("deviceList", listob);
                LogUtil.eLength("更新场景", listob.size() + "");
                dialogUtil.loadDialog();
                sraum_updateScene(mapedi);
                break;
            case R.id.backrela_id:
                AddsignsceneActivity.this.finish();
                break;
            case R.id.savebtn:
                Map<String, Object> map = new HashMap<>();
                map.put("token", TokenUtil.getToken(AddsignsceneActivity.this));
                map.put("boxNumber", boxnumber);//网关编号
                map.put("sceneName", sceneName);//场景名称
                map.put("sceneType", sceneType);//场景类型
                map.put("deviceList", listob);//智能设备列表
                map.put("panelNumber", panelNumber);
                map.put("buttonNumber", buttonNumber);
                if (listob.size() == 0) {
                    ToastUtil.showDelToast(AddsignsceneActivity.this, "请选择设备");
                } else {
                    dialogUtil.loadDialog();
                    addSraumScene(map);
                }
                break;
        }
    }

    /**
     * 添加Sraum场景
     * @param map
     */
    private void addSraumScene(final Map<String, Object> map) {
        MyOkHttp.postMapObject(ApiHelper.sraum_addScene,
                map, new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {
                        Map<String, Object> map = new HashMap<>();
                        map.put("token", TokenUtil.getToken(AddsignsceneActivity.this));
                        map.put("boxNumber", boxnumber);//网关编号
                        map.put("sceneName", sceneName);//场景名称
                        map.put("sceneType", sceneType);//场景类型
                        map.put("deviceList", listob);//智能设备列表
                        map.put("panelNumber", panelNumber);
                        map.put("buttonNumber", buttonNumber);
                        addSraumScene(map);
                    }
                }, AddsignsceneActivity.this, dialogUtil) {
                    @Override
                    public void threeCode() {
                        super.threeCode();
                        ToastUtil.showDelToast(AddsignsceneActivity.this, "场景名不正确");
                    }

                    @Override
                    public void fourCode() {
                        super.fourCode();
                        ToastUtil.showDelToast(AddsignsceneActivity.this, "场景类型不正确");
                    }

                    @Override
                    public void fiveCode() {
                        super.fiveCode();
                        ToastUtil.showDelToast(AddsignsceneActivity.this, "场景开关状态不正确");
                    }

                    @Override
                    public void sixCode() {
                        super.sixCode();
                        ToastUtil.showDelToast(AddsignsceneActivity.this, "场景设备信息不正确");
                    }

                    @Override
                    public void onSuccess(User user) {
                        super.onSuccess(user);
                        AppManager.getAppManager().finishAllActivity();
                        ToastUtil.showDelToast(AddsignsceneActivity.this, "场景添加成功");
                        IntentUtil.startActivity(AddsignsceneActivity.this, MainfragmentActivity.class, "addflag", "1");
                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                    }
                });
    }

    /**
     * sraum_updateScene
     * @param mapedi
     */
    private void sraum_updateScene(final Map<String, Object> mapedi) {
        MyOkHttp.postMapObject(sraum_updateScene, mapedi,
                new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {
                        Map<String, Object> mapedi = new HashMap<>();
                        mapedi.put("token", TokenUtil.getToken(AddsignsceneActivity.this));
                        mapedi.put("boxNumber", boxnumber);
                        mapedi.put("sceneName", sceneName);
                        mapedi.put("deviceList", listob);
                        LogUtil.eLength("更新场景", listob.size() + "");
                        sraum_updateScene(mapedi);
                    }
                }, AddsignsceneActivity.this, dialogUtil) {
                    @Override
                    public void onSuccess(User user) {
                        super.onSuccess(user);
                        AddsignsceneActivity.this.finish();
                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                    }
                });
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        //下拉刷新
//        ToastUtil.showToast(AddsignsceneActivity.this,"onRefresh");
        upload(pullToRefreshLayout);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

    }

    //下拉刷新
    private void upload(final PullToRefreshLayout pullToRefreshLayout) {
        Map<String, String> mapdevice = new HashMap<>();
        mapdevice.put("token", TokenUtil.getToken(AddsignsceneActivity.this));
        mapdevice.put("boxNumber", boxnumber);
        add_sign_scene(pullToRefreshLayout, mapdevice);
    }

    /**
     * 添加场景测试
     * @param pullToRefreshLayout
     * @param mapdevice
     */
    private void add_sign_scene(final PullToRefreshLayout pullToRefreshLayout,final Map<String, String> mapdevice) {
        MyOkHttp.postMapString(ApiHelper.sraum_getAllDevice, mapdevice, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {
                upload(pullToRefreshLayout);
            }
        }, AddsignsceneActivity.this, dialogUtil) {
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
            public void wrongBoxnumber() {
                super.wrongBoxnumber();
                if (pullToRefreshLayout != null) {
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                }
            }

            @Override
            public void emptyResult() {
                super.emptyResult();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
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
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                deviceList.clear();
                deviceList.addAll(user.deviceList);
                //进行保存状态设置,并添加数据
                boolean flag = false;
                if (mapflag) {
                    for (int i = 0; i < deviceList.size(); i++) {
                        User.device ud = deviceList.get(i);
                        SerializableMap serializabledevice = new SerializableMap();
                        serializabledevice.setItemcode(i + "");//serializabledevice.setItemcode(i + "");
                        serializabledevice.setItemflag(true);
                        Map<String, Object> mapseri = new HashMap<>();
                        mapseri.put("type", ud.type);
                        mapseri.put("number", ud.number);
                        mapseri.put("status", "1");//状态为1-开，0-关，3，切换（若为窗帘另说）
                        mapseri.put("dimmer", ud.dimmer);
                        mapseri.put("mode", ud.mode);
                        mapseri.put("temperature", ud.temperature);
                        mapseri.put("speed", ud.speed);
                        LogUtil.eLength("添加数据", ud.type + ud.number);
                        for (User.devicesce udsce : deviceListsce) {
                            if (ud.number.trim().equals(udsce.number.trim())) {
                                mapseri.put("type", udsce.type);
                                mapseri.put("number", udsce.number);
                                mapseri.put("status", udsce.status);
                                mapseri.put("dimmer", udsce.dimmer);
                                mapseri.put("mode", udsce.mode);
                                mapseri.put("temperature", udsce.temperature);
                                mapseri.put("speed", udsce.speed);
                            }
                        }
                        serializabledevice.setMap(mapseri);
                        listmap.add(serializabledevice);
                    }
                }
                //只用于第一次刷新添加虚假数据
                mapflag = false;
//                ToastUtil.showToast(AddsignsceneActivity.this,"只用于第一次刷新添加虚假数据");
                for (User.device ud : deviceList) {
                    for (User.devicesce udsce : deviceListsce) {
                        if (ud.number.trim().equals(udsce.number.trim())) {//编辑场景设备时，就是原来的场景设备存在
                            Map<String, Object> map = new HashMap<>();
                            map.put("type", udsce.type);
                            map.put("number", udsce.number);
                            map.put("status", udsce.status);
                            map.put("dimmer", udsce.dimmer);
                            map.put("mode", udsce.mode);
                            map.put("temperature", udsce.temperature);
                            map.put("speed", udsce.speed);
                            listob.add(map);
                            flag = true;//编辑场景设备时，就是原来的场景设备存在flag = true,checkbox出现
                        }
                    }
                    list.add(flag);
                    flag = false;//添加场景设备时，就是原来的场景设备不存在flag = false,checkbox不出现
                }
                if (isFirstIn) {
                    adapter = new AddsignAdapter(AddsignsceneActivity.this, deviceList, list);
                    maclistview_id.setAdapter(adapter);
                    isFirstIn = false;
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
         boolean isStatus_back = false;
        View v = parent.getChildAt(position - maclistview_id.getFirstVisiblePosition());
        cb = (CheckBox) v.findViewById(R.id.checkbox);
        cb.toggle();
        //设置checkbox现在状态
        AddsignAdapter.getIsSelected().put(position, cb.isChecked());
        if (cb.isChecked()) {
            boolean isSelect = list.get(position);//根据有没有被选中，来判断是编辑还是添加


              /*窗帘所需要的属性值*/
            SerializableMap aserializableMap = new SerializableMap();
            String sericode = position + "";
            for (SerializableMap serializableMap : listmap) {
                if (serializableMap.getItemcode().trim().equals(sericode.trim())) {
                    aserializableMap = serializableMap;
                }
            }

            //listob_new.add(serializableMap.getMap());//添加这个是为了记录设备添加的历史记录
            //遍历历史选中记录，恢复历史按钮按下状态,拿到历史选中记录的number，然后更新在次选中的状态为历史按钮选中状态
//            listobtwo_new.clear();
            listobtwo_new = new ArrayList<>();
            String listnumber = deviceList.get(position).number.toString().trim();
            for (Map<String, Object> m : listob_new) {
                String mapnumber = m.get("number").toString().trim();
                //status
                if (mapnumber.equals(listnumber)) {
                    LogUtil.eLength("点击数据", "查看");
                    isStatus_back = true;//说明是历史按钮记录，
                    aserializableMap.setMap(m);
                    listobtwo_new.add(m);
                    listob_new.removeAll(listobtwo_new);
                    break;
                }
            }

            Bundle bundle = new Bundle();
            bundle.putBoolean("actflag",isSelect);//这个是添加场景和编辑场景的标志
            bundle.putString("type", deviceList.get(position).type);
            bundle.putBoolean("isStatus_back", isStatus_back);//说明是历史按钮记录，
            bundle.putString("number", deviceList.get(position).number);
            bundle.putString("name1", deviceList.get(position).name1);
            bundle.putString("name2", deviceList.get(position).name2);
            bundle.putString("name", deviceList.get(position).name);
            bundle.putString("itemcode", position + "");

            bundle.putSerializable("listmap", aserializableMap);
            LogUtil.eLength("名字", deviceList.get(position).name1 + deviceList.get(position).name2);
            Intent intent = new Intent(AddsignsceneActivity.this, AddlamplightActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, REQUEST_CAPTURE);
        } else {
            listobtwo.clear();
            for (Map<String, Object> m : listob) {
                String mapnumber = m.get("number").toString().trim();
                String listnumber = deviceList.get(position).number.toString().trim();
                if (mapnumber.equals(listnumber)) {
                    LogUtil.eLength("点击数据", "查看");
                    listobtwo.add(m);
                }
            }
            listob.removeAll(listobtwo);
        }
        LogUtil.eLength("点击数据", position + "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.eLength("数据返回", requestCode + "数据" + resultCode + "");
        if (data == null) {
            LogUtil.eLength("数据进入", "你看");
            return;
        } else {
            if (requestCode == REQUEST_CAPTURE) {
                LogUtil.eLength("数据没有", "你看");
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    SerializableMap serializableMap = (SerializableMap) bundle.get("map");
                    for (SerializableMap addseri : listmap) {
                        if (addseri.getItemcode().trim().equals(serializableMap.getItemcode().trim())) {
                            Collections.replaceAll(listmap, addseri, serializableMap);
                        }
                    }
                    Map<String, Object> map = serializableMap.getMap();
                    listob.add(map);
                    listob_new.add(map);//添加这个是为了记录设备添加的历史记录
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //添加AddsingsceneActivity销毁时，清空
    }
}
