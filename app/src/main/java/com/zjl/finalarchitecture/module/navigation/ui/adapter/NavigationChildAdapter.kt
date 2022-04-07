package com.zjl.finalarchitecture.module.navigation.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zjl.base.utils.ext.toHtml
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.module.home.model.ArticleListVO

/**
 * 体系内层Adapter
 */
class NavigationChildAdapter: BaseQuickAdapter<ArticleListVO, BaseViewHolder>(
    R.layout.item_system_child
) {
    override fun convert(holder: BaseViewHolder, item: ArticleListVO) {
        holder.setText(R.id.system_child_title, item.title.toHtml())
    }
}