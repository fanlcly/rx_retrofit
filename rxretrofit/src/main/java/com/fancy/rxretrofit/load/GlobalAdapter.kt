package com.fancy.rxretrofit.load

import android.view.View

/**
 *
 * @Description:
 * @Author: fanlei
 * @CreateDate: 13/9/2021 下午5:20
 * @Version: 1.0
 */
class GlobalAdapter : Gloading.Adapter {
    override fun getView(holder: Gloading.Holder?, convertView: View?, status: Int): View? {
        var loadingStatusView: GlobalLoadingStatusView? = null
        if (convertView is GlobalLoadingStatusView) {
            loadingStatusView = convertView
        }
        if (loadingStatusView == null) {
            loadingStatusView = GlobalLoadingStatusView(holder?.context, holder?.retryTask)
        }
        loadingStatusView.setStatus(status)
        val data: Any? = holder!!.getData()
        //show or not show msg view
        //show or not show msg view
        val hideMsgView = "hide_loading" == data
        loadingStatusView.setMsgViewVisibility(!hideMsgView)
        return loadingStatusView
    }
}