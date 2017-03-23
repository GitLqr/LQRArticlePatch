package com.lqr.articlepatch.ui.presenter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadQueueSet;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.lqr.adapter.LQRAdapterForRecyclerView;
import com.lqr.adapter.LQRViewHolderForRecyclerView;
import com.lqr.articlepatch.R;
import com.lqr.articlepatch.app.AppConst;
import com.lqr.articlepatch.core.PicPathDecoder;
import com.lqr.articlepatch.core.VideoPathDecoder;
import com.lqr.articlepatch.model.TaskModel;
import com.lqr.articlepatch.model.Video;
import com.lqr.articlepatch.ui.base.BaseActivity;
import com.lqr.articlepatch.ui.base.BasePresenter;
import com.lqr.articlepatch.ui.listener.MyFileDownloadListener;
import com.lqr.articlepatch.ui.view.IMainAtView;
import com.lqr.articlepatch.util.FileUtils;
import com.lqr.articlepatch.util.LogUtils;
import com.lqr.articlepatch.util.UIUtils;
import com.lqr.articlepatch.widget.CustomDialog;
import com.lqr.recyclerview.LQRRecyclerView;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainPresenter extends BasePresenter<IMainAtView> {

    private LQRAdapterForRecyclerView<Video> mAdapter;
    private LQRRecyclerView mRvVideos;
    private List<Video> mData;
    private CustomDialog mDialog;
    private View mView;

    public MainPresenter(BaseActivity context) {
        super(context);
    }

    public void getShareUrlAndParse() {
        String shareUrl = mContext.getIntent().getStringExtra(Intent.EXTRA_TEXT);
        if (!TextUtils.isEmpty(shareUrl)) {
            parseUrl(shareUrl);
        }
    }

    public void parseUrl(String shareUrl) {
        //取出分享地址（或网页地址）
        LogUtils.sf("share url = " + shareUrl);
        Pattern pattern = Pattern.compile("【(.+)】\\n(http.+)");//从今日头条app转发链接过来的内容匹配规则
        Matcher matcher = pattern.matcher(shareUrl);
        if (matcher.find()) {
//            String title = matcher.group(1);
            beginParse(matcher.group(2));
        } else if (shareUrl.contains("www.toutiao.com") || shareUrl.contains("365yg.com")) {//是网页版的url
            beginParse(shareUrl);
        } else {
            UIUtils.showToast(UIUtils.getString(R.string.url_format_error));
        }
    }

    private void beginParse(String htmUrl) {
        mContext.showWaitingDialog(UIUtils.getString(R.string.please_wait));
        //解析地址
        parseVideo(htmUrl);
    }

    private void parseVideo(final String htmUrl) {
        new VideoPathDecoder() {
            @Override
            public void onSuccess(List<Video> videoList) {

                mContext.hideWaitingDialog();

                UIUtils.showToast(UIUtils.getString(R.string.parse_success));

                if (videoList != null && videoList.size() > 0) {

                    if (mData == null) {
                        mData = new ArrayList<>(3);
                    }
                    mData.clear();
                    mData.addAll(videoList);

                    if (mDialog == null) {
                        mView = View.inflate(mContext, R.layout.dialog_single_choose, null);
                        mDialog = new CustomDialog(mContext, mView, R.style.MyDialog);
                        mRvVideos = (LQRRecyclerView) mView.findViewById(R.id.rvVideos);
                        if (mAdapter == null) {
                            mAdapter = new LQRAdapterForRecyclerView<Video>(mContext, mData, R.layout.item_video_choose) {
                                @Override
                                public void convert(LQRViewHolderForRecyclerView helper, Video video, int i) {
                                    helper.setText(R.id.tvTitle, video.definition).getView(R.id.root).setOnClickListener(v -> {
                                        mDialog.dismiss();
                                        String filePath = FileUtils.updateFilePath(new File(AppConst.VIDEO_DOWNLOAD_FOLDER, video.title) + "." + video.vtype);
                                        String fileName = FileUtils.getFileNameFromPath(filePath);
//                                        LogUtils.sf("下载地址：" + video.main_url);
//                                        LogUtils.sf("下载路径：" + filePath);
                                        //开始下载视频
                                        downloadVideo(video.main_url, fileName, filePath);
                                    });

                                }
                            };
                            mRvVideos.setAdapter(mAdapter);
                        } else {
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                    mDialog.show();
                } else {
                    UIUtils.showToast(UIUtils.getString(R.string.no_find_video));
                }
            }

            @Override
            public void onDecodeError(Throwable e) {
                parsePic(htmUrl);
            }
        }.decodePath(htmUrl);
    }

    private void parsePic(String htmUrl) {
        new PicPathDecoder() {
            @Override
            public void onSuccess(List<String> picList) {
                mContext.hideWaitingDialog();
                UIUtils.showToast(UIUtils.getString(R.string.parse_success));
                downloadPic(picList, mTitle);
            }

            @Override
            public void onDecodeError(Throwable e) {
                mContext.hideWaitingDialog();
                LogUtils.sf(e.getLocalizedMessage());
                UIUtils.showToast(UIUtils.getString(R.string.parse_error));
            }
        }.decodePath(htmUrl);
    }

    private void downloadVideo(String url, String name, String path) {

        //生成id
        int id = FileDownloadUtils.generateId(url, path);
        //本地数据库保存一份下载任务记录
        new TaskModel(id, url, name, path).saveAsync().listen(success -> {
            if (success) {
                //开始下载任务
                FileDownloader.getImpl()
                        .create(url)
                        .setPath(path)
                        .setListener(new MyFileDownloadListener())
                        .start();
            }
        });
//        if(FileDownloadUtils.isNetworkOnWifiType())
    }

    private void downloadPic(List<String> picList, String folderName) {
        List<TaskModel> taskModels;
        if (picList != null && picList.size() > 0) {
            taskModels = new ArrayList<>(picList.size());
            for (String url : picList) {
                String name = FileUtils.getFileNameFromPath(url);//得到文件名
                if (TextUtils.isEmpty(FileUtils.getExtensionName(name))) {
                    name = name + ".png";
                }
                String path = FileUtils.updateFilePath(AppConst.PIC_DOWNLOAD_FOLDER + File.separator + folderName + File.separator + name);
                int id = FileDownloadUtils.generateId(url, path);
                taskModels.add(new TaskModel(id, url, name, path));
            }
            //记录所有下载任务
            DataSupport.saveAllAsync(taskModels).listen(success -> {
                if (success) {
                    //创建所有的下载任务
                    final FileDownloadQueueSet queueSet = new FileDownloadQueueSet(new MyFileDownloadListener());
                    List<BaseDownloadTask> tasks = new ArrayList<>(taskModels.size());
                    for (int i = 0; i < taskModels.size(); i++) {
                        tasks.add(FileDownloader.getImpl()
                                .create(taskModels.get(i).getUrl())
                                .setPath(taskModels.get(i).getPath()));
                    }
                    // 所有任务在下载失败的时候都自动重试一次
                    queueSet.setAutoRetryTimes(1);
                    queueSet.downloadTogether(tasks).start();
                }
            });
        }

    }


}
