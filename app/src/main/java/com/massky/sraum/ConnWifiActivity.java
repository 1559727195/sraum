package com.massky.sraum;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import com.base.Basecactivity;
import com.fragment.ConfigDialogFragment;
import com.yanzhenjie.statusview.StatusUtils;
import com.yanzhenjie.statusview.StatusView;
import butterknife.InjectView;

/**
 * Created by zhu on 2018/5/30.
 */

public class ConnWifiActivity extends Basecactivity {
    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.status_view)
    StatusView statusView;
    @InjectView(R.id.select_wlan_rel_big)
    PercentRelativeLayout select_wlan_rel_big;
    @InjectView(R.id.conn_btn_dev)
    Button conn_btn_dev;
    private ConfigDialogFragment newFragment;

    @Override
    protected int viewId() {
        return R.layout.conn_wifi_act;
    }

    @Override
    protected void onView() {
        back.setOnClickListener(this);
        select_wlan_rel_big.setOnClickListener(this);
        conn_btn_dev.setOnClickListener(this);
        StatusUtils.setFullToStatusBar(this);  // StatusBar.
        back.setOnClickListener(this);
        if (!StatusUtils.setStatusBarDarkFont(this, true)) {// Dark font for StatusBar.
            statusView.setBackgroundColor(Color.BLACK);
        }
        initDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                ConnWifiActivity.this.finish();
                break;
            case R.id.select_wlan_rel_big:
                showPopwindow();
                break;
            case R.id.conn_btn_dev:
//                startActivity(new Intent(ConnWifiActivity.this,));
                //在这里弹出dialogFragment对话框
                if (!newFragment.isAdded()) {//DialogFragment.show()内部调用了FragmentTransaction.add()方法，所以调用DialogFragment.show()方法时候也可能
                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction ft = manager.beginTransaction();
                    ft.add(newFragment, "dialog");
                    ft.commit();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        connWifiInterfacer.conn_wifi_interface();
                    }
                },30);

                break;
        }
    }

    private  ConnWifiInterfacer connWifiInterfacer;
    public interface  ConnWifiInterfacer {
        void conn_wifi_interface();
    }

    /**
     * 初始化dialog
     */
    private void initDialog() {
        // TODO Auto-generated method stub
        newFragment = ConfigDialogFragment.newInstance(ConnWifiActivity.this, "", "", new ConfigDialogFragment.DialogClickListener() {

            @Override
            public void doRadioWifi() {//wifi快速配置

            }

            @Override
            public void doRadioScanDevice() {

            }

            @Override
            public void dialogDismiss() {

            }

        });//初始化快配和搜索设备dialogFragment

        connWifiInterfacer = (ConnWifiInterfacer) newFragment;

    }


    /**
     * 显示popupWindow
     */
    private void showPopwindow() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View outerView = inflater.inflate(R.layout.wheel_view, null);

//        View outerView = LayoutInflater.from(this).inflate(R.layout.wheel_view, null);
        ListView wv = (ListView) outerView.findViewById(R.id.wheel_view_wv);
//        wv.setOffset(1);
//        wv.setItems(Arrays.asList(PLANETS));
//        wv.setSeletion(2);
//        wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
//            @Override
//            public void onSelected(int selectedIndex, String item) {
//                Log.d(TAG, "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
//            }
//        });

        final String[] data = {"类型一", "类型二", "类型三", "类型四"};
        ArrayAdapter<String> array = new ArrayAdapter<>(this,
                R.layout.simple_expandable_list_item_new, data);
        wv.setAdapter(array);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);//水平
        // 初始化自定义的适配器
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int displayWidth = dm.widthPixels;
        int displayHeight = dm.heightPixels;

        final PopupWindow window = new PopupWindow(outerView,
                displayWidth / 2,
                WindowManager.LayoutParams.WRAP_CONTENT);//高度写死

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        window.setBackgroundDrawable(dw);
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;

        getWindow().setAttributes(lp);
        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.style_pop_animation);
        // 在底部显示
//        window.showAtLocation(WuYeTouSu_NewActivity.this.findViewById(R.id.tousu_select),
//                Gravity.CENTER_HORIZONTAL, 0, 0);
//        window.showAsDropDown(select_wlan_rel_big);

        // 将pixels转为dip
        int xoffInDip = pxTodip(displayWidth) / 4 * 3;

        window.showAsDropDown(select_wlan_rel_big, xoffInDip, xoffInDip / 3);
        //popWindow消失监听方法
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                System.out.println("popWindow消失");
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        wv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                tousu_txt1.setText(data[position]);
                window.dismiss();
            }
        });

    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public int pxTodip(float pxValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
