package com.fancy.rxretrofit.callback

import android.content.Context
import com.fancy.rxretrofit.NetError
import com.fancy.rxretrofit.impl.LoadCancelListener
import com.fancy.rxretrofit.impl.LoadingListener
import com.fancy.rxretrofit.load.Gloading
import io.reactivex.observers.DisposableObserver
import retrofit2.Response

/**
 *
 * @Description:
 * @Author: fanlei
 * @CreateDate: 14/9/2021 上午10:12
 * @Version: 1.0
 */
abstract class RxBaseCallBack<T>(mContext: Context) : DisposableObserver<Response<T>>(), LoadingListener, LoadCancelListener {
    protected var mContext: Context? = null
    private var holder: Gloading.Holder? = null
    protected val defaultFailCode = -1

    init {
        this.mContext = mContext
    }

    override fun onNext(response: Response<T>) {
        holder?.showLoadSuccess()
        if (response.raw().code() == 200) {
            onObtain(response)
        } else { //失败响应
            onLose(
                    response,
                    NetError.buildError(response)!!.getErrorMessage(),
                    response.raw().code()
            )
        }
    }

    override fun onError(t: Throwable) {
        holder?.showLoadSuccess()
        onLose(null, NetError.buildError(t)!!.getErrorMessage(), defaultFailCode)
    }

    override fun onComplete() {
        onLoadCance()
    }

    override fun onLoadCance() {
        if (!this.isDisposed) {
            dispose()
        }
    }


    override fun setLoading(holder: Gloading.Holder?) {
        this.holder = holder
    }


    protected abstract fun onObtain(response: Response<T>?)

    protected abstract fun onLose(response: Response<T>?, message: String?, failCode: Int)

}