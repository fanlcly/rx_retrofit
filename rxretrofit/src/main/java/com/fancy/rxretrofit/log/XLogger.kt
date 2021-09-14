package com.fancy.rxretrofit.log

import java.io.PrintWriter
import java.io.StringWriter
import java.io.Writer

/**
 *
 * @Description:
 * @Author: fanlei
 * @CreateDate: 13/9/2021 下午5:44
 * @Version: 1.0
 */
object XLogger : BaseLog() {
//    val sss:Array<String> = arrayOf("dd","ssss")
//    fun aaa(vararg msgs: Any) {
//        printLog(LogType.V, null, null,*sss)
//    }
    /**
     * 初始化配置
     */
    // 包名 - 默认的Logger输出Tag
    private var APP_PKG_NAME = "netlog"

    // Log开关 - 设置Log开关,可根据debug-release配置
    var LOG_OPEN = true

    // Log显示Level, >= 这个Level的log才显示
    var LOG_LEVEL = LogType.V

    private const val PARAM = "param"
    private const val NULL = "null"

    // V
    fun v(vararg msgs: Any) {
        printLog(LogType.V, null, null, *parseMsgs(*msgs))
    }

    fun v(tag: LogTag, vararg msgs: Any) {
        printLog(LogType.V, tag, null, *parseMsgs(*msgs))
    }

    fun v(e: Throwable) {
        printLog(LogType.V, null, e)
    }

    fun v(tag: LogTag, e: Throwable) {
        printLog(LogType.V, tag, e)
    }

    fun v(e: Throwable, vararg msgs: Any) {
        printLog(LogType.V, null, e, *parseMsgs(*msgs))
    }

    fun v(tag: LogTag, e: Throwable, vararg msgs: Any) {
        printLog(LogType.V, tag, e, *parseMsgs(*msgs))
    }

    // json日志
    fun json(jsonFormat: String) {
        printLog(LogType.JSON, null, null, jsonFormat)
    }

    fun json(tag: LogTag, jsonFormat: String) {
        printLog(LogType.JSON, tag, null, jsonFormat)
    }

    // xml日志
    fun xml(xml: String) {
        printLog(LogType.XML, null, null, xml)
    }

    fun xml(tag: LogTag, xml: String) {
        printLog(LogType.XML, tag, null, xml)
    }

    /**
     * 输出日志
     *
     * @param type
     * @param pTag
     * @param thr
     * @param msgs
     */
    @Synchronized
    private fun printLog(type: LogType, pTag: LogTag?, thr: Throwable?, vararg msgs: String) {
        if (!LOG_OPEN) {
            //检测开关
            return
        }
        if (type.intValue() < LOG_LEVEL.intValue()) {
            //检测显示等级
            return
        }
        //包装内容
        val contents = wrapperContent(pTag, thr, *msgs)
        val tag = LogTag.mk(contents[0])
        val msgStr = contents[1]
        val headStr = contents[2]
        when (type) {
            LogType.V, LogType.D, LogType.I, LogType.W, LogType.E, LogType.A -> printLog(type, tag, headStr + msgStr)
            LogType.JSON -> JsonLog.printJson(tag, headStr, msgStr)
            LogType.XML -> XmlLog.printXml(tag, headStr, msgStr)
        }
    }


    /**
     * 获取tagStr/msgStr/headStr
     *
     * @param tag
     * @param thr
     * @param msgs
     * @return
     */
    private fun wrapperContent(tag: LogTag?, thr: Throwable?, vararg msgs: String): Array<String> {
        val tagStr = if (tag?.gTag() == null) APP_PKG_NAME else tag.gTag()!!
        val msgStr = getMessagesStr(thr, *msgs)
        return arrayOf(tagStr, msgStr, "")
    }

    /**
     * 过去message + throwable 内容
     *
     * @param thr
     * @param msgs
     * @return
     */
    private fun getMessagesStr(thr: Throwable?, vararg msgs: String): String {
        return if (msgs.isNotEmpty() || thr != null) {
            val stringBuilder = StringBuilder()
            if (msgs != null && msgs.isNotEmpty()) {
                val len = msgs.size
                stringBuilder.append("\n")
                for (i in 0 until len) {
                    val message = msgs[i].replace("\n", "\n\t")
                    if (len > 1) {
                        stringBuilder.append("\t").append(PARAM).append("[").append(i).append("]").append(" = ")
                    }
                    if (message == null) {
                        stringBuilder.append("\t").append(NULL)
                    } else {
                        stringBuilder.append("\t").append(message)
                    }
                    if (i < len - 1) {
                        stringBuilder.append("\n")
                    }
                }
            }
            if (thr != null) {
                stringBuilder.append("\n")
                stringBuilder.append("* Throwable Message Start ====================\n ")
                val writer: Writer = StringWriter()
                val printWriter = PrintWriter(writer)
                thr.printStackTrace(printWriter)
                var cause = thr.cause
                while (cause != null) {
                    cause.printStackTrace(printWriter)
                    cause = cause.cause
                }
                printWriter.close()
                stringBuilder.append("\t").append(writer.toString())
                stringBuilder.append("* Throwable Message End ====================")
            }
            stringBuilder.toString()
        } else {
            """
 Log with null message"""
        }
    }

    /**
     * Object 可变参 -> 转换为 String
     *
     * @param msgs
     * @return
     */
    fun parseMsgs(vararg msgs: Any): Array<String> {
        val rmsgs: Array<String>
        val len = msgs.size
        rmsgs = Array(len) { "" }
        for (i in 0 until len) {
            rmsgs[i] = msgs[i].toString()
        }
        return rmsgs
    }

    // D
    fun d(vararg msgs: Any) {
        printLog(LogType.D, null, null, *parseMsgs(*msgs))
    }

    fun d(tag: LogTag, vararg msgs: Any) {
        printLog(LogType.D, tag, null, *parseMsgs(*msgs))
    }

    fun d(e: Throwable) {
        printLog(LogType.D, null, e)
    }

    fun d(tag: LogTag, e: Throwable) {
        printLog(LogType.D, tag, e)
    }

    fun d(e: Throwable, vararg msgs: Any) {
        printLog(LogType.D, null, e, *parseMsgs(*msgs))
    }

    fun d(tag: LogTag, e: Throwable, vararg msgs: Any) {
        printLog(LogType.D, tag, e, *parseMsgs(*msgs))
    }

    // I
    fun i(vararg msgs: Any) {
        printLog(LogType.I, null, null, *parseMsgs(*msgs))
    }

    fun i(tag: LogTag, vararg msgs: Any) {
        printLog(LogType.I, tag, null, *parseMsgs(*msgs))
    }

    fun i(e: Throwable) {
        printLog(LogType.I, null, e)
    }

    fun i(tag: LogTag, e: Throwable) {
        printLog(LogType.I, tag, e)
    }

    fun i(e: Throwable, vararg msgs: Any) {
        printLog(LogType.I, null, e, *parseMsgs(*msgs))
    }

    fun i(tag: LogTag, e: Throwable, vararg msgs: Any) {
        printLog(LogType.I, tag, e, *parseMsgs(*msgs))
    }

    // W
    fun w(vararg msgs: Any) {
        printLog(LogType.W, null, null, *parseMsgs(*msgs))
    }

    fun w(tag: LogTag, vararg msgs: Any) {
        printLog(LogType.W, tag, null, *parseMsgs(*msgs))
    }

    fun w(e: Throwable) {
        printLog(LogType.W, null, e)
    }

    fun w(tag: LogTag, e: Throwable) {
        printLog(LogType.W, tag, e)
    }

    fun w(e: Throwable, vararg msgs: Any) {
        printLog(LogType.W, null, e, *parseMsgs(*msgs))
    }

    fun w(tag: LogTag, e: Throwable, vararg msgs: Any) {
        printLog(LogType.W, tag, e, *parseMsgs(*msgs))
    }

    // E
    fun e(vararg msgs: Any) {
        printLog(LogType.E, null, null, *parseMsgs(*msgs))
    }

    fun e(tag: LogTag, vararg msgs: Any) {
        printLog(LogType.E, tag, null, *parseMsgs(*msgs))
    }

    fun e(e: Throwable) {
        printLog(LogType.E, null, e)
    }

    fun e(tag: LogTag, e: Throwable) {
        printLog(LogType.E, tag, e)
    }

    fun e(e: Throwable, vararg msgs: Any) {
        printLog(LogType.E, null, e, *parseMsgs(*msgs))
    }

    fun e(tag: LogTag, e: Throwable, vararg msgs: Any) {
        printLog(LogType.E, tag, e, *parseMsgs(*msgs))
    }

    // A
    fun a(vararg msgs: Any) {
        printLog(LogType.A, null, null, *parseMsgs(*msgs))
    }

    fun a(tag: LogTag, vararg msgs: Any) {
        printLog(LogType.A, tag, null, *parseMsgs(*msgs))
    }

    fun a(e: Throwable) {
        printLog(LogType.A, null, e)
    }

    fun a(tag: LogTag, e: Throwable) {
        printLog(LogType.A, tag, e)
    }

    fun a(e: Throwable, vararg msgs: Any) {
        printLog(LogType.A, null, e, *parseMsgs(*msgs))
    }

    fun a(tag: LogTag, e: Throwable, vararg msgs: Any) {
        printLog(LogType.A, tag, e, *parseMsgs(*msgs))
    }
}