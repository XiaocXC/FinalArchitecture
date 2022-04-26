package com.zjl.finalarchitecture.module.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.zjl.base.adapter.DefaultLoadStateAdapter
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.autoCleared
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentAskBinding
import com.zjl.finalarchitecture.databinding.FragmentPlazaBinding
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleAdapter
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleDividerItemDecoration
import com.zjl.finalarchitecture.module.home.viewmodel.AskViewModel
import com.zjl.finalarchitecture.module.home.viewmodel.PlazaViewModel
import com.zjl.finalarchitecture.utils.multistate.handleWithPaging3
import com.zjl.finalarchitecture.utils.smartrefresh.handleWithPaging3
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @description:首页问答
 * @author: zhou
 * @date : 2022/1/20 17:58
 */
class AskFragment: BaseFragment<FragmentAskBinding>(), OnRefreshListener {

    companion object {
        fun newInstance() = AskFragment()
    }

    private val mAskViewModel by viewModels<AskViewModel>()

    private var mArticleAdapter by autoCleared<ArticleAdapter>()

    override fun bindView() = FragmentAskBinding.inflate(layoutInflater)


    override fun initViewAndEvent() {
        // 下拉刷新
        mBinding.refreshLayout.setOnRefreshListener(this)

        // 列表适配器
        mArticleAdapter = ArticleAdapter()


        // 给ArticleAdapter加上分页的状态尾
        val articleWithFooterAdapter = mArticleAdapter.withLoadStateFooter(DefaultLoadStateAdapter {
            mArticleAdapter.retry()
        })

        mBinding.recyclerView.adapter = articleWithFooterAdapter

        // 分割线
        mBinding.recyclerView.addItemDecoration(
            ArticleDividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
    }

    override fun createObserver() {

        viewLifecycleOwner.lifecycleScope.launch {
            mAskViewModel.askPagingFlow.collectLatest {
                mArticleAdapter.submitData(it)
            }
        }

        // 下拉刷新,上拉分页,LEC状态观察
        viewLifecycleOwner.lifecycleScope.launch {
            mArticleAdapter.loadStateFlow.collectLatest {
                // 处理SmartLayout与Paging3相关状态联动
                mBinding.refreshLayout.handleWithPaging3(it)
                // 处理Paging3状态与整个布局状态相关联动
                uiRootState.handleWithPaging3(it, mArticleAdapter.itemCount <= 0) {
                    refresh()
                }
            }
        }

    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        refresh()
    }


    private fun refresh() {
        // 刷新Paging
        mArticleAdapter.refresh()
    }
}