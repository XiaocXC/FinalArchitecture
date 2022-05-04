package com.zjl.finalarchitecture.module.home.ui.fragment

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
class WeChatFragment : BaseFragment<FragmentProjectBinding>(), OnRefreshListener {

    companion object {
        fun newInstance() = WeChatFragment()
    }

    private val mWechatViewModel by viewModels<WechatViewModel>()

    /* 微信公众号分类 */
    private var mWechatCategoryAdapter by autoCleared<ProjectCategoryAdapter>()

    /* 微信公众号详情列表 */
    private var mWechatListAdapter by autoCleared<ProjectAdapter>()

    override fun bindView() = FragmentProjectBinding.inflate(layoutInflater)

    override fun initViewAndEvent() {
        // 下拉刷新
        mBinding.refreshLayout.setOnRefreshListener(this)

        /**
         * 微信公众号分类 rv adapter
         */
        mWechatCategoryAdapter = ProjectCategoryAdapter()
        mBinding.rvCategory.adapter = mWechatCategoryAdapter

        mWechatCategoryAdapter.check(mWechatViewModel.checkPosition)

        mWechatCategoryAdapter.setCheckClick { id, position ->
            mWechatViewModel.onCidChanged(id)
            mWechatViewModel.checkPosition = position
        }


        /**
         * 微信公众号详情列表 rv adapter
         */
        mWechatListAdapter = ProjectAdapter()
        // 给adapter添加尾部页
        val mFooterAdapter = mWechatListAdapter.withLoadState()
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

            /* 微信公众号分类 */
            launch {
                mWechatViewModel.categoryList.collectLatest {
                    mWechatCategoryAdapter.setList(it)
                }
            }

            /* 微信公众号列表数据 */
            launch {
                mWechatViewModel.wechatArticlePagingFlow.collect {
                    mWechatListAdapter.submitData(it)
                }
            }

            // 下拉刷新,上拉分页,LEC状态观察
            launch {
                mWechatListAdapter.loadStateFlow.collectLatest {
                    // 处理SmartLayout与Paging3相关状态联动
                    //处理下拉刷新的状态
                    mBinding.refreshLayout.handleWithPaging3(it)
                    // 处理Paging3状态与整个布局状态相关联动
                    uiRootState.handleWithPaging3(it, mWechatListAdapter.itemCount <= 0) {
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
        // 刷新Paging
        mWechatListAdapter.refresh()
    }


}