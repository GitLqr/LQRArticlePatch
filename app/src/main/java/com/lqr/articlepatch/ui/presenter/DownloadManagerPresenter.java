package com.lqr.articlepatch.ui.presenter;

import android.view.View;
import android.widget.ProgressBar;

import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.lqr.adapter.LQRAdapterForRecyclerView;
import com.lqr.adapter.LQRViewHolderForRecyclerView;
import com.lqr.articlepatch.R;
import com.lqr.articlepatch.model.TaskModel;
import com.lqr.articlepatch.ui.base.BaseActivity;
import com.lqr.articlepatch.ui.base.BasePresenter;
import com.lqr.articlepatch.ui.listener.MyFileDownloadListener;
import com.lqr.articlepatch.ui.view.IDownloadManagerFgView;
import com.lqr.articlepatch.util.UIUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lqr on 2017/3/12.
 */

public class DownloadManagerPresenter extends BasePresenter<IDownloadManagerFgView> {

    private List<TaskModel> mData;
    private LQRAdapterForRecyclerView<TaskModel> mAdapter;

    public DownloadManagerPresenter(BaseActivity context) {
        super(context);
    }

    public void loadTasks() {
        loadData();
        setAdapter();
    }

    private void loadData() {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.clear();

        //得到所有下载任务（包括已完成，未完成）
        mData.addAll(DataSupport.findAll(TaskModel.class));

        if (mData.size() > 0) {
            getView().getRvTasks().setVisibility(View.VISIBLE);
            getView().getTvNoDataTip().setVisibility(View.GONE);
        } else {
            getView().getRvTasks().setVisibility(View.GONE);
            getView().getTvNoDataTip().setVisibility(View.VISIBLE);
        }
    }

    private void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new LQRAdapterForRecyclerView<TaskModel>(mContext, mData, R.layout.item_download_task) {
                @Override
                public void convert(LQRViewHolderForRecyclerView helper, TaskModel item, int position) {
                    int taskId = item.getTaskId();
                    if (!FileDownloader.getImpl().isServiceConnected()) {
                        FileDownloader.getImpl().bindService(() -> fillView(helper, item, taskId));
                    }
                    fillView(helper, item, taskId);
                }

                private void fillView(LQRViewHolderForRecyclerView helper, TaskModel item, int taskId) {
                    byte status = FileDownloader.getImpl().getStatus(taskId, item.getPath());
                    String statusStr = "...";
                    switch (status) {
                        case FileDownloadStatus.pending:
                            statusStr = UIUtils.getString(R.string.pending);
                            break;
                        case FileDownloadStatus.progress:
                            statusStr = UIUtils.getString(R.string.progress);
                            break;
                        case FileDownloadStatus.paused:
                            statusStr = UIUtils.getString(R.string.paused);
                            break;
                        case FileDownloadStatus.completed:
                            statusStr = UIUtils.getString(R.string.completed);
                            break;
                        case FileDownloadStatus.error:
                            statusStr = UIUtils.getString(R.string.error);
                            break;
                        case FileDownloadStatus.warn:
                            statusStr = UIUtils.getString(R.string.warn);
                            break;
                    }

                    helper.setText(R.id.tvFileName, item.getName())
                            .setText(R.id.tvStatus, statusStr);
                    ProgressBar pbProgress = helper.getView(R.id.pbProgress);
                    pbProgress.setMax((int) FileDownloader.getImpl().getTotal(taskId));
                    pbProgress.setProgress((int) FileDownloader.getImpl().getSoFar(taskId));
                }
            };

            mAdapter.setOnItemClickListener((lqrViewHolder, viewGroup, view, i) -> {
                TaskModel item = mAdapter.getData().get(i);
                int taskId = item.getTaskId();
                byte status = FileDownloader.getImpl().getStatus(taskId, item.getPath());
                switch (status) {
                    case FileDownloadStatus.pending:
                        pauseTask(taskId);
                        break;
                    case FileDownloadStatus.progress:
                        pauseTask(taskId);
                        break;
                    case FileDownloadStatus.paused:
                        startTask(item);
                        break;
                    case FileDownloadStatus.error:
                        startTask(item);
                        break;
                }
                updateRv();
            });
        }
        getView().getRvTasks().setAdapter(mAdapter);
    }

    private void startTask(TaskModel item) {
        FileDownloader.getImpl()
                .create(item.getUrl())
                .setPath(item.getPath())
                .setListener(new MyFileDownloadListener())
                .start();
    }

    private void pauseTask(int taskId) {
        FileDownloader.getImpl().pause(taskId);
    }

    private void updateRv() {
        updateRv(false);
    }


    public void updateRv(boolean shouldReLoadData) {
        if (shouldReLoadData) {
            loadData();
        }

        if (mAdapter != null && getView().getRvTasks() != null) {
            mAdapter.notifyDataSetChanged();
        }

    }

}
