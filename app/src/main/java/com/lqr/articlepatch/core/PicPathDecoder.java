package com.lqr.articlepatch.core;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.lqr.articlepatch.api.AppClient;
import com.lqr.articlepatch.model.Gallery;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @创建者 CSDN_LQR
 * @描述 图片地址解析(jsoup解析)
 */

public abstract class PicPathDecoder {
    public String mTitle;

    public void decodePath(final String srcUrl) {
        AppClient.getApiService().getHtml(srcUrl)
                .map(response -> {
                    Document doc = Jsoup.parse(response);

                    //第一种图片网页(http://www.toutiao.com/a6378970059536564482/)
//                    Elements titleH1 = doc.select("h1.article-title");
//                    if (titleH1 != null && titleH1.size() > 0) {
//                        mTitle = titleH1.get(0).text();
//                        Element contentDiv = doc.select("div.article-content").get(0);
//                        if (contentDiv != null) {
//                            Elements imgs = contentDiv.select("img");
//                            List<String> picList = getPicList(imgs);
//                            if (picList != null) return picList;
//                        }
//                    }

                    //第二种图片网页(http://www.toutiao.com/a6366487645505765634/)
//                    Elements title = doc.select("title");
//                    if (title != null && title.size() > 0) {
//                        mTitle = title.get(0).text();
//                        Element imageListDiv = doc.select("div.imageList").get(0);
//                        if (imageListDiv != null) {
//                            Elements imageListUl = imageListDiv.select("ul.image-list");
//                            if (imageListUl != null) {
//                                Elements imgs = imageListUl.select("img");
//                                List<String> picList = getPicList(imgs);
//                                if (picList != null) return picList;
//                            }
//                        }
//                    }

                    Elements title = doc.select("title");
                    if (title != null && title.size() > 0) {
                        mTitle = title.get(0).text();
                        mTitle = mTitle.replaceAll("/", "&");
                        //第一种图片网页(http://www.toutiao.com/a6378970059536564482/)
                        Elements contentDiv = doc.select("div.article-content");
                        if (contentDiv != null && contentDiv.size() > 0) {
                            Elements imgs = contentDiv.get(0).select("img");
                            List<String> picList = getPicList(imgs);
                            if (picList != null && picList.size() > 0) return picList;
                        } else {
                            //第二种图片网页(http://www.toutiao.com/a6366487645505765634/)
                            Pattern pattern = Pattern.compile("var gallery = \\{(.+)\\};");
                            Matcher matcher = pattern.matcher(response);
                            if (matcher.find()) {
                                String json = "{" + matcher.group(1) + "}";
                                Gson gson = new Gson();
                                Gallery gallery = gson.fromJson(json, Gallery.class);
                                List<Gallery.SubImagesBean> subImages = gallery.getSub_images();
                                List<String> picList = new ArrayList<>(subImages.size());
                                for (Gallery.SubImagesBean sib : subImages) {
                                    picList.add(sib.getUrl());
                                }
                                if (picList != null && picList.size() > 0) return picList;
                            }
                        }
                    }
                    return null;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(picList -> onSuccess(picList), this::onDecodeError);
    }

    @Nullable
    private List<String> getPicList(Elements imgs) {
        if (imgs != null && imgs.size() > 0) {
            List<String> picList = new ArrayList<>(imgs.size());
            for (Element img : imgs) {
                picList.add(img.attr("src"));
            }
            return picList;
        }
        return null;
    }

    public abstract void onSuccess(List<String> picList);

    public abstract void onDecodeError(Throwable e);
}
