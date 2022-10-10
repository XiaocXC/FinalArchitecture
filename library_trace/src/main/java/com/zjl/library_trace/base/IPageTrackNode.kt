package com.zjl.library_trace.base

/**
 * @author Xiaoc
 * @since 2022-10-10
 *
 * 定义数据埋点节点（跨页面的）
 */
interface IPageTrackNode: ITrackModel {

    /**
     * 映射KeyMap
     * 跨页面后，上一个页面的埋点数据可能需要传递过来
     * 例如 上一个页面 cur_page = "main" 传递来后
     * 这个 cur_page Key可能需要变成 from_page
     * 所以这里返回的Map就是对应的映射
     * @return 对应Key的映射
     */
    fun referrerKeyMap(): Map<String, String>?

    /**
     * 返回对应需要处理的跨页面的埋点数据
     * @return ITrackNode 节点埋点数据
     */
    fun referrerSnapshot(): ITrackNode?
}