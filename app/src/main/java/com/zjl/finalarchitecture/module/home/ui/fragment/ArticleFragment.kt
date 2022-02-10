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
import com.zy.multistatepage.state.ErrorState
import com.zy.multistatepage.state.LoadingState
import com.zy.multistatepage.state.SuccessState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @description:
 * @author: zhou
 * @date : 2022/1/20 17:52
 */
class ArticleFragment : BaseFragment<FragmentArticleBinding>(), OnRefreshListener,
    OnRefreshLoadMoreListener, OnItemClickListener {

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

        mBinding.rvArticle.adapter = ConcatAdapter(bannerAdapter, articleAdapter)

        // 下拉刷新
        mBinding.refreshLayout.setOnRefreshLoadMoreListener(this)

        //item点击事件
        articleAdapter.setOnItemClickListener(this)
    }

    override fun createObserver() {
        // Banner数据观察
        articleViewModel.bannerListUiModel.observe(viewLifecycleOwner){ bannerList ->
            bannerAdapter.setList(
                listOf(BannerVOWrapper(bannerList))
            )
        }

        // 文章数据
        articleViewModel.articleListUiModel.observe(viewLifecycleOwner){ articleList ->
            articleAdapter.setList(articleList)
        }

        // 文章增量数据
        articleViewModel.addArticleList.observe(viewLifecycleOwner){
            mBinding.refreshLayout.finishLoadMore()

            if(!it.isNullOrEmpty()){
                articleAdapter.addData(it)
            }
        }

        launchAndRepeatWithViewLifecycle {
            launch {
                // 整个页面状态数据
                articleViewModel.rootViewState.collect { it ->
                    it.onSuccess {
                        mBinding.refreshLayout.finishRefresh()

                        uiRootState.show(SuccessState())

                    }.onLoading {
                        mBinding.refreshLayout.autoRefreshAnimationOnly()
                    }.onFailure { _, throwable ->
                        mBinding.refreshLayout.finishRefresh()
                        uiRootState.show<ErrorState> {
                            it.setErrorMsg(throwable.message ?: "")
                        }
                    }
                }
            }
        }



    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        //重新请求banner
        articleViewModel.toReFresh()
        //重新请求列表 ?
//        refreshLayout.autoRefresh(200)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        articleViewModel.loadMoreArticle()
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        findNavController().navigate(R.id.action_mainFragment_to_searchFragment)
    }


}