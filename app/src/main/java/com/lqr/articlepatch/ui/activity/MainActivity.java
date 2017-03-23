package com.lqr.articlepatch.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.lqr.articlepatch.R;
import com.lqr.articlepatch.ui.base.BaseActivity;
import com.lqr.articlepatch.ui.base.BaseFragment;
import com.lqr.articlepatch.ui.fragment.AboutFragment;
import com.lqr.articlepatch.ui.fragment.DownloadManagerFragment;
import com.lqr.articlepatch.ui.fragment.ExplainFragment;
import com.lqr.articlepatch.ui.fragment.FragmentFactory;
import com.lqr.articlepatch.ui.fragment.PicListFragment;
import com.lqr.articlepatch.ui.fragment.SettingsFragment;
import com.lqr.articlepatch.ui.fragment.VideoListFragment;
import com.lqr.articlepatch.ui.presenter.MainPresenter;
import com.lqr.articlepatch.ui.view.IMainAtView;
import com.lqr.articlepatch.util.UIUtils;
import com.lqr.articlepatch.widget.CustomDialog;

import butterknife.Bind;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class MainActivity extends BaseActivity<IMainAtView, MainPresenter> implements IMainAtView, NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.flContent)
    FrameLayout mFlContent;

    private int mExit = 0;
    private boolean mHasPermission = true;
    private CustomDialog mDialog;
    private View mView;
    private EditText mEtUrl;

    @Override
    public void initView() {
        initNavigationView();
        PermissionGen.with(MainActivity.this)
                .addRequestCode(100)
                .permissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .request();
        if (mHasPermission) {
            //得到分享链接
            mPresenter.getShareUrlAndParse();
        }

        //默认显示视频列表
        getSupportFragmentManager().beginTransaction().add(R.id.flContent, FragmentFactory.getInstance().getVideoListFragment()).commit();
//        getSupportFragmentManager().beginTransaction().add(R.id.flContent, FragmentFactory.getInstance().getDownloadManagerFragment()).commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mPresenter.getShareUrlAndParse();
    }

    @Override
    public void initListener() {
        fab.setOnClickListener(view -> showInputUrlDialog());
    }

    private void showInputUrlDialog() {
        if (mDialog == null) {
            mView = View.inflate(this, R.layout.dialog_input_video, null);
            mDialog = new CustomDialog(this, mView, R.style.MyDialog);
            mDialog.setCancelable(false);
            mEtUrl = (EditText) mView.findViewById(R.id.etUrl);
            mView.findViewById(R.id.tvCancle).setOnClickListener(v -> mDialog.dismiss());
            mView.findViewById(R.id.tvSure).setOnClickListener(v -> {

                String url = mEtUrl.getText().toString().trim();
                if (!TextUtils.isEmpty(url)) {
                    mDialog.dismiss();
                    mPresenter.parseUrl(url);
                }
            });
        } else {
            mEtUrl.setText("");
        }
        mDialog.show();
    }

    private void switchContent(BaseFragment newFragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContent, newFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            if (mExit++ == 1) {
                finish();
            } else {
                UIUtils.showToast(UIUtils.getString(R.string.double_click_exit));
                UIUtils.postTaskDelay(() -> mExit = 0, 2000);
            }

        }
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean isToolbarCanBack() {
        return false;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_video_list:
                if (!(FragmentFactory.getInstance().getCurrentFragment() instanceof VideoListFragment))
                    switchContent(FragmentFactory.getInstance().getVideoListFragment());
                break;
            case R.id.nav_pic_list:
                if (!(FragmentFactory.getInstance().getCurrentFragment() instanceof PicListFragment))
                    switchContent(FragmentFactory.getInstance().getPicListFragment());
                break;
            case R.id.nav_download_manager:
                if (!(FragmentFactory.getInstance().getCurrentFragment() instanceof DownloadManagerFragment))
                    switchContent(FragmentFactory.getInstance().getDownloadManagerFragment());
                break;
            case R.id.nav_setting:
                if (!(FragmentFactory.getInstance().getCurrentFragment() instanceof SettingsFragment))
                    switchContent(FragmentFactory.getInstance().getSettingsFragment());
                break;
            case R.id.nav_share:
                UIUtils.showToast("share");
                break;
            case R.id.nav_explain:
                if (!(FragmentFactory.getInstance().getCurrentFragment() instanceof ExplainFragment))
                    switchContent(FragmentFactory.getInstance().getExplainFragment());
                break;
            case R.id.nav_about:
                if (!(FragmentFactory.getInstance().getCurrentFragment() instanceof AboutFragment))
                    switchContent(FragmentFactory.getInstance().getAboutFragment());
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initNavigationView() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @PermissionSuccess(requestCode = 100)
    public void doSomething() {
        mHasPermission = true;
    }

    @PermissionFail(requestCode = 100)
    public void doFailSomething() {
        mHasPermission = false;
    }

}
