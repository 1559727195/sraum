package com.zhongtianli.overalls;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhongtianli.overalls.fragment.StoreFragment;
import com.zhongtianli.overalls.fragment.TakeOutFragment;
import com.zhongtianli.overalls.interfaces.StoreRefreshListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView txt_take_out;
    private TextView txt_store;
    private StoreFragment mFragStore;
    private TakeOutFragment mFragTakeOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        CustomDialogFragment newFragment = CustomDialogFragment.newInstance("title", "message", null);
//        newFragment.show(getSupportFragmentManager(), "dialog");
        initView();
        initStoreCard();//首次初始化存储选项卡属性
        initEvent();

    }

    private void initEvent() {
        selectFragment(0);
        txt_take_out.setOnClickListener(this);
        txt_store.setOnClickListener(this);
    }

    private void initView() {
        txt_take_out = (TextView) findViewById(R.id.txt_take_out);//取出
        txt_store = (TextView) findViewById(R.id.txt_store);//存储
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_store:
                initStoreCard();
                //切换到存储fragment碎片
                 selectFragment(0);
                break;//存储
            case R.id.txt_take_out:
                //切换到取出fragment碎片
                initTakeOutCard();
                selectFragment(1);
                break;//取出
        }
    }

    private void initTakeOutCard() {//初始化取出选项卡属性
        txt_store.setBackgroundColor(getResources().getColor(R.color.blue_txt));
        txt_store.setTextColor(getResources().getColor(R.color.white_txt));
        txt_take_out.setBackgroundColor(getResources().getColor(R.color.gray_txt));
        txt_take_out.setTextColor(getResources().getColor(R.color.black_txt));
    }

    private void initStoreCard() {//初始化存储选项卡属性
        txt_store.setBackgroundColor(getResources().getColor(R.color.gray_txt));
        txt_store.setTextColor(getResources().getColor(R.color.black_txt));
        txt_take_out.setBackgroundColor(getResources().getColor(R.color.blue_txt));
        txt_take_out.setTextColor(getResources().getColor(R.color.gray_txt));
    }

    //fragment碎片切换
    private void selectFragment (int i) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (i) {
            case 0:
                hideFragment(transaction);
                if (mFragStore == null) {
                    mFragStore = StoreFragment.newInstance(MainActivity.this, new StoreRefreshListener() {
                        @Override
                        public void storeRefresh() {
                        }
                    });
                    transaction.add(R.id.frame_container, mFragStore);
                } else {
                    transaction.show(mFragStore);
                }
                break;
            case 1:
                hideFragment(transaction);
                if (mFragTakeOut == null) {
                    mFragTakeOut= TakeOutFragment.newInstance(MainActivity.this);
                    transaction.add(R.id.frame_container, mFragTakeOut);
                } else {
                    transaction.show(mFragTakeOut);
                }
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    //碎片隐藏
    private void hideFragment(FragmentTransaction transaction) {
        if (mFragStore != null) {
            transaction.hide(mFragStore);
        }
        if (mFragTakeOut != null) {
            transaction.hide(mFragTakeOut);
        }
    }
}
