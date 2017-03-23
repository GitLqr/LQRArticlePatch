package com.lqr.articlepatch.core;

import android.util.Base64;

import com.lqr.articlepatch.api.ApiService;
import com.lqr.articlepatch.api.AppClient;
import com.lqr.articlepatch.model.ResultResponse;
import com.lqr.articlepatch.model.Video;
import com.lqr.articlepatch.model.VideoModel;
import com.lqr.articlepatch.util.FileUtils;
import com.lqr.articlepatch.util.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @创建者 CSDN_LQR
 * @描述 视频地址解析
 */
public abstract class VideoPathDecoder {

    String mTitle;

    public void decodePath(final String srcUrl) {

        AppClient.getApiService().getHtml(srcUrl)
                .flatMap(new Func1<String, Observable<ResultResponse<VideoModel>>>() {
                    @Override
                    public Observable<ResultResponse<VideoModel>> call(String response) {
                        //获取视频标题
                        Pattern pattern = Pattern.compile("title: \'(.+)\'\\.replace");//阳光宽带网
                        Matcher matcher = pattern.matcher(response);
                        if (matcher.find()) {
                            mTitle = matcher.group(1);
                        } else {
                            pattern = Pattern.compile("title: \'(.+)\',");//今日头条官网
                            matcher = pattern.matcher(response);
                            if (matcher.find()) {
                                mTitle = matcher.group(1);
                            } else { //没找到文章（视频）标题
                                String tempUrl = srcUrl;
                                if (srcUrl.endsWith(File.separator)) {
                                    tempUrl = srcUrl.substring(0, srcUrl.length() - 1);
                                }
                                mTitle = FileUtils.getFileNameFromPath(tempUrl);
                            }
                        }

                        //获取视频id
                        pattern = Pattern.compile("videoid:\'(.+)\'");
                        matcher = pattern.matcher(response);
                        if (matcher.find()) {
                            String videoId = matcher.group(1);
                            LogUtils.sf(videoId);
                            //1.将/video/urls/v/1/toutiao/mp4/{videoid}?r={Math.random()}，进行crc32加密。
                            String r = getRandom();
                            CRC32 crc32 = new CRC32();
                            String s = String.format(ApiService.URL_VIDEO, videoId, r);
                            //进行crc32加密
                            crc32.update(s.getBytes());
                            String crcString = crc32.getValue() + "";
                            //2.访问http://i.snssdk.com/video/urls/v/1/toutiao/mp4/{videoid}?r={Math.random()}&s={crc32值}
                            String url = ApiService.HOST_VIDEO + s + "&s=" + crcString;
                            LogUtils.sf(url);
                            return AppClient.getApiService().getVideoData(url);
                        }
                        return Observable.error(new Exception());
                    }
                })
                .map(new Func1<ResultResponse<VideoModel>, List<Video>>() {
                    @Override
                    public List<Video> call(ResultResponse<VideoModel> videoModelResultResponse) {
                        VideoModel.VideoListBean data = videoModelResultResponse.data.video_list;

                        List<Video> videoList = new ArrayList<>(3);

                        if (data.video_3 != null) {
                            videoList.add(updateVideo(data.video_3));
                        }
                        if (data.video_2 != null) {
                            videoList.add(updateVideo(data.video_2));
                        }
                        if (data.video_1 != null) {
                            videoList.add(updateVideo(data.video_1));
                        }

                        return videoList;
                    }

                    private String getRealPath(String base64) {
                        return new String(Base64.decode(base64.getBytes(), Base64.DEFAULT));
                    }

                    private Video updateVideo(Video video) {
                        //base64解码
                        video.main_url = getRealPath(video.main_url);
                        video.title = mTitle;
                        return video;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(videoList -> onSuccess(videoList), this::onDecodeError);
    }

    public abstract void onSuccess(List<Video> videoList);

    public abstract void onDecodeError(Throwable e);

    private String getRandom() {
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            result.append(random.nextInt(10));
        }
        return result.toString();
    }

}
