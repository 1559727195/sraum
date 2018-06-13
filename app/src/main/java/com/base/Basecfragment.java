package com.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.Util.SharedPreferencesUtil;
import com.fragment.MacFragment;
import com.fragment.MacdeviceFragment;
import com.fragment.MygatewayFragment;
import com.fragment.MysceneFragment;
import com.fragment.RoomFragment;
import com.fragment.SceneFragment;
import butterknife.ButterKnife;
/*用于fragment的基类*/

/**
 * Created by masskywcy on 2016-07-08.
 */
public abstract class Basecfragment extends Fragment implements View.OnClickListener {
    public Context mContext;
    protected boolean isVisible;
    private boolean isPrepared;
    public static boolean isForegrounds = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(viewId(), null);
        ButterKnife.inject(this, rootView);
        onView();
        setPage();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void setPage() {
        if (this instanceof MacFragment || this instanceof RoomFragment ||
                this instanceof SceneFragment) {
            SharedPreferencesUtil.saveData(getActivity(), "pagetag", "3");
        } else if (this instanceof MacdeviceFragment) {
            SharedPreferencesUtil.saveData(getActivity(), "pagetag", "4");
        } else if (this instanceof MygatewayFragment) {
            SharedPreferencesUtil.saveData(getActivity(), "pagetag", "5");
        } else if (this instanceof MysceneFragment) {
            SharedPreferencesUtil.saveData(getActivity(), "pagetag", "6");
        } else {
            SharedPreferencesUtil.saveData(getActivity(), "pagetag", "");
        }
    }

    protected abstract int viewId();

    protected abstract void onView();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            lazyLoad();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        Log.d("TAG", "fragment->onActivityCreated");
        isPrepared = true;
        lazyLoad();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);
//        Log.d("TAG", "fragment->onCreate");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        setPage();
    }

    /**
     * 懒加载
     */
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }

        initData();
    }

    public abstract void initData();

    //do something
    protected void onInvisible() {

    }

    @Override
    public void onResume() {
        isForegrounds = true;
        super.onResume();
    }

    @Override
    public void onPause() {
        isForegrounds = false;
        super.onPause();
    }

}
