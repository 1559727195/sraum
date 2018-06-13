package com.zhongtianli.overalls.fragment;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhongtianli.overalls.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;


/**
 * Created by zhu on 2016/12/23.
 */

public class IpConfigDialogFragment extends DialogFragment implements View.OnTouchListener
,View.OnClickListener{

    private ImageView force_close;
    private Dialog dialog;
    private Button ip_config;
    private EditText ip_txt;
    private SharedPreferences sp_ip;
    private SharedPreferences.Editor ip_edtior;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {//按下下拉按钮-弹出popwindow
            switch (v.getId()) {
                case R.id.cv_1:
                    dialog.dismiss();
                    break;//强制关闭DialogFragment
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ip_config :
                if (ip_txt.getText().toString().isEmpty()) {//ip输入为空
                    Toast.makeText(getActivity(),"服务器地址为空",Toast.LENGTH_SHORT).show();
                    break;
                }
                dialog.dismiss();
                if (mListener != null) {
                    mListener.doNegativeClick(ip_txt.getText().toString());
                    //将用户提交的服务器地址写入sp
                    ip_edtior.putString("IP",ip_txt.getText().toString());
                    ip_edtior.commit();
                }
                break;//IP配置按钮
        }
    }

    public interface DialogClickListener{
         void doPositiveClick();
         void doNegativeClick(String ip);
    }

    static DialogClickListener mListener;

    public IpConfigDialogFragment(){

    }

    public static IpConfigDialogFragment newInstance(String title, String message, DialogClickListener listener){
        IpConfigDialogFragment frag = new IpConfigDialogFragment();
        Bundle b = new Bundle();
        b.putString("title", title);
        b.putString("message", message);
        frag.setArguments(b);
        mListener = listener;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getActivity(), R.style.DialogStyle);
        LayoutInflater inflater = (LayoutInflater) getActivity()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.ip_config_dialog_fragment, null, false);
        //添加这一行
//       LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linear);
//        linearLayout.getBackground().setAlpha(255);//0~255透明度值
        String title = getArguments().getString("title");
        String message = getArguments().getString("message");
        initSpIp();//初始化存入服务器地址的sp
        initView(view);
        initEvent();
        dialog.setContentView(view);
        setCancelable(false);//这句话调用这个方法时，按对话框以外的地方不起作用。按返回键也不起作用 -setCancelable (false);按返回键也不起作用
        return dialog;
    }

    /**
     * 初始化存入服务器地址的sp
     */
    private void initSpIp() {
        sp_ip = getActivity().getSharedPreferences("IPInfo", MODE_PRIVATE);
        ip_edtior = sp_ip.edit();
    }

    private void initView(View view) {
        //强制关闭按钮
        force_close = (ImageView) view.findViewById(R.id.cv_1);
        //配好IP后按确认键
        ip_config = (Button) view.findViewById(R.id.ip_config);
        //ip
        ip_txt = (EditText) view.findViewById(R.id.ip_txt);
        //读sp的ip地址给editText显示
        ip_txt.setText(sp_ip.getString("IP","").toString());
        ip_txt.setSelection(ip_txt.getText().length());//editTextView-让光标位于文本后面
    }

    private void initEvent() {
        force_close.setOnTouchListener(this);//强制关闭DialogFragment
        ip_config.setOnClickListener(this);//配置好Ip按确认键
    }
}
