package com.lqr.articlepatch.ui.fragment;

import android.widget.TextView;

import com.lqr.articlepatch.R;
import com.lqr.articlepatch.ui.activity.MainActivity;
import com.lqr.articlepatch.ui.base.BaseFragment;
import com.lqr.articlepatch.ui.presenter.VideoListPresenter;
import com.lqr.articlepatch.ui.view.IVideoListFgView;
import com.lqr.articlepatch.util.LogUtils;
import com.lqr.recyclerview.LQRRecyclerView;

import butterknife.Bind;


public class VideoListFragment extends BaseFragment<IVideoListFgView, VideoListPresenter> implements IVideoListFgView {

    @Bind(R.id.rvVideos)
    LQRRecyclerView mRvVideos;
    @Bind(R.id.tvNoDataTip)
    TextView mTvNoDataTip;

    @Override
    public void initData() {
        mPresenter.loadVideoList();
        LogUtils.sf("VideoListFragment -- initData");
    }

    @Override
    protected VideoListPresenter createPresenter() {
        return new VideoListPresenter((MainActivity) getActivity());
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_video_list;
    }

    @Override
    public LQRRecyclerView getRvVideos() {
        return mRvVideos;
    }

    @Override
    public TextView getTvNoDataTip() {
        return mTvNoDataTip;
    }
}
