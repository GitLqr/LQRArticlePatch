package com.lqr.articlepatch.ui.view;

import android.widget.TextView;

import com.lqr.recyclerview.LQRRecyclerView;

/**
 * Created by lqr on 2017/3/12.
 */

public interface IVideoListFgView {

    LQRRecyclerView getRvVideos();

    TextView getTvNoDataTip();
}
