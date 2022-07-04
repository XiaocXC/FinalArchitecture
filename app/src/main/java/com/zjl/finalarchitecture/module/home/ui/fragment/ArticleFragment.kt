package com.zjl.finalarchitecture.module.home.ui.fragment

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.autoCleared
import com.zjl.base.utils.launchAndRepeatWithViewLifecycle
import com.zjl.finalarchitecture.databinding.FragmentArticleBinding
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleAdapter
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleBannerWrapperAdapter
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleDividerItemDecoration
import com.zjl.finalarchitecture.module.home.ui.adapter.BannerVOWrapper
import com.zjl.finalarchitecture.module.home.viewmodel.ArticleViewModel
import com.zjl.finalarchitecture.utils.ext.multistate.handleWithPaging3
import com.zjl.finalarchitecture.utils.ext.paging.withLoadState
import com.zjl.finalarchitecture.utils.ext.smartrefresh.handleWithPaging3
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * @description:首页文章
 * @author: zhou
 * @date : 2022/1/20 17:52
 */
class ArticleFragment : BaseFragment<FragmentArticleBinding, ArticleViewModel>(), OnRefreshListener{

    companion object {
        fun newInstance() = ArticleFragment()
    }

    /**
     * 经测试，Adapter需要在视图清空时释放，否则可能持有导致内存泄漏
     */
    private var mBannerAdapter by autoCleared<ArticleBannerWrapperAdapter>()
    private var mArticleAdapter by autoCleared<ArticleAdapter>()

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        // Banner适配器
        // 开了弟弟的眼界
        mBannerAdapter = ArticleBannerWrapperAdapter(viewLifecycleOwner.lifecycle)

        // 列表适配器
        mArticleAdapter = ArticleAdapter {
            mViewModel.updateCollectState(it.id, !it.collect)
        }

        // 给ArticleAdapter加上分页的状态尾
        val withFooterAdapter = mArticleAdapter.withLoadState()

        // 将BannerAdapter和ArticleAdapter整合为一个Adapter
        mBinding.rvArticle.adapter = ConcatAdapter(mBannerAdapter, withFooterAdapter)
        // 分割线
        mBinding.rvArticle.addItemDecoration(
            ArticleDividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )

        // 下拉刷新
        mBinding.refreshLayout.setOnRefreshListener(this)
    }

    override fun createObserver() {
        // Banner数据观察
//        articleViewModel.bannerListUiModel.observe(viewLifecycleOwner){ bannerList ->
//            mBannerAdapter.setList(
//                listOf(BannerVOWrapper(bannerList))
//            )
//        }

        mViewModel.articlePagingFlow.observe(viewLifecycleOwner) {
            mArticleAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        launchAndRepeatWithViewLifecycle {
            launch {
                mViewModel.bannerList.collect { bannerList ->
                    mBannerAdapter.setList(
                        listOf(BannerVOWrapper(bannerList))
                    )
                }
            }

//            // 文章分页数据
//            launch {
//                mArticleViewModel.articlePagingFlow.collect {
//                    Timber.i("事件触发3")
//                    mArticleAdapter.submitData(it)
//                }
//            }

            // 下拉刷新,上拉分页,LEC状态观察
            launch {
                mArticleAdapter.loadStateFlow.collectLatest {
                    // 处理SmartLayout与Paging3相关状态联动
                    //处理下拉刷新的状态
                    mBinding.refreshLayout.handleWithPaging3(it)
                    // 处理Paging3状态与整个布局状态相关联动
                    uiRootState.handleWithPaging3(it, mArticleAdapter.itemCount <= 0){
                        refresh()
                    }
                }
            }

        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        refresh()
    }

    private fun refresh(){
        // 重新请求，如果文章列表没有数据，则整个界面会重新显示loading状态（当然这里意义不大，没有用处）
        mViewModel.initData(mArticleAdapter.itemCount <= 0)
        // 刷新Paging
        mArticleAdapter.refresh()
    }


}