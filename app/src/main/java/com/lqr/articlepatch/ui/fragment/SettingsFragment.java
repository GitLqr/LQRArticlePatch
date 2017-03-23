package com.lqr.articlepatch.ui.fragment;

import com.lqr.articlepatch.R;
import com.lqr.articlepatch.ui.base.BaseFragment;
import com.lqr.articlepatch.ui.base.BasePresenter;


public class SettingsFragment extends BaseFragment {

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_settings;
    }
}
