package com.zjl.finalarchitecture.module.home.ui.fragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.ui.onFailure
import com.zjl.base.ui.onLoading
import com.zjl.base.ui.onSuccess
import com.zjl.base.utils.launchAndRepeatWithViewLifecycle
import com.zjl.base.weight.recyclerview.SpaceItemDecoration
import com.zjl.finalarchitecture.databinding.FragmentArticleBinding
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleAdapter
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleBannerAdapter
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
class ArticleFragment : BaseFragment<FragmentArticleBinding>(), OnRefreshListener {

    companion object {
        fun newInstance() = ArticleFragment()
    }

    private val articleViewModel by viewModels<ArticleViewModel>()

    private lateinit var bannerAdapter: ArticleBannerAdapter
    private lateinit var articleAdapter: ArticleAdapter

    override fun bindView() = FragmentArticleBinding.inflate(layoutInflater)

    override fun initViewAndEvent() {
        // Banner适配器
        bannerAdapter = ArticleBannerAdapter()
        mBinding.articleBanner.setAdapter(bannerAdapter)
        mBinding.articleBanner.setIndicatorSliderWidth(10, 10)
        // Banner轮播图设置lifecycle
        mBinding.articleBanner.setLifecycleRegistry(viewLifecycleOwner.lifecycle)

        // 列表适配器
        articleAdapter = ArticleAdapter()
        mBinding.rvArticle.adapter = articleAdapter
        mBinding.rvArticle.layoutManager = LinearLayoutManager(context)
        mBinding.rvArticle.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f), false))

        // 下拉刷新
        mBinding.refreshLayout.setOnRefreshListener(this)
    }

    override fun createObserver() {
        // Banner数据观察
        articleViewModel.bannerListUiModel.observe(viewLifecycleOwner){ bannerList ->
            mBinding.articleBanner.create(bannerList)
        }

        // 文章数据
        articleViewModel.articleListUiModel.observe(viewLifecycleOwner){ articleList ->
            if(articleViewModel.pageNo == 0){
                articleAdapter.setList(articleList)
            } else {
                articleAdapter.addData(articleList)
            }
        }

        launchAndRepeatWithViewLifecycle {
            launch {
                // 整个页面状态数据
                articleViewModel.rootViewState.collect {
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



}