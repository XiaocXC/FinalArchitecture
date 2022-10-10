package com.zjl.library_trace.base

/**
 * @author Xiaoc
 * @since 2022-10-10
 *
 * 定义数据埋点节点（该接口在普通埋点上可以无需处理，因为普通埋点可以根据View树来自动处理）
 */
interface ITrackNode: ITrackModel{

    /**
     * 当前节点的父亲节点
     * 用于达成一个树结构
     */
    val parentNode: ITrackNode?
}