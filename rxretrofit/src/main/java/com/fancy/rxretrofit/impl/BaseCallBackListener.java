package com.fancy.rxretrofit.impl;

/**
 * 回调的接口
 *
 * @author fanlei
 * @version 1.0 2019\1\2 0002
 * @since JDK 1.7
 */
public interface BaseCallBackListener {

    void onSuccess(String result);

    void onFault(String errorMsg);
}
