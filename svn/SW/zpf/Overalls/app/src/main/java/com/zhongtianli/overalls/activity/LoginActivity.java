package com.zhongtianli.overalls.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.zhongtianli.overalls.R;
import com.zhongtianli.overalls.fragment.IpConfigDialogFragment;
import com.zhongtianli.overalls.utils.FinishUtil;

import okhttp3.MediaType;


/**
 * Created by zhu on 2016/12/28.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private Button login_config_btn1;
    private TextView ip_config_txt;
    private EditText edt_user_name1;
    private EditText edt_user_password1;
    private SharedPreferences sp;
    private SharedPreferences sp_quit;//是否退出应用程序
    private String ipString = "";//用户需要配置的服务器地址
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private SharedPreferences.Editor editor;
    private SharedPreferences.Editor sp_quit_editor;
    private String TAG = "robin debug";
    private SharedPreferences sp_ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FinishUtil.addActivity(this);
        initSp();
        initView();
        initData();
        initEvent();
    }

    private void initSp() {
        sp_ip = getSharedPreferences("IPInfo", MODE_PRIVATE);
    }

    private void initData() {
        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        sp_quit = getSharedPreferences("quitInfo",MODE_PRIVATE);//从主界面退到登录界面
        sp_quit_editor = sp_quit.edit();//
        editor = sp.edit();
        initSpData();//初始化sp操作
    }

    private void initSpData() {
        if (sp.getString("UserName", "").equals("massky") && sp.getString("PassWord", "")
                    .equals("massky")) {//如果sp的用户名和密码等于massky，则直接跳转到OverallsActivity中
                if (sp_quit.getInt("Quit",0) == 1) {//说明sp从主界面退到登录界面
                    sp_quit_editor.putInt("Quit",2);
                    sp_quit_editor.commit();
                    editor.putString("PassWord","");//密码清空
                    editor.commit();
                } else {
                    Intent intent = new Intent(LoginActivity.this, OverallsActivity.class);
                    if (ipString.isEmpty()) {//说明没有配置服务器地址或者没有传服务器地址的动作
                        //从sp中取服务器地址
                        ipString = sp_ip.getString("IP","");
                    }
                    intent.putExtra("IP", ipString);
                    startActivity(intent);
                    LoginActivity.this.finish();
                }
            }
            String userName = sp.getString("UserName", "");
            edt_user_name1.setText(userName);
            edt_user_name1.setSelection(edt_user_name1.getText().length());
    }

    private void initEvent() {
        login_config_btn1.setOnClickListener(this);
        ip_config_txt.setOnClickListener(this);
    }

    private void initView() {
        login_config_btn1 = (Button) findViewById(R.id.start_btn_login);//登录
        ip_config_txt = (TextView) findViewById(R.id.ip_config_txt);//服务器配置
        //用户名
        edt_user_name1 = (EditText) findViewById(R.id.edt_user_name);

        //密码
        edt_user_password1 = (EditText) findViewById(R.id.edt_user_password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_btn_login:
                //校验用户名和密码
                login();
                break;//登录
            case R.id.ip_config_txt :
                IpConfigDialogFragment newFragment = IpConfigDialogFragment.newInstance("title", "message", new IpConfigDialogFragment.DialogClickListener(){
                    @Override
                    public void doPositiveClick() {
                    }

                    @Override
                    public void doNegativeClick(String ip) {
                     ipString = ip;
                    }
                });
                newFragment.show(getSupportFragmentManager(), "dialog");
                break;//服务器配置
        }
    }

    private void login() {
        if (edt_user_name1.getText().toString().isEmpty() ||
                edt_user_password1.getText().toString().isEmpty()) {//用户名或密码为空
            Toast.makeText(LoginActivity.this,"账号或密码为空",Toast.LENGTH_SHORT).show();
        } else {
            if (!edt_user_name1.getText().toString().contains("massky") || !edt_user_password1.getText().toString()
                    .contains("massky")) {
                Toast.makeText(LoginActivity.this,"账号或密码不正确",Toast.LENGTH_SHORT).show();
            }
        }
        if (edt_user_name1.getText().toString().contains("massky") && edt_user_password1.getText().toString()
                .contains("massky")) {//用户名和密码输入正确
            Intent intent = new Intent(LoginActivity.this, OverallsActivity.class);
            if (ipString.isEmpty()) {//说明没有配置服务器地址或者没有传服务器地址的动作
                //从sp中取服务器地址
                ipString = sp_ip.getString("IP","");
            }
            intent.putExtra("IP", ipString);
            Log.e(TAG,"ipString:" + ipString);
            //将用户名和密码写入sp
            editor.putString("UserName", "massky");
            editor.putString("PassWord", "massky");
            editor.commit();//提交
            startActivity(intent);
            LoginActivity.this.finish();
        }
    }

}
