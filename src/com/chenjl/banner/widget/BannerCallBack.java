package com.chenjl.banner.widget;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.widget.ImageView;

public interface BannerCallBack<T>{
    
    public void setup(List<T> datas, PagerAdapter adapter);
    
    public void setCurrentItem(int index, boolean smoothScroll);

    public interface AutoScrollController{

        public void startAutoScroll();
        
        public void pauseAutoScroll();

        public boolean isRunning();
    }
    
    public interface ImageListener<T> {
        
        public void display(T item, ImageView imageView);

        public void onClick(T item, int postion, View v);
    }
}
