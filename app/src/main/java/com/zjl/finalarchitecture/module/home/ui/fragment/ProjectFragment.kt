package com.zjl.finalarchitecture.module.home.ui.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.zjl.base.adapter.DefaultLoadStateAdapter
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.ui.data
import com.zjl.base.utils.autoCleared
import com.zjl.base.utils.launchAndRepeatWithViewLifecycle
import com.zjl.finalarchitecture.databinding.FragmentProjectBinding
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleDividerItemDecoration
import com.zjl.finalarchitecture.module.home.ui.adapter.ProjectAdapter
import com.zjl.finalarchitecture.module.home.ui.adapter.ProjectCategoryAdapter
import com.zjl.finalarchitecture.module.home.viewmodel.ProjectViewModel
import com.zjl.finalarchitecture.utils.ext.handlePagingStatus
import com.zy.multistatepage.MultiStateContainer
import com.zy.multistatepage.bindMultiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * @description:首页项目
 * @author: zhou
 * @date : 2022/1/20 18:04
 */
class ProjectFragment : BaseFragment<FragmentProjectBinding, ProjectViewModel>(), OnRefreshListener {

    companion object {
        fun newInstance() = ProjectFragment()
    }

    private lateinit var articleStateContainer: MultiStateContainer

    /* 项目分类 */
    private var mProjectCategoryAdapter by autoCleared<ProjectCategoryAdapter>()

    /* 项目详情列表 */
    private var mProjectListAdapter by autoCleared<ProjectAdapter>()

    override fun initViewAndEvent(savedInstanceState: Bundle?) {

        // 将文章列表绑定到状态布局上
        articleStateContainer = mBinding.refreshLayout.bindMultiState()

        // 下拉刷新
        mBinding.refreshLayout.setOnRefreshListener(this)

        /**
         * 项目分类 rv adapter
         */
        mProjectCategoryAdapter = ProjectCategoryAdapter()

        mBinding.rvCategory.adapter = mProjectCategoryAdapter

        mProjectCategoryAdapter.check(mViewModel.checkPosition)

        mProjectCategoryAdapter.setCheckClick { id, position ->
            Timber.e("收到的id是：${id}")
            mViewModel.onCidChanged(id)

            mViewModel.checkPosition = position
        }


        /**
         * 项目详情列表 rv adapter
         */
        mProjectListAdapter = ProjectAdapter()
        // 加载更多
        mProjectListAdapter.loadMoreModule.setOnLoadMoreListener {
            mViewModel.loadMore()
        }

        mBinding.rvProject.adapter = mProjectListAdapter
        // 分割线
        mBinding.rvProject.addItemDecoration(
            ArticleDividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
    }

    override fun createObserver() {
        launchAndRepeatWithViewLifecycle {
            // 项目栏目顶部分类
            launch {
                mViewModel.categoryList.collectLatest {
                    mProjectCategoryAdapter.setList(it.data)
                }
            }

            // 对应分类的数据
            launch {
                mViewModel.articleList.collectLatest {
                    it.handlePagingStatus(mProjectListAdapter, articleStateContainer, mBinding.refreshLayout){
                        refresh()
                    }
                }
            }


        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        mViewModel.onCidChanged(mViewModel.cid.value)
    }

    private fun refresh() {
        // 刷新Paging
        mViewModel.initData()
    }

}