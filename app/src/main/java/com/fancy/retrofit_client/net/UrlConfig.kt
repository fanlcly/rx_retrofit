package com.fancy.retrofit_client.net

import com.fancy.androidutils.BuildConfig

object UrlConfig {
    private const val TEST_URL = "http://8.143.202.139:8080/"
    // ===============================================================================
// 正式环境
    private const val PRO_URL = "http://8.143.202.139:8080/"

    @JvmField
    var BASE_URL: String? = null

    init {
        BASE_URL = if (!BuildConfig.DEBUG) { // release
            PRO_URL
        } else { // debug
            TEST_URL
        }
    }
}