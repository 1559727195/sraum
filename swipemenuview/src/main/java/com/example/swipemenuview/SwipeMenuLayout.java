package com.example.swipemenuview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.Toast;

/**
 * Created by louiewh on 16/6/16.
 */
public class SwipeMenuLayout extends FrameLayout {
    final static String TAG = "SwipeMenuLayout";

    private View mRightMenuView;
    private View mContextView;

    private int mLeftMenuViewId;
    private int mRightMenuViewId;
    private int mContextViewId;

    final String RIGHTMENUVIEW = "swipe_right_menu";
    final String CONTEXTVIEW   = "swipe_context";

    private float mDownX;
    private int mRightMargin;
    private int mTouchSlop;
    private int mPosition;
    private int defScrollTime = 500;
    private int mScrollTime;
    private boolean mAutoMenu;
    private boolean mMenuShow;
    private boolean mResterListener;

    private ScrollerCompat  mScroller;
    private OnMenuClickListener  mOnMenuClickListener;

    static  public SwipeMenuLayout mSlideView;
    private int index_slidemenu = 0;
    private String accountType;

    public SwipeMenuLayout(Context context) {
        super(context);
        initUI();
    }

    public SwipeMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        initUI();
    }

    public SwipeMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        initUI();
    }

    private void initAttrs(AttributeSet attrs){
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SwipeMenuLayout);
        mScrollTime = a.getInt(R.styleable.SwipeMenuLayout_duration, defScrollTime);
        mAutoMenu   = a.getBoolean(R.styleable.SwipeMenuLayout_auto_menu, true);
    }

    private void initUI() {
        mScroller = ScrollerCompat.create(getContext());

        ViewConfiguration config = ViewConfiguration.get(getContext());
        mTouchSlop = config.getScaledTouchSlop();

//        mLeftMenuViewId  = getContext().getResources().getIdentifier(LEFTMENUVIEW, "id", getContext().getPackageName());
        mRightMenuViewId = getContext().getResources().getIdentifier(RIGHTMENUVIEW, "id", getContext().getPackageName());
        mContextViewId   = getContext().getResources().getIdentifier(CONTEXTVIEW, "id", getContext().getPackageName());

        if (mRightMenuViewId == 0 || mContextViewId == 0) {
            throw new RuntimeException(String.format("initUI Exception" ));
        }
    }

    public void initView() {

//        if(mLeftMenuView == null && mLeftMenuViewId != View.NO_ID) {
//            mLeftMenuView = this.findViewById(mLeftMenuViewId);
//            mLeftMenuView.setVisibility(GONE);
//        }

        if(mRightMenuView == null && mRightMenuViewId != View.NO_ID) {
            mRightMenuView = this.findViewById(mRightMenuViewId);
        }

        if(mContextView == null && mContextViewId != View.NO_ID)
            mContextView = this.findViewById(mContextViewId);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnMenuClickListener != null) {
                    mOnMenuClickListener.onItemClick(v, mPosition);
                }
            }
        });
    }

    private void menuViewShow(int dis) {
//        if(mLeftMenuView != null && dis > 0) {
//            mLeftMenuView.setVisibility(View.GONE);
//        }

        if(mRightMenuView != null && dis < 0) {
            mRightMenuView.setVisibility(View.VISIBLE);
        }

        mMenuShow = true;
    }

    private void menuViewHide() {
 /*       if(mLeftMenuView != null ) {
            mLeftMenuView.setVisibility(View.INVISIBLE);
        }*/
        if(mRightMenuView != null) {
            mRightMenuView.setVisibility(View.INVISIBLE);
        }
        mMenuShow = false;
    }

    public void setOnMenuClickListener(OnMenuClickListener  listener) {
       mOnMenuClickListener = listener;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public void setAccountType (String accountType) {
        this.accountType = accountType;
    }

    public int getPosition() {
        return mPosition;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();

//                if(mLeftMenuView != null)
//                    mLeftMargin = mLeftMenuView.getWidth();

                if(mRightMenuView != null)
                    mRightMargin = mRightMenuView.getWidth();

                if(mSlideView != null && mSlideView.isMenuOpen()) {//this != mSlideView &&
                mSlideView.closeMenu();
                event.setAction(MotionEvent.ACTION_CANCEL);
            }

                super.onTouchEvent(event);
                return true;
            case MotionEvent.ACTION_MOVE:
//                if(mSlideView != null) {//this != mSlideView && //添加这句话的意思是成员，不能进行加减所以，就不能滑动SwipeMenuLayout布局。
//                    mSlideView.closeMenu();
//                   break;
//                }

                //异步发送数据
//                switch (accountType) {
//                    case "1":
//
//                        break;//    break;//主机，业主-写死
//                    case "2":
//                        if (mSlideView != null) {//this != mSlideView && //添加这句话的意思是成员，不能进行加减所以，就不能滑动SwipeMenuLayout布局。
//                            mSlideView.closeMenu();
//                        }
//                        break;//从机，成员
//                }

                if (accountType.equals("1")) {

                } else {
                    if (mSlideView != null) {//this != mSlideView && //添加这句话的意思是成员，不能进行加减所以，就不能滑动SwipeMenuLayout布局。
                        mSlideView.closeMenu();
                    }
                    break;
                }

                int dx = (int) (event.getX() - mDownX);
                Log.e("robin debug","dx:" + dx);
                Log.e("robin debug", "Event ACTION_DOWN mMenuShow:" + mMenuShow);

                if (!mMenuShow) {//解决slidemenu和SwipeMenuLayout滑动冲突
                    if (mOnMenuClickListener != null)
                        mOnMenuClickListener.onInterceptTouch_end();
                } else {
                    if (mOnMenuClickListener != null)
                        mOnMenuClickListener.onInterceptTouch();
                }
                if(Math.abs(dx) < mTouchSlop)
                    break;
                if (dx > 0){//加这一句话解决SwipeMenuLayout 与SlideMenu的滑动冲突
                    return true;
                }
                if(!mMenuShow) {
                    menuViewShow(dx);
                    getParent().requestDisallowInterceptTouchEvent(true);
                    mSlideView = this;
                }  else if (mMenuShow && dx < 0 && mRightMenuView.getVisibility() == View.INVISIBLE) {
                    menuViewHide();
                    menuViewShow(dx);
                }

//                if(dx > 0 && mLeftMenuView == null) break;
                if(dx < 0 && mRightMenuView == null) break;

                if (dx < 0 && dx < -mRightMargin) {
                    dx = -mRightMargin;
                }

                layoutContextView(dx);
                Log.d(TAG, "Event ACTION_MOVE mMenuShow:" + mMenuShow + " dx" + dx);
                if(mMenuShow)
                    event.setAction(MotionEvent.ACTION_CANCEL);

                return super.onTouchEvent(event);
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "Event ACTION_CANCEL mMenuShow:" + mMenuShow);
            case MotionEvent.ACTION_UP:
                int dis = mContextView.getLeft();
                /**
                 *  dis > 0, move to right, dis < mLeftMargin/2, close menu, dis < mLeftMargin open menu.
                 *  dis < 0, move to left, dis > -mRightMargin/2, close menu, dis > -mRightMargin, open menu
                 */
                if(dis < 0 && mRightMenuView != null) {
                    if(dis > -mRightMargin/2) {
                        mScroller.startScroll(dis, 0, -dis, 0, mScrollTime);  //close
                    } else if(dis > -mRightMargin) {
                        mScroller.startScroll(dis, 0, -mRightMargin-dis, 0, mScrollTime);
                    }

                    postInvalidate();
                }

                mDownX = 0;
                Log.d(TAG, "Event ACTION_UP mMenuShow:" + mMenuShow);
                if(mMenuShow)
                    event.setAction(MotionEvent.ACTION_CANCEL);
                return super.onTouchEvent(event);
            default:
                return super.onTouchEvent(event);
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initView();
        menuViewHide();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();

        if(mScroller.computeScrollOffset()) {
            layoutContextView(mScroller.getCurrX());
            postInvalidate();
        }
    }

    private void layoutContextView(int dx) {
        if(mContextView != null)
            mContextView.layout(dx, 0, mContextView.getMeasuredWidth()+dx, mContextView.getMeasuredHeight());

        if(!mResterListener && (dx == -mRightMargin)) {
            Log.d(TAG, "registerListener dx:" + dx);
            registerListener(dx);
        } else if (mResterListener && dx == 0) {
            Log.d(TAG, "unregisterListener dx:" + dx);
            unregisterListener(dx);
        }
    }

    public boolean isMenuOpen() {
        int dis = mContextView.getLeft();
        if(dis == -mRightMargin )
            return true;

        return false;
    }

    public  void closeMenu () {
        while(mScroller.computeScrollOffset()) {
            mScroller.abortAnimation();
        }
        layoutContextView(0);
        menuViewHide();
    }

    private void  registerListener(int dis) {
//        if(mLeftMenuView != null && dis == mLeftMargin) {
//            mLeftMenuView.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mOnMenuClickListener != null)
//                        mOnMenuClickListener.onMenuClick(v, mPosition);
//                }
//            });
//        }

        if(mRightMenuView != null && dis == -mRightMargin) {
            mRightMenuView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnMenuClickListener != null)
                        mOnMenuClickListener.onMenuClick(v, mPosition);
                }
            });
        }

        mResterListener = true;
    }

    private void unregisterListener(int dis) {
//        if(mLeftMenuView != null && dis > 0) {
//            mLeftMenuView.setOnClickListener(null);
//        }

        if(mRightMenuView != null && dis < 0) {
            mRightMenuView.setOnClickListener(null);
        }

        mResterListener = false;
    }

    public static void clearSideView() {
        if (mSlideView != null) {
            mSlideView = null;
        }
    }

    public interface OnMenuClickListener {

        void onMenuClick(View v, int position);

        void onItemClick(View v, int position);
        void onInterceptTouch ();

        void onInterceptTouch_end ();

    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        if (mMenuShow) {
//            Log.e("zhu","onInterceptTouchEvent:"+ mMenuShow);
//            //在这里拦截slidemenu的事件
//            if (mOnMenuClickListener != null)
//                mOnMenuClickListener.onInterceptTouch_end();
//            return  true;
//        }
//        if (mOnMenuClickListener != null)
//                mOnMenuClickListener.onInterceptTouch();
//        return false;
//    }
}
