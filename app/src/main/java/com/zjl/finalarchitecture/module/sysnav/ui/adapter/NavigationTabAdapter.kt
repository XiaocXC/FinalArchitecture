package com.zjl.finalarchitecture.module.sysnav.ui.adapter

import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zjl.base.utils.ext.getAttrColor
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.data.model.NavigationVO

/**
 * @author Xiaoc
 * @since 2022-04-11
 *
 * 导航内容左侧的Tab Adapter
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
        val tvTitle = holder.getView<TextView>(R.id.tv_title)
        val container = holder.getView<View>(R.id.container_tab_title)
        if(selected){
            container.setBackgroundResource(R.drawable.shape_navigation_tab_s)
            holder.setTextColor(R.id.tv_title, Color.WHITE)
        } else {
            container.setBackgroundResource(0)
            holder.setTextColor(R.id.tv_title, context.getAttrColor(R.attr.colorOnSurface))
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