package com.zjl.finalarchitecture.module.search.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.gyf.immersionbar.ImmersionBar
import com.zjl.base.adapter.DefaultLoadStateAdapter
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.autoCleared
import com.zjl.base.utils.findNavController
import com.zjl.base.utils.launchAndCollectIn
import com.zjl.finalarchitecture.NavMainDirections
import com.zjl.finalarchitecture.databinding.FragmentSearchResultBinding
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleAdapter
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleDividerItemDecoration
import com.zjl.finalarchitecture.module.search.viewmodel.SearchResultViewModel
import com.zjl.finalarchitecture.utils.ext.handlePagingStatus
import com.zy.multistatepage.MultiStateContainer
import com.zy.multistatepage.bindMultiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @author Xiaoc
 * @since  2022-05-03
 *
 * 搜索结果页面
 **/
class SearchResultFragment: BaseFragment<FragmentSearchResultBinding, SearchResultViewModel>() {

    private lateinit var searchResultStateContainer: MultiStateContainer

    private var mArticleAdapter by autoCleared<ArticleAdapter>()

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        mBinding.toolbarSearchResult.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        searchResultStateContainer = mBinding.rvSearchResult.bindMultiState()

        // 列表适配器
        mArticleAdapter = ArticleAdapter()
        mArticleAdapter.loadMoreModule.setOnLoadMoreListener {
            mViewModel.onLoadMoreData()
        }

        // Item点击事件
        mArticleAdapter.setOnItemClickListener { _, _, position ->
            // 跳转到网页
            findNavController().navigate(NavMainDirections.actionGlobalToWebFragment(mArticleAdapter.getItem(position)))
        }

        // 将BannerAdapter和ArticleAdapter整合为一个Adapter
        mBinding.rvSearchResult.adapter = mArticleAdapter
        // 分割线
        mBinding.rvSearchResult.addItemDecoration(
            ArticleDividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
    }

    override fun createObserver() {
        mViewModel.searchResults.launchAndCollectIn(viewLifecycleOwner){
            it.handlePagingStatus(mArticleAdapter, searchResultStateContainer, null){
                retryAll()
            }
        }

        mViewModel.title.launchAndCollectIn(viewLifecycleOwner){
            mBinding.toolbarSearchResult.title = it
        }
    }

    override fun retryAll() {
        super.retryAll()
        mViewModel.onRefreshData()
    }
}