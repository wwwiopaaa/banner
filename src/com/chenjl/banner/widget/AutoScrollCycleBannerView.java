package com.chenjl.banner.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * 支持无限滑动
 * 
 * @author chenjl 2016-04-06
 */
public class AutoScrollCycleBannerView<T> extends AutoScrollBannerView<T> {

    public AutoScrollCycleBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setIndicatorPosition(POSITION_BOTTOM_CENTER);
    }

    @Override
    protected ViewPager generationViewPager(Context context, AttributeSet attrs) {
        return new CycleViewPager(context, attrs);
    }

    @Override
    protected void jumpNextPage() {

        int next = mViewPager.getCurrentItem() + 1;
        if (next == mViewPager.getAdapter().getCount() + 1) {
            next = 1;
        }

        mViewPager.setCurrentItem(next, next != 1);
    }
}
