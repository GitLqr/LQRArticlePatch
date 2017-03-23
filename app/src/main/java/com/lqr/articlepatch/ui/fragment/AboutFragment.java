package com.lqr.articlepatch.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.widget.TextView;

import com.lqr.articlepatch.R;
import com.lqr.articlepatch.ui.base.BaseFragment;
import com.lqr.articlepatch.ui.base.BasePresenter;

import butterknife.Bind;


public class AboutFragment extends BaseFragment {

    @Bind(R.id.tvJianShuUrl)
    TextView mTvJianShuUrl;
    @Bind(R.id.tvCSDNUrl)
    TextView mTvCSDNUrl;

    @Override
    public void initListener() {
//        mTvJianShuUrl.setOnClickListener(v -> openUrl(mTvJianShuUrl.getText().toString()));
//        mTvCSDNUrl.setOnClickListener(v -> openUrl(mTvCSDNUrl.getText().toString()));
    }

    private void openUrl(String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        startActivity(intent);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_about;
    }
}
