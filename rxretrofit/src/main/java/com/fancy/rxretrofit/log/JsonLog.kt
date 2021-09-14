package com.fancy.rxretrofit.log

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 *
 * @Description:
 * @Author: fanlei
 * @CreateDate: 13/9/2021 下午5:54
 * @Version: 1.0
 */
object JsonLog {
    private const val JSON_INDENT = 4 //缩进

    private val LINE_SEPARATOR = System.getProperty("line.separator") //行分隔

    fun printJson(tag: LogTag?, headString: String, msg: String) {
        var message: String
        try {
            val str = checkStartChar(msg)
            when {
                str.startsWith("{") -> {
                    val jsonObject = JSONObject(msg)
                    message = jsonObject.toString(JSON_INDENT)
                    message = """
                        
                        $message
                        """.trimIndent()
                }
                str.startsWith("[") -> {
                    val jsonArray = JSONArray(msg)
                    message = jsonArray.toString(JSON_INDENT)
                    message = """
                        
                        $message
                        """.trimIndent()
                }
                else -> {
                    message = msg
                }
            }
        } catch (e: JSONException) {
            message = msg
        }
        BaseLog.printLine(LogType.JSON, tag, true)
        message = headString + LINE_SEPARATOR + message
        val lines = message.split(LINE_SEPARATOR.toRegex()).toTypedArray()
        for (line in lines) {
            if (!BaseLog.isEmpty(line)) {
                BaseLog.printSub(LogType.JSON, tag, "║ $line")
            }
        }
        BaseLog.printLine(LogType.JSON, tag, false)
    }

    private fun checkStartChar(msg: String): String {
        return if (msg.startsWith("\n") || msg.startsWith("\t")) {
            checkStartChar(msg.substring(1, msg.length))
        } else msg
    }
}