package com.zhongtianli.overalls.fragment;

import android.app.Dialog;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zhongtianli.overalls.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.zhongtianli.overalls.R.id.cv_1;


/**
 * Created by zhu on 2016/12/23.
 */

public class CustomDialogFragment extends DialogFragment implements View.OnTouchListener {

    private ImageView force_close;
    private Dialog dialog;

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

    public interface DialogClickListener{
         void doPositiveClick();
         void doNegativeClick();
    }

    static DialogClickListener mListener;

    public CustomDialogFragment(){
    }

    public static CustomDialogFragment newInstance(String title, String message, DialogClickListener listener){
        CustomDialogFragment frag = new CustomDialogFragment();
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
        View view = inflater.inflate(R.layout.dialog_fragment, null, false);
        //添加这一行
       LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linear);
//        linearLayout.getBackground().setAlpha(255);//0~255透明度值
         linearLayout.setBackgroundDrawable(new BitmapDrawable());
        String title = getArguments().getString("title");
        String message = getArguments().getString("message");

        //强制关闭按钮
         force_close = (ImageView) view.findViewById(R.id.cv_1);
          initEvent();

//
//        ok.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                if (mListener != null) {
//                    mListener.doPositiveClick();
//                }
//            }
//
//        });
//
//        cancel.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                if (mListener != null) {
//                    mListener.doNegativeClick();
//                }
//            }
//
//        });

        dialog.setContentView(view);
        setCancelable(false);//这句话调用这个方法时，按对话框以外的地方不起作用。按返回键也不起作用 -setCancelable (false);按返回键也不起作用
        return dialog;
    }

    private void initEvent() {
        force_close.setOnTouchListener(this);//强制关闭DialogFragment
    }
}
