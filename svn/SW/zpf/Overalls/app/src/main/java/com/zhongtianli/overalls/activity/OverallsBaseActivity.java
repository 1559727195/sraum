package com.zhongtianli.overalls.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.zhongtianli.overalls.R;
import com.zhongtianli.overalls.bean.ActionItem;
import com.zhongtianli.overalls.bean.GetClothesBean;
import com.zhongtianli.overalls.fragment.StoreFragment;
import com.zhongtianli.overalls.fragment.TakeOutFragment;
import com.zhongtianli.overalls.interfaces.StoreRefreshListener;
import com.zhongtianli.overalls.presenter.OverallsPresenter;
import com.zhongtianli.overalls.utils.FinishUtil;
import com.zhongtianli.overalls.view.Overalls_clothes_getIn_view;
import com.zhongtianli.overalls.window.TitlePopup;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by zhu on 2016/12/27.
 */

public abstract class OverallsBaseActivity extends AppCompatActivity implements View.OnClickListener,View.OnTouchListener
,Overalls_clothes_getIn_view{
    private TextView txt_take_out;
    private TextView txt_store;
    private StoreFragment mFragStore;
    private TakeOutFragment mFragTakeOut;
    public List<Fragment> mFragmentList;
    public ViewPager mContentPager;
    private PagerAdapter mPagerAdapter;
    private LinearLayout card_lia_left1;
    private LinearLayout card_lia_right1;
    private String ipString;//拿到用户输入的IP地址
    private OverallsPresenter overallsPresenter;
    private LinearLayout pop_menu_lay;
    private ImageView img_first;
    private ImageView img_second;
    private ImageView img_third;
    public TitlePopup titlePopup;
    private SharedPreferences sp_quit;
    private SharedPreferences.Editor editor_quit;
    private Dialog dialog;
    private Timer timer;
    private MyTimerTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overalls_base_act);
        FinishUtil.addActivity(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                //在2s秒内搜索蓝牙设备列表
                dialog = createLoadingDialog(OverallsBaseActivity.this, "正在加载");//创建 loadingDialog
                dialog.show();
                dialogFullScreen();//让loadingDialog全屏
                Looper.loop();
            }
        }).start();
        new LoginThread().start();//延时2秒，让dialog自己消失
        initData ();
        initFragments();
        initView();
        initStoreCard();//首次初始化存储选项卡属性
        initEvent();
    }

    /**
     * 让Dialog全屏显示
     */
    private void dialogFullScreen() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.height = display.getHeight();//设置高度
        lp.width = (int) (display.getWidth()); //设置宽度
        dialog.getWindow().setAttributes(lp);
    }

    private void initData() {//拿到用户配置的Ip
        sp_quit = getSharedPreferences("quitInfo",MODE_PRIVATE);//从主界面退到登录界面
        editor_quit =  sp_quit.edit();
        ipString = getIntent().getStringExtra("IP");
        overallsPresenter = new OverallsPresenter(this);//注册获取存货接口
        overallsPresenter.Overalls_clothes_getInLists(OverallsBaseActivity.this,ipString);
    }

    private void initFragments() {
        mFragmentList = new ArrayList<Fragment>(2);
        StoreFragment storeFragment = StoreFragment.newInstance(OverallsBaseActivity.this,new StoreRefreshListener(){

            @Override
            public void storeRefresh() {//StoreFragment刷新，然后去重新拉取数据
                overallsPresenter.Overalls_clothes_getInLists(OverallsBaseActivity.this,ipString);
            }
        });
        overalls_getIn_listener = (Overalls_GetIn_Listener) storeFragment;//把storeFragment强制转换为overalls_getIn_listener类型
        mFragmentList.add(storeFragment);
        TakeOutFragment takeOutFragment = TakeOutFragment.newInstance(OverallsBaseActivity.this);
        mFragmentList.add(takeOutFragment);
    }

    //    第二种方法相对来说就比较复杂了，你需要自定义请求超时操作
//    1）自定义一个TimerTask，用于向handler发送请求超时消息

    private class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            handlerTimeOut.sendEmptyMessage(2000);
        }
    }

//    2）定义一个设置请求超时的方法，超时时间为5秒

    private void checkTimeOut() {
        try {
            timer = new Timer();
            task = new MyTimerTask();
            timer.schedule(task, 2000);//延时2秒在执行task
        } catch (Exception e) {
            Log.e("timer", e.getMessage());
        }
    }

//    3）开发登录线程

    private class LoginThread extends Thread {
        @Override
        public void run() {
            try {
                checkTimeOut();
            } catch (Exception e) {
                Log.e("LoginThread", e.getMessage());
            }
        }
    }

//    在handler中处理请求超时或者请求成功的操作

    Handler handlerTimeOut = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    //关掉timer
                    timer.cancel();
                    //处理请求超时时要做的操作
                    dialog.dismiss();
                    break;
            }
        }
    };


    /**
     * 得到自定义的progressDialog
     *
     * @param context
     * @param msg
     * @return
     */
    public Dialog createLoadingDialog(Context context, String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(msg);// 设置加载信息
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

        /*
        * Window window = loadingDialog.getWindow(); dialog_style
        * 让Dialog淡入淡出，去除闪屏
        * */
        Window window = loadingDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.dialog_style);

        loadingDialog.setCancelable(true);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
        return loadingDialog;
    }

    Handler handlerDialog = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {//关闭dialog
                dialog.dismiss();
            } else if (msg.what == 1) {//打开dialog
                //创建对话框
                dialog = createLoadingDialog(OverallsBaseActivity.this, "正在加载");//创建 loadingDialog
                dialog.show();
                dialogFullScreen();//让loadingDialog全屏
            }
        }
    };

    private void initEvent() {
        //默认让左边收藏选项卡被选中
        txt_take_out.setOnClickListener(this);
        txt_store.setOnClickListener(this);
        //去云端拉去库存信息
        pop_menu_lay.setOnTouchListener(this);//监听popwindow菜单的监听事件
    }

    private void initView() {
        //右上角popWindow弹出框容器
        pop_menu_lay = (LinearLayout) findViewById(R.id.pop_menu_lay);
        //第一个小圆形
        img_first = (ImageView) findViewById(R.id.img_first);
        //第二个小圆形
        img_second = (ImageView) findViewById(R.id.img_second);
        //第三个小圆形
        img_third = (ImageView) findViewById(R.id.img_third);

        //实例化标题栏弹窗
        titlePopup = new TitlePopup(this, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //监听popwindow的item的点击事件
        titlePopup.setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {
            @Override
            public void onItemClick(ActionItem item, int position) {
                //退出应用程序,退到登录界面
                editor_quit.putInt("Quit",1);
                editor_quit.commit();
                FinishUtil.finishActivity();
                  startActivity(new Intent(OverallsBaseActivity.this,LoginActivity.class));
            }
        });
        //给标题栏弹窗添加子类
        addTitleChildClass();

        //存储选项卡容器
        card_lia_left1 = (LinearLayout) findViewById(R.id.card_lia_left);
        //取出选项卡容器
        card_lia_right1 = (LinearLayout) findViewById(R.id.card_lia_right);
        txt_take_out = (TextView) findViewById(R.id.txt_take_out);//取出
        txt_store = (TextView) findViewById(R.id.txt_store);//存储
        mContentPager = (ViewPager) findViewById(R.id.mViewPager);
        mPagerAdapter=getPagerAdapter();
        mContentPager.setAdapter(mPagerAdapter);
        mContentPager.setOffscreenPageLimit(2);//设置这句话的好处就是在viewapger下可以同时刷新3个fragment
        mContentPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        initStoreCard();
                        break;//存储
                    case 1:
                         initTakeOutCard();
                        break;//取出
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    /**
     * 给标题栏弹窗添加子类
     */
    private void addTitleChildClass() {
        titlePopup.addAction(new ActionItem(this, "退出", R.drawable.quit_true));
    }

    //三个小圆点变色
    public void dotColor_white() {
        img_first.setBackgroundColor(getResources().getColor(R.color.white_menu));
        img_second.setBackgroundColor(getResources().getColor(R.color.white_menu));
        img_third.setBackgroundColor(getResources().getColor(R.color.white_menu));
    }

    public void dotColor_blue() {
        img_first.setBackgroundColor(getResources().getColor(R.color.blue_menu));
        img_second.setBackgroundColor(getResources().getColor(R.color.blue_menu));
        img_third.setBackgroundColor(getResources().getColor(R.color.blue_menu));
    }

    public void initTakeOutCard() {//初始化取出选项卡属性
        card_lia_left1.setSelected(true);
        card_lia_right1.setSelected(false);
//        txt_store.setBackgroundColor(getResources().getColor(R.color.blue_txt));
        txt_store.setTextColor(getResources().getColor(R.color.white_txt));
//        txt_take_out.setBackgroundColor(getResources().getColor(R.color.gray_txt));
        txt_take_out.setTextColor(getResources().getColor(R.color.black_txt));
    }

    public void initStoreCard() {//初始化存储选项卡属性
        card_lia_left1.setSelected(false);
        card_lia_right1.setSelected(true);
//        txt_store.setBackgroundColor(getResources().getColor(R.color.gray_txt));
        txt_store.setTextColor(getResources().getColor(R.color.black_txt));
//        txt_take_out.setBackgroundColor(getResources().getColor(R.color.blue_txt));
        txt_take_out.setTextColor(getResources().getColor(R.color.gray_txt));
    }


    public Overalls_GetIn_Listener overalls_getIn_listener;
    public interface Overalls_GetIn_Listener {//测试接口-在这里写一个设置activity多次携带数据返回fragment的接口实现
        void overalls_GetIn(GetClothesBean getClothesBean);//设备管理名称
    }
    protected  abstract PagerAdapter getPagerAdapter();
}
