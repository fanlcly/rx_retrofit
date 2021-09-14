package com.fancy.rxretrofit.log

/**
 *
 * @Description:
 * @Author: fanlei
 * @CreateDate: 13/9/2021 下午5:55
 * @Version: 1.0
 */
object XmlLog {
    private val LINE_SEPARATOR = System.getProperty("line.separator") //行分隔

    fun printXml(tag: LogTag?, headString: String, xml: String?) {
        val message: String = if (xml == null) {
            "$headString$LINE_SEPARATOR Log with null object"
        } else {
            headString + LINE_SEPARATOR + xml
        }
        BaseLog.printLine(LogType.XML, tag, true)
        val lines = message.split(LINE_SEPARATOR.toRegex()).toTypedArray()
        for (line in lines) {
            if (!BaseLog.isEmpty(line)) {
                BaseLog.printSub(LogType.XML, tag, "║ $line")
            }
        }
        BaseLog.printLine(LogType.XML, tag, false)
    }
}