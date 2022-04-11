package com.zjl.finalarchitecture.module.system.ui.adapter

import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.data.model.NavigationVO

/**
 * @author Xiaoc
 * @since 2022-04-11
 */
class NavigationTabAdapter(
    private val tabClicked: (NavigationTab, Int) -> Unit
): BaseQuickAdapter<NavigationTab, BaseViewHolder>(
    R.layout.item_navigation_tab
) {
    override fun convert(holder: BaseViewHolder, item: NavigationTab) {
        holder.itemView.tag = item
        holder.setText(R.id.tv_title, item.navigationVO.name)

        handleSelectedState(item.selected, holder)
    }

    override fun convert(holder: BaseViewHolder, item: NavigationTab, payloads: List<Any>) {
        if(payloads.isNullOrEmpty()){
            return
        }
        handleSelectedState(item.selected, holder)
    }

    private fun handleSelectedState(selected: Boolean, holder: BaseViewHolder){
        if(selected){
            holder.setTextColor(R.id.tv_title, Color.RED)
        } else {
            holder.setTextColor(R.id.tv_title, Color.BLACK)
        }
    }

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        viewHolder.itemView.setOnClickListener {
            tabClicked(it.tag as NavigationTab, viewHolder.absoluteAdapterPosition)
        }
    }
}

data class NavigationTab(
    var selected: Boolean,
    val navigationVO: NavigationVO
)