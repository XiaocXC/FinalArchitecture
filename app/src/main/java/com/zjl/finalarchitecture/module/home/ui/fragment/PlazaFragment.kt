package com.zjl.finalarchitecture.module.home.ui.fragment

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.zjl.base.adapter.DefaultLoadStateAdapter
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.autoCleared
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentPlazaBinding
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleAdapter
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleDividerItemDecoration
import com.zjl.finalarchitecture.module.home.viewmodel.PlazaViewModel
import com.zjl.finalarchitecture.utils.ext.multistate.handleWithPaging3
import com.zjl.finalarchitecture.utils.ext.paging.withLoadState
import com.zjl.finalarchitecture.utils.ext.smartrefresh.handleWithPaging3
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @description:首页广场
 * @author: zhou
 * @date : 2022/1/20 17:56
 */
class PlazaFragment : BaseFragment<FragmentPlazaBinding, PlazaViewModel>(), OnRefreshListener {


    companion object {
        fun newInstance() = PlazaFragment()
    }

    private val mPlazaViewModel by viewModels<PlazaViewModel>()

    private var mArticleAdapter by autoCleared<ArticleAdapter>()

    override fun initViewAndEvent(savedInstanceState: Bundle?) {

        // 下拉刷新
        mBinding.refreshLayout.setOnRefreshListener(this)

        // 列表适配器
        mArticleAdapter = ArticleAdapter()

        // 给ArticleAdapter加上分页的状态尾
        val articleWithFooterAdapter = mArticleAdapter.withLoadState()

        mBinding.recyclerView.adapter = articleWithFooterAdapter

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
//        launchAndRepeatWithViewLifecycle {
//            launch {
//                mViewModel.mPlazaListFlow.collect {
//                    mPlazaArticleAdapter.addData(it)
//                }
//            }
//
//            launch {
//                mViewModel.mAddPlazaListFlow.collect {
//                    mPlazaArticleAdapter.addData(it)
//                }
//            }
//        }

        viewLifecycleOwner.lifecycleScope.launch {
            mPlazaViewModel.plazaPagingFlow.collectLatest {
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