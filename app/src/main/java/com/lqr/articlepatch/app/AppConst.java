package com.lqr.articlepatch.app;


import android.os.Environment;

import com.lqr.articlepatch.util.LogUtils;
import com.lqr.articlepatch.util.SPUtils;
import com.lqr.articlepatch.util.UIUtils;

import java.io.File;

/**
 * @创建者 CSDN_LQR
 * @描述 全局常量类
 */
public class AppConst {

    public static final String APP_NAME = "LQRAriticlePatch";
    public static final String TAG = "CSDN_LQR";
    public static final int DEBUGLEVEL = LogUtils.LEVEL_ALL;//日志输出级别

    //SP的key名常量
    public static final String SETTINGS_DOWNLOAD_FOLDER_NAME = "settings_download_folder_name";

    //下载配置相关
    private static final String DOWNLOAD_FOLDER_NAME = SPUtils.getInstance(UIUtils.getContext()).getString("SETTINGS_DOWNLOAD_FOLDER_NAME", "").equalsIgnoreCase("") ? APP_NAME : SPUtils.getInstance(UIUtils.getContext()).getString("SETTINGS_DOWNLOAD_FOLDER_NAME", "");//下载目录名
    private static final String DOWNLOAD_FOLDER_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + DOWNLOAD_FOLDER_NAME;//默认下载目录
    public static final String VIDEO_DOWNLOAD_FOLDER = DOWNLOAD_FOLDER_PATH + File.separator + "video";//视频下载目录
    public static final String PIC_DOWNLOAD_FOLDER = DOWNLOAD_FOLDER_PATH + File.separator + "pic";//图片下载目录

}
