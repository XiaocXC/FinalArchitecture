package com.zjl.finalarchitecture.module.system.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.module.home.model.system.SystemVO

/**
 * 体系外层Adapter
 */
class SystemGroupAdapter: BaseQuickAdapter<SystemVO, BaseViewHolder>(
    R.layout.item_system_group
) {
    override fun convert(holder: BaseViewHolder, item: SystemVO) {
        holder.setText(R.id.system_classify_title, item.name)
        val rvSystemChild = holder.getView<RecyclerView>(R.id.rv_system_child)
        (rvSystemChild.adapter as SystemChildAdapter).setList(item.children)
    }

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        val rvSystemChild = viewHolder.getView<RecyclerView>(R.id.rv_system_child)
        rvSystemChild.layoutManager = FlexboxLayoutManager(context).apply {
            // 左对齐
            justifyContent = JustifyContent.FLEX_START
        }
        rvSystemChild.adapter = SystemChildAdapter()
    }
}