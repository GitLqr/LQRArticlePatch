package com.lqr.articlepatch.api;

import com.google.gson.GsonBuilder;
import com.lqr.articlepatch.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AppClient {

    public static Retrofit mRetrofit;

    public static Retrofit retrofit() {
        if (mRetrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            if (BuildConfig.DEBUG) {
//                builder.addInterceptor(new LoggingInterceptor());//使用自定义的Log拦截器

                // Log信息拦截器
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                //设置 Debug Log 模式
                builder.addInterceptor(loggingInterceptor);
            }
            //设置头信息
            builder.addInterceptor(chain -> {
                Request.Builder builder1 = chain.request().newBuilder();
                builder1.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.108 Safari/537.36 2345Explorer/8.0.0.13547");
                builder1.addHeader("Cache-Control", "max-age=0");
                builder1.addHeader("Upgrade-Insecure-Requests", "1");
                builder1.addHeader("X-Requested-With", "XMLHttpRequest");
                builder1.addHeader("Cookie", "uuid=\"w:f2e0e469165542f8a3960f67cb354026\"; __tasessionId=4p6q77g6q1479458262778; csrftoken=7de2dd812d513441f85cf8272f015ce5; tt_webid=36385357187");
                return chain.proceed(builder1.build());
            });

            OkHttpClient client = builder.build();
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(ApiService.API_SERVER_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())//可以把请求后得到的内容转成String(不加这个的话，就得不到网页String内容，也就不能进行正则表达式查找关键字)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))//可以把请求后得到的内容转成bean
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client)
                    .build();
        }
        return mRetrofit;
    }

    public static ApiService getApiService() {
        return retrofit().create(ApiService.class);
    }


    //自定义的Log拦截器（上面已经使用了'com.squareup.okhttp3:logging-interceptor:3.3.1',这个没用了）
//    static class LoggingInterceptor implements Interceptor {
//        @Override
//        public Response intercept(Interceptor.Chain chain) throws IOException {
//            //这个chain里面包含了request和response，所以你要什么都可以从这里拿
//            Request request = chain.request();
//
//            long t1 = System.nanoTime();//请求发起的时间
//            LogUtils.sf(String.format("发送请求 %s on %s%n%s",
//                    request.url(), chain.connection(), request.headers()));
//
//            Response response = chain.proceed(request);
//
//            long t2 = System.nanoTime();//收到响应的时间
//
//            //这里不能直接使用response.body().string()的方式输出日志
//            //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
//            //个新的response给应用层处理
//            ResponseBody responseBody = response.peekBody(1024 * 1024);
//
//            LogUtils.sf(String.format("接收响应: [%s] %n返回json:【%s】 %.1fms%n%s",
//                    response.request().url(),
//                    responseBody.string(),
//                    (t2 - t1) / 1e6d,
//                    response.headers()));
//
//            return response;
//        }
//    }
}
