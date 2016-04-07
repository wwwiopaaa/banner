package com.chenjl.banner.widget;

import com.chenjl.banner.utils.DensityUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

/**
 * 
 * IndicatorView
 * 
 * @author chenjl 2015-09-23
 *
 */
public class IndicatorView extends View implements Indicator {

    public static final int DEFAULT_HORIZONAL_SPACE = 10;
    public static final int DEFAULT_RADIUS_SIZE = 5;
    public static final int DEFAULT_STROKE_WIDTH = 2;

    private Paint mPaint = null;
    private Paint mEdgePaint = null;
    private int mCurrentIndex = -1;
    private int mIndicatorSize = 0;
    private int mHorizonalSpace;
    private int mRadius;
    private int mStrokeWidth;

    private int mSelectedColor = Color.argb(255, 230, 20, 20);
    private int mNormalColor = Color.argb(255, 200, 200, 200);

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initAttrs(context, attrs);

        initPaint();
    }

    /**
     * 初始化尺寸
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        mHorizonalSpace = DensityUtil.dip2px(context, DEFAULT_HORIZONAL_SPACE);
        mRadius = DensityUtil.dip2px(context, DEFAULT_RADIUS_SIZE);
        mStrokeWidth = DensityUtil.dip2px(context, DEFAULT_STROKE_WIDTH);
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Style.FILL);
        mPaint.setStrokeWidth(mStrokeWidth);// 设置画笔宽度

        mEdgePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mEdgePaint.setARGB(255, 255, 255, 255);
        mEdgePaint.setStyle(Style.STROKE);
        mEdgePaint.setStrokeWidth(mStrokeWidth);// 设置画笔宽度
    }

    @Override
    public void setCurrentIndex(int index) {
        this.mCurrentIndex = index;
        invalidate();
    }

    @Override
    public int getCurrentIndex() {
        return this.mCurrentIndex;
    }

    @Override
    public int getIndicatorSize() {
        return this.mIndicatorSize;
    }

    public void setHorizonalSpace(int space) {
        this.mHorizonalSpace = space;
        invalidate();
    }

    @Override
    public void setIndicatorSize(int size) {
        this.mIndicatorSize = size;
        if (size <= 1) {
            if (getVisibility() != View.INVISIBLE) {
                setVisibility(View.INVISIBLE);
            }
        } else {
            if (getVisibility() != View.VISIBLE) {
                setVisibility(View.VISIBLE);
            }
        }
        requestLayout();
    }

    public void setRadius(int px) {
        mRadius = px;
        invalidate();
    }

    public void setCircleEdgeColor(int color) {
        mEdgePaint.setColor(color);
        invalidate();
    }

    public void setCircleSelectedColor(int color) {
        mSelectedColor = color;
        invalidate();
    }

    public void setCircleColor(int color) {
        mNormalColor = color;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int y = getMeasuredHeight() / 2;
        int x = mStrokeWidth / 2 + getPaddingLeft();
        for (int i = 0; i < mIndicatorSize; i++) {
            x += mRadius;
            // 外圆
            canvas.drawCircle(x, y, mRadius, mEdgePaint);

            // 内圆
            mPaint.setColor(isCurrentIndex(i) ? mSelectedColor : mNormalColor);
            canvas.drawCircle(x, y, mRadius, mPaint);
            
            x += mHorizonalSpace + mStrokeWidth / 2;
        }
    }

    private boolean isCurrentIndex(int index) {
        return mCurrentIndex == index;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredLength(widthMeasureSpec, true),
                getMeasuredLength(heightMeasureSpec, false));
    }

    private int getMeasuredLength(int length, boolean isWidth) {
        int specMode = MeasureSpec.getMode(length);
        int specSize = MeasureSpec.getSize(length);
        int size;
        int padding =
                isWidth ? getPaddingLeft() + getPaddingRight() : getPaddingTop()
                        + getPaddingBottom();
        if (specMode == MeasureSpec.EXACTLY) {
            size = specSize;
        } else {
            if (isWidth) {
                size =
                        padding
                                + (mIndicatorSize > 0
                                        ? (mRadius + mStrokeWidth / 2 + mIndicatorSize
                                                * (mRadius + mStrokeWidth / 2) + (mIndicatorSize - 1)
                                                * mHorizonalSpace)
                                        : 0);
            } else {
                size = (mIndicatorSize > 0 ? (mRadius * 2 + mStrokeWidth) : 0) + padding;
            }

            if (specMode == MeasureSpec.AT_MOST) {
                size = Math.min(size, specSize);
            }
        }
        return size;
    }

    static class SavedState extends BaseSavedState {
        int currentPos;
        int size;

        /**
         * Constructor called from {@link ProgressBar#onSaveInstanceState()}
         */
        SavedState(Parcelable superState) {
            super(superState);
        }

        /**
         * Constructor called from {@link #CREATOR}
         */
        private SavedState(Parcel in) {
            super(in);
            currentPos = in.readInt();
            size = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(currentPos);
            out.writeInt(size);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

    @Override
    public Parcelable onSaveInstanceState() {
        // Force our ancestor class to save its state
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);

        ss.currentPos = mCurrentIndex;
        ss.size = mIndicatorSize;

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        setCurrentIndex(ss.currentPos);
        setIndicatorSize(ss.size);
    }
}
