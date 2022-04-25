package com.zjl.finalarchitecture.module.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

/**
 * @description:首页问答
 * @author: zhou
 * @date : 2022/1/20 17:58
 */
class AskFragment: BaseFragment<FragmentPlazaBinding>(), OnRefreshListener {

    companion object {
        fun newInstance() = AskFragment()
    }

    private var mArticleAdapter by autoCleared<ArticleAdapter>()

    override fun bindView() = FragmentPlazaBinding.inflate(layoutInflater)


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
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {

    }


}