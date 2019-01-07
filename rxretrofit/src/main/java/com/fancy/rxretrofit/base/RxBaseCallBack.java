package com.fancy.rxretrofit.base;

import android.content.Context;
import android.util.Log;

import com.fancy.rxretrofit.error.Error;
import com.fancy.rxretrofit.impl.LoadCancelListener;

import io.reactivex.observers.DisposableObserver;
import retrofit2.Response;

/**
 * 回调基类
 *
 * @author fanlei
 * @version 1.0 2019\1\2 0002
 * @since JDK 1.7
 */
public abstract class RxBaseCallBack<T> extends DisposableObserver<Response<T>> implements LoadCancelListener {

    protected final int defaultFailCode = -1;
    protected final Context mContext;

    /**
     * @param context 上下文
     */
    public RxBaseCallBack(Context context) {
        this.mContext = context;
    }

    @Override
    protected void onStart() {
        Log.i("RxBaseCallBack", "onStart:");
    }

    @Override
    public void onNext(Response<T> response) {
        if (response.raw().code() == 200) {
            onSuc(response);
        } else {//失败响应
            onFail(response, Error.buildError(response).getErrorMessage(), response.raw().code());
        }

    }

    @Override
    public void onError(Throwable t) {
        try {
            onFail(null, Error.buildError(t).getErrorMessage(), defaultFailCode);
        } finally {
            onLoadCance();
        }

    }

    @Override
    public void onComplete() {
        onLoadCance();
    }

    @Override
    public void onLoadCance() {
        if (!this.isDisposed()) {
            this.dispose();
        }
    }

    public abstract void onSuc(Response<T> response);

    public abstract void onFail(Response<T> response, String message, int failCode);

}
