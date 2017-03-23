package com.lqr.articlepatch.ui.fragment;

import com.lqr.articlepatch.ui.base.BaseFragment;

/**
 * Created by lqr on 2017/3/12.
 */

public class FragmentFactory {

    private static FragmentFactory mInstance;
    private BaseFragment mCurrentFragment;
    private VideoListFragment mVideoListFragment;
    private PicListFragment mPicListFragment;
    private DownloadManagerFragment mDownloadManagerFragment;
    private SettingsFragment mSettingsFragment;
    private ExplainFragment mExplainFragment;
    private AboutFragment mAboutFragment;

    private FragmentFactory() {
        super();
    }

    public static FragmentFactory getInstance() {
        if (mInstance == null) {
            synchronized (FragmentFactory.class) {
                if (mInstance == null) {
                    mInstance = new FragmentFactory();
                }
            }
        }
        return mInstance;
    }

    public BaseFragment getCurrentFragment() {
        return mCurrentFragment;
    }

    public VideoListFragment getVideoListFragment() {
        if (mVideoListFragment == null) {
            synchronized (FragmentFactory.class) {
                if (mVideoListFragment == null) {
                    mVideoListFragment = new VideoListFragment();
                }
            }
        }
        mCurrentFragment = mVideoListFragment;
        return mVideoListFragment;
    }

    public PicListFragment getPicListFragment() {
        if (mPicListFragment == null) {
            synchronized (FragmentFactory.class) {
                if (mPicListFragment == null) {
                    mPicListFragment = new PicListFragment();
                }
            }
        }
        mCurrentFragment = mPicListFragment;
        return mPicListFragment;
    }

    public DownloadManagerFragment getDownloadManagerFragment() {
        if (mDownloadManagerFragment == null) {
            synchronized (FragmentFactory.class) {
                if (mDownloadManagerFragment == null) {
                    mDownloadManagerFragment = new DownloadManagerFragment();
                }
            }
        }
        mCurrentFragment = new DownloadManagerFragment();
        return mDownloadManagerFragment;
    }

    public SettingsFragment getSettingsFragment() {
        if (mSettingsFragment == null) {
            synchronized (FragmentFactory.class) {
                if (mSettingsFragment == null) {
                    mSettingsFragment = new SettingsFragment();
                }
            }
        }
        mCurrentFragment = mSettingsFragment;
        return mSettingsFragment;
    }

    public ExplainFragment getExplainFragment() {
        if (mExplainFragment == null) {
            synchronized (FragmentFactory.class) {
                if (mExplainFragment == null) {
                    mExplainFragment = new ExplainFragment();
                }
            }
        }
        mCurrentFragment = mExplainFragment;
        return mExplainFragment;
    }

    public AboutFragment getAboutFragment() {
        if (mAboutFragment == null) {
            synchronized (FragmentFactory.class) {
                if (mAboutFragment == null) {
                    mAboutFragment = new AboutFragment();
                }
            }
        }
        mCurrentFragment = mAboutFragment;
        return mAboutFragment;
    }
}
