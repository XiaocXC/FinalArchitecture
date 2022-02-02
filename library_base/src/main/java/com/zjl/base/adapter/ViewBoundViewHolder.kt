package com.zjl.base.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * @author Xiaoc
 * @since 2021/7/11
 *
 * 适用于 [DataBoundListAdapter] 的ViewHolder视图缓存
 * 基于ViewBinding
 * 内置一个非空Item，可以直接使用，如果你需要更多操作，你可以集成该类实现更多内容
 */
open class ViewBoundViewHolder<T,out V: ViewBinding> constructor(
    val binding: V
): RecyclerView.ViewHolder(binding.root) {

    internal var _item: T? = null

    val item: T get() = _item!!
}