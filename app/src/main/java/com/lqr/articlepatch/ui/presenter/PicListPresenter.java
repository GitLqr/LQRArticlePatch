package com.lqr.articlepatch.ui.presenter;

import android.view.View;

import com.lqr.adapter.LQRAdapterForRecyclerView;
import com.lqr.adapter.LQRViewHolderForRecyclerView;
import com.lqr.articlepatch.R;
import com.lqr.articlepatch.app.AppConst;
import com.lqr.articlepatch.model.FolderModel;
import com.lqr.articlepatch.ui.base.BaseActivity;
import com.lqr.articlepatch.ui.base.BasePresenter;
import com.lqr.articlepatch.ui.view.IPicListFgView;
import com.lqr.articlepatch.util.MediaFileUtils;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PicListPresenter extends BasePresenter<IPicListFgView> {

    private List<FolderModel> mData = new ArrayList<>();
    private LQRAdapterForRecyclerView<FolderModel> mAdapter;
    private SimpleDateFormat mSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public PicListPresenter(BaseActivity context) {
        super(context);
    }

    public void loadPicFolderList() {
        loadData();
        setAdapter();
    }

    private void loadData() {
        mData.clear();
        File file = new File(AppConst.PIC_DOWNLOAD_FOLDER);
        if (file != null) {
            File[] folders = file.listFiles();//所有图片文件夹
            if (folders != null && folders.length > 0) {
                for (File folder : folders) {
                    File[] pics = folder.listFiles();//一个图片文件夹中所有的图片文件
                    if (pics != null && pics.length > 0) {
                        List<ImageInfo> infos = new ArrayList<>();
                        for (File pic : pics) {
                            if (MediaFileUtils.isImageFileType(pic.getAbsolutePath())) {
                                ImageInfo imageInfo = new ImageInfo();
                                String url = "file://" + pic.getAbsolutePath();
                                imageInfo.setBigImageUrl(url);
                                imageInfo.setThumbnailUrl(url);
                                infos.add(imageInfo);
                            }
                        }
                        mData.add(new FolderModel(folder, infos));
                    }
                }
            }
        }

        if (mData.size() > 0) {
            getView().getRvPic().setVisibility(View.VISIBLE);
            getView().getTvNoDataTip().setVisibility(View.GONE);
        } else {
            getView().getRvPic().setVisibility(View.GONE);
            getView().getTvNoDataTip().setVisibility(View.VISIBLE);
        }
    }

    private void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new LQRAdapterForRecyclerView<FolderModel>(mContext, mData, R.layout.item_pic_list) {
                @Override
                public void convert(LQRViewHolderForRecyclerView helper, FolderModel item, int position) {
                    helper.setText(R.id.tvFolderName, item.getFolder().getName())
                            .setText(R.id.tvDate, mSdf.format(item.getFolder().lastModified()));

                    NineGridView ngv = helper.getView(R.id.ngv);
                    ngv.setAdapter(new NineGridViewClickAdapter(mContext, item.getImageInfos()));
                }
            };
        }
        getView().getRvPic().setAdapter(mAdapter);
    }
}
