#Banner （viewpager + indicator）

支持自动滚动、无限滑动、易使用和扩展

1.BannerView 基本BannerView 默认指示器是圆点

2.TextIndicatorBannerView 自定义指示器 文字：1/4 // 指示器有不同需求，可参照TextIndicatorBannerView #generationIndicatorView() 指示器需实现Indicator接口

3.AutoScrollBannerView 可自动滚动

4.AutoScrollCycleBannerView 自动滚动+无限滑动

#使用

mBannerView.setup(List<?>, new ImageListener()); 或 mBannerView.setup(List<?>, new PagerAdapter());
