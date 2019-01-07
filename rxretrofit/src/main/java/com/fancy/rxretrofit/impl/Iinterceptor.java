package com.fancy.rxretrofit.impl;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * file explain
 *
 * @author fanlei
 * @version 1.0 2018\12\28 0028
 * @since JDK 1.7
 */
public interface Iinterceptor extends Interceptor {
    @Override
    Response intercept(Chain chain) throws IOException;
}
