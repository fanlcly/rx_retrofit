package com.fancy.rxretrofit.interceptor

import android.util.Log
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.internal.http.HttpHeaders
import okhttp3.internal.platform.Platform
import okio.Buffer
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.EOFException
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException
import java.util.concurrent.TimeUnit

/**
 *
 * @Description:
 * @Author: fanlei
 * @CreateDate: 14/7/2021 下午2:17
 * @Version: 1.0
 */
class FormatLogInterceptor(logger: Logger) : Interceptor {
    private val UTF8 = Charset.forName("UTF-8")

    enum class Level {
        /**
         * 没有日志
         */
        NONE,

        /**
         * 记录请求和响应行。
         *
         *
         * 例：
         * --> POST /greeting http/1.1 (3-byte body)
         * 例：
         * <-- 200 OK (22ms, 6-byte body)
         */
        BASIC,

        /**
         * 记录请求和响应行及其各自的标头。
         *
         *
         * 例：
         * --> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         * --> END POST
         *
         *
         * 例：
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         * <-- END HTTP
         */
        HEADERS,

        /**
         * 记录请求和响应行及其各自的头和主体（如果存在）。
         *
         *
         * 例：
         * --> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         *
         *
         * 例：
         * --> END POST
         *
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         *
         *
         * Hello!
         * <-- END HTTP
         */
        BODY
    }

    interface Logger {
        fun log(message: String?)

        companion object {
            /**
             * A [Logger] defaults output appropriate for the current platform.
             */
            val DEFAULT: Logger = object : Logger {
                override fun log(message: String?) {
                    Platform.get().log(Log.INFO, message, null)
                }
            }
        }
    }

    constructor() : this(Logger.DEFAULT)

    private var logger: Logger? = null

    init {
        this.logger = logger
    }


    @Volatile
    private var level = Level.NONE

    /**
     * 更改监听器的级别。
     *
     * @param level
     * @return
     */
    fun setLevel(level: Level?): FormatLogInterceptor? {
        if (level == null) throw NullPointerException("level == null. Use Level.NONE instead.")
        this.level = level
        return this
    }

    fun getLevel(): Level? {
        return level
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response? {
        val level = level
        val request = chain.request()
        if (level == Level.NONE) {
            return chain.proceed(request)
        }
        val logBody = level == Level.BODY
        val logHeaders = logBody || level == Level.HEADERS
        val requestBody = request.body()
        val hasRequestBody = requestBody != null
        val connection = chain.connection()
        val requestBuffer = StringBuilder()
        val protocol = if (connection != null) connection.protocol() else Protocol.HTTP_1_1

        var requestStartMessage =
            """--> ${request.method()} ${request.url()} $protocol
"""
        if (!logHeaders && hasRequestBody) {
            requestStartMessage += """ (${requestBody!!.contentLength()}-byte body)
"""
        }
        requestBuffer.append(requestStartMessage)
        if (logHeaders) {
            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody!!.contentType() != null) {
                    requestBuffer.append("Content-Type: ").append(requestBody.contentType())
                        .append("\n")
                }
                if (requestBody.contentLength() != -1L) {
                    requestBuffer.append("Content-Length: ").append(requestBody.contentLength())
                        .append("\n")
                }
            }
            val headers = request.headers()
            var i = 0
            val count = headers.size()
            while (i < count) {
                val name = headers.name(i)
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equals(
                        name, ignoreCase = true
                    ) && !"Content-Length".equals(name, ignoreCase = true)
                ) {
                    requestBuffer.append(name).append(": ").append(headers.value(i)).append("\n")
                }
                i++
            }
            if (!logBody || !hasRequestBody) {
                requestBuffer.append("--> END ").append(request.method()).append("\n")
            } else if (bodyEncoded(request.headers())) {
                requestBuffer.append("--> END ").append(request.method())
                    .append(" (encoded body omitted)").append("\n")
            } else {
                val buffer = Buffer()
                requestBody!!.writeTo(buffer)
                var charset = UTF8
                val contentType = requestBody.contentType()
                if (contentType != null) {
                    charset = contentType.charset(UTF8)
                }
                if (isPlaintext(buffer)) {
                    requestBuffer.append(buffer.readString(charset)).append("\n")
                    requestBuffer.append("--> END ").append(request.method()).append(" (").append(
                        requestBody.contentLength()
                    ).append("-byte body)")
                } else {
                    requestBuffer.append("--> END ").append(request.method()).append(" (binary ")
                        .append(
                            requestBody.contentLength()
                        ).append("-byte body omitted)")
                }
            }
        }
        logger!!.log(requestBuffer.toString())
        val startNs = System.nanoTime()
        val response: Response
        val responseBuilder = StringBuilder()
        response = try {
            chain.proceed(request)
        } catch (e: Exception) {
            logger!!.log("<-- HTTP FAILED: $e")
            throw e
        }
        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
        val responseBody = response.body()
        val contentLength = responseBody!!.contentLength()
        val bodySize = if (contentLength != -1L) "$contentLength-byte" else "unknown-length"
        responseBuilder.append("<-- ").append(response.code()).append(' ')
            .append(response.message()).append(' ')
            .append(response.request().url()).append(" (").append(tookMs).append("ms").append(
                if (!logHeaders) ", "
                        + bodySize + " body" else ""
            ).append(')').append("\n")
        if (logHeaders) {
            val headers = response.headers()
            var i = 0
            val count = headers.size()
            while (i < count) {
                responseBuilder.append(headers.name(i)).append(": ").append(headers.value(i))
                    .append("\n")
                i++
            }
            if (!logBody || !HttpHeaders.hasBody(response)) {
                responseBuilder.append("<-- END HTTP").append("\n")
            } else if (bodyEncoded(response.headers())) {
                responseBuilder.append("<-- END HTTP (encoded body omitted)").append("\n")
            } else {
                val source = responseBody.source()
                source.request(Long.MAX_VALUE) // Buffer the entire body.
                val buffer = source.buffer()
                var charset = UTF8
                val contentType = responseBody.contentType()
                if (contentType != null) {
                    charset = try {
                        contentType.charset(UTF8)
                    } catch (e: UnsupportedCharsetException) {
                        responseBuilder.append("Couldn't decode the response body; charset is likely malformed.")
                        responseBuilder.append("<-- END HTTP")
                        return response
                    }
                }
                if (!isPlaintext(buffer)) {
                    responseBuilder.append("<-- END HTTP (binary ").append(buffer.size())
                        .append("-byte body omitted)")
                    return response
                }
                if (contentLength != 0L) {
                    val msg = buffer.clone().readString(charset)
                    var message: String
                    try {
                        val str = checkStartChar(msg)
                        if (str.startsWith("{")) {
                            val jsonObject = JSONObject(msg)
                            message = jsonObject.toString(4)
                            message = """
                            
                            $message
                            """.trimIndent()
                        } else if (str.startsWith("[")) {
                            val jsonArray = JSONArray(msg)
                            message = jsonArray.toString(4)
                            message = """
                            
                            $message
                            """.trimIndent()
                        } else {
                            message = msg
                        }
                    } catch (e: JSONException) {
                        message = msg
                    }
                    responseBuilder.append(message).append("\n")
                }
                responseBuilder.append("<-- END HTTP (").append(buffer.size()).append("-byte body)")
            }
        }
        logger!!.log(responseBuilder.toString())
        //        logger.log(responseBodyBuffer.toString());
        return response
    }

    /**
     * 判断是否是纯文本
     */
    fun isPlaintext(buffer: Buffer): Boolean {
        return try {
            val prefix = Buffer()
            val byteCount = if (buffer.size() < 64) buffer.size() else 64
            buffer.copyTo(prefix, 0, byteCount)
            for (i in 0..15) {
                if (prefix.exhausted()) {
                    break
                }
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
            }
            true
        } catch (e: EOFException) {
            false // Truncated UTF-8 sequence.
        }
    }

    private fun bodyEncoded(headers: Headers): Boolean {
        val contentEncoding = headers["Content-Encoding"]
        return contentEncoding != null && !contentEncoding.equals("identity", ignoreCase = true)
    }

    private fun checkStartChar(msg: String): String {
        return if (msg.startsWith("\n") || msg.startsWith("\t")) {
            checkStartChar(msg.substring(1, msg.length))
        } else msg
    }
}