package com.zjl.finalarchitecture.module.home.ui.fragment

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.autoCleared
import com.zjl.base.utils.launchAndRepeatWithViewLifecycle
import com.zjl.finalarchitecture.databinding.FragmentProjectBinding
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleDividerItemDecoration
import com.zjl.finalarchitecture.module.home.ui.adapter.ProjectAdapter
import com.zjl.finalarchitecture.module.home.ui.adapter.ProjectCategoryAdapter
import com.zjl.finalarchitecture.module.home.ui.adapter.WechatCategoryAdapter
import com.zjl.finalarchitecture.module.home.viewmodel.WechatViewModel
import com.zjl.finalarchitecture.utils.ext.handlePagingStatus
import com.zjl.finalarchitecture.utils.ext.multistate.handleWithPaging3
import com.zjl.finalarchitecture.utils.ext.paging.withLoadState
import com.zjl.finalarchitecture.utils.ext.smartrefresh.handleWithPaging3
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * @description:首页公众号
 * @author: zhou
 * @date : 2022/1/20 18:07
 */
class WeChatFragment : BaseFragment<FragmentProjectBinding, WechatViewModel>(), OnRefreshListener {

    companion object {
        fun newInstance() = WeChatFragment()
    }

    /* 微信公众号分类 */
    private var mWechatCategoryAdapter by autoCleared<ProjectCategoryAdapter>()

    /* 微信公众号详情列表 */
    private var mWechatListAdapter by autoCleared<ProjectAdapter>()

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        // 下拉刷新
        mBinding.refreshLayout.setOnRefreshListener(this)

        /**
         * 微信公众号分类 rv adapter
         */
        mWechatCategoryAdapter = ProjectCategoryAdapter()
        mBinding.rvCategory.adapter = mWechatCategoryAdapter

        mWechatCategoryAdapter.check(mViewModel.checkPosition)

        mWechatCategoryAdapter.setCheckClick { id, position ->
            mViewModel.onCidChanged(id)
            mViewModel.checkPosition = position
        }


        /**
         * 微信公众号详情列表 rv adapter
         */
        mWechatListAdapter = ProjectAdapter()
        mWechatListAdapter.loadMoreModule.setOnLoadMoreListener {
            mViewModel.loadMore()
        }

        mBinding.rvProject.adapter = mWechatListAdapter
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
                mViewModel.categoryList.collectLatest {
                    mWechatCategoryAdapter.setList(it)
                }
            }

            launch {
                mViewModel.articleList.collectLatest {
                    it.handlePagingStatus(mWechatListAdapter, null, mBinding.refreshLayout){
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