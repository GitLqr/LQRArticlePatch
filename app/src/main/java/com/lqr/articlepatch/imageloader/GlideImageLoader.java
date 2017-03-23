package com.lqr.articlepatch.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lzy.ninegrid.NineGridView;

/**
 * Glide 加载
 */
public class GlideImageLoader implements NineGridView.ImageLoader {
    @Override
    public void onDisplayImage(Context context, ImageView imageView, String url) {
        Glide.with(context).load(url)//
//                .placeholder(R.drawable.ic_default_color)//
//                .error(R.drawable.ic_default_color)//
                .diskCacheStrategy(DiskCacheStrategy.ALL)//
                .into(imageView);
    }

    @Override
    public Bitmap getCacheImage(String url) {
        return null;
    }
}