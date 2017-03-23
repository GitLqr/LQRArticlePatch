package com.lqr.articlepatch.app;

import com.liulishuo.filedownloader.FileDownloader;
import com.lqr.articlepatch.app.base.BaseApp;
import com.lqr.articlepatch.imageloader.GlideImageLoader;
import com.lzy.ninegrid.NineGridView;

import org.litepal.LitePal;

/**
 * @创建者 CSDN_LQR
 * @描述 BaseApp的拓展，用于设置其他第三方的初始化
 */
public class MyApp extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();
        FileDownloader.init(getContext());
        LitePal.initialize(this);
        NineGridView.setImageLoader(new GlideImageLoader());
    }

}
