package com.zjl.library_trace.base

import android.util.Log
import android.view.View
import com.zjl.library_trace.ext.getActivityFromView
import com.zjl.library_trace.ext.trackModel

/**
 * @author Xiaoc
 * @since 2022-10-10
 *
 * Tracker主要内容文件
 */

private const val TAG = "Tracker"

/**
 * 执行埋点上传事件
 * 该方法会从底向上遍历埋点数据，最终将完整的埋点数据进行汇总
 * @param eventName 埋点事件名
 * @param otherParams 埋点数据
 */
internal fun Any.doTrackEvent(eventName: String, otherParams: TrackParams? = null): TrackParams?{
    // TODO 1.判断是否存在上传器

    // 2. 收集完整埋点数据
    val params = if(this is View || this is ITrackModel){
        fillTrackParams(this, otherParams)
    } else {
        otherParams
    }

    if(params == null){
        return otherParams
    }

    // TODO 3.Debug下打印日志
    val logStrBuilder = StringBuilder().apply {
        append(" ")
        append("\nonEvent：$eventName")
        for ((key, value) in params) {
            append("\n$key = $value")
        }
    }

    // TODO 4.执行上传操作
    Log.d(TAG, logStrBuilder.toString())

    return params
}

/**
 * 收集完整埋点数据
 * @param node 节点 会判断节点的类型来达到从底向上遍历所有埋点数据
 * @param params 其余的单独的埋点参数
 */
internal fun fillTrackParams(node: Any?, params: TrackParams? = null): TrackParams {
    val result = params ?: TrackParams()
    var curNode = node
    while (null != curNode) {
        when (curNode) {
            is View -> {
                if (android.R.id.content == curNode.id) {
                    // 如果是Activity的根View，我们判断一下是否需要传入跨页面数据
                    val activity = getActivityFromView(curNode)
                    curNode = if (activity is IPageTrackNode) {
                        // Activity的跨页面数据
                        activity.fillTrackParams(result)
                        activity.referrerSnapshot()
                    } else {
                        null
                    }
                } else {
                    // 如果是View
                    curNode.trackModel?.fillTrackParams(result)
                    curNode = curNode.parent
                }
            }
            is ITrackNode -> {
                // 如果是普通节点
                curNode.fillTrackParams(result)
                curNode = curNode.parentNode
            }
            else -> {
                curNode = null
            }
        }
    }
    return result
}