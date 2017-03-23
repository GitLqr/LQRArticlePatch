package com.lqr.articlepatch.model;

import com.lzy.ninegrid.ImageInfo;

import java.io.File;
import java.util.List;


public class FolderModel {

    private File mFolder;//当前文件夹

    private List<ImageInfo> mImageInfos;//文件夹下所有图片的信息

    public FolderModel(File folder, List<ImageInfo> imageInfos) {
        mFolder = folder;
        mImageInfos = imageInfos;
    }

    public File getFolder() {
        return mFolder;
    }

    public void setFolder(File folder) {
        mFolder = folder;
    }

    public List<ImageInfo> getImageInfos() {
        return mImageInfos;
    }

    public void setImageInfos(List<ImageInfo> imageInfos) {
        mImageInfos = imageInfos;
    }
}
