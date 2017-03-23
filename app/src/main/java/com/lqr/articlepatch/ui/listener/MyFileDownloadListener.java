package com.lqr.articlepatch.ui.listener;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.lqr.articlepatch.model.TaskModel;
import com.lqr.articlepatch.ui.fragment.DownloadManagerFragment;
import com.lqr.articlepatch.ui.fragment.FragmentFactory;

import org.litepal.crud.DataSupport;

/**
 * Created by lqr on 2017/3/12.
 */

public class MyFileDownloadListener extends FileDownloadListener {

    private void uploadStatus(boolean shouldReLoadData) {
        if (FragmentFactory.getInstance().getCurrentFragment() instanceof DownloadManagerFragment) {
            FragmentFactory.getInstance().getDownloadManagerFragment().mPresenter.updateRv(shouldReLoadData);
        }
    }

    @Override
    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        uploadStatus(true);
    }

    @Override
    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        uploadStatus(false);
    }

    @Override
    protected void completed(BaseDownloadTask task) {
        //删除本地任务数据库
        DataSupport.deleteAll(TaskModel.class, "taskId = ?", task.getId() + "");
        uploadStatus(true);
    }

    @Override
    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        uploadStatus(false);
    }

    @Override
    protected void error(BaseDownloadTask task, Throwable e) {
        uploadStatus(false);
    }

    @Override
    protected void warn(BaseDownloadTask task) {
        uploadStatus(false);
    }
}
