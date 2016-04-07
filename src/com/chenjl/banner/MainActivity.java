package com.chenjl.banner;

import java.util.ArrayList;
import java.util.List;

import com.chenjl.banner.widget.AutoScrollBannerView;
import com.chenjl.banner.widget.AutoScrollCycleBannerView;
import com.chenjl.banner.widget.BannerCallBack.ImageListener;
import com.chenjl.banner.widget.BannerView;
import com.chenjl.banner.widget.TextIndicatorBannerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static List<Integer> mPictures;

    static {
        mPictures = new ArrayList<Integer>();
        mPictures.add(R.drawable.ic_picture_01);
        mPictures.add(R.drawable.ic_picture_02);
        mPictures.add(R.drawable.ic_picture_03);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // BannerView
        BannerView<Integer> mBannerView = (BannerView<Integer>) findViewById(R.id.bannerview);
        mBannerView.setup(mPictures, mListener);
        // mBannerView.setup(datas, adapter);//亦可使用PageAdapter
        // mBannerView.setIndicatorPosition(Banner.POSITION_BOTTOM_CENTER);//设置指示器位置
        // mBannerView.setIndicatorHorizonalSpace(space);//设置指示器间隙距离
        // mBannerView.setIndicatorRadius(px);//设置指示器原点大小
        // mBannerView.setIndicatorRadius(px);//设置指示器原点大小
        // mBannerView.setIndicatorColor(edge, seleted, normal);//设置指示器颜色

        // 指示器有不同需求：TextIndicatorBannerView
        // 可参照TextIndicatorBannerView #generationIndicatorView()  指示器需实现Indicator接口
        TextIndicatorBannerView<Integer> mBannerViewText =
                (TextIndicatorBannerView<Integer>) findViewById(R.id.bannerview_textindicator);
        mBannerViewText.setup(mPictures, mListener);

        // 可自动滚动 ：AutoScrollBannerView
        AutoScrollBannerView<Integer> mAutoBannerView =
                (AutoScrollBannerView<Integer>) findViewById(R.id.bannerview_autoscroll);
        mAutoBannerView.setup(mPictures, mListener);
        mAutoBannerView.startAutoScroll();
        // mAutoBannerView.pauseAutoScroll();

        // 自动滚动+无限滑动： AutoScrollCycleBannerView
        AutoScrollCycleBannerView<Integer> mAutoCycleBannerView =
                (AutoScrollCycleBannerView<Integer>) findViewById(R.id.bannerview_autoscroll_cycle);
        mAutoCycleBannerView.setup(mPictures, mListener);
        mAutoCycleBannerView.startAutoScroll();
    }

    private ImageListener<Integer> mListener = new ImageListener<Integer>() {

        @Override
        public void onClick(Integer item, int postion, View v) {
            Toast.makeText(MainActivity.this, "item click :" + item, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void display(Integer item, ImageView imageView) {
            imageView.setImageResource(item);
        }
    };
}
