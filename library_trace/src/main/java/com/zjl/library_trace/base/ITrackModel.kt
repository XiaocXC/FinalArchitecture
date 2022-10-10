package com.zjl.library_trace.base

/**
 * @author Xiaoc
 * @since 2022-10-10
 *
 * 定义数据埋点能力
 */
fun interface ITrackModel {

    /**
     * 埋点数据填充
     */
    fun fillTrackParams(trackParams: TrackParams)
}