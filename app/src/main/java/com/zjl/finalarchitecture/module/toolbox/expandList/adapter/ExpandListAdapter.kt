package com.zjl.finalarchitecture.module.toolbox.expandList.adapter

import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.module.toolbox.expandList.data.ExpandListData

/**
 * @author Xiaoc
 * @since 2022-11-03
 *
 * 展开折叠Item Adapter
 */
class ExpandListAdapter : BaseQuickAdapter<ExpandListData, BaseViewHolder>(
    R.layout.item_expand_list
) {

    override fun convert(holder: BaseViewHolder, item: ExpandListData) {
        holder.setText(R.id.expandTitle, item.title)
            .setText(R.id.expandDesc, item.desc)
            .setText(R.id.expandDuration, item.date)

        handleExpand(holder, item.isExpand)

        // 理论上在onCreateViewHolder里面创建点击事件最佳
        // 我们这里在convert里面创建更快速
        holder.itemView.setOnClickListener {
            // 点击Item后我们来处理展开折叠动画
            val parent = holder.itemView.parent as? ViewGroup ?: return@setOnClickListener
            val expanded = item.isExpand

            // 启动动画
            val transition = TransitionInflater.from(holder.itemView.context)
                .inflateTransition(R.transition.expand_list_toggle)
            TransitionManager.beginDelayedTransition(parent, transition)
            // 更改数据源
            item.isExpand = !expanded

            // 更新视图显示
            handleExpand(holder, !expanded)
        }
    }

    private fun handleExpand(holder: BaseViewHolder, isExpand: Boolean) {
        holder.setGone(R.id.expandDesc, !isExpand)
            .setGone(R.id.btnDetail, !isExpand)
    }
}