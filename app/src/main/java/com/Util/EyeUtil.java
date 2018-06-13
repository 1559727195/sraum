package com.Util;

import android.content.Context;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.widget.ImageView;

import com.massky.sraum.R;

/**
 * Created by masskywcy on 2017-01-18.
 */
//用于EditText密码形式是否显示
public class EyeUtil {
    //上下文对象
    private Context context;
    //传入点击眼睛图片
    private ImageView imageView;
    //传入editext
    private ClearEditText clearEditText;
    //进行判断闭眼或开眼操作
    private boolean flag;

    //传入数据
    public EyeUtil(Context context, ImageView imageView,
                   ClearEditText clearEditText, boolean flag) {
        this.context = context;
        this.imageView = imageView;
        this.clearEditText = clearEditText;
        this.flag = flag;
    }

    //进行密码形式是否显示操作
    public void EyeStatus() {
        if (flag) {
            //设置密码可见
            imageView.setImageResource(R.drawable.eyeopen);
            clearEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            flag = false;
        } else {
            //设置密码不可见
            imageView.setImageResource(R.drawable.eyeclose);
            clearEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
            flag = true;
        }
        //切换后将EditText光标置于末尾
        CharSequence charSequence = clearEditText.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
    }
}
