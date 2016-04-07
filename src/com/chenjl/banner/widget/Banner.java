package com.chenjl.banner.widget;

import java.lang.reflect.Field;

import com.chenjl.banner.utils.DensityUtil;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * 广告Banner
 * 
 * @author chenjl 2016-04-05
 */
public abstract class Banner extends ViewGroup implements OnPageChangeListener {

    private static final int INDICATOR_DEFAULT_MARGIN_DP = 5;

    public static final int POSITION_TOP_LEFT = 1;
    public static final int POSITION_TOP_RIGHT = 2;
    public static final int POSITION_TOP_CENTER = 3;
    public static final int POSITION_BOTTOM_LEFT = 4;
    public static final int POSITION_BOTTOM_RIGHT = 5;
    public static final int POSITION_BOTTOM_CENTER = 6;
    public static final int POSITION_CENTER = 7;

    protected ViewPager mViewPager;
    protected Indicator mIndicator;
    private int mIndicatorPosition;
    private int mIndicatorMargin;
    private OnPageChangeListener mChangeListener;
    
    public Banner(Context context, AttributeSet attrs) {
        super(context, attrs);

        mIndicatorMargin = DensityUtil.dip2px(context, INDICATOR_DEFAULT_MARGIN_DP);

        mViewPager = generationViewPager(context, attrs);
        if (mViewPager == null) {
            throw new IllegalArgumentException("you have to provider a ViewPager");
        }
        mViewPager.setOnPageChangeListener(this);

        this.addView(mViewPager, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));

        mIndicator = generationIndicatorView(context, attrs);
        if (!(mIndicator instanceof View)) {
            throw new IllegalArgumentException("Indicator instance of view");
        }
        this.addView((View) mIndicator, new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));

        mIndicatorPosition = POSITION_BOTTOM_CENTER;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int w = r - l;
        final int h = b - t;

        mViewPager.layout(getPaddingLeft(), getPaddingTop(), w - getPaddingRight(), h
                - getPaddingBottom());
        
        layoutIndicator(w, h);
    }

    private void layoutIndicator(int w, int h) {
        if (mIndicator == null) {
            return;
        }

        final View indicatorView = (View) mIndicator;

        final int width = indicatorView.getMeasuredWidth();
        final int height = indicatorView.getMeasuredHeight();

        int left, top, right, bottom;

        switch (mIndicatorPosition) {
            case POSITION_TOP_LEFT:
                left = mIndicatorMargin;
                right = width + mIndicatorMargin;
                top = mIndicatorMargin;
                bottom = height + mIndicatorMargin;
                break;
            case POSITION_TOP_RIGHT:
                left = w - width - mIndicatorMargin;
                right = w - mIndicatorMargin;
                top = mIndicatorMargin;
                bottom = height + mIndicatorMargin;
                break;
            case POSITION_TOP_CENTER:
                left = (w - width) / 2;
                right = (w + width) / 2;
                top = mIndicatorMargin;
                bottom = height + mIndicatorMargin;
                break;
            case POSITION_BOTTOM_LEFT:
                left = mIndicatorMargin;
                right = width + mIndicatorMargin;
                top = h - height - mIndicatorMargin;
                bottom = h - mIndicatorMargin;
                break;
            case POSITION_BOTTOM_CENTER:
                left = (w - width) / 2;
                right = (w + width) / 2;
                top = h - height - mIndicatorMargin;
                bottom = h - mIndicatorMargin;
                break;
            case POSITION_BOTTOM_RIGHT:
                top = h - height - mIndicatorMargin;
                bottom = h - mIndicatorMargin;
                left = w - width - mIndicatorMargin;
                right = w - mIndicatorMargin;
                break;
            case POSITION_CENTER:
                top = (h - height) / 2;
                bottom = (h + height) / 2;
                left = (w - width) / 2;
                right = (w + width) / 2;
                break;
            default:
                throw new IllegalArgumentException("not found suitable position");
        }
        indicatorView.layout(Math.max(left, 0), Math.max(top, 0), Math.min(right, w),
                Math.min(bottom, h));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        measureChild(mViewPager, widthMeasureSpec, heightMeasureSpec);

        if (mIndicator != null) {
            measureChild((View) mIndicator, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if(mChangeListener != null){
            mChangeListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        if(mChangeListener != null){
            mChangeListener.onPageScrolled(arg0, arg1, arg2);
        }
    }

    @Override
    public void onPageSelected(int index) {
        if(mChangeListener != null){
            mChangeListener.onPageSelected(index);
        }
        mIndicator.setCurrentIndex(index);
    }
    
    public void setOnPageChangeListener(OnPageChangeListener listener){
        mChangeListener = listener;
    }

    public int getIndicatorPosition() {
        return mIndicatorPosition;
    }

    public void setIndicatorPosition(int mIndicatorPosition) {
        this.mIndicatorPosition = mIndicatorPosition;
        requestLayout();
    }

    protected abstract Indicator generationIndicatorView(Context context, AttributeSet attrs);

    protected abstract ViewPager generationViewPager(Context context, AttributeSet attrs);

    /**
     * 使用反射设置滚动时长
     */
    public static void setScrollSpeed(ViewPager viewPager, int duration) {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller =
                    new FixedSpeedScroller(viewPager.getContext(), new AccelerateInterpolator());
            field.set(viewPager, scroller);
            scroller.setmDuration(duration);
        } catch (Exception e) {}
    }

    /**
     * 用于设置滚动时长
     */
    private static class FixedSpeedScroller extends Scroller {

        private int mDuration = 1000;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        public void setmDuration(int time) {
            mDuration = time;
        }

        public int getmDuration() {
            return mDuration;
        }
    }
}
