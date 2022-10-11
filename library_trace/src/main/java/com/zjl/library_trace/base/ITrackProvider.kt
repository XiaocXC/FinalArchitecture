package com.zjl.library_trace.base

import androidx.annotation.UiThread

/**
 * @author Xiaoc
 * @since 2022-10-11
 *
 * 埋点上传实现器
 */
interface ITrackProvider {

    /**
     * 是否开启当前实现器
     */
    abstract var enabled: Boolean

    /**
     * 该实现器的Tag
     */
    abstract var tag: String

    /**
     * 初始化
     */
    @UiThread
    abstract fun onInit()

    /**
     * 埋点数据上传操作
     */
    abstract fun onEvent(eventName: String, params: TrackParams)
}