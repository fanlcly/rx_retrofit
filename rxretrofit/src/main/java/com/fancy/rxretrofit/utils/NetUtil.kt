package com.fancy.rxretrofit.utils

import android.content.Context
import android.net.ConnectivityManager

/**
 *
 * @Description:
 * @Author: fanlei
 * @CreateDate: 13/9/2021 下午5:28
 * @Version: 1.0
 */
object NetUtil {
    /**
     * 判断是否有网络连接
     * @param context context
     * @return true: connected, false:not, null:unknown
     */
    @JvmStatic
    fun isNetworkConnected(mContext: Context): Boolean {
        var context = mContext
        try {
            context = context.applicationContext
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = cm.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        } catch (ignored: Exception) {
        }
        return false
    }


    /**
     * 判断WIFI网络是否可用
     *
     * @param context
     * @return
     */
    fun isWifiConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                ?: return false
        return cm.activeNetworkInfo!!.type == ConnectivityManager.TYPE_WIFI
    }

    /**
     * 获取当前网络连接的类型信息
     *
     * @return
     */
    fun getConnectedType(context: Context): Int {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected) {
            return -1 //没网
        }
        val type = activeNetworkInfo.type
        when (type) {
            ConnectivityManager.TYPE_MOBILE -> return ConnectivityManager.TYPE_MOBILE //移动数据
            ConnectivityManager.TYPE_WIFI -> return ConnectivityManager.TYPE_WIFI //WIFI
            else -> {
            }
        }
        return -1 //没网
    }

}