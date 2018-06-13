package com.Util.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Looper;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.massky.sraum.R;


/**
 * Author: Owen Chan
 * DATE: 2017-03-02.
 */

public class SlideSwitchButton extends View {

    private int widthSize = 280;
    private int heightSize = 140;

    private int mInnerRadius;
    private int mOutRadius;

    private int themeColor;
    private boolean isOpen;

    private static final int PADDING = 6;

    private Paint mPaint;
    private int mAlpha;

    private RectF mOutRect = new RectF();

    private int changingValue;
    private int maxValue;
    private int minValue;
    private int mStartValue = PADDING;

    private int eventStartX;
    private boolean isRunning = false;

    private SlideListener listener;
    private SlideSwitch slideswitch;

    public SlideSwitchButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideSwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        listener = null;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.slideswitch);
        if (typedArray != null) {
            themeColor = typedArray.getColor(R.styleable.slideswitch_themeColor, Color.parseColor("#E0C287"));
            isOpen = typedArray.getBoolean(R.styleable.slideswitch_isOpen, false);
            typedArray.recycle();
        }
    }

    /**
     * 测量控件的大小, 如果是Wrap content 取默认值
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int withModel = MeasureSpec.getMode(widthMeasureSpec);
        int heightModel = MeasureSpec.getMode(heightMeasureSpec);

        if (withModel == MeasureSpec.EXACTLY) {// MeasureSpec.EXACTLY是width = "30dp" ,height = "60dp"
            widthSize = width; //
        }
        if (heightModel == MeasureSpec.EXACTLY) {
            heightSize = height;
        }
        setMeasuredDimension(widthSize, heightSize);
        initValue();
    }

    public void initValue() {
        mInnerRadius = (heightSize - 2 * PADDING) / 2;
        mOutRadius = heightSize / 2;

        minValue = PADDING;
        maxValue = widthSize - 2 * mInnerRadius - PADDING;
        if (isOpen) {
            changingValue = maxValue;
            mAlpha = 255;
        } else {
            changingValue = PADDING;
            mAlpha = 0;
        }
        mStartValue = changingValue;
    }

    /**
     * 绘制包括两部分 一部分是底部的背景
     * 第二部分是上层滑动的圆
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //绘制底层背景
        mPaint.setColor(Color.GRAY);
        mOutRect.set(0, 0, widthSize, heightSize);
        canvas.drawRoundRect(mOutRect, mOutRadius, mOutRadius, mPaint);
        //绘制改变透明度后的背景
        mPaint.setColor(themeColor);
        mPaint.setAlpha(mAlpha);//mPaint
        canvas.drawRoundRect(mOutRect, mOutRadius, mOutRadius, mPaint);
        //绘制滑动的圆圈
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(changingValue + mInnerRadius, PADDING + mInnerRadius, mInnerRadius, mPaint);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent e) {
//        int y = (int) e.getRawY();
//        int x = (int) e.getRawX();
//        boolean resume = false;
//        switch (e.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                // 发生down事件时,记录y坐标
//                mLastMotionY = y;
//                mLastMotionX = x;
//                resume = false;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                // deltaY > 0 是向下运动,< 0是向上运动
//                int deltaY = y - mLastMotionY;
//                int deleaX = x - mLastMotionX;
//
//                if (Math.abs(deleaX) > Math.abs(deltaY)) {
//                    resume = false;
//                } else {
//                    //当前正处于滑动
//                    if (isRefreshViewScroll(deltaY)) {
//                        resume = true;
//                    }
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                break;
//        }
//        return resume;
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isRunning) {//这句话的意思是当SlideSwitchButton运动时，事件传递给父View
            super.onTouchEvent(event);
        }
        if(slideswitch != null)
        slideswitch.slide_switch();//说明slideSwitch在滑动
        int action = MotionEventCompat.getActionMasked(event);
        getParent().requestDisallowInterceptTouchEvent(true);//解决了子View拦截父view的事件
        //SlideSwitchButton和ViewPager的滑动冲突
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                eventStartX = (int) event.getRawX();//手指点到屏幕最左边
                break;
            case MotionEvent.ACTION_MOVE:
                int eventLastX = (int) event.getRawX();
                int distance = eventLastX - eventStartX;// -
                int movePoint = distance + mStartValue;
                movePoint = (movePoint > maxValue ? maxValue : movePoint);
                movePoint = (movePoint < minValue ? minValue : movePoint);
                if (movePoint >= minValue && movePoint <= maxValue) {
                    changingValue = movePoint;
                    mAlpha = (int) (255 * (float) movePoint / (float) maxValue);
                    invalidateView();
                }
                break;
            case MotionEvent.ACTION_UP:
                int wholeX = (int) (event.getRawX() - eventStartX);
                mStartValue = changingValue;
                boolean toRight;
                toRight = (mStartValue > maxValue / 2);
                if (Math.abs(wholeX) < 3) {
                    toRight = !toRight;
                }
                startAnimator(toRight);
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 自动向左或者向右滑动动画
     */
    public void startAnimator(final boolean toRight) {
        ValueAnimator toDestAnim = ValueAnimator.ofInt(changingValue, toRight ? maxValue : minValue);
        toDestAnim.setDuration(300);
        toDestAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        toDestAnim.start();
        toDestAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                changingValue = (Integer) animation.getAnimatedValue();
                mAlpha = (int) (255 * (float) changingValue / (float) maxValue);
                invalidateView();
            }
        });

        toDestAnim.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                isRunning = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isRunning = false;
                if (toRight) {
                    isOpen = true;
                    if (listener != null) {
                        listener.openState(true, SlideSwitchButton.this);
                    }
                    mStartValue = maxValue;
                } else {
                    isOpen = false;
                    if (listener != null) {
                        listener.openState(false, SlideSwitchButton.this);
                    }
                    mStartValue = minValue;
                }
            }
        });
    }

    /**
     * 改名开关状态
     */
    public void changeOpenState(boolean isOpen) {
        this.isOpen = isOpen;
        initValue();
        invalidateView();
        if (listener != null) {
            if (isOpen) {
                listener.openState(true, SlideSwitchButton.this);
            } else {
                listener.openState(false, SlideSwitchButton.this);
            }
        }
    }

    /**
     * 重新绘制，如果是UI线程调用invalidate()
     * 否则调用postInvalidate();
     */
    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    public void setSlideListener(SlideListener listener) {
        this.listener = listener;
    }

    public interface SlideListener {
        void openState(boolean isOpen, View view);
    }

    public void setSlideSwitchListener (SlideSwitch slideswitch) {
        this.slideswitch =  slideswitch;
    }

    public interface   SlideSwitch {
        void  slide_switch();
    }
}
