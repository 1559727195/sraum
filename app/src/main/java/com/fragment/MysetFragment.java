package com.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Util.MusicUtil;
import com.Util.SharedPreferencesUtil;
import com.base.Basecfragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.massky.sraum.AccountnumberActivity;
import com.massky.sraum.MainfragmentActivity;
import com.massky.sraum.R;
import com.suke.widget.SwitchButton;
import com.wujay.fund.GestureEditActivity;

import butterknife.InjectView;

import static com.massky.sraum.R.id.addimage_id;
import static com.massky.sraum.R.id.cenimage_id;
import static com.massky.sraum.R.id.centext_id;

/**
 * Created by masskywcy on 2016-09-20.
 */
//我的设置界面
public class MysetFragment extends Basecfragment implements SwitchButton.OnCheckedChangeListener {
    @InjectView(R.id.onerelative)
    RelativeLayout onerelative;
    @InjectView(R.id.fourrelative)
    RelativeLayout fourrelative;
    @InjectView(R.id.swibtnone)
    SwitchButton swibtnone;
    @InjectView(R.id.swibtntwo)
    SwitchButton swibtntwo;
    @InjectView(R.id.swibtnthree)
    SwitchButton swibtnthree;

    @InjectView(R.id.sideslip_id)
    RelativeLayout sideslip_id;
    @InjectView(R.id.addrelative_id)
    RelativeLayout addrelative_id;
    @InjectView(R.id.addimage_id)
    ImageView addimage_id;
    @InjectView(R.id.cenimage_id)
    ImageView cenimage_id;
    @InjectView(R.id.centext_id)
    TextView centext_id;

    private Vibrator vibrator;
    private boolean vibflag, musicflag, editFlag;
    private String loginPhone;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private MainfragmentActivity mainfragmentActivity;
    private static SlidingMenu mySlidingMenu;

    public static MysetFragment newInstance(SlidingMenu mySlidingMenu1) {
        MysetFragment newFragment = new MysetFragment();
        Bundle bundle = new Bundle();
        mySlidingMenu = mySlidingMenu1;
        newFragment.setArguments(bundle);
        return newFragment;

    }

    private void  chageSlideMenu () {
        if ( mySlidingMenu != null) {
            if ( mySlidingMenu.isMenuShowing()) {
                mySlidingMenu.showContent();
            } else {
                mySlidingMenu.showMenu();
            }
        }
    }

    @Override
    protected int viewId() {
        return R.layout.myset;
    }

    @Override
    protected void onView() {

        cenimage_id.setVisibility(View.GONE);
        centext_id.setVisibility(View.VISIBLE);
        addimage_id.setVisibility(View.GONE);
        centext_id.setText("设置");
        mainfragmentActivity = (MainfragmentActivity) getActivity();
        loginPhone = (String) SharedPreferencesUtil.getData(getActivity(), "loginPhone", "");
        preferences = getActivity().getSharedPreferences("sraum" + loginPhone, Context.MODE_PRIVATE);
        editor = preferences.edit();
        vibflag = preferences.getBoolean("vibflag", false);
        musicflag = preferences.getBoolean("musicflag", false);
        swibtntwo.setChecked(musicflag);
        swibtnone.setChecked(vibflag);
        onerelative.setOnClickListener(this);
        fourrelative.setOnClickListener(this);
        swibtnone.setOnCheckedChangeListener(this);
        swibtntwo.setOnCheckedChangeListener(this);
        swibtnthree.setOnCheckedChangeListener(this);
        sideslip_id.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        editFlag = preferences.getBoolean("editFlag", false);
        swibtnthree.setChecked(editFlag);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.onerelative:
                Intent intent = new Intent(getActivity(), AccountnumberActivity.class);
                startActivity(intent);
                break;
            case R.id.sideslip_id:
                chageSlideMenu();
                break;
        }
    }

    //switchbtton设置
    @Override
    public void onCheckedChanged(SwitchButton view, boolean isChecked) {
        switch (view.getId()) {
            case R.id.swibtnone:
                editor.putBoolean("vibflag", isChecked);
                editor.commit();
                if (isChecked) {
                    vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(200);
                }
                break;
            case R.id.swibtntwo:
                if (isChecked) {
                    MusicUtil.startMusic(getActivity(), 1);
                } else {
                    MusicUtil.stopMusic(getActivity());
                }
                editor.putBoolean("musicflag", isChecked);
                editor.commit();
                break;
            case R.id.swibtnthree:
                if (isChecked) {
                    Intent intentges = new Intent(getActivity(), GestureEditActivity.class);
                    startActivity(intentges);
                } else {
                    editor.putBoolean("editFlag", isChecked);
                    editor.commit();
                }
                break;
        }
    }
}
