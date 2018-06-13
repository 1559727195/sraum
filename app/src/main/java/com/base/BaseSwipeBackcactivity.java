package com.base;

/**
 * Created by masskywcy on 2016-08-19.
 */

import android.os.Bundle;
import android.view.View;

import com.chenhongxin.autolayout.AutoLayoutActivity;

import butterknife.ButterKnife;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;
/*所有可侧滑返回activity的基类*/

/**
 * Created by masskywcy on 2016-07-07.
 */
public abstract class BaseSwipeBackcactivity extends AutoLayoutActivity implements View.OnClickListener, SwipeBackActivityBase {
    private SwipeBackActivityHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(viewId());
        ButterKnife.inject(this);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
        onView();
    }

    protected abstract int viewId();

    protected abstract void onView();

    //用于设置侧滑返回上级页面
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }
}

