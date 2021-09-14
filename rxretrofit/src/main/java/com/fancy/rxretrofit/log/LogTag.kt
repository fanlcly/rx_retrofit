package com.fancy.rxretrofit.log

/**
 *
 * @Description:
 * @Author: fanlei
 * @CreateDate: 13/9/2021 下午5:46
 * @Version: 1.0
 */
class LogTag(tag: String?) {
    private var tag: String? = null

    init {
        this.tag = tag
    }


    companion object {
        @JvmStatic
        fun mk(tag: String?): LogTag? {
            return LogTag(tag)
        }
    }

    fun gTag(): String? {
        return tag
    }
}