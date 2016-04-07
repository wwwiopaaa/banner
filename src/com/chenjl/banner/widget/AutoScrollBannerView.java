package com.chenjl.banner.widget;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 支持自动滚动
 * 
 * @author chenjl 2016-04-05
 */
public class AutoScrollBannerView<T> extends BannerView<T> implements BannerCallBack.AutoScrollController {

    private static final long TIMER_PERIOD = 3000;
    private static final int SCROLL_DURATION = 600;
    
    private boolean isRunning = false;
    private long mTimerPeriod = TIMER_PERIOD;

    public AutoScrollBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setScrollSpeed(mViewPager, SCROLL_DURATION);
        
        setIndicatorPosition(POSITION_BOTTOM_RIGHT);

        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        startTimerTask();// 开始图片滚动
                        break;
                    default:
                        stopTimerTask();// 停止图片滚动
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void setup(List<T> datas, ImageListener<T> listener) {
        stopTimerTask();
        super.setup(datas, listener);
    }

    @Override
    public void setup(List<T> datas, PagerAdapter adapter) {
        stopTimerTask();
        super.setup(datas, adapter);
    }

    private final Runnable mImageTimerTask = new Runnable() {
        @Override
        public void run() {

            jumpNextPage();

            isRunning = false;

            startTimerTask();
        }
    };

    protected void jumpNextPage() {
        int next = (mViewPager.getCurrentItem() + 1) % mViewPager.getAdapter().getCount();
        mViewPager.setCurrentItem(next, next != 0);
    }

    private void startTimerTask() {
        if (!isRunning) {
            PagerAdapter adapter = mViewPager.getAdapter();
            if (adapter == null || adapter.getCount() <= 1) {
                return;
            }
            isRunning = true;
            postDelayed(mImageTimerTask, mTimerPeriod);
        }
    }

    private void stopTimerTask() {
        if (isRunning) {
            removeCallbacks(mImageTimerTask);
            isRunning = false;
        }
    }

    @Override
    public void pauseAutoScroll() {
        stopTimerTask();
    }

    @Override
    public void startAutoScroll() {
        startTimerTask();
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void setCurrentItem(int index, boolean smoothScroll) {
        final boolean running = isRunning;
        if (running) {
            pauseAutoScroll();
        }
        
        super.setCurrentItem(index, smoothScroll);

        if (running) {
            startAutoScroll();
        }
    }
}
