package com.fancy.rxretrofit.base;

import android.content.Context;
import android.support.annotation.NonNull;

import com.fancy.rxretrofit.error.Error;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * retrofit基本返回值
 *
 * @author fanlei
 * @version 1.0 2019\1\4 0004
 * @since JDK 1.7
 */
public abstract class BaseCallBack<T> implements Callback<T> {

    protected final int defaultFailCode = -1;

    protected final Context mContext;

    public BaseCallBack(@NonNull Context context) {
        this.mContext = context;
    }


    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.raw().code() == 200) {
            onSuc(response);
        } else {//失败响应
            onFail(response, Error.buildError(response).getErrorMessage(), response.raw().code());
        }

    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {//网络问题会走该回调
        onFail(null, Error.buildError(t).getErrorMessage(), defaultFailCode);
    }

    public abstract void onSuc(Response<T> response);

    public abstract void onFail(Response<T> response, String message, int failCode);

}

