package com.fancy.rxretrofit.impl

import com.fancy.rxretrofit.load.Gloading

/**
 *
 * @Description:
 * @Author: fanlei
 * @CreateDate: 13/9/2021 下午5:36
 * @Version: 1.0
 */
interface LoadingListener {
    fun setLoading(holder: Gloading.Holder?)
}