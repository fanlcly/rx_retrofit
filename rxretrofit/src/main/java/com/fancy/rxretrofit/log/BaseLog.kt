package com.fancy.rxretrofit.log

import android.text.TextUtils
import android.util.Log

/**
 *
 * @Description:
 * @Author: fanlei
 * @CreateDate: 13/9/2021 下午5:45
 * @Version: 1.0
 */
open class BaseLog {
    companion object {
        private const val MAX_LENGTH = 50000

        fun printSub(type: LogType, tag: LogTag?, msg: String) {
            when (type) {
                LogType.V -> Log.v(tag?.gTag(), msg)
                LogType.D -> Log.d(tag?.gTag(), msg)
                LogType.I -> Log.i(tag?.gTag(), msg)
                LogType.W -> Log.w(tag?.gTag(), msg)
                LogType.E -> Log.e(tag?.gTag(), msg)
                LogType.A -> Log.wtf(tag?.gTag(), msg)
                LogType.JSON -> Log.w(tag?.gTag(), msg)
                LogType.XML -> Log.w(tag?.gTag(), msg)
            }
        }

        fun printLog(type: LogType, tag: LogTag?, message: String) {
            val msgs = message.split("\n".toRegex()).toTypedArray()
            printLine(type, tag, true)
            for (msg in msgs) {
                var index = 0
                val msgLen = msg.length
                val countOfSub = msgLen / MAX_LENGTH //输出条数
                if (countOfSub > 0) {
                    for (j in 0 until countOfSub) {
                        val len = index + MAX_LENGTH
                        val sub = msg.substring(index, if (len < msgLen) len else msgLen)
                        printSub(type, tag, "║ $sub")
                        index += MAX_LENGTH
                    }
                    printSub(type, tag, "║ " + msg.substring(index))
                } else {
                    printSub(type, tag, "║ $msg")
                }
            }
            printLine(type, tag, false)
        }

        fun printLine(type: LogType, tag: LogTag?, isTop: Boolean) {
            if (isTop) {
                printSub(
                        type, tag,
                        "╔════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════"
                )
            } else {
                printSub(
                        type, tag,
                        "╚════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════"
                )
            }
        }

        fun isEmpty(line: String): Boolean {
            return TextUtils.isEmpty(line) || TextUtils.isEmpty(line.trim { it <= ' ' }) || line == "\n" || line == "\t"
        }

    }

}