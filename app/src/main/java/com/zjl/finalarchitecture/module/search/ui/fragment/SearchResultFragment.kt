package com.zjl.finalarchitecture.module.search.ui.fragment

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.zjl.base.adapter.DefaultLoadStateAdapter
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.autoCleared
import com.zjl.base.utils.findNavController
import com.zjl.base.utils.launchAndRepeatWithViewLifecycle
import com.zjl.finalarchitecture.databinding.FragmentSearchResultBinding
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleAdapter
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleDividerItemDecoration
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleOldAdapter
import com.zjl.finalarchitecture.module.search.viewmodel.SearchResultViewModel
import com.zjl.finalarchitecture.utils.ext.handlePagingStatus
import com.zjl.finalarchitecture.utils.ext.multistate.handleWithPaging3
import com.zjl.finalarchitecture.utils.ext.paging.withLoadState
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
            mViewModel.loadMore()
        }

        // 给ArticleAdapter加上分页的状态尾
        val withFooterAdapter = mArticleAdapter

        // 将BannerAdapter和ArticleAdapter整合为一个Adapter
        mBinding.rvSearchResult.adapter = withFooterAdapter
        // 分割线
        mBinding.rvSearchResult.addItemDecoration(
            ArticleDividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
    }

    override fun createObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                mViewModel.searchResults.collectLatest {
                    it.handlePagingStatus(mArticleAdapter, searchResultStateContainer, null){
                        refresh()
                    }
                }
            }

            launch {
                mViewModel.title.collectLatest {
                    mBinding.toolbarSearchResult.title = it
                }
            }
        }
    }

    private fun refresh(){
        // 刷新
        mViewModel.loadMore()
    }
}