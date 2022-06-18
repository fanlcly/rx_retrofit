package com.fancy.retrofit_client.adapter

import android.widget.ImageView
import android.widget.TextView
import com.fancy.androidutils.recyclerviewhelper.base.BaseQuickAdapter
import com.fancy.androidutils.recyclerviewhelper.base.BaseViewHolder
import com.fancy.androidutils.utils.PicassoUtils
import com.fancy.retrofit_client.R

class SampleAdapter(mData: List<String?>) :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_sample, mData) {
    override fun convert(helper: BaseViewHolder, item: String?) {
        val tv = helper.getView<TextView>(R.id.tv)
        val iv = helper.getView<ImageView>(R.id.iv)

        if (item?.endsWith(".png", true) == true
            || item?.endsWith(".jpg", true) == true
            || item?.endsWith(".jpeg", true) == true
            || item?.endsWith(".gif", true) == true
        ) {
            PicassoUtils.getInstance().loadImage(mContext, item, iv)
        } else {
            tv.text = item ?: ""
        }


    }
}