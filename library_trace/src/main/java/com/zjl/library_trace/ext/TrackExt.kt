package com.zjl.library_trace.ext

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zjl.library_trace.R
import com.zjl.library_trace.base.ITrackModel
import com.zjl.library_trace.base.TrackParams
import com.zjl.library_trace.base.doTrackEvent
import com.zjl.library_trace.base.fillTrackParams


private const val KEY_REFERRER_SNAPSHOT = "referrer_node"

/**
 * 从View中获取或设置trackModel埋点轨迹
 */
var View.trackModel: ITrackModel?
    get() = this.getTag(R.id.trace_tag) as? ITrackModel
    set(value) {
        this.setTag(R.id.trace_tag, value)
    }

/**
 * Activity里直接执行上传埋点事件的方法
 */
fun Activity?.traceEvent(eventName: String, params: TrackParams? = null): TrackParams?{
    return findRootView(this)?.doTrackEvent(eventName, params)
}

/**
 * Fragment里直接执行上传埋点事件的方法
 */
fun Fragment?.traceEvent(eventName: String, params: TrackParams? = null): TrackParams?{
    return this?.requireView()?.doTrackEvent(eventName, params)
}

/**
 * View里直接执行上传埋点事件的方法
 */
fun View?.traceEvent(eventName: String, params: TrackParams? = null): TrackParams?{
    return this?.doTrackEvent(eventName, params)
}

/**
 * 给Intent设置传递页面的埋点数据
 * @param node 埋点数据
 */
fun Intent.setReferrerSnapshot(node: ITrackModel?) {
    if (null != node) {
        setReferrerSnapshot(fillTrackParams(node))
    }
}

/**
 * 给Intent设置传递页面的埋点数据
 * @param node View视图
 */
fun Intent.setReferrerSnapshot(node: View?) {
    if (null != node) {
        setReferrerSnapshot(fillTrackParams(node))
    }
}

/**
 * 给Intent设置传递页面的埋点数据
 * @param params 埋点参数
 */
fun Intent.setReferrerSnapshot(params: TrackParams?) {
    if (null != params) {
        putExtra(KEY_REFERRER_SNAPSHOT, params)
    }
}

fun Intent.getReferrerParams(): TrackParams? {
    return getSerializableExtra(KEY_REFERRER_SNAPSHOT) as TrackParams?
}

/**
 * 得到Activity的根视图
 */
private fun findRootView(activity: Activity?): View? {
    val contentView = activity?.findViewById<ViewGroup>(android.R.id.content)
    return when (contentView?.childCount) {
        1 -> contentView.getChildAt(0)
        0 -> null
        else -> null
    }
}