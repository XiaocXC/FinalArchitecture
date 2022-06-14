package com.zjl.finalarchitecture.module.toolbox.draghelper.adapter

import androidx.recyclerview.widget.ItemTouchHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zjl.finalarchitecture.R

/**
 * @author Xiaoc
 * @since 2022-06-14
 *
 * RecyclerView拖拽的Adapter
 */
class DragStringItemAdapter: BaseQuickAdapter<String, BaseViewHolder>(
    R.layout.item_drag_string
) {

    var itemTouchHelper: ItemTouchHelper? = null

    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.drag_item_title, item)
    }
}