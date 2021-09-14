package com.fancy.rxretrofit.callback

import android.content.Context
import com.fancy.rxretrofit.NetError
import com.fancy.rxretrofit.impl.LoadingListener
import com.fancy.rxretrofit.load.Gloading
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *
 * @Description:
 * @Author: fanlei
 * @CreateDate: 13/9/2021 下午3:37
 * @Version: 1.0
 */
abstract class BaseCallBack<T>(context: Context) : Callback<T>, LoadingListener {
    protected val defaultFailCode = -1

    protected var mContext: Context? = null
    private var holder: Gloading.Holder? = null

    init {
        mContext = context
    }

    override fun onResponse(call: Call<T>?, response: Response<T>) {
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

    override fun onFailure(call: Call<T>?, t: Throwable?) { //网络问题会走该回调
        holder?.showLoadSuccess()
        onLose(null, NetError.buildError(t!!)!!.getErrorMessage(), defaultFailCode)
    }


    protected abstract fun onObtain(response: Response<T>?)

    protected abstract fun onLose(response: Response<T>?, message: String?, failCode: Int)

    override fun setLoading(holder: Gloading.Holder?) {
        this.holder = holder
    }
}