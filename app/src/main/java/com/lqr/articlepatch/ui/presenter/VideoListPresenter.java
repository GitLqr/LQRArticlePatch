package com.lqr.articlepatch.ui.presenter;

import android.view.View;
import android.widget.ImageView;

import com.lqr.adapter.LQRAdapterForRecyclerView;
import com.lqr.adapter.LQRViewHolderForRecyclerView;
import com.lqr.articlepatch.R;
import com.lqr.articlepatch.app.AppConst;
import com.lqr.articlepatch.ui.base.BaseActivity;
import com.lqr.articlepatch.ui.base.BasePresenter;
import com.lqr.articlepatch.ui.view.IVideoListFgView;
import com.lqr.articlepatch.util.FileOpenUtils;
import com.lqr.articlepatch.util.MediaFileUtils;
import com.lqr.articlepatch.util.StringUtils;
import com.lqr.articlepatch.util.VideoThumbLoader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.fanrunqi.swipelayoutlibrary.SwipeLayout;

/**
 * Created by lqr on 2017/3/12.
 */

public class VideoListPresenter extends BasePresenter<IVideoListFgView> {

    private List<File> mData = new ArrayList<>();
    private LQRAdapterForRecyclerView<File> mAdapter;
    private SimpleDateFormat mSdf = new SimpleDateFormat("yyyy年MM月dd日");
    private Set<SwipeLayout> mSwipeLayoutSet = new HashSet<>();

    public VideoListPresenter(BaseActivity context) {
        super(context);
    }

    public void loadVideoList() {
        loadData();
        setAdapter();
    }

    private void loadData() {
        File folder = new File(AppConst.VIDEO_DOWNLOAD_FOLDER);
        if (folder.exists()) {
            mData.clear();

            //得到下载目录下所有的视频文件
            File[] files = folder.listFiles();
            if (files != null)
                for (File file : files) {
                    if (MediaFileUtils.isVideoFileType(file.getAbsolutePath())) {
                        mData.add(file);
                    }
                }

            if (mData.size() > 0) {
                getView().getRvVideos().setVisibility(View.VISIBLE);
                getView().getTvNoDataTip().setVisibility(View.GONE);
            } else {
                getView().getRvVideos().setVisibility(View.GONE);
                getView().getTvNoDataTip().setVisibility(View.VISIBLE);
            }
        }
    }

    private void setAdapter() {
        //设置列表
        if (mAdapter == null) {
            mAdapter = new LQRAdapterForRecyclerView<File>(mContext, mData, R.layout.item_video_list) {
                @Override
                public void convert(LQRViewHolderForRecyclerView helper, File item, int position) {
                    helper.setText(R.id.tvTitle, item.getName())
                            .setText(R.id.tvSize, StringUtils.formatFileSize(item.length()))
                            .setText(R.id.tvDate, mSdf.format(item.lastModified()));

                    ImageView ivPic = helper.getView(R.id.ivPic);
                    VideoThumbLoader.getInstance().showThumb(item.getAbsolutePath(), ivPic, 70, 50);

                    //当一个SwipeLayout被滑动出来的时候，其他SwipeLayout收起来
                    SwipeLayout swipeLayout = helper.getView(R.id.swipeLayout);
                    mSwipeLayoutSet.add(swipeLayout);
                    swipeLayout.setOnTouchListener((v, event) -> {
                        for (SwipeLayout sl : mSwipeLayoutSet) {
                            if (sl != swipeLayout)
                                SwipeLayout.removeSwipeView(sl);
                        }
                        return false;
                    });

                    helper.getView(R.id.btnDelete).setOnClickListener(
                            v -> mContext.showMaterialDialog(null, "确定删除吗?", "确定", "取消",
                                    v1 -> {
                                        item.delete();
                                        mData.remove(item);
                                        closeAllSwipeLayout();
                                        setAdapter();
                                        mContext.hideMaterialDialog();
                                    },
                                    v2 -> mContext.hideMaterialDialog()));

                    //条目的点击事件
                    helper.getView(R.id.llVideo).setOnClickListener(v -> FileOpenUtils.openFile(mContext, item.getAbsolutePath()));
                }
            };
        }
        getView().getRvVideos().setAdapter(mAdapter);
    }

    public void closeAllSwipeLayout() {
        for (SwipeLayout sl : mSwipeLayoutSet) {
            SwipeLayout.removeSwipeView(sl);
        }
    }
}
