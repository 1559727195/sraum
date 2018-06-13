package com.Util;


/**
 * Created by masskywcy on 2017-01-23.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.massky.sraum.LoginActivity;
import com.massky.sraum.R;

/**
 * 用于token过期dialog加载
 */
public class TokenDialog {
    private TextView dtext_id, belowtext_id;
    private Button qxbutton_id, checkbutton_id;
    private DialogUtil dialogUtil;
    // 单例模式
    private static TokenDialog instance;

    private TokenDialog() {
    }

    /**
     * 单一实例
     */
    public static TokenDialog getTokenDialog() {
        if (instance == null) {
            instance = new TokenDialog();
        }
        return instance;
    }

    public void showToDialog(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.check, null);
        dtext_id = (TextView) view.findViewById(R.id.dtext_id);
        belowtext_id = (TextView) view.findViewById(R.id.belowtext_id);
        qxbutton_id = (Button) view.findViewById(R.id.qxbutton_id);
        checkbutton_id = (Button) view.findViewById(R.id.checkbutton_id);
        dtext_id.setText("账号过期");
        belowtext_id.setText("请重新登录！");
        qxbutton_id.setVisibility(View.GONE);
        dialogUtil = new DialogUtil(context, view);
        dialogUtil.loadViewFalsedialog();
        checkbutton_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUtil.removeviewDialog();
                SharedPreferencesUtil.saveData(context, "loginflag", false);
                IntentUtil.startActivityAndFinishFirst(context, LoginActivity.class);
                AppManager.getAppManager().finishAllActivity();
            }
        });
    }
}
