package com.zjl.finalarchitecture.module.sysnav.ui.fragment

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.zjl.base.adapter.DefaultLoadStateAdapter
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.autoCleared
import com.zjl.finalarchitecture.databinding.FragmentSystemDetailInnerBinding
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleAdapter
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleDividerItemDecoration
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleOldAdapter
import com.zjl.finalarchitecture.module.sysnav.viewmodel.SystemDetailInnerViewModel
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
    private var mArticleAdapter by autoCleared<ArticleOldAdapter>()

    override fun initViewAndEvent(savedInstanceState: Bundle?) {

        mArticleAdapter = ArticleOldAdapter()

        mBinding.rvSystemChild.adapter = mArticleAdapter.withLoadState()
        // 分割线
        mBinding.rvSystemChild.addItemDecoration(
            ArticleDividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
    }

    override fun createObserver() {

        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.systemArticlePagingFlow.collectLatest {
                mArticleAdapter.submitData(it)
            }
        }


        // 下拉刷新,上拉分页,LEC状态观察
        viewLifecycleOwner.lifecycleScope.launch {
            mArticleAdapter.loadStateFlow.collectLatest {
                // 处理Paging3状态与整个布局状态相关联动
                uiRootState.handleWithPaging3(it,
                    mArticleAdapter.itemCount <= 0){
                    refresh()
                }
            }
        }
    }

    private fun refresh(){
        // 刷新Paging
        mArticleAdapter.refresh()
    }
}