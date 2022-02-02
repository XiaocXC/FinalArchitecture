package com.zjl.finalarchitecture.module.home.ui.adapter

import com.google.android.material.imageview.ShapeableImageView
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder
import com.zjl.base.utils.ext.load
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.module.home.model.BannerVO

/**
 * @author Xiaoc
 * @since 2022-02-02
 *
 * 文章栏目的Banner适配器
 */
class ArticleBannerAdapter: BaseBannerAdapter<BannerVO>() {

    override fun bindData(
        holder: BaseViewHolder<BannerVO>?,
        data: BannerVO?,
        position: Int,
        pageSize: Int
    ) {
        holder?.let {
            val ivBanner = it.findViewById<ShapeableImageView>(R.id.iv_banner)
            ivBanner.load(data?.imgUrl)
        }
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.banner_item_article_top
    }
}