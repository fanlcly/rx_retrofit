package com.fancy.rxretrofit.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 *
 * @Description:
 * @Author: fanlei
 * @CreateDate: 14/7/2021 下午3:45
 * @Version: 1.0
 */
class XInterceptor(header: RequestHeader) : Interceptor {

    var header: RequestHeader? = null

    init {
        this.header = header
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request? = chain.request()
        if (header != null) {
            request = header!!.onProceedRequest(request, chain)
        }
        val response = chain.proceed(request)
        if (header != null) {
            val tmp = header!!.onProceedResponse(response, chain)
            if (tmp != null) {
                return tmp
            }
        }
        return response
    }
}