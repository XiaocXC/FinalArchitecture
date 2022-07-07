package com.zjl.finalarchitecture.module.home.ui.fragment

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.autoCleared
import com.zjl.base.utils.launchAndRepeatWithViewLifecycle
import com.zjl.finalarchitecture.databinding.FragmentArticleBinding
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleAdapter
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleBannerWrapperAdapter
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleDividerItemDecoration
import com.zjl.finalarchitecture.module.home.ui.adapter.BannerVOWrapper
import com.zjl.finalarchitecture.module.home.viewmodel.ArticleViewModel
import com.zjl.finalarchitecture.utils.ext.handlePagingStatus
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

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
        mArticleAdapter = ArticleAdapter {
//            mViewModel.updateCollectState(it.id, !it.collect)
        }

//        val mArticleAdapter2: ArticleAdapter by lazy { ArticleAdapter() }

        // 加载更多
//        mArticleAdapter.loadMoreModule.setOnLoadMoreListener {
//            mViewModel.loadMore()
//        }

        // 将BannerAdapter和ArticleAdapter整合为一个Adapter
        mBinding.rvArticle.adapter = mArticleAdapter
        // 分割线
        mBinding.rvArticle.addItemDecoration(
            ArticleDividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )

        // 下拉刷新
//        mBinding.refreshLayout.setOnRefreshListener(this)

        mBinding.refreshLayout.setOnRefreshLoadMoreListener(this)

    }

    override fun createObserver() {

        launchAndRepeatWithViewLifecycle {
            // Banner数据
            launch {
                mViewModel.bannerList.collect { bannerList ->
                    mBannerAdapter.setList(
                        listOf(BannerVOWrapper(bannerList))
                    )
                }
            }

            // 文章分页数据
            launch {
                mViewModel.articleList.collectLatest { uiModel ->
                    uiModel.handlePagingStatus(
                        mArticleAdapter,
                        uiRootState,
                        mBinding.refreshLayout
                    ) {
                        refresh()
                    }
                }
            }

        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        refresh()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mViewModel.loadMore()
    }

    private fun refresh() {
        // 重新请求，如果文章列表没有数据，则整个界面会重新显示loading状态
        mViewModel.initData(mArticleAdapter.itemCount <= 0)
    }


}