package com.zjl.finalarchitecture.module.home.ui.adapter

import androidx.lifecycle.Lifecycle
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zhpan.bannerview.BannerViewPager
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.data.model.BannerVO

/**
 * @author Xiaoc
 * @since 2022-02-02
 *
 * 文章栏目的Banner适配器包裹
 * ArticleFragment使用ConcatAdapter包裹不同的内容
 * 因NestedScrollView在Navigation跳转并返回后无法保留滑动状态
 * 我们则将banner的视图统一放置到RecyclerView中使用
 */
class ArticleBannerWrapperAdapter(
    private val lifecycle: Lifecycle
): BaseQuickAdapter<BannerVOWrapper, BaseViewHolder>(
    R.layout.item_article_wrapper_banner
) {

    override fun convert(holder: BaseViewHolder, item: BannerVOWrapper) {
        val bannerViewPager = holder.getView<BannerViewPager<BannerVO>>(R.id.article_banner)
        bannerViewPager.create(item.bannerList)
    }

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        val bannerViewPager = viewHolder.getView<BannerViewPager<BannerVO>>(R.id.article_banner)
        bannerViewPager.adapter = ArticleBannerAdapter()
        bannerViewPager.setIndicatorSliderWidth(10, 10)
        // Banner轮播图设置lifecycle
        bannerViewPager.setLifecycleRegistry(lifecycle)
    }

}

data class BannerVOWrapper(
    val bannerList: List<BannerVO>
)