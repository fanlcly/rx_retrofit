package com.fancy.rxretrofit

import com.fancy.rxretrofit.interceptor.RequestHeader
import okhttp3.Interceptor

/**
 *
 * @Description:
 * @Author: fanlei
 * @CreateDate: 13/9/2021 下午2:48
 * @Version: 1.0
 */
interface NetProvider {

    fun configConnectTimeoutMills(): Long

    fun configReadTimeoutMills(): Long

    fun configInterceptors(): Array<Interceptor?>?

    fun configLogEnable(): Boolean

    fun configHeader(): RequestHeader?
}