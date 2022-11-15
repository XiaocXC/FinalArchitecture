package com.zjl.finalarchitecture.module.home.ui.fragment

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.ui.data
import com.zjl.base.utils.autoCleared
import com.zjl.base.utils.launchAndCollectIn
import com.zjl.finalarchitecture.databinding.FragmentProjectBinding
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleDividerItemDecoration
import com.zjl.finalarchitecture.module.home.ui.adapter.ProjectAdapter
import com.zjl.finalarchitecture.module.home.ui.adapter.ProjectCategoryAdapter
import com.zjl.finalarchitecture.module.home.viewmodel.WechatViewModel
import com.zjl.finalarchitecture.utils.ext.handlePagingStatus
import com.zy.multistatepage.MultiStateContainer
import com.zy.multistatepage.bindMultiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * @description:首页公众号
 * @author: zhou
 * @date : 2022/1/20 18:07
 */
class WeChatFragment : BaseFragment<FragmentProjectBinding, WechatViewModel>(), OnRefreshLoadMoreListener {

    companion object {
        fun newInstance() = WeChatFragment()
    }

    private lateinit var articleStateContainer: MultiStateContainer

    /* 微信公众号分类 */
    private var mWechatCategoryAdapter by autoCleared<ProjectCategoryAdapter>()

    /* 微信公众号详情列表 */
    private var mWechatListAdapter by autoCleared<ProjectAdapter>()

    override fun initViewAndEvent(savedInstanceState: Bundle?) {

        // 将文章列表绑定到状态布局上
        articleStateContainer = mBinding.refreshLayout.bindMultiState()

        // 下拉刷新
        mBinding.refreshLayout.setOnRefreshListener(this)

        /**
         * 微信公众号分类 rv adapter
         */
        mWechatCategoryAdapter = ProjectCategoryAdapter()
        mBinding.rvCategory.adapter = mWechatCategoryAdapter

        mWechatCategoryAdapter.check(mViewModel.checkPosition)

        mWechatCategoryAdapter.setCheckClick { id, position ->
            mViewModel.onRefreshCategoryId(id)
            mViewModel.checkPosition = position
        }


        /**
         * 微信公众号详情列表 rv adapter
         */
        mWechatListAdapter = ProjectAdapter()
//        mWechatListAdapter.loadMoreModule.setOnLoadMoreListener {
//            mViewModel.loadMore()
//        }

        mBinding.rvProject.adapter = mWechatListAdapter
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
        // 分类
        mViewModel.categoryList.launchAndCollectIn(viewLifecycleOwner){
            mWechatCategoryAdapter.setList(it.data)
        }

        // 对应分类的数据
        mViewModel.articleList.launchAndCollectIn(viewLifecycleOwner){
            it.handlePagingStatus(mWechatListAdapter, articleStateContainer, mBinding.refreshLayout){
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