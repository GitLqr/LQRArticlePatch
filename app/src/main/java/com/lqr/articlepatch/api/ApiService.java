package com.lqr.articlepatch.api;

import com.lqr.articlepatch.model.ResultResponse;
import com.lqr.articlepatch.model.VideoModel;

import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

public interface ApiService {

    String HOST = "http://www.toutiao.com/";
    String API_SERVER_URL = HOST + "api/";

    String URL_ARTICLE_FEED = "article/feed";
    String URL_COMMENT_LIST = "comment/list/";
    String HOST_VIDEO = "http://i.snssdk.com";
    String URL_VIDEO = "/video/urls/v/1/toutiao/mp4/%s?r=%s";

    //获取视频页的html代码
    @GET
    Observable<String> getHtml(@Url String url);

    //获取视频数据json
    @GET
    Observable<ResultResponse<VideoModel>> getVideoData(@Url String url);
}
