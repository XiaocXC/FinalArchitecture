package com.zjl.finalarchitecture.module.home.ui.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.zjl.base.adapter.DefaultLoadStateAdapter
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.ui.data
import com.zjl.base.utils.autoCleared
import com.zjl.base.utils.findNavController
import com.zjl.base.utils.launchAndCollectIn
import com.zjl.finalarchitecture.NavMainDirections
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
class ProjectFragment : BaseFragment<FragmentProjectBinding, ProjectViewModel>(), OnRefreshLoadMoreListener {

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
            mViewModel.refreshCategoryList(id)

            mViewModel.checkPosition = position
        }


        /**
         * 项目详情列表 rv adapter
         */
        mProjectListAdapter = ProjectAdapter()

        // Item点击事件
        mProjectListAdapter.setOnItemClickListener { _, _, position ->
            // 跳转到网页
            findNavController().navigate(NavMainDirections.actionGlobalToWebFragment(mProjectListAdapter.getItem(position)))
        }

        mBinding.rvProject.adapter = mProjectListAdapter
        mBinding.refreshLayout.setOnRefreshLoadMoreListener(this)

        // 分割线
        mBinding.rvProject.addItemDecoration(
            ArticleDividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
    }

    override fun createObserver() {
        // 项目栏目顶部分类
        mViewModel.categoryList.launchAndCollectIn(viewLifecycleOwner){
            mProjectCategoryAdapter.setList(it.data)
        }

        // 对应分类的数据
        mViewModel.articleList.launchAndCollectIn(viewLifecycleOwner){
            it.handlePagingStatus(mProjectListAdapter, articleStateContainer, mBinding.refreshLayout){
                retryAll()
            }
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        mViewModel.onRefreshData()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mViewModel.onLoadMoreData()
    }

    override fun retryAll() {
        mBinding.refreshLayout.autoRefresh()
    }

    override fun configImmersive(immersionBar: ImmersionBar): ImmersionBar? {
        // 内部Fragment不处理沉浸式，防止被覆盖
        return null
    }

}