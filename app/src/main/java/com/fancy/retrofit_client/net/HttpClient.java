package com.fancy.retrofit_client.net;

import com.fancy.rxretrofit.HttpRetrofit;
import com.fancy.rxretrofit.impl.Iinterceptor;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * file explain
 *
 * @author fanlei
 * @version 1.0 2019\1\4 0004
 * @since JDK 1.7
 */
public class HttpClient {
    private static HttpClient singleHttp = null;
    private HttpClient() {
    }

    public static HttpClient getInstance() {
        if (singleHttp == null) {
            synchronized (HttpClient.class) {
                if (singleHttp == null) {
                    singleHttp = new HttpClient();
                }
            }
        }
        return singleHttp;
    }


    public  ApiService getRetrofitService() {

        Retrofit retrofit = HttpRetrofit.getInstance().getRetrofit("http://api.laifudao.com/"
                , new TokenInterceptor(),
                true);

        return retrofit.create(ApiService.class);
    }

}
