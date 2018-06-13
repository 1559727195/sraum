package com.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.DialogUtil;
import com.Util.IntentUtil;
import com.Util.LogUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.MycallbackNest;
import com.Util.SharedPreferencesUtil;
import com.Util.ToastUtil;
import com.Util.TokenUtil;
import com.adapter.MygatewayAdapter;
import com.base.Basecfragment;
import com.data.Allbox;
import com.data.User;
import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.massky.sraum.AddgatewayActivity;
import com.massky.sraum.ChangeWangGuanpassActivity;
import com.massky.sraum.DetailActivity;
import com.massky.sraum.MainfragmentActivity;
import com.massky.sraum.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;
import okhttp3.Call;

import static com.Util.TokenUtil.getBoxnumber;

/**
 * Created by masskywcy on 2016-09-12.
 */
//我的网关界面
public class MygatewayFragment extends Basecfragment implements
        AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {
    @InjectView(R.id.add_id)
    RelativeLayout add_id;
    @InjectView(R.id.mygatecir)
    ListView mygatecir;

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

    PopupWindow popupWindow;
    private RelativeLayout rightrelative_id, leftrelative_id;
    private DialogUtil dialogUtil;
    private MygatewayAdapter adapter;
    private List<Allbox> allboxList = new ArrayList<Allbox>();
    private int posit;
    private TextView dtext_id, belowtext_id;
    private Button qxbutton_id, checkbutton_id;
    private MainfragmentActivity mainfragmentActivity;
    List<Allbox> allist = new ArrayList<Allbox>();
    private Context context;
    private RelativeLayout rightrelativesecond_id;
    private static SlidingMenu mySlidingMenu;
    private String accountType;

    public static MygatewayFragment newInstance(SlidingMenu mySlidingMenu1) {
        MygatewayFragment newFragment = new MygatewayFragment();
        Bundle bundle = new Bundle();
        mySlidingMenu = mySlidingMenu1;
        newFragment.setArguments(bundle);
        return newFragment;
    }

    private void chageSlideMenu() {
        if (mySlidingMenu != null) {
            mySlidingMenu.toggle();
        }
    }

    @Override
    protected int viewId() {
        return R.layout.mygateway;
    }

    @Override
    public void onStart() {//fragment的onStart方法可以在由Activity退回到fragment时在次执行
        super.onStart();
        LogUtil.i("这是开启方法");
//        if (TokenUtil.getTokenflag(getActivity())) {
//            getAllBox();
//        } else {
//            allist.clear();
//            adapter = new MygatewayAdapter(getActivity(), allist);
//            mygatecir.setAdapter(adapter);
//        }
        allist.clear();
        adapter = new MygatewayAdapter(getActivity(), allist);
        mygatecir.setAdapter(adapter);
        getAllBox();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
//            if (TokenUtil.getTokenflag(getActivity())) {
//                getAllBox();
//            } else {
//                allist.clear();
//                adapter = new MygatewayAdapter(getActivity(), allist);
//                mygatecir.setAdapter(adapter);
//            }
            allist.clear();
            adapter = new MygatewayAdapter(getActivity(), allist);
            mygatecir.setAdapter(adapter);
            getAllBox();
        }
    }

    @Override
    protected void onView() {

        cenimage_id.setVisibility(View.GONE);
        centext_id.setVisibility(View.GONE);
        addimage_id.setVisibility(View.GONE);

        registerMessageReceiver();
        mainfragmentActivity = (MainfragmentActivity) getActivity();
        deleteDialog();
        addPopwinwow();
        add_id.setOnClickListener(this);
        rightrelative_id.setOnClickListener(this);
        leftrelative_id.setOnClickListener(this);
        rightrelativesecond_id.setOnClickListener(this);
        mygatecir.setOnItemLongClickListener(this);
        mygatecir.setOnItemClickListener(this);
        LogUtil.i("这是创建", "onView: ");
        sideslip_id.setOnClickListener(this);

        //成员，业主accountType->addrelative_id
        accountType = (String) SharedPreferencesUtil.getData(getActivity(), "accountType","");
    }

    //删除dialog提示框
    private void deleteDialog() {
        View view = getActivity().getLayoutInflater().inflate(R.layout.check, null);
        dtext_id = (TextView) view.findViewById(R.id.dtext_id);
        belowtext_id = (TextView) view.findViewById(R.id.belowtext_id);
        qxbutton_id = (Button) view.findViewById(R.id.qxbutton_id);
        checkbutton_id = (Button) view.findViewById(R.id.checkbutton_id);
        dtext_id.setText("点击删除");
        belowtext_id.setText("确定删除吗？");
        qxbutton_id.setOnClickListener(this);
        checkbutton_id.setOnClickListener(this);
        dialogUtil = new DialogUtil(getActivity(), view);
    }

    //获取全部网关信息
    private void getAllBox() {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(getActivity()));
        dialogUtil.loadDialog();
        MyOkHttp.postMapObject(ApiHelper.sraum_getAllBox, map, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {
                getAllBox();
            }
        }, getActivity(), dialogUtil) {
            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                allboxList.clear();
                for (User.box bo : user.boxList) {
                    Allbox allbox = new Allbox(bo.type, bo.number, bo.name, bo.status, bo.sign);
                    allboxList.add(allbox);
                }
                if (allboxList.size() == 0) {
                    Log.e("zhu","no-gateway_allboxlist");
//                    mySlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                    mySlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    SharedPreferencesUtil.saveData(getActivity(), "boxnumber", "");
//                    SharedPreferencesUtil.saveData(getActivity(), "sraumboxNumber", "");
                } else {
//                    mySlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    mySlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    for (Allbox ab : allboxList) {
                        if (ab.sign.equals("1")) {
                            SharedPreferencesUtil.saveData(getActivity(), "boxstatus", ab.status);
                            SharedPreferencesUtil.saveData(getActivity(), "boxnumber", ab.number);
                            LogUtil.eLength("存储数据", ab.number);

                        }
                    }
                }
                BubbleSort(allboxList);
                allist.clear();
                int boxtop = 1;
                for (Allbox bo : allboxList) {
                    Allbox allbox = new Allbox(bo.type, bo.number, bo.name, bo.status, bo.sign, boxtop);
                    allist.add(allbox);
                    boxtop++;
                }
                LogUtil.eLength("这是获取数据", getBoxnumber(getActivity()));
                adapter = new MygatewayAdapter(getActivity(), allist);
                mygatecir.setAdapter(adapter);
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }

    @Override
    public void initData() {

    }

    //加载主界面右上角popwindow
    private void addPopwinwow() {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.mygatepopwindow, null);
        rightrelative_id = (RelativeLayout) v.findViewById(R.id.rightrelative_id);
        leftrelative_id = (RelativeLayout) v.findViewById(R.id.leftrelative_id);
        rightrelativesecond_id = (RelativeLayout) v.findViewById(R.id.rightrelativesecond_id);
        popupWindow = new PopupWindow(v,
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sideslip_id:
//                String boxnumber = getBoxnumber(getActivity());
//                if (boxnumber.equals("")) {
//                    mySlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
//                } else {
//                    mySlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//                    chageSlideMenu();
//                }
                chageSlideMenu();
                break;
            //添加网关界面
            case R.id.add_id:
                switch (accountType) {
                    case "1":
                        Intent intent = new Intent(getActivity(), AddgatewayActivity.class);
                        startActivity(intent);
                        break;//业主
                    case "2":
                        ToastUtil.showToast(getActivity(),"非业主账号不能进行添加删除修改");
                        break;//家庭成员
                }
                break;
            //跳转网关详情界面
            case R.id.rightrelative_id:
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                Bundle bundle = new Bundle();
                bundle.putString("boxName", allboxList.get(posit).name);
                bundle.putString("boxnumber", allboxList.get(posit).number);
                bundle.putString("boxStatus", allboxList.get(posit).status);//status-网关状态
                IntentUtil.startActivity(getActivity(), DetailActivity.class, bundle);
                break;
            //删除网关
            case R.id.leftrelative_id:
                switch (accountType) {
                    case "1":
                        dialogUtil.loadViewdialog();
                        break;//业主
                    case "2":
                        ToastUtil.showToast(getActivity(),"非业主账号不能进行添加删除修改");
                        break;//家庭成员
                }
                break;
            //取消删除网关
            case R.id.qxbutton_id:
                dialogUtil.removeviewDialog();
                break;
            //确定删除网关
            case R.id.checkbutton_id:
                dialogUtil.removeviewDialog();
                deleteBox();
                break;

            case R.id.rightrelativesecond_id://修改网关密码
                switch (accountType) {
                    case "1":
                        if (popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                        Bundle bundle_change_boxpass = new Bundle();
                        bundle_change_boxpass.putString("boxName", allboxList.get(posit).name);
                        bundle_change_boxpass.putString("boxnumber", allboxList.get(posit).number);
                        IntentUtil.startActivity(getActivity(), ChangeWangGuanpassActivity.class, bundle_change_boxpass);
                        break;//业主
                    case "2":
                        ToastUtil.showToast(getActivity(),"非业主账号不能进行添加删除修改");
                        break;//家庭成员
                }
                break;
        }
    }

    //长按弹出popwindow
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        View v = parent.getChildAt(position - mygatecir.getFirstVisiblePosition());
        posit = position;
        popupWindow.showAsDropDown(v.findViewById(R.id.gaterelative_id),
                getResources().getDimensionPixelSize(R.dimen.d70),
                getResources().getDimensionPixelSize(R.dimen.d15));//popupWindow.showAsDropDown显示在R.id.gaterelative_id的下面
        return true;
    }

    //删除网关
    private void deleteBox() {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        dialogUtil.loadDialog();
        delete_mygateway();
    }

    private void delete_mygateway() {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(getActivity()));
        map.put("boxNumber", allboxList.get(posit).number);
        MyOkHttp.postMapObject(ApiHelper.sraum_deleteBox, map, new Mycallback(new AddTogglenInterfacer() {
            @Override
            public void addTogglenInterfacer() {
                delete_mygateway();
            }
        }, getActivity(), dialogUtil) {
            @Override
            public void onSuccess(User user) {
                super.onSuccess(user);
                List<Allbox> listdelbox = new ArrayList<Allbox>();
                Allbox delallbox = allboxList.get(posit);
                listdelbox.add(delallbox);
                allboxList.removeAll(listdelbox);
                adapter.notifyDataSetChanged();
                Log.e("peng","adapter:allboxList:" + allboxList);
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }

    //比较网关状态
    private void getBoxstatus() {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(getActivity()));
        map.put("boxNumber", getBoxnumber(getActivity()));
        dialogUtil.loadDialog();
        MyOkHttp.postMapObject(ApiHelper.sraum_getBoxStatus, map,
                new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {
                        getBoxstatus();
                    }
                }, getActivity(), dialogUtil) {
                    @Override
                    public void onSuccess(User user) {
                        super.onSuccess(user);
                        SharedPreferencesUtil.saveData(getActivity(), "boxstatus", user.status);
                    }

                    @Override
                    public void wrongToken() {
                        super.wrongToken();
                    }
                });
    }

    //设置点击item改变状态
    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        Map<String, Object> mapitembox = new HashMap<>();
        mapitembox.put("token", TokenUtil.getToken(getActivity()));
        mapitembox.put("boxNumber", allist.get(position).number);
        dialogUtil.loadDialog();
        LogUtil.eLength("点击传入数据", new Gson().toJson(mapitembox));
        set_onItemCLick(position,mapitembox);
    }

    /**
     * 设置点击item改变状态
     * @param position
     * @param mapitembox
     */
    private void set_onItemCLick(final int position,final Map<String, Object> mapitembox) {
        MyOkHttp.postMapObjectnest(ApiHelper.sraum_changeBox, mapitembox, new MycallbackNest(new AddTogglenInterfacer(){
            @Override
            public void addTogglenInterfacer() {//刷新togglen数据
                Map<String, Object> mapitembox = new HashMap<>();
                mapitembox.put("token", TokenUtil.getToken(getActivity()));
                mapitembox.put("boxNumber", allist.get(position).number);
                set_onItemCLick(position, mapitembox);
            }
        },
            getActivity(), dialogUtil) {
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
            }

            @Override
            public void onSuccess(User user) {
                adapter.changeState(position);
                //dialogUtil.loadDialog();
                SharedPreferencesUtil.saveData(getActivity(), "boxnumber", allist.get(position).number);
                getBoxstatus();
            }

            @Override
            public void wrongToken() {
                super.wrongToken();
            }
        });
    }

    private void BubbleSort(List<Allbox> sortlist) {
        Allbox temp;//定义一个临时变量
        for (int i = 0; i < sortlist.size(); i++) {//冒泡趟数
            for (int j = 0; j < sortlist.size() - 1 - i; j++) {
                if (sortlist.get(j).sign.trim().equals("0")) {
                    temp = sortlist.get(j);
                    sortlist.set(j, sortlist.get(j + 1));
                    sortlist.set((j + 1), temp);//交换数据
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(mMessageReceiver);
    }

    public MessageReceiver mMessageReceiver;
    public static String ACTION_INTENT_RECEIVER = "com.massky.sraum.mygatewayeceiver";

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
//                    if (TokenUtil.getTokenflag(getActivity())) {
//                        getAllBox();
//                    } else {
//                        allist.clear();
//                        adapter = new MygatewayAdapter(getActivity(), allist);
//                        mygatecir.setAdapter(adapter);
//                    }
                    allist.clear();
                    adapter = new MygatewayAdapter(getActivity(), allist);
                    mygatecir.setAdapter(adapter);
                    getAllBox();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        android.util.Log.e("peng", "MyGatewayFragment->onResume:name:");
    }
}
