package com.zjl.base.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding

/**
 * @author Xiaoc
 * @since 2021-09-20
 *
 * 基于ViewBinding的适用于Paging3的RecyclerView基类
 *
 * T 为要显示的Bean类型
 * V 为DataBinding生成的Binding类
 *
 * 该适配器适用于不同情境的Item内容，包括多种类型，只需要 T 作为基类声明，例如 Any，并 V 声明为 [ViewBinding]
 * 当适配多种类型时，需要重写 [getItemViewType] 类型返回对应布局类型
 */
abstract class ViewBoundPagingListAdapter<T: Any,V: ViewBinding>(
    diffCallback: DiffUtil.ItemCallback<T>
): PagingDataAdapter<T,ViewBoundViewHolder<T,V>>(
    diffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewBoundViewHolder<T, V> {
        return createBinding(parent,viewType) ?: throw IllegalArgumentException("错误的布局数据")
    }

    /**
     * 需要重写的方法，用于创建ViewBinding的视图
     * 你可以在此方法中进行点击事件声明等内容操作
     */
    protected abstract fun createBinding(parent: ViewGroup, viewType: Int): ViewBoundViewHolder<T,V>?

    override fun onBindViewHolder(holder: ViewBoundViewHolder<T, V>, position: Int) {
        val item = getItem(position) ?: return
        bind(holder.binding,item,position,null)
    }

    override fun onBindViewHolder(
        holder: ViewBoundViewHolder<T,V>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val item = getItem(position) ?: return

        holder._item = item
        bind(holder.binding,item,position,payloads)
    }

    /**
     * 需要重写的方法，进行对应内容的显示
     */
    protected abstract fun bind(binding: V, item: T, position: Int, payloads: MutableList<Any>?)
}