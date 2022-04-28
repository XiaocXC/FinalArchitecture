package com.zjl.finalarchitecture.module.home.ui.fragment

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.zjl.base.adapter.DefaultLoadStateAdapter
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.autoCleared
import com.zjl.base.utils.launchAndRepeatWithViewLifecycle
import com.zjl.finalarchitecture.databinding.FragmentProjectBinding
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleDividerItemDecoration
import com.zjl.finalarchitecture.module.home.ui.adapter.ProjectAdapter
import com.zjl.finalarchitecture.module.home.ui.adapter.ProjectCategoryAdapter
import com.zjl.finalarchitecture.module.home.viewmodel.ProjectViewModel
import com.zjl.finalarchitecture.module.sysnav.ui.adapter.NavigationTabAdapter
import com.zjl.finalarchitecture.utils.multistate.handleWithPaging3
import com.zjl.finalarchitecture.utils.smartrefresh.handleWithPaging3
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * @description:首页项目
 * @author: zhou
 * @date : 2022/1/20 18:04
 */
class ProjectFragment : BaseFragment<FragmentProjectBinding>(), OnRefreshListener {


    companion object {
        fun newInstance() = ProjectFragment()
    }

    private val mProjectViewModel by viewModels<ProjectViewModel>()

    /* 项目分类 */
    private var mProjectCategoryAdapter by autoCleared<ProjectCategoryAdapter>()

    /* 项目详情列表 */
    private var mProjectListAdapter by autoCleared<ProjectAdapter>()

    override fun bindView() = FragmentProjectBinding.inflate(layoutInflater)

    override fun initViewAndEvent() {

        // 下拉刷新
        mBinding.refreshLayout.setOnRefreshListener(this)

        /**
         * 项目分类
         */
        mProjectCategoryAdapter = ProjectCategoryAdapter()
        mBinding.rvCategory.adapter = mProjectCategoryAdapter
        mProjectCategoryAdapter.setCheckClick {
            Timber.e("收到的id是：${it}")
            mProjectViewModel.cid = it
            // 刷新Paging
            refresh()
        }

        /**
         * 项目详情列表
         */
        mProjectListAdapter = ProjectAdapter()
        // 给ArticleAdapter加上分页的状态尾
        val mFooterAdapter = mProjectListAdapter.withLoadStateFooter(DefaultLoadStateAdapter {
            mProjectListAdapter.retry()
        })
        mBinding.rvProject.adapter = mFooterAdapter
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

            launch {
                mProjectViewModel.categoryList.collectLatest {
                    mProjectCategoryAdapter.setList(it)
                }
            }

            //项目分页数据
            launch {
                mProjectViewModel.projectArticlePagingFlow.collect {
                    mProjectListAdapter.submitData(it)
                }
            }

            // 下拉刷新,上拉分页,LEC状态观察
            launch {
                mProjectListAdapter.loadStateFlow.collectLatest {
                    // 处理SmartLayout与Paging3相关状态联动
                    //处理下拉刷新的状态
                    mBinding.refreshLayout.handleWithPaging3(it)
                    // 处理Paging3状态与整个布局状态相关联动
                    uiRootState.handleWithPaging3(it, mProjectListAdapter.itemCount <= 0) {
                        refresh()
                    }
                }
            }

        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        refresh()
    }

    private fun refresh() {
        // 重新请求，如果列表没有数据，则整个界面会重新显示loading状态（当然这里意义不大，没有用处）
        mProjectViewModel.toRefresh(mProjectListAdapter.itemCount <= 0)
        // 刷新Paging
        mProjectListAdapter.refresh()
    }

}