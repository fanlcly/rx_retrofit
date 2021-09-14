package com.fancy.rxretrofit

import com.fancy.rxretrofit.callback.BaseCallBack
import com.fancy.rxretrofit.interceptor.FormatLogInterceptor
import com.fancy.rxretrofit.interceptor.RequestHeader
import com.fancy.rxretrofit.interceptor.XInterceptor
import com.fancy.rxretrofit.load.Gloading
import com.fancy.rxretrofit.log.NetLogger
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

/**
 *
 * @Description:
 * @Author: fanlei
 * @CreateDate: 13/9/2021 下午2:44
 * @Version: 1.0
 */
class HttpClient private constructor() {
    private val connectTimeoutMills: Long = 15
    private val readTimeoutMills: Long = 15
    private val sProvider: NetProvider? = null

    private val retrofitMap: MutableMap<String, Retrofit> = HashMap()


    companion object {
        @JvmStatic
        private var sProvider: NetProvider? = null

        @JvmStatic
        val instance: HttpClient by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            HttpClient()
        }


        /**
         * 请求失败重连次数
         */
        private const val RETRY_COUNT = 0


        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        @JvmStatic
        fun <S> get(baseUrl: String?, service: Class<S>?): S {
            return instance.getRetrofit(baseUrl ?: "", false)?.create(service)!!
        }

        @JvmStatic
        fun registerProvider(provider: NetProvider?) {
            sProvider = provider
        }

        @JvmStatic
        fun getCommonProvider(): NetProvider? {
            return sProvider
        }

        @JvmStatic
        fun clearCache() {
            instance.retrofitMap.clear()
        }
        @JvmStatic
        fun <P> request(call: Call<P>, callBack: BaseCallBack<P>) {
            request(null, call, callBack)
        }
        @JvmStatic
        fun <P> request(holder: Gloading.Holder?, call: Call<P>, callBack: BaseCallBack<P>) {
            if (holder != null) {
                holder.showLoading()
                callBack.setLoading(holder)
            }
            call.enqueue(callBack)
        }


        /**
         * 检查provider配置
         *
         * @param provider
         */
        fun checkProvider(provider: NetProvider?) {
            checkNotNull(provider) { "must register provider first" }
        }


        /**
         * 设置订阅 和 所在的线程环境
         */
        fun <W> toSubscribe(o: Observable<W>, s: DisposableObserver<W>) {
            o.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .retry(RETRY_COUNT.toLong()) //请求失败重连次数
                    .subscribe(s)
        }


    }


    fun getRetrofit(baseUrl: String, useRx: Boolean): Retrofit? {
        return getRetrofit(baseUrl, sProvider, useRx)
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun getRetrofit(baseUrl: String, provider: NetProvider?, useRx: Boolean): Retrofit? {
        if (retrofitMap[baseUrl] != null) return retrofitMap[baseUrl]
        checkProvider(provider)
        val builder: Retrofit.Builder = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getClient(provider))
                .addConverterFactory(GsonConverterFactory.create())
        if (useRx) {
            builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        }
        val retrofit: Retrofit = builder.build()
        retrofitMap[baseUrl] = retrofit
        return retrofit
    }


    @Suppress(
            "IMPLICIT_BOXING_IN_IDENTITY_EQUALS",
            "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"
    )
    private fun getClient(provider: NetProvider?): OkHttpClient? {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
        builder.connectTimeout(
                if (provider?.configConnectTimeoutMills() != 0L) provider?.configConnectTimeoutMills()
                        ?: connectTimeoutMills else connectTimeoutMills,
                TimeUnit.SECONDS
        )
        builder.readTimeout(
                if (provider?.configReadTimeoutMills() != 0L) provider?.configReadTimeoutMills()
                        ?: connectTimeoutMills else readTimeoutMills,
                TimeUnit.SECONDS
        )
        builder.retryOnConnectionFailure(true)
        val header: RequestHeader = provider?.configHeader()!!
        builder.addNetworkInterceptor(XInterceptor(header))
        val interceptors: Array<Interceptor?>? = provider.configInterceptors()
        if (interceptors != null && interceptors.isNotEmpty()) {
            for (interceptor in interceptors) {
                builder.addInterceptor(interceptor)
            }
        }
        if (provider.configLogEnable()) {
            val netLogger = FormatLogInterceptor(NetLogger())
            netLogger.setLevel(if (provider.configLogEnable()) FormatLogInterceptor.Level.BODY else FormatLogInterceptor.Level.NONE)
            builder.addInterceptor(netLogger)
        }
        return builder.build()
    }
}