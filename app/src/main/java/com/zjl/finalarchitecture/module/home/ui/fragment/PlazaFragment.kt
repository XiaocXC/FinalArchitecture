package com.zjl.finalarchitecture.module.home.ui.fragment

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.zjl.base.adapter.DefaultLoadStateAdapter
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.autoCleared
import com.zjl.base.utils.findNavController
import com.zjl.base.utils.launchAndCollectIn
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentPlazaBinding
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleAdapter
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleDividerItemDecoration
import com.zjl.finalarchitecture.module.home.viewmodel.PlazaViewModel
import com.zjl.finalarchitecture.utils.ext.handlePagingStatus
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @description:首页广场
 * @author: zhou
 * @date : 2022/1/20 17:56
 */
class PlazaFragment : BaseFragment<FragmentPlazaBinding, PlazaViewModel>(), OnRefreshLoadMoreListener {


    companion object {
        fun newInstance() = PlazaFragment()
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

        mBinding.recyclerView.adapter = mArticleAdapter
        mBinding.refreshLayout.setOnRefreshLoadMoreListener(this)

        // 分割线
        mBinding.recyclerView.addItemDecoration(
            ArticleDividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )

        mBinding.floatBar.setOnClickListener {
            ToastUtils.showShort("尿我嘴里！")
            findNavController().navigate(R.id.action_mainFragment_to_addPlazaArticleFragment)
        }
    }

    override fun createObserver() {

        // 文章分页数据
        mViewModel.plazaList.launchAndCollectIn(viewLifecycleOwner){
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