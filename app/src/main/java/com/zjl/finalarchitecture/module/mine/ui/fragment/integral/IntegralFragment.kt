package com.zjl.finalarchitecture.module.mine.ui.fragment.integral

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.autoCleared
import com.zjl.base.utils.findNavController
import com.zjl.base.utils.launchAndCollectIn
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentIntegralBinding
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleDividerItemDecoration
import com.zjl.finalarchitecture.module.mine.ui.adapter.IntegralAdapter
import com.zjl.finalarchitecture.module.mine.viewmodel.IntegralViewModel
import com.zjl.finalarchitecture.utils.ext.handlePagingStatus
import com.zy.multistatepage.MultiStateContainer
import com.zy.multistatepage.bindMultiState


/**
 * @description: 我的积分页面
 * @author: zhou
 * @date : 2022/11/30 20:35
 */
class IntegralFragment : BaseFragment<FragmentIntegralBinding, IntegralViewModel>(), OnRefreshLoadMoreListener, Toolbar.OnMenuItemClickListener {


    /* 个人积分详情列表 */
    private var mIntegralAdapter by autoCleared<IntegralAdapter>()

    private lateinit var mStateContainer: MultiStateContainer

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        //标题
        mBinding.toolbar.title = StringUtils.getString(R.string.my_rank_integral)

        mBinding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        mBinding.toolbar.setOnMenuItemClickListener(this)

        // 将列表绑定到状态布局上
        mStateContainer = mBinding.refreshLayout.bindMultiState()

        /**
         * 微信公众号详情列表 rv adapter
         */
        mIntegralAdapter = IntegralAdapter()

        mBinding.rv.adapter = mIntegralAdapter

        mBinding.refreshLayout.setOnRefreshLoadMoreListener(this)
        // 分割线
        mBinding.rv.addItemDecoration(ArticleDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
    }

    override fun createObserver() {
        /* 本地 userInfo 信息回调 */
        mViewModel.userInfo.launchAndCollectIn(viewLifecycleOwner) { userInfo ->
            if (userInfo != null) {
                doIntAnim(mBinding.txtCoin, userInfo.coinInfo.coinCount, 1500)
            }
        }

        /* 个人积分列表回调 */
        mViewModel.coinRecordList.launchAndCollectIn(viewLifecycleOwner) {
            it.handlePagingStatus(mIntegralAdapter, mStateContainer, mBinding.refreshLayout) {
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

    override fun configImmersive(immersionBar: ImmersionBar): ImmersionBar? {
        // 内部Fragment不处理沉浸式，防止被覆盖
        return immersionBar.titleBar(mBinding.toolbar)
    }

    private fun doIntAnim(target: TextView, to: Int, duration: Long) {
        val fromStr = target.text.toString()
        val from: Int = try {
            fromStr.toInt()
        } catch (e: NumberFormatException) {
            0
        }
        val animator = ValueAnimator.ofInt(from, to)
        animator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            target.text = String.format("%d", value)
        }
        animator.duration = duration
        animator.interpolator = DecelerateInterpolator()
        animator.start()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_integral, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_coin_rank -> {
                //积分排名
                ToastUtils.showShort("积分排名")
                findNavController().navigate(R.id.action_integralFragment_to_rankFragment)
                return true
            }

            R.id.menu_coin_rule -> {
                ToastUtils.showShort("积分规则")
                return true
            }
        }
        return false
    }


}