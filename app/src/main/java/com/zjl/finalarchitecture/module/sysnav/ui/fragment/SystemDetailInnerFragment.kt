package com.zjl.finalarchitecture.module.sysnav.ui.fragment

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
import com.zjl.finalarchitecture.module.sysnav.viewmodel.SystemDetailInnerViewModel
import com.zjl.finalarchitecture.utils.multistate.handleWithPaging3
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SystemDetailInnerFragment: BaseFragment<FragmentSystemDetailInnerBinding>() {

    companion object{
        fun newInstance(id: String): SystemDetailInnerFragment{
            return SystemDetailInnerFragment().apply {
                arguments = bundleOf(
                    "id" to id
                )
            }
        }
    }

    private val systemDetailInnerViewModel by viewModels<SystemDetailInnerViewModel>()

    //这里用的也是 ArticleAdapter
    private var mArticleAdapter by autoCleared<ArticleAdapter>()

    override fun bindView(): FragmentSystemDetailInnerBinding {
        return FragmentSystemDetailInnerBinding.inflate(layoutInflater)
    }

    override fun initViewAndEvent() {

        mArticleAdapter = ArticleAdapter()

        mBinding.rvSystemChild.adapter = mArticleAdapter.withLoadStateFooter(DefaultLoadStateAdapter{
            mArticleAdapter.retry()
        })
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
            systemDetailInnerViewModel.systemArticlePagingFlow.collectLatest {
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