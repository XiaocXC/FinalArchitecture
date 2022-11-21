package com.zjl.finalarchitecture.module.sysnav.ui.fragment.tutorial

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.ui.onFailure
import com.zjl.base.ui.onLoading
import com.zjl.base.ui.onSuccess
import com.zjl.base.utils.autoCleared
import com.zjl.base.utils.findNavController
import com.zjl.base.utils.launchAndCollectIn
import com.zjl.base.utils.navArgs
import com.zjl.finalarchitecture.databinding.FragmentTutorialDetailArrBinding
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleAdapter
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleDividerItemDecoration
import com.zjl.finalarchitecture.module.sysnav.viewmodel.TutorialDetailArrViewModel
import com.zjl.finalarchitecture.utils.ext.handlePagingStatus
import com.zy.multistatepage.MultiStateContainer
import com.zy.multistatepage.bindMultiState


class TutorialDetailArrFragment :
    BaseFragment<FragmentTutorialDetailArrBinding, TutorialDetailArrViewModel>(),
    OnRefreshLoadMoreListener {

    private val args by navArgs<TutorialDetailArrFragmentArgs>()

    private var mArticleAdapter by autoCleared<ArticleAdapter>()

    private lateinit var articleStateContainer: MultiStateContainer

    override fun initViewAndEvent(savedInstanceState: Bundle?) {

        // 将文章列表绑定到状态布局上
        articleStateContainer = mBinding.refreshLayout.bindMultiState()

        //标题
        mBinding.toolbarTutorialArr.title = args.detailTitle
        //返回
        mBinding.toolbarTutorialArr.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // 列表适配器
        mArticleAdapter = ArticleAdapter {
            //收藏回调
        }

        // 将BannerAdapter和ArticleAdapter整合为一个Adapter
        mBinding.rv.adapter = mArticleAdapter

        // 分割线
        mBinding.rv.addItemDecoration(
            ArticleDividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )

        mBinding.refreshLayout.setOnRefreshLoadMoreListener(this)

    }

    override fun createObserver() {

        /* 教程列表数据回调 */
        mViewModel.tutorialDetailList.launchAndCollectIn(viewLifecycleOwner) {
            it.handlePagingStatus(mArticleAdapter, articleStateContainer, mBinding.refreshLayout){
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