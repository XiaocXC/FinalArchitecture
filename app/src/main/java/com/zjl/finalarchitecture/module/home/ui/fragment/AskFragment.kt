package com.zjl.finalarchitecture.module.home.ui.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.autoCleared
import com.zjl.base.utils.launchAndCollectIn
import com.zjl.finalarchitecture.databinding.FragmentAskBinding
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleAdapter
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleDividerItemDecoration
import com.zjl.finalarchitecture.module.home.viewmodel.AskViewModel
import com.zjl.finalarchitecture.utils.ext.handlePagingStatus
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @description:首页问答
 * @author: zhou
 * @date : 2022/1/20 17:58
 */
class AskFragment: BaseFragment<FragmentAskBinding, AskViewModel>(), OnRefreshLoadMoreListener {

    companion object {
        fun newInstance() = AskFragment()
    }

    private var mArticleAdapter by autoCleared<ArticleAdapter>()

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        // 下拉刷新
        mBinding.refreshLayout.setOnRefreshListener(this)

        // 列表适配器
        mArticleAdapter = ArticleAdapter()

//        mArticleAdapter.loadMoreModule.setOnLoadMoreListener {
//            mViewModel.loadMore()
//        }

        // 给ArticleAdapter加上分页的状态尾
        val articleWithFooterAdapter = mArticleAdapter

        mBinding.recyclerView.adapter = articleWithFooterAdapter

        mBinding.refreshLayout.setOnRefreshLoadMoreListener(this)

        // 分割线
        mBinding.recyclerView.addItemDecoration(
            ArticleDividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
    }

    override fun createObserver() {
        // 文章分页数据
        mViewModel.askList.launchAndCollectIn(viewLifecycleOwner){
            it.handlePagingStatus(mArticleAdapter, uiRootState, mBinding.refreshLayout){
                refresh()
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
        mViewModel.initData()
    }
}