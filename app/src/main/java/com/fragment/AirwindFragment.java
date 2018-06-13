package com.fragment;

import android.view.View;
import android.widget.ImageView;

import com.Util.LogUtil;
import com.base.Basecfragment;
import com.massky.sraum.R;

import butterknife.InjectView;

/**
 * Created by masskywcy on 2016-09-08.
 */
public class AirwindFragment extends Basecfragment {
    @InjectView(R.id.imageone_id)
    ImageView imageone_id;
    @InjectView(R.id.imagetwo_id)
    ImageView imagetwo_id;
    @InjectView(R.id.imagethree_id)
    ImageView imagethree_id;
    @InjectView(R.id.imagefour_id)
    ImageView imagefour_id;
    @InjectView(R.id.imagefive_id)
    ImageView imagefive_id;
    @InjectView(R.id.imagesix_id)
    ImageView imagesix_id;
    @Override
    protected int viewId() {
        return R.layout.airwind;
    }

    @Override
    protected void onView() {
        LogUtil.i("这是第一个fragment", "onView: ");
        onclick();
    }
    private void onclick(){
        imageone_id.setOnClickListener(this);
        imagetwo_id.setOnClickListener(this);
        imagethree_id.setOnClickListener(this);
        imagefour_id.setOnClickListener(this);
        imagefive_id.setOnClickListener(this);
        imagesix_id.setOnClickListener(this);
    }
    @Override
    public void initData() {

    }
    //用于设置清除图标变为默认
    private void clear(){
        imageone_id.setImageResource(R.drawable.lowwindh);
        imagetwo_id.setImageResource(R.drawable.mediumwindh);
        imagethree_id.setImageResource(R.drawable.highwindh);
        imagefour_id.setImageResource(R.drawable.strongwindh);
        imagefive_id.setImageResource(R.drawable.sendwindh);
        imagesix_id.setImageResource(R.drawable.auto_01);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageone_id:
                clear();
                imageone_id.setImageResource(R.drawable.lowwind_01h);
                break;
            case R.id.imagetwo_id:
                clear();
                imagetwo_id.setImageResource(R.drawable.mediumwind_01h);
                break;
            case R.id.imagethree_id:
                clear();
                imagethree_id.setImageResource(R.drawable.highwind_01h);
                break;
            case R.id.imagefour_id:
                clear();
                imagefour_id.setImageResource(R.drawable.strongwind_01h);
                break;
            case R.id.imagefive_id:
                clear();
                imagefive_id.setImageResource(R.drawable.sendwind_01h);
                break;
            case R.id.imagesix_id:
                clear();
                imagesix_id.setImageResource(R.drawable.autoh);
                break;
        }
    }
}
