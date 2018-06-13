package com.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.Util.StatusBarCompat;
import com.Util.view.RoundProgressBar;
import com.massky.sraum.AddZigbeeDevActivity;
import com.massky.sraum.AddZigbeeDeviceScucessActivity;
import com.massky.sraum.ConnWifiActivity;
import com.massky.sraum.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


/**
 * Created by zhu on 2016/12/23.
 */

public class ConfigZigbeeConnDialogFragment extends DialogFragment implements View.OnClickListener,
        AddZigbeeDevActivity.ConnWifiInterfacer {

    private ImageView force_close;
    private Dialog dialog;
    private Button ip_config;
    private EditText ip_txt;
    private SharedPreferences sp_ip;
    private SharedPreferences.Editor ip_edtior;
    private Button start_btn_store;
    //	private  RadarScanView radarScanView;//雷达弹出视图
    private static Context context;
    private Toolbar toolbars;
    private TextView tvPrevTitle;
    private ImageView ivBack;
    private RoundProgressBar roundProgressBar2;
    private ImageView back;
    private LinearLayout progress_loading_linear;
    private LinearLayout loading_error_linear;
    private Button add_net_btn;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                if (dialog != null) dialog.dismiss();
                break;
            case R.id.add_net_btn:
                if (dialog != null) dialog.dismiss();
                startActivity(new Intent(getActivity(), AddZigbeeDeviceScucessActivity.class));
                break;
        }
    }

    @Override
    public void conn_wifi_interface() {
        init_status_bar();
    }

    public interface DialogClickListener {
        void doRadioWifi();

        void doRadioScanDevice();

        void dialogDismiss();
    }

    static DialogClickListener mListener;

    public ConfigZigbeeConnDialogFragment() {

    }

    public static ConfigZigbeeConnDialogFragment newInstance(Context context1, String title, String message, DialogClickListener listener) {
        ConfigZigbeeConnDialogFragment frag = new ConfigZigbeeConnDialogFragment();
        Bundle b = new Bundle();
        b.putString("title", title);
        b.putString("message", message);
        frag.setArguments(b);
        mListener = listener;
        context = context1;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getActivity(), R.style.DialogStyle);
        LayoutInflater inflater = (LayoutInflater) getActivity()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.config_zig_con_dialog_fragment, null, false);
        //添加这一行
//       LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linear);
//        linearLayout.getBackground().setAlpha(255);//0~255透明度值
        String title = getArguments().getString("title");
        String message = getArguments().getString("message");
        initView(view);
        initEvent();

        //在这里配置wifi

        dialog.setContentView(view);
        setCancelable(true);//这句话调用这个方法时，按对话框以外的地方不起作用。按返回键也不起作用 -setCancelable (false);按返回键也不起作用
        StatusBarCompat.compat(getActivity(), getResources().getColor(R.color.colorgraystatusbar));//更改标题栏的颜色
        return dialog;
    }

    /**
     * 让dialogFragment铺满整个屏幕的好办法
     */
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        Window win = getDialog().getWindow();
        // 一定要设置Background，如果不设置，window属性设置无效
        win.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        android.view.WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = Gravity.BOTTOM;
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width = LayoutParams.MATCH_PARENT;
        params.height = LayoutParams.WRAP_CONTENT;
        win.setAttributes(params);

    }


    private void initView(View view) {
        roundProgressBar2 = (RoundProgressBar) view.findViewById(R.id.roundProgressBar2);
        back = (ImageView) view.findViewById(R.id.back);//progress_loading_linear,loading_error_linear
        progress_loading_linear = (LinearLayout) view.findViewById(R.id.progress_loading_linear);
        loading_error_linear = (LinearLayout) view.findViewById(R.id.loading_error_linear);
        add_net_btn = (Button) view.findViewById(R.id.add_net_btn);

    }

    private void initEvent() {
        back.setOnClickListener(this);
        //progress_loading_linear,loading_error_linear
//        progress_loading_linear.setOnClickListener(this);
//        loading_error_linear.setOnClickListener(this);
        add_net_btn.setOnClickListener(this);
    }

    private void init_status_bar() {
        roundProgressBar2.setMax(6);
        final int[] index = {6};
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                boolean is_index = true;
                while (is_index) {
                    index[0]--;
                    try {
                        Thread.sleep(1000);
                        roundProgressBar2.setProgress(i++);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (index[0] < 0) {
                        is_index = false;
                        if(getActivity() != null)
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                progress_loading_linear.setVisibility(View.GONE);
//                                loading_error_linear.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }
            }
        }).start();
    }
}
