package com.zhongtianli.overalls.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.zhongtianli.overalls.R;
import com.zhongtianli.overalls.bean.GetClothesBean;
import com.zhongtianli.overalls.utils.FinishUtil;

import java.util.ArrayList;
import java.util.List;

import static com.zhongtianli.overalls.R.id.employees_content;
import static com.zhongtianli.overalls.R.id.employees_num_content;
import static com.zhongtianli.overalls.R.id.name_label;
import static com.zhongtianli.overalls.R.id.rfid_content;
import static com.zhongtianli.overalls.R.id.store_time_content;

/**
 * Created by zhu on 2016/12/26.
 */
public class DetailActivity extends AppCompatActivity {
    private Toolbar toolbar_detail;
    private GetClothesBean.InClothes inClothes;
    private TextView rfid_content1;
    private TextView employees_content1;
    private TextView employees_num_content1;
    private TextView store_time_content1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        FinishUtil.addActivity(this);
        initIntent ();
        initView();
        initEvent();
    }

    private void initIntent() {
        inClothes = (GetClothesBean.InClothes) getIntent().getSerializableExtra("InClothes");
    }

    private void initEvent() {
        toolbar_detail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initView() {
        rfid_content1 = (TextView) findViewById(R.id.rfid_content);//衣服标签号
        employees_content1 = (TextView) findViewById(R.id.employees_content);//员工姓名
        employees_num_content1 = (TextView) findViewById(R.id.employees_num_content);//员工编号
        store_time_content1 = (TextView) findViewById(R.id.store_time_content);//入库时间
        if (inClothes != null) {
            rfid_content1.setText(inClothes.getRFID());
            employees_content1.setText(inClothes.getName());
            employees_num_content1.setText(inClothes.getId());
            store_time_content1.setText(inClothes.getInTime());
        }
        toolbar_detail = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar_detail);
        getSupportActionBar().setDisplayShowHomeEnabled(true);//toolbar返回键设置
    }
}
