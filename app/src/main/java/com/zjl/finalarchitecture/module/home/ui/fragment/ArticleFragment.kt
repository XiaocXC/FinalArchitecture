package com.zjl.finalarchitecture.module.home.ui.fragment

import androidx.fragment.app.viewModels
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.ui.onFailure
import com.zjl.base.ui.onLoading
import com.zjl.base.ui.onSuccess
import com.zjl.finalarchitecture.databinding.FragmentArticleBinding
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleBannerAdapter
import com.zjl.finalarchitecture.module.home.viewmodel.ArticleViewModel
import com.zy.multistatepage.state.ErrorState
import com.zy.multistatepage.state.LoadingState
import com.zy.multistatepage.state.SuccessState

/**
 * @description:
 * @author: zhou
 * @date : 2022/1/20 17:52
 */
class ArticleFragment : BaseFragment<FragmentArticleBinding>() {

    companion object {
        fun newInstance() = ArticleFragment()
    }

    private val articleViewModel by viewModels<ArticleViewModel>()

    private lateinit var bannerAdapter: ArticleBannerAdapter

    override fun bindView() =  FragmentArticleBinding.inflate(layoutInflater)

    override fun initViewAndEvent() {
        // Banner适配器
        bannerAdapter = ArticleBannerAdapter()
        mBinding.articleBanner.setAdapter(bannerAdapter)
        // Banner轮播图设置lifecycle
        mBinding.articleBanner.setLifecycleRegistry(viewLifecycleOwner.lifecycle)
    }

    override fun createObserver() {
        // Banner状态及数据观察
        articleViewModel.bannerListUiModel.observe(this){ uiModel ->
            uiModel.onSuccess {
                uiRootState.show(SuccessState())
                mBinding.articleBanner.create(it)
            }.onFailure { _, throwable ->
                // 展示错误信息
                uiRootState.show<ErrorState> {
                    it.setErrorMsg(throwable.message ?: "")
                }
            }.onLoading {
                uiRootState.show(LoadingState())
            }
        }
    }

}