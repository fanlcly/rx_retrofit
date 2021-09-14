package com.fancy.rxretrofit.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 *
 * @Description:
 * @Author: fanlei
 * @CreateDate: 13/9/2021 下午2:56
 * @Version: 1.0
 */
interface RequestHeader {
    @Throws(IOException::class)
    fun onProceedRequest(request: Request?, chain: Interceptor.Chain?): Request?

    @Throws(IOException::class)
    fun onProceedResponse(response: Response?, chain: Interceptor.Chain?): Response?
}