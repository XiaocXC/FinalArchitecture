package com.zjl.finalarchitecture.module.home.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.data.model.ArticleListVO

class PlazaArticleAdapter : BaseQuickAdapter<ArticleListVO, BaseViewHolder>(R.layout.item_plaza_list_data) {

    override fun convert(holder: BaseViewHolder, item: ArticleListVO) {
    }

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
    }
}