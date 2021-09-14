package com.fancy.rxretrofit.load

import android.R
import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import java.lang.Float.MAX_VALUE

/**
 *
 * @Description:
 * @Author: fanlei
 * @CreateDate: 13/9/2021 下午4:34
 * @Version: 1.0
 */
class Gloading private constructor() {
    private var mAdapter: Adapter? = null

    companion object {
        const val STATUS_LOADING = 1
        const val STATUS_LOAD_SUCCESS = 2
        const val STATUS_LOAD_FAILED = 3
        const val STATUS_EMPTY_DATA = 4

        private var DEBUG = false


        /**
         * 全局使用的默认Gloading对象
         * @return 默认Gloading对象
         */
        val mDefault: Gloading by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            Gloading()
        }

        /**
         * 是否设置调试模式
         * @param debug true:debug 模式, false:非 debug 模式
         */
        fun debug(debug: Boolean) {
            DEBUG = debug
        }

        /**
         * 创建不同于默认Gloading的新Gloading
         * @param adapter 另一个不同于默认适配器的适配器
         * @return Gloading
         */
        fun from(adapter: Adapter?): Gloading? {
            val gloading = Gloading()
            gloading.mAdapter = adapter
            return gloading
        }

        /**
         * 初始化默认的加载状态视图创建者([Adapter])
         * @param adapter 用于创建所有状态视图的适配器
         */
        fun initDefault(adapter: Adapter?) {
            mDefault.mAdapter = adapter
        }

        private fun printLog(msg: String) {
            if (DEBUG) {
                Log.e("Gloading", msg)
            }
        }
    }


    /**
     * 提供显示当前加载状态的视图
     */
    interface Adapter {
        /**
         * 获取当前状态的视图
         * @param holder Holder
         * @param convertView
         * @param status 当前状态
         * @return 要显示的状态视图。可能需要转换视图以便重用。
         * @see Holder
         */
        fun getView(holder: Holder?, convertView: View?, status: Int): View?
    }


    /**
     * Gloading（加载状态视图）包装整个activity
     *
     * @param activity 当前的activity
     * @return holder of Gloading
     */
    fun wrap(activity: Activity): Holder? {
        val wrapper = activity.findViewById<ViewGroup>(R.id.content)
        return Holder(mAdapter!!, activity, wrapper)
    }

    /**
     * Gloading（加载状态视图）包装特定视图。
     * @param view 要包装的视图
     * @return Holder
     */
    fun wrap(view: View): Holder? {
        val wrapper = FrameLayout(view.context)
        val lp = view.layoutParams
        if (lp != null) {
            wrapper.layoutParams = lp
        }
        if (view.parent != null) {
            val parent = view.parent as ViewGroup
            val index = parent.indexOfChild(view)
            parent.removeView(view)
            parent.addView(wrapper, index)
        }
        val newLp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        wrapper.addView(view, newLp)
        return Holder(mAdapter!!, view.context, wrapper)
    }

    /**
     * loadingStatusView显示使用相同LayoutParams对象覆盖视图
     * 此方法对于RelativeLayout和ConstraintLayout很有用
     * @param view 需要显示加载状态的视图
     * @return Holder
     */
    fun cover(view: View): Holder? {
        val parent = view.parent
                ?: throw RuntimeException("view has no parent to show gloading as cover!")
        val viewGroup = parent as ViewGroup
        val wrapper = FrameLayout(view.context)
        viewGroup.addView(wrapper, view.layoutParams)
        return Holder(mAdapter!!, view.context, wrapper)
    }

    /**
     * Gloading holder
     * create by [Gloading.wrap] or [Gloading.wrap]
     * 显示所有状态视图的核心API
     */
    class Holder(adapter: Adapter, mContext: Context, wrapper: ViewGroup) {
        private val mAdapter: Adapter?
        val context: Context?

        /**
         * get wrapper
         * @return container of gloading
         */
        val wrapper: ViewGroup?

        init {
            mAdapter = adapter
            this.context = mContext
            this.wrapper = wrapper
        }

        /**
         * get retry task
         * @return retry task
         */
        var retryTask: Runnable? = null
            private set
        private var mCurStatusView: View? = null


        private var curState = 0
        private val mStatusViews = SparseArray<View>(4)
        private var mData: Any? = null

        /**
         * 在用户单击“加载失败”页中的“重试”按钮时设置重试任务
         * @param task 当用户在加载失败的UI中单击时，运行此任务
         * @return this
         */
        fun withRetry(task: Runnable?): Holder {
            retryTask = task
            return this
        }

        /**
         * 设置扩展数据
         * @param data
         * @return this
         */
        fun withData(data: Any?): Holder {
            mData = data
            return this
        }

        /** show UI for status: [.STATUS_LOADING]  */
        fun showLoading() {
            showLoadingStatus(Gloading.STATUS_LOADING)
        }

        /** show UI for status: [.STATUS_LOAD_SUCCESS]  */
        fun showLoadSuccess() {
            showLoadingStatus(Gloading.STATUS_LOAD_SUCCESS)
        }

        /** show UI for status: [.STATUS_LOAD_FAILED]  */
        fun showLoadFailed() {
            showLoadingStatus(Gloading.STATUS_LOAD_FAILED)
        }

        /** show UI for status: [.STATUS_EMPTY_DATA]  */
        fun showEmpty() {
            showLoadingStatus(Gloading.STATUS_EMPTY_DATA)
        }

        /**
         * 显示特定状态用户界面
         * @param status status
         * @see .showLoading
         * @see .showLoadFailed
         * @see .showLoadSuccess
         * @see .showEmpty
         */
        private fun showLoadingStatus(status: Int) {
            if (curState == status || !validate()) {
                return
            }
            curState = status
            //first try to reuse status view
            var convertView = mStatusViews[status]
            if (convertView == null) {
                //secondly try to reuse current status view
                convertView = mCurStatusView
            }
            try {
                //call customer adapter to get UI for specific status. convertView can be reused
                val view = mAdapter!!.getView(this, convertView, status)
                if (view == null) {
                    printLog(mAdapter.javaClass.name + ".getView returns null")
                    return
                }
                if (view !== mCurStatusView || wrapper!!.indexOfChild(view) < 0) {
                    if (mCurStatusView != null) {
                        wrapper!!.removeView(mCurStatusView)
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        view.elevation = MAX_VALUE
                    }
                    wrapper!!.addView(view)
                    val lp = view.layoutParams
                    if (lp != null) {
                        lp.width = ViewGroup.LayoutParams.MATCH_PARENT
                        lp.height = ViewGroup.LayoutParams.MATCH_PARENT
                    }
                } else if (wrapper.indexOfChild(view) != wrapper.childCount - 1) {
                    // make sure loading status view at the front
                    view.bringToFront()
                }
                mCurStatusView = view
                mStatusViews.put(status, view)
            } catch (e: Exception) {
                if (Gloading.DEBUG) {
                    e.printStackTrace()
                }
            }
        }

        private fun validate(): Boolean {
            if (mAdapter == null) {
                printLog("Gloading.Adapter is not specified.")
            }
            if (context == null) {
                printLog("Context is null.")
            }
            if (wrapper == null) {
                printLog("The mWrapper of loading status view is null.")
            }
            return mAdapter != null && context != null && wrapper != null
        }

        /**
         *
         * get extension data
         * @param <T> return type
         * @return data
        </T> */
        fun getData(): Any? {
            try {
                return mData
            } catch (e: Exception) {
                if (Gloading.DEBUG) {
                    e.printStackTrace()
                }
            }
            return null
        }
    }
}