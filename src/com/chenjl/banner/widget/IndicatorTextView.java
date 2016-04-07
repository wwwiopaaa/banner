package com.chenjl.banner.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.chenjl.banner.R;

public class IndicatorTextView extends TextView implements Indicator {

    private static final String FORMAT_STRING = "%d/%d";
    private static final int DEFAULT_TEXTSIZE = 12;

    private int mCurrentIndex = 0;
    private int mIndicatorSize = 0;

    public IndicatorTextView(Context context, AttributeSet attrs) {
        super(context);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXTSIZE);
        setBackgroundResource(R.drawable.bg_text_indicator);
        setTextColor(Color.WHITE);
    }

    @Override
    public void setCurrentIndex(int index) {
        mCurrentIndex = index;
        setText(String.format(FORMAT_STRING, mCurrentIndex + 1, mIndicatorSize));
    }

    @Override
    public int getCurrentIndex() {
        return mCurrentIndex;
    }

    @Override
    public int getIndicatorSize() {
        return mIndicatorSize;
    }

    @Override
    public void setIndicatorSize(int size) {
        mIndicatorSize = size;
        setText(String.format(FORMAT_STRING, mCurrentIndex + 1, mIndicatorSize));
    }

}
