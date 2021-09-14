package com.fancy.rxretrofit.log

import android.util.Log

/**
 *
 * @Description:
 * @Author: fanlei
 * @CreateDate: 13/9/2021 下午5:46
 * @Version: 1.0
 */
enum class LogType(type: Int) {
    //逐渐增大
    V(Log.VERBOSE), D(Log.DEBUG), I(Log.INFO), W(Log.WARN), E(Log.ERROR), A(Log.ASSERT), JSON(Log.ASSERT + 1), XML(
            Log.ASSERT + 2), NULL(Log.ASSERT + 4);

    private var mType = 0x1 // 默认Log类型
    fun intValue(): Int {
        return mType
    }

    override fun toString(): String {
        when (mType) {
            0x1 -> return "V"
            0x2 -> return "D"
            0x3 -> return "I"
            0x4 -> return "W"
            0x5 -> return "E"
            0x6 -> return "A"
            0x7 -> return "JSON"
            0x8 -> return "XML"
            0x9 -> return "NULL"
        }
        return super.toString()
    }

    init {
        mType = type
    }
}