package com.zjl.finalarchitecture.module.system.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zjl.base.utils.ext.toHtml
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.module.home.model.system.ClassifyVO

/**
 * 体系内层Adapter
 */
class SystemChildAdapter: BaseQuickAdapter<ClassifyVO, BaseViewHolder>(
    R.layout.item_system_child
) {
    override fun convert(holder: BaseViewHolder, item: ClassifyVO) {
        holder.setText(R.id.system_child_title, item.name.toHtml())
    }
}