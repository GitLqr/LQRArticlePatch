package com.lqr.articlepatch.ui.fragment;

import android.widget.TextView;

import com.lqr.articlepatch.R;
import com.lqr.articlepatch.ui.activity.MainActivity;
import com.lqr.articlepatch.ui.base.BaseFragment;
import com.lqr.articlepatch.ui.presenter.DownloadManagerPresenter;
import com.lqr.articlepatch.ui.view.IDownloadManagerFgView;
import com.lqr.recyclerview.LQRRecyclerView;

import butterknife.Bind;


public class DownloadManagerFragment extends BaseFragment<IDownloadManagerFgView, DownloadManagerPresenter> implements IDownloadManagerFgView {

    @Bind(R.id.rvTasks)
    LQRRecyclerView mRvTasks;
    @Bind(R.id.tvNoDataTip)
    TextView mTvNoDataTip;

    @Override
    public void initData() {
        mPresenter.loadTasks();
    }

    @Override
    protected DownloadManagerPresenter createPresenter() {
        return new DownloadManagerPresenter((MainActivity) getActivity());
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_download_manager;
    }

    @Override
    public LQRRecyclerView getRvTasks() {
        return mRvTasks;
    }

    @Override
    public TextView getTvNoDataTip() {
        return mTvNoDataTip;
    }
}
