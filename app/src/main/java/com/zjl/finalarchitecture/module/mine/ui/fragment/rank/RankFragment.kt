package com.zjl.finalarchitecture.module.mine.ui.fragment.rank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.StringUtils
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.autoCleared
import com.zjl.base.utils.findNavController
import com.zjl.base.utils.launchAndCollectIn
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.data.model.RankVO
import com.zjl.finalarchitecture.databinding.FragmentRankBinding
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleDividerItemDecoration
import com.zjl.finalarchitecture.module.mine.ui.adapter.IntegralAdapter
import com.zjl.finalarchitecture.module.mine.ui.adapter.RankAdapter
import com.zjl.finalarchitecture.module.mine.viewmodel.IntegralViewModel
import com.zjl.finalarchitecture.module.mine.viewmodel.RankViewModel
import com.zjl.finalarchitecture.utils.ext.handlePagingStatus
import com.zy.multistatepage.MultiStateContainer
import com.zy.multistatepage.bindMultiState


class RankFragment : BaseFragment<FragmentRankBinding, RankViewModel>(), OnLoadMoreListener {

    private lateinit var mStateContainer: MultiStateContainer

    /* 积分排行列表 */
    private var rankAdapter by autoCleared<RankAdapter>()

    override fun initViewAndEvent(savedInstanceState: Bundle?) {

        // 将列表绑定到状态布局上
        mStateContainer = mBinding.refreshLayout.bindMultiState()
        mBinding.refreshLayout.setOnLoadMoreListener(this)
        //标题
        mBinding.toolbar.title = StringUtils.getString(R.string.my_rank)
        mBinding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        rankAdapter = RankAdapter()
        mBinding.rv.adapter = rankAdapter
        // 分割线
        mBinding.rv.addItemDecoration(
            ArticleDividerItemDecoration(
                requireContext(), LinearLayoutManager.VERTICAL
            )
        )

    }

    override fun createObserver() {
        /* 积分排行列表回调 */
        mViewModel.rankListStateFlow.launchAndCollectIn(viewLifecycleOwner){
            it.handlePagingStatus(rankAdapter, mStateContainer, mBinding.refreshLayout){
                retryAll()
            }
        }
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mViewModel.onLoadMoreData()
    }


}