package com.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.AddTogenInterface.AddTogglenInterfacer;
import com.Util.ApiHelper;
import com.Util.DialogUtil;
import com.Util.MyOkHttp;
import com.Util.Mycallback;
import com.Util.SharedPreferencesUtil;
import com.Util.ToastUtil;
import com.Util.TokenUtil;
import com.adapter.PanelAdapter;
import com.andview.refreshview.XRefreshView;
import com.base.Basecfragment;
import com.data.User;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.massky.sraum.FastEditPanelActivity;
import com.massky.sraum.MacdeviceActivity;
import com.massky.sraum.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.InjectView;
import okhttp3.Call;
import static com.fragment.Mainviewpager.getDeviceId;


/**
 * Created by masskywcy on 2017-05-12.
 */
//用于我的面板fragment加载
public class PanelFragment extends Basecfragment {
    @InjectView(R.id.panelrecycl)
    ListView panelrecycl;
    @InjectView(R.id.refresh_view)
    XRefreshView refresh_view;
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
    @InjectView(R.id.panel_bottom_add_rel)
    LinearLayout panel_bottom_add_rel;
    @InjectView(R.id.addtxt_id)
    ImageView addtxt_id;
    private DialogUtil dialogUtil;
    private PanelAdapter adapter;
    private List<User.panellist> panelList = new ArrayList<>();
    private static SlidingMenu mySlidingMenu;
    public String accountType;

    public static PanelFragment newInstance(SlidingMenu mySlidingMenu1) {
        PanelFragment newFragment = new PanelFragment();
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
        return R.layout.panelrecylview;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getData(true);
        }
    }

    @Override
    protected void onView() {
        cenimage_id.setVisibility(View.GONE);
        centext_id.setVisibility(View.VISIBLE);
        addimage_id.setVisibility(View.GONE);
        centext_id.setText("我的面板");
        addtxt_id.setVisibility(View.VISIBLE);
//        addtxt_id.setText("快捷编辑");
        addtxt_id.setOnClickListener(this);
        dialogUtil = new DialogUtil(getActivity());
        refresh_view.setScrollBackDuration(300);
        refresh_view.setPinnedTime(1000);
        refresh_view.setPullLoadEnable(false);
        refresh_view.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
                getData(false);
            }
        });
        addcircle_id.setOnClickListener(this);
        sideslip_id.setOnClickListener(this);
        accountType = (String) SharedPreferencesUtil.getData(getActivity(), "accountType", "");
        //panel_bottom_add_rel
        //异步发送数据
        switch (accountType) {
            case "1":
                panel_bottom_add_rel.setVisibility(View.VISIBLE);
                break;//    break;//主机，业主-写死
            case "2":
                panel_bottom_add_rel.setVisibility(View.GONE);
                break;//从机，成员
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getData(true);
    }

    private void getData(boolean flag) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", TokenUtil.getToken(getActivity()));
        map.put("boxNumber", TokenUtil.getBoxnumber(getActivity()));
        if (flag) {
            dialogUtil.loadDialog();
        }
        MyOkHttp.postMapObject(ApiHelper.sraum_getAllPanel, map,
                new Mycallback(new AddTogglenInterfacer() {
                    @Override
                    public void addTogglenInterfacer() {
                        getData(false);
                    }
                }, getActivity(), dialogUtil) {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                        refresh_view.stopRefresh(false);
                    }

                    @Override
                    public void onSuccess(User user) {
                        super.onSuccess(user);
                        refresh_view.stopRefresh();
                        panelList.clear();
                        panelList = user.panelList;
                        adapter = new PanelAdapter(getActivity(), panelList, mySlidingMenu, accountType
                                , new PanelAdapter.PanelAdapterListener() {
                            @Override
                            public void panel_adapter_listener() {//删除item时刷新界面
                                getData(false);
                            }
                        });
                        panelrecycl.setAdapter(adapter);
                        if (backToMainTitleListener != null)
//                        backToMainTitleListener.backToMainTitleLength(panelList.size());
                            centext_id.setText("我的面板(" + panelList.size() + ")");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sideslip_id:
                chageSlideMenu();
                break;
            case R.id.addcircle_id:
//                Intent intent = new Intent(getActivity(), MacpanelActivity.class);
//                intent.putExtra("name", "2");
//                startActivity(intent);
                sraum_setBox_accent();
                break;
            case R.id.addtxt_id://快捷编辑
                startActivity(new Intent(getActivity(), FastEditPanelActivity.class));
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
        android.util.Log.e("peng", "PanelFragment->onResume:name:");
    }
}
