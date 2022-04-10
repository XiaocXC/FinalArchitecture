package com.zjl.finalarchitecture.module.home.ui.fragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.zjl.base.adapter.DefaultLoadStateAdapter
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.autoCleared
import com.zjl.base.utils.launchAndRepeatWithViewLifecycle
import com.zjl.finalarchitecture.databinding.FragmentArticleBinding
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleAdapter
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleBannerWrapperAdapter
import com.zjl.finalarchitecture.module.home.ui.adapter.BannerVOWrapper
import com.zjl.finalarchitecture.module.home.viewmodel.ArticleViewModel
import com.zjl.finalarchitecture.utils.multistate.handleWithPaging3
import com.zjl.finalarchitecture.utils.smartrefresh.handleWithPaging3
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @description:
 * @author: zhou
 * @date : 2022/1/20 17:52
 */
class ArticleFragment : BaseFragment<FragmentArticleBinding>(), OnRefreshListener{

    companion object {
        fun newInstance() = ArticleFragment()
    }

    private val mArticleViewModel by viewModels<ArticleViewModel>()

    /**
     * 经测试，Adapter需要在视图清空时释放，否则可能持有导致内存泄漏
     */
    private var mBannerAdapter by autoCleared<ArticleBannerWrapperAdapter>()
    private var mArticleAdapter by autoCleared<ArticleAdapter>()

    override fun bindView() = FragmentArticleBinding.inflate(layoutInflater)

    override fun initViewAndEvent() {
        // Banner适配器
        // 开了弟弟的眼界
        mBannerAdapter = ArticleBannerWrapperAdapter(viewLifecycleOwner.lifecycle)

        // 列表适配器
        mArticleAdapter = ArticleAdapter()

        // 给ArticleAdapter加上分页的状态尾
        val withFooterAdapter = mArticleAdapter.withLoadStateFooter(DefaultLoadStateAdapter{
            mArticleAdapter.retry()
        })

        // 将BannerAdapter和ArticleAdapter整合为一个Adapter
        mBinding.rvArticle.adapter = ConcatAdapter(mBannerAdapter, withFooterAdapter)

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

        launchAndRepeatWithViewLifecycle {
            launch {
                mArticleViewModel.bannerList.collect { bannerList ->
                    mBannerAdapter.setList(
                        listOf(BannerVOWrapper(bannerList))
                    )
                }
            }
        }

        // 文章分页数据
        viewLifecycleOwner.lifecycleScope.launch {
            mArticleViewModel.articlePagingFlow.collect {
                mArticleAdapter.submitData(it)
            }
        }

        // 下拉刷新,上拉分页,LEC状态观察
        viewLifecycleOwner.lifecycleScope.launch {
            mArticleAdapter.loadStateFlow.collectLatest {
                // 处理SmartLayout与Paging3相关状态联动
                mBinding.refreshLayout.handleWithPaging3(it)
                // 处理Paging3状态与整个布局状态相关联动
                uiRootState.handleWithPaging3(it,
                    mArticleAdapter.itemCount <= 0){
                    refresh()
                }
            }
        }

    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        refresh()
    }

    private fun refresh(){
        // 重新请求，如果文章列表没有数据，则整个界面会重新显示loading状态（当然这里意义不大，没有用处）
        mArticleViewModel.toRefresh(mArticleAdapter.itemCount <= 0)
        // 刷新Paging
        mArticleAdapter.refresh()
    }


}