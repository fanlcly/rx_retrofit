package com.fancy.retrofit_client.net;

import com.fancy.rxretrofit.impl.Iinterceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * file explain
 *
 * @author fanlei
 * @version 1.0 2018\5\31 0031
 * @since JDK 1.7
 */
public class TokenInterceptor implements Iinterceptor {

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {

        Request originalRequest = chain.request();

        Request authorised = originalRequest.newBuilder()
                //.header(SIGNATURE,signature)
                .addHeader("Accept", "application/json")
                .addHeader("token", "***")// 约定好的请求头参数
                .build();
        return chain.proceed(authorised);
    }
}
