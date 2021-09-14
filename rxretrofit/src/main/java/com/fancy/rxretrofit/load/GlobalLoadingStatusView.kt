package com.fancy.rxretrofit.load

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.fancy.rxretrofit.R
import com.fancy.rxretrofit.load.Gloading.Companion.STATUS_EMPTY_DATA
import com.fancy.rxretrofit.load.Gloading.Companion.STATUS_LOADING
import com.fancy.rxretrofit.load.Gloading.Companion.STATUS_LOAD_FAILED
import com.fancy.rxretrofit.load.Gloading.Companion.STATUS_LOAD_SUCCESS
import com.fancy.rxretrofit.utils.NetUtil.isNetworkConnected

/**
 *
 * @Description:
 * @Author: fanlei
 * @CreateDate: 13/9/2021 下午5:21
 * @Version: 1.0
 */
class GlobalLoadingStatusView : LinearLayout, View.OnClickListener {

    private var mTextView: TextView? = null
    private var mRetryTask: Runnable? = null
    private var mImageView: ImageView? = null

    constructor(context: Context?, retryTask: Runnable?) : super(context) {
        orientation = VERTICAL
        gravity = Gravity.CENTER_HORIZONTAL
        LayoutInflater.from(context).inflate(R.layout.view_loading, this, true)
        mImageView = findViewById(R.id.image)
        mTextView = findViewById(R.id.text)
        mRetryTask = retryTask
        setBackgroundColor(ContextCompat.getColor(context!!, R.color.transparent))
    }

    fun setMsgViewVisibility(visible: Boolean) {
        mTextView!!.visibility = if (visible) VISIBLE else GONE
    }

    fun setStatus(status: Int) {
        var show = true
        var onClickListener: OnClickListener? = null
        var image: Int = R.drawable.loading
        var str: Int = R.string.str_none
        when (status) {
            STATUS_LOAD_SUCCESS -> show = false
            STATUS_LOADING -> str = R.string.loading
            STATUS_LOAD_FAILED -> {
                str = R.string.load_failed
                image = R.drawable.icon_failed
                val networkConn: Boolean = isNetworkConnected(context)
                if (!networkConn) {
                    str = R.string.load_failed_no_network
                    image = R.drawable.icon_no_wifi
                }
                onClickListener = this
            }
            STATUS_EMPTY_DATA -> {
                str = R.string.empty
                image = R.drawable.icon_empty
            }
            else -> {
            }
        }
        mImageView!!.setImageResource(image)
        setOnClickListener(onClickListener)
        mTextView!!.setText(str)
        visibility = if (show) VISIBLE else GONE
    }

    override fun onClick(v: View?) {
        mRetryTask?.run()
    }
}