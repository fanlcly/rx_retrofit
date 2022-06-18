package com.fancy.retrofit_client.net

import com.fancy.rxretrofit.HttpClient


class Api private constructor()  {
    companion object {
        val instance: ApiService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            HttpClient.instance
                .getRetrofit(UrlConfig.BASE_URL ?: "", MyNetProvider(), false)!!.create(
                    ApiService::class.java
                )
        }
    }
}