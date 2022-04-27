package com.zjl.finalarchitecture.module.toolbox.multilist.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zjl.finalarchitecture.R

/**
 * @author Xiaoc
 * @since 2022-04-27
 */
class MultiListExpandChildAdapter: BaseQuickAdapter<String, BaseViewHolder>(
    R.layout.item_multi_list_expand_child
) {
    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.tv_expand_des ,item)
    }
}