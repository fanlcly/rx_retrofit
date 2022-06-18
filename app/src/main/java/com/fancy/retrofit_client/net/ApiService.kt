package com.fancy.retrofit_client.net

import com.alibaba.fastjson.JSONObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiService {


    /**
     * 随机图片
     *
     * @return
     */
    @GET("https://www.dmoe.cc/random.php")
    fun randomImage(@QueryMap jsonObject: JSONObject): Call<BaseModle<JSONObject>>

}