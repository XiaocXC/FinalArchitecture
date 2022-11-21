package com.zjl.finalarchitecture.module.sysnav.ui.fragment.navigation

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.autoCleared
import com.zjl.base.utils.findNavController
import com.zjl.base.utils.launchAndCollectIn
import com.zjl.finalarchitecture.NavMainDirections
import com.zjl.finalarchitecture.databinding.FragmentNavigationBinding
import com.zjl.finalarchitecture.module.sysnav.ui.adapter.NavigationGroupAdapter
import com.zjl.finalarchitecture.module.sysnav.viewmodel.NavigationViewModel
import com.zjl.finalarchitecture.module.sysnav.ui.adapter.NavigationTab
import com.zjl.finalarchitecture.module.sysnav.ui.adapter.NavigationTabAdapter


/**
 * 先用NavigationFragment2
 * @see NavigationFragment
 */
@Deprecated("有bug")
class NavigationFragment : BaseFragment<FragmentNavigationBinding, NavigationViewModel>() {

    private var navigationGroupAdapter by autoCleared<NavigationGroupAdapter>()

    /* 导航左侧 adapter */
    private var navigationTabAdapter by autoCleared<NavigationTabAdapter>()

    private var shouldScroll = false
    private var scrollToPosition = RecyclerView.NO_POSITION
    private var lastPosition = RecyclerView.NO_POSITION

    companion object {
        fun newInstance() = NavigationFragment()
    }

    //这个吊NavigationViewModel还有其他地方用到吗？
//    override fun createViewModel(): NavigationViewModel {
//        val viewModel by viewModels<NavigationViewModel>(ownerProducer = {requireParentFragment()})
//        return viewModel
//    }

    override fun initViewAndEvent(savedInstanceState: Bundle?) {

        navigationGroupAdapter = NavigationGroupAdapter{ vo, position ->
            val articleVo = vo.articles[position]
            findNavController().navigate(NavMainDirections.actionGlobalToWebFragment(articleVo))
        }

        navigationTabAdapter = NavigationTabAdapter{ tabWrapper, position ->
            moveToPosition(mBinding.rvNavigation.layoutManager as LinearLayoutManager, mBinding.rvNavigation, position)
        }

        mBinding.rvNavigation.adapter = navigationGroupAdapter
        mBinding.rvNavigationTab.adapter = navigationTabAdapter

        mBinding.rvNavigation.addOnScrollListener(object: RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                //第一个可见的view的位置
                val manager = recyclerView.layoutManager as LinearLayoutManager
                val position: Int = manager.findFirstVisibleItemPosition()
                if (lastPosition != position) {
                    if(lastPosition != RecyclerView.NO_POSITION){
                        navigationTabAdapter.getItem(lastPosition).selected = false
                    }
                    if(position != RecyclerView.NO_POSITION){
                        navigationTabAdapter.getItem(position).selected = true
                        navigationTabAdapter.notifyItemChanged(position, "")
                        navigationTabAdapter.notifyItemChanged(lastPosition, "")
                    }
                }
                lastPosition = position
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (shouldScroll) {
                    shouldScroll = false
                    moveToPosition(mBinding.rvNavigation.layoutManager as LinearLayoutManager, mBinding.rvNavigation, scrollToPosition)
                }
            }
        })
    }


    override fun createObserver() {

        // 更新数据
        mViewModel.navigationList.launchAndCollectIn(viewLifecycleOwner){
            navigationGroupAdapter.setList(it)
            navigationTabAdapter.setList(it.map {
                NavigationTab(false, it)
            })
        }
    }

    fun moveToPosition(manager: LinearLayoutManager, mRecyclerView: RecyclerView, position: Int) {
        // 第一个可见的view的位置
        val firstItem = manager.findFirstVisibleItemPosition()
        // 最后一个可见的view的位置
        val lastItem = manager.findLastVisibleItemPosition()
        if (position <= firstItem) {
            // 如果跳转位置firstItem 之前(滑出屏幕的情况)，就smoothScrollToPosition可以直接跳转，
            mRecyclerView.smoothScrollToPosition(position)
        } else if (position <= lastItem) {
            // 跳转位置在firstItem 之后，lastItem 之间（显示在当前屏幕），smoothScrollBy来滑动到指定位置
            val top = mRecyclerView.getChildAt(position - firstItem).top
            mRecyclerView.smoothScrollBy(0, top)
        } else {
            // 如果要跳转的位置在lastItem 之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
            // 再通过onScrollStateChanged控制再次调用当前moveToPosition方法，执行上一个判断中的方法
            mRecyclerView.smoothScrollToPosition(position)
            scrollToPosition = position
            shouldScroll = true
        }
    }
}