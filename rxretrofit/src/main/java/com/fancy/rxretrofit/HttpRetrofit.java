package com.fancy.rxretrofit;

import com.fancy.rxretrofit.impl.Iinterceptor;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * retrofit的入口类
 *
 * @author fanlei
 * @version 1.0 2018\12\28 0028
 * @since JDK 1.7
 */
public class HttpRetrofit {
    private static final int DEFAULT_CONNECT_TIMEOUT = 15;
    private static final int DEFAULT_WRITE_TIMEOUT = 15;
    private static final int DEFAULT_READ_TIMEOUT = 15;

    /**
     * 请求失败重连次数
     */
    private int RETRY_COUNT = 0;

    private static HttpRetrofit singleHttp = null;
    private static volatile Retrofit mRetrofit;

    private final OkHttpClient.Builder okHttpClientBuilder;
    private final Retrofit.Builder retrofitBuilder;


    private HttpRetrofit() {
        retrofitBuilder = new Retrofit.Builder();
        okHttpClientBuilder = new OkHttpClient.Builder();
    }

    public static HttpRetrofit getInstance() {
        if (singleHttp == null) {
            synchronized (HttpRetrofit.class) {
                if (singleHttp == null) {
                    singleHttp = new HttpRetrofit();
                }
            }
        }
        return singleHttp;
    }


    public Retrofit getRetrofit(String baseUrl, Iinterceptor interceptor, boolean isDebug) {
        if (mRetrofit == null) {
            synchronized (Retrofit.class) {
                if (mRetrofit == null) {
                    mRetrofit = getInstance().retrofitBuilder
                            .baseUrl(baseUrl)
                            .client(getOkHttpClient(interceptor, isDebug))
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();
                }
            }
        }
        return mRetrofit;

    }


    public OkHttpClient getOkHttpClient(Iinterceptor interceptor, boolean isdebug) {
        return getInstance().okHttpClientBuilder
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true) //错误重连
                .addInterceptor(getLoggingInterceptor(isdebug))
                .addNetworkInterceptor(interceptor)
                .build();
    }


    /**
     * 对外暴露 OkHttpClient,方便自定义
     */
    public static OkHttpClient.Builder getOkHttpClientBuilder() {
        return getInstance().okHttpClientBuilder;
    }

    /**
     * 对外暴露 Retrofit,方便自定义
     */
    public static Retrofit.Builder getRetrofitBuilder() {
        return getInstance().retrofitBuilder;
    }


    /**
     * log拦截器
     *
     * @return
     */
    public HttpLoggingInterceptor getLoggingInterceptor(boolean isDebug) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(isDebug ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        return loggingInterceptor;
    }


    /**
     * 设置订阅 和 所在的线程环境
     */
    public <W> void toSubscribe(Observable<W> o, DisposableObserver<W> s) {

        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retry(RETRY_COUNT)//请求失败重连次数
                .subscribe(s);
    }

}
