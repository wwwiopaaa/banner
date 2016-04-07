package com.chenjl.banner.widget;

import android.content.Context;
import android.util.AttributeSet;

public class TextIndicatorBannerView<T> extends BannerView<T> {

    public TextIndicatorBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setIndicatorPosition(POSITION_BOTTOM_RIGHT);
    }

    @Override
    protected Indicator generationIndicatorView(Context context, AttributeSet attrs) {
        return new IndicatorTextView(context, attrs);
    }
}
