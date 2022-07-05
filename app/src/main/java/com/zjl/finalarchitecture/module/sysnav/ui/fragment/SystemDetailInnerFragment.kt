package com.zjl.finalarchitecture.module.sysnav.ui.fragment

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.zjl.base.adapter.DefaultLoadStateAdapter
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.autoCleared
import com.zjl.base.utils.launchAndRepeatWithViewLifecycle
import com.zjl.finalarchitecture.databinding.FragmentSystemDetailInnerBinding
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleAdapter
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleDividerItemDecoration
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleOldAdapter
import com.zjl.finalarchitecture.module.sysnav.viewmodel.SystemDetailInnerViewModel
import com.zjl.finalarchitecture.utils.ext.handlePagingStatus
import com.zjl.finalarchitecture.utils.ext.multistate.handleWithPaging3
import com.zjl.finalarchitecture.utils.ext.paging.withLoadState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SystemDetailInnerFragment: BaseFragment<FragmentSystemDetailInnerBinding, SystemDetailInnerViewModel>() {

    companion object{
        fun newInstance(id: String): SystemDetailInnerFragment{
            return SystemDetailInnerFragment().apply {
                arguments = bundleOf(
                    "id" to id
                )
            }
        }
    }

    //这里用的也是 ArticleAdapter
    private var mArticleAdapter by autoCleared<ArticleAdapter>()

    override fun initViewAndEvent(savedInstanceState: Bundle?) {

        mArticleAdapter = ArticleAdapter()

        mArticleAdapter.loadMoreModule.setOnLoadMoreListener {
            mViewModel.loadMore()
        }

        mBinding.rvSystemChild.adapter = mArticleAdapter
        // 分割线
        mBinding.rvSystemChild.addItemDecoration(
            ArticleDividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
    }

    override fun createObserver() {

        launchAndRepeatWithViewLifecycle {
            launch {
                mViewModel.systemArticleList.collectLatest {
                    it.handlePagingStatus(mArticleAdapter, uiRootState, null){
                        refresh()
                    }
                }
            }
        }
    }

    private fun refresh(){
        // 刷新Paging
        mViewModel.initData()
    }
}