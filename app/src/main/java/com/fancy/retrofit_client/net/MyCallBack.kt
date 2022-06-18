package com.fancy.retrofit_client.net

import android.content.Context
import android.widget.Toast
import com.fancy.rxretrofit.NetError
import com.fancy.rxretrofit.callback.BaseCallBack
import com.fancy.rxretrofit.log.XLogger
import retrofit2.Response

/**
 *
 * @Description:
 * @Author: fanlei
 * @CreateDate: 18/6/2022 上午8:00
 * @Version: 1.0
 */
abstract class MyCallBack<T>(mContext: Context) : BaseCallBack<BaseModle<T>>(mContext) {

    override fun onObtain(response: Response<BaseModle<T>>?) {
        if (response?.raw()?.code() == 200) {

            try {
                if (response.body()?.code == 200) {
                    onSuccess(response.body()?.data)
                } else {
                    onFail(response.body()?.message, response.body()?.code)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                XLogger.d(e.message + e.localizedMessage)
                onFail("服务器异常", -1)
            }
        } else {
            onFail(NetError.buildError(response)?.getErrorMessage(), response?.raw()?.code())
        }
    }

    override fun onLose(response: Response<BaseModle<T>>?, message: String?, failCode: Int) {
        onFail(message, failCode)
    }

    abstract fun onSuccess(response: T?)

    fun onFail(message: String?, failCode: Int?) {
        if (message?.isNotEmpty() == true) {
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
        }
    }
}