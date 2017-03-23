package com.lqr.articlepatch.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lqr.articlepatch.R;
import com.lqr.articlepatch.ui.activity.MainActivity;
import com.lqr.articlepatch.ui.base.BaseFragment;
import com.lqr.articlepatch.ui.base.BasePresenter;
import com.lqr.articlepatch.util.UIUtils;

import butterknife.Bind;

/**
 * @创建者 CSDN_LQR
 * @描述 说明界面
 */
public class ExplainFragment extends BaseFragment {

    @Bind(R.id.webview)
    WebView mWebview;

    @Override
    public void initView(View rootView) {
        WebSettings webSettings = mWebview.getSettings();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        mWebview.setBackgroundColor(Color.TRANSPARENT);
        mWebview.loadUrl("file:///android_asset/explain/index.html");

        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                ((MainActivity) getActivity()).showWaitingDialog(UIUtils.getString(R.string.please_wait));
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                ((MainActivity) getActivity()).hideWaitingDialog();
            }
        });

        mWebview.setOnKeyListener((v, keyCode, event) -> {
            if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebview.canGoBack()) {
                UIUtils.postTaskSafely(() -> mWebview.goBack());
                return true;
            }
            return false;
        });
    }


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_explain;
    }
}
