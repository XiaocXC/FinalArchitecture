package com.zjl.finalarchitecture.module.home.ui.fragment

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.zjl.base.adapter.DefaultLoadStateAdapter
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.ui.onFailure
import com.zjl.base.ui.onLoading
import com.zjl.base.ui.onSuccess
import com.zjl.base.utils.launchAndRepeatWithViewLifecycle
import com.zjl.base.weight.recyclerview.SpaceItemDecoration
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentArticleBinding
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleAdapter
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleBannerAdapter
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleBannerWrapperAdapter
import com.zjl.finalarchitecture.module.home.ui.adapter.BannerVOWrapper
import com.zjl.finalarchitecture.module.home.viewmodel.ArticleViewModel
import com.zjl.finalarchitecture.utils.multistate.handleWithPaging3
import com.zjl.finalarchitecture.utils.smartrefresh.handleWithPaging3
import com.zy.multistatepage.state.ErrorState
import com.zy.multistatepage.state.LoadingState
import com.zy.multistatepage.state.SuccessState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * @description:
 * @author: zhou
 * @date : 2022/1/20 17:52
 */
class ArticleFragment : BaseFragment<FragmentArticleBinding>(), OnRefreshListener, OnItemClickListener {

    companion object {
        fun newInstance() = ArticleFragment()
    }

    private val articleViewModel by viewModels<ArticleViewModel>()

    private lateinit var bannerAdapter: ArticleBannerWrapperAdapter
    private lateinit var articleAdapter: ArticleAdapter

    override fun bindView() = FragmentArticleBinding.inflate(layoutInflater)

    override fun initViewAndEvent() {
        // Banner适配器
        bannerAdapter = ArticleBannerWrapperAdapter(viewLifecycleOwner.lifecycle)

        // 列表适配器
        articleAdapter = ArticleAdapter()
        mBinding.rvArticle.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f), false))

        // 给ArticleAdapter加上分页的状态尾
        val withFooterAdapter = articleAdapter.withLoadStateFooter(DefaultLoadStateAdapter{
            articleAdapter.retry()
        })

        // 将BannerAdapter和ArticleAdapter整合为一个Adapter
        mBinding.rvArticle.adapter = ConcatAdapter(bannerAdapter, withFooterAdapter)

        // 下拉刷新
        mBinding.refreshLayout.setOnRefreshListener(this)
    }

    override fun createObserver() {
        // Banner数据观察
        articleViewModel.bannerListUiModel.observe(viewLifecycleOwner){ bannerList ->
            bannerAdapter.setList(
                listOf(BannerVOWrapper(bannerList))
            )
        }

        // 文章分页数据
        viewLifecycleOwner.lifecycleScope.launch {
            articleViewModel.articlePagingFlow.collect {
                articleAdapter.submitData(it)
            }
        }

        // 分页状态观察
        viewLifecycleOwner.lifecycleScope.launch {
            articleAdapter.loadStateFlow.collectLatest {
                // 处理SmartLayout与Paging3相关状态联动
                mBinding.refreshLayout.handleWithPaging3(it)
                // 处理Paging3状态与整个布局状态相关联动
                uiRootState.handleWithPaging3(it, articleAdapter.itemCount <= 0){
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
        articleViewModel.toRefresh(articleAdapter.itemCount <= 0)
        // 刷新Paging
        articleAdapter.refresh()
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        findNavController().navigate(R.id.action_mainFragment_to_searchFragment)
    }


}