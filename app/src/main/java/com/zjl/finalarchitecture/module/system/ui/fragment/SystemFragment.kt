package com.zjl.finalarchitecture.module.system.ui.fragment

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.ui.onFailure
import com.zjl.base.ui.onLoading
import com.zjl.base.ui.onSuccess
import com.zjl.base.utils.launchAndRepeatWithViewLifecycle
import com.zjl.finalarchitecture.databinding.FragmentSystemBinding
import com.zjl.finalarchitecture.module.main.ui.fragment.MainFragmentDirections
import com.zjl.finalarchitecture.module.system.ui.adapter.SystemGroupAdapter
import com.zjl.finalarchitecture.module.system.ui.viewmodel.SystemViewModel
import com.zy.multistatepage.state.ErrorState
import com.zy.multistatepage.state.LoadingState
import com.zy.multistatepage.state.SuccessState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 体系Fragment
 */
class SystemFragment : BaseFragment<FragmentSystemBinding>() {

    private lateinit var systemGroupAdapter: SystemGroupAdapter

    private val systemViewModel by viewModels<SystemViewModel>()

    override fun bindView(): FragmentSystemBinding {
        return FragmentSystemBinding.inflate(layoutInflater)
    }

    override fun initViewAndEvent() {
        systemGroupAdapter = SystemGroupAdapter{ systemVO, index ->
            // 点击内部的子栏目按钮跳转
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToSystemDetailArrFragment(systemVO, index)
            )
        }

        // 点击整个Item体系跳转
        systemGroupAdapter.setOnItemClickListener { _, _, position ->
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToSystemDetailArrFragment(systemGroupAdapter.getItem(position), 0)
            )
        }

        mBinding.rvSystem.adapter = systemGroupAdapter
    }

    override fun createObserver() {

        launchAndRepeatWithViewLifecycle {

            // 监听状态
            launch {
                systemViewModel.rootViewState.collectLatest {
                    it.onSuccess {
                        uiRootState.show(SuccessState())
                    }.onLoading {
                        uiRootState.show(LoadingState())
                    }.onFailure { _, _ ->
                        uiRootState.show(ErrorState())
                    }
                }
            }

            // 更新数据
            launch {
                systemViewModel.systemList.collectLatest {
                    systemGroupAdapter.setList(it)
                }
            }
        }

    }


}