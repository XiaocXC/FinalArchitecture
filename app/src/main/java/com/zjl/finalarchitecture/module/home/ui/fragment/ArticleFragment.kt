package com.zjl.finalarchitecture.module.home.ui.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialSharedAxis
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.ui.onSuccess
import com.zjl.base.utils.autoCleared
import com.zjl.base.utils.findNavController
import com.zjl.base.utils.launchAndCollectIn
import com.zjl.finalarchitecture.NavMainDirections
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentArticleBinding
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleAdapter
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleBannerWrapperAdapter
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleDividerItemDecoration
import com.zjl.finalarchitecture.module.home.ui.adapter.BannerVOWrapper
import com.zjl.finalarchitecture.module.home.viewmodel.ArticleViewModel
import com.zjl.finalarchitecture.utils.ext.handlePagingStatus

/**
 * @description:首页文章
 * @author: zhou
 * @date : 2022/1/20 17:52
 */
class ArticleFragment : BaseFragment<FragmentArticleBinding, ArticleViewModel>(), OnRefreshListener,
    OnRefreshLoadMoreListener {

    companion object {
        fun newInstance() = ArticleFragment()
    }

    /**
     * 经测试，Adapter需要在视图清空时释放，否则可能持有导致内存泄漏
     */
    private var mBannerAdapter by autoCleared<ArticleBannerWrapperAdapter>()
    private var mArticleAdapter by autoCleared<ArticleAdapter>()

    override fun initViewAndEvent(savedInstanceState: Bundle?) {

        // Banner适配器
        // 开了弟弟的眼界
        mBannerAdapter = ArticleBannerWrapperAdapter(viewLifecycleOwner.lifecycle)

        // 列表适配器
        mArticleAdapter = ArticleAdapter()

        mArticleAdapter.addChildClickViewIds(R.id.item_home_collect)
        // Item内部控件点击
        mArticleAdapter.setOnItemChildClickListener { _, view, position ->
            if(view.id == R.id.item_home_collect){
                val item = mArticleAdapter.getItem(position)
                if(item.collect){
                    mViewModel.unCollectArticle(item.id)
                } else {
                    mViewModel.collectArticle(item.id)
                }
            }
        }

        // Item点击事件
        mArticleAdapter.setOnItemClickListener { _, _, position ->
            // 跳转到网页
            findNavController().navigate(NavMainDirections.actionGlobalToWebFragment(mArticleAdapter.getItem(position)))
        }

        // 将BannerAdapter和ArticleAdapter整合为一个Adapter
        mBinding.rvArticle.adapter = mArticleAdapter
        // 分割线
        mBinding.rvArticle.addItemDecoration(
            ArticleDividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )

        mBinding.refreshLayout.setOnRefreshLoadMoreListener(this)

    }

    override fun createObserver() {

        mViewModel.collectArticleEvent.launchAndCollectIn(viewLifecycleOwner){ event ->
            // 更新点赞视图
            event.onSuccess {
                mArticleAdapter.notifyItemChanged(it)
            }
        }

        // Banner数据
        mViewModel.bannerList.launchAndCollectIn(viewLifecycleOwner){ bannerList ->
            mBannerAdapter.setList(
                listOf(BannerVOWrapper(bannerList))
            )
        }

        // 文章分页数据
        mViewModel.articleList.launchAndCollectIn(viewLifecycleOwner){ uiModel ->
            uiModel.handlePagingStatus(
                mArticleAdapter,
                uiRootState,
                mBinding.refreshLayout
            ) {
                retryAll()
            }
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        mViewModel.onRefreshData()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mViewModel.onLoadMoreData()
    }

    override fun configImmersive(immersionBar: ImmersionBar): ImmersionBar? {
        // 内部Fragment不处理沉浸式，防止被覆盖
        return null
    }

    override fun retryAll() {
        super.retryAll()
        mBinding.refreshLayout.autoRefresh()
    }

}