package com.fancy.retrofit_client.net

data class BaseModle<T>(
    var data: T?,
    var code: Int?,
    var message: String?,
    var imgurl: String?
)