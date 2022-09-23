package com.zjl.finalarchitecture.module.toolbox.progressList.timer

import android.os.Bundle
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.launchAndCollectIn
import com.zjl.finalarchitecture.databinding.FragmentTimerProgressListBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @author Xiaoc
 * @since 2022-09-20
 *
 * 倒计时列表案例 Fragment
 */
class TimerProgressListFragment: BaseFragment<FragmentTimerProgressListBinding, TimerProgressListViewModel>() {

    private lateinit var adapter: TimerProgressAdapter

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        adapter = TimerProgressAdapter()
        mBinding.rvTimer.adapter = adapter
    }

    override fun createObserver() {
        mViewModel.timerList.launchAndCollectIn(viewLifecycleOwner){
            adapter.setList(it)
        }
    }
}