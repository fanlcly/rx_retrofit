package com.fancy.retrofit_client.net

import com.fancy.androidutils.utils.SpUtils
import com.fancy.rxretrofit.interceptor.RequestHeader
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class HeaderInterceptor: RequestHeader {
    override fun onProceedRequest(request: Request?, chain: Interceptor.Chain?): Request? {
        val builder = request?.newBuilder()
        builder?.addHeader("Accept", "application/json")
        builder?.addHeader("Content-Type", "application/json;charset=UTF-8")
//        builder?.addHeader("token", SpUtils.getString(BaseApplication.mContext, Constant.TOKEN))
        return builder?.build()
    }

    override fun onProceedResponse(response: Response?, chain: Interceptor.Chain?): Response? {
        return null
    }
}