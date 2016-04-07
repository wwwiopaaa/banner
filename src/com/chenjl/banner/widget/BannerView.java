package com.chenjl.banner.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * BannerView
 * 
 * @author chenjl 2016-04-07
 */
public class BannerView<T> extends Banner implements BannerCallBack<T>{

    private PagerAdapter mAdapter;

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setup(List<T> datas, ImageListener<T> listener) {
        setup(datas, gennerationAdapter(datas, listener));
    }

    @Override
    public void setup(List<T> datas, PagerAdapter adapter) {
        if (adapter == null) {
            throw new IllegalArgumentException("adapter is null");
        }

        mIndicator.setIndicatorSize(datas != null ? datas.size() : 0);
        mIndicator.setCurrentIndex(0);

        mAdapter = adapter;
        mViewPager.setAdapter(mAdapter);
    }

    @Override
    public void setCurrentItem(int index, boolean smoothScroll) {
        if (index < 0 || index >= mViewPager.getAdapter().getCount()) {
            throw new IllegalArgumentException("over index");
        }
        mViewPager.setCurrentItem(index, smoothScroll);
    }
    
    protected PagerAdapter gennerationAdapter(List<T> datas, ImageListener<T> listener) {
        return new BannerAdapter<T>(getContext(), datas, listener);
    }

    protected ViewPager generationViewPager(Context context, AttributeSet attrs) {
        return new ViewPager(context, attrs);
    };

    @Override
    protected Indicator generationIndicatorView(Context context, AttributeSet attrs) {
        return new IndicatorView(context, attrs);
    }

    public void setIndicatorHorizonalSpace(int space){
        if(mIndicator instanceof IndicatorView){
            ((IndicatorView)mIndicator).setHorizonalSpace(space);
        }
    }
    
    public void setIndicatorRadius(int px){
        if(mIndicator instanceof IndicatorView){
            ((IndicatorView)mIndicator).setRadius(px);
        }
    }
    
    public void setIndicatorColor(int edge, int seleted, int normal){
        if(mIndicator instanceof IndicatorView){
            IndicatorView indicatorView = (IndicatorView)mIndicator;
            indicatorView.setCircleEdgeColor(edge);
            indicatorView.setCircleColor(normal);
            indicatorView.setCircleSelectedColor(seleted);
        }
    }
    
    private static class BannerAdapter<T> extends PagerAdapter implements OnClickListener {
        private List<T> mAdList;

        private ArrayList<ImageView> mImageViewCacheList;

        private ImageListener<T> mImageCycleViewListener;

        private Context mContext;

        public BannerAdapter(Context context, List<T> datas, ImageListener<T> imageCycleViewListener) {
            mContext = context;
            mAdList = datas;
            mImageCycleViewListener = imageCycleViewListener;
            mImageViewCacheList = new ArrayList<ImageView>();
        }

        @Override
        public int getCount() {
            return mAdList != null ? mAdList.size() : 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView imageView = null;
            if (mImageViewCacheList.isEmpty()) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                imageView = mImageViewCacheList.remove(0);
            }
            // 设置图片点击监听
            imageView.setOnClickListener(this);
            imageView.setTag(position);
            container.addView(imageView);
            if (mImageCycleViewListener != null) {
                mImageCycleViewListener.display(mAdList.get(position), imageView);
            }
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ImageView view = (ImageView) object;
            container.removeView(view);
            mImageViewCacheList.add(view);
        }

        @Override
        public void onClick(View v) {
            Integer pos = (Integer) v.getTag();
            if (pos != null && mImageCycleViewListener != null) {
                mImageCycleViewListener.onClick(mAdList.get(pos), pos, v);
            }
        }
    }
}
