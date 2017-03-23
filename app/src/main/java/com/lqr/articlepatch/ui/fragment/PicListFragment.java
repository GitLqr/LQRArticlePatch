package com.lqr.articlepatch.ui.fragment;

import android.widget.TextView;

import com.lqr.articlepatch.R;
import com.lqr.articlepatch.ui.activity.MainActivity;
import com.lqr.articlepatch.ui.base.BaseFragment;
import com.lqr.articlepatch.ui.presenter.PicListPresenter;
import com.lqr.articlepatch.ui.view.IPicListFgView;
import com.lqr.recyclerview.LQRRecyclerView;

import butterknife.Bind;

/**
 * @创建者 CSDN_LQR
 * @描述 图片列表界面
 */
public class PicListFragment extends BaseFragment<IPicListFgView, PicListPresenter> implements IPicListFgView {

    @Bind(R.id.rvPic)
    LQRRecyclerView mRvPic;
    @Bind(R.id.tvNoDataTip)
    TextView mTvNoDataTip;

    @Override
    public void initData() {
        mPresenter.loadPicFolderList();
    }

    @Override
    protected PicListPresenter createPresenter() {
        return new PicListPresenter((MainActivity) getActivity());
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_pic_list;
    }

    @Override
    public LQRRecyclerView getRvPic() {
        return mRvPic;
    }

    @Override
    public TextView getTvNoDataTip() {
        return mTvNoDataTip;
    }
}
