package com.zjl.finalarchitecture.module.system.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.module.home.model.system.SystemVO

/**
 * 体系外层Adapter
 */
class SystemGroupAdapter(
    private val itemChildClicked: (SystemVO, Int) -> Unit
): BaseQuickAdapter<SystemVO, BaseViewHolder>(
    R.layout.item_system_group
) {

    override fun convert(holder: BaseViewHolder, item: SystemVO) {
        holder.itemView.tag = item

        holder.setText(R.id.system_classify_title, item.name)
        val rvSystemChild = holder.getView<RecyclerView>(R.id.rv_system_child)
        (rvSystemChild.adapter as SystemChildAdapter).setList(item.children)
    }

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        val rvSystemChild = viewHolder.getView<RecyclerView>(R.id.rv_system_child)
        /**
         * 注：我们尽量不要在convert（onBindViewHolder）中做一些固定性的视图创建工作
         * 这样可以省去一些滑动性能开销
         */
        rvSystemChild.layoutManager = FlexboxLayoutManager(context).apply {
            // 左对齐
            justifyContent = JustifyContent.FLEX_START
        }
        val adapter = SystemChildAdapter()
        adapter.setOnItemClickListener { _, _, position ->
            itemChildClicked(viewHolder.itemView.tag as SystemVO, position)

        }
        rvSystemChild.adapter = adapter
    }
}