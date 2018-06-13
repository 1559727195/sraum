package com.jingchen.pulltorefresh.pullableview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by masskywcy on 2017-01-17.
 */
//只有下拉刷新gritview
public class PullableRefreshGridView extends GridView implements Pullable {
    public PullableRefreshGridView(Context context) {
        super(context);
    }

    public PullableRefreshGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullableRefreshGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown() {
        if (getCount() == 0) {
            // 没有item的时候也可以下拉刷新
            return true;
        } else if (getFirstVisiblePosition() == 0
                && getChildAt(0).getTop() >= 0) {
            // 滑到顶部了
            return true;
        } else
            return false;
    }

    @Override
    public boolean canPullUp() {
        return false;
    }
}
