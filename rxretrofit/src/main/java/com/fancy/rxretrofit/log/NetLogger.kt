package com.fancy.rxretrofit.log

import com.fancy.rxretrofit.interceptor.FormatLogInterceptor

/**
 *
 * @Description:
 * @Author: fanlei
 * @CreateDate: 14/9/2021 上午9:45
 * @Version: 1.0
 */
class NetLogger : FormatLogInterceptor.Logger {
    override fun log(message: String?) {
        XLogger.json(message ?: "")
    }
}