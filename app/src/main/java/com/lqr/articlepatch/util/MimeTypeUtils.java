package com.lqr.articlepatch.util;

import android.webkit.MimeTypeMap;

/**
 * @创建者 CSDN_LQR
 * @描述 MimeType工具类
 */
public class MimeTypeUtils {

    private MimeTypeUtils() {
    }

    public static String getMimeType(final String fileName) {
        String result = "application/octet-stream";
        int extPos = fileName.lastIndexOf(".");
        if (extPos != -1) {
            String ext = fileName.substring(extPos + 1);
            result = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
        }
        return result;
    }
}