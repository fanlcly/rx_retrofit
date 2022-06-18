package com.fancy.retrofit_client.net

import com.fancy.androidutils.BuildConfig
import com.fancy.rxretrofit.NetProvider
import com.fancy.rxretrofit.interceptor.RequestHeader
import okhttp3.Interceptor

class MyNetProvider:NetProvider {
    override fun configConnectTimeoutMills(): Long {
        return 15
    }

    override fun configReadTimeoutMills(): Long {
         return 15
    }

    override fun configInterceptors(): Array<Interceptor?>? {
        return arrayOfNulls(0)
    }

    override fun configLogEnable(): Boolean {
        return BuildConfig.DEBUG
    }

    override fun configHeader(): RequestHeader? {
        return HeaderInterceptor()
    }
}