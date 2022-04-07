package com.zjl.finalarchitecture.module.navigation.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.module.home.model.navigation.NavigationVO
import com.zjl.finalarchitecture.module.system.ui.adapter.SystemChildAdapter

/**
 * @author Xiaoc
 * @since 2022-04-07
 */
class NavigationGroupAdapter(
    private val itemChildClicked: (NavigationVO, Int) -> Unit
): BaseQuickAdapter<NavigationVO, BaseViewHolder>(
    R.layout.item_system_group
) {

    override fun convert(holder: BaseViewHolder, item: NavigationVO) {
        holder.itemView.tag = item

        holder.setText(R.id.system_classify_title, item.name)
        val rvSystemChild = holder.getView<RecyclerView>(R.id.rv_system_child)
        (rvSystemChild.adapter as NavigationChildAdapter).setList(item.articles)
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
        val adapter = NavigationChildAdapter()
        adapter.setOnItemClickListener { _, _, position ->
            itemChildClicked(viewHolder.itemView.tag as NavigationVO, position)

        }
        rvSystemChild.adapter = adapter
    }
}