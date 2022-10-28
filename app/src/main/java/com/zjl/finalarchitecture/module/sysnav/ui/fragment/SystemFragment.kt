package com.zjl.finalarchitecture.module.sysnav.ui.fragment

import android.os.Bundle
import com.gyf.immersionbar.ImmersionBar
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.ui.onFailure
import com.zjl.base.ui.onLoading
import com.zjl.base.ui.onSuccess
import com.zjl.base.utils.autoCleared
import com.zjl.base.utils.findNavController
import com.zjl.base.utils.launchAndCollectIn
import com.zjl.finalarchitecture.databinding.FragmentSystemBinding
import com.zjl.finalarchitecture.module.main.ui.fragment.MainFragmentDirections
import com.zjl.finalarchitecture.module.sysnav.ui.adapter.SystemGroupAdapter
import com.zjl.finalarchitecture.module.sysnav.viewmodel.SystemViewModel
import com.zy.multistatepage.state.ErrorState
import com.zy.multistatepage.state.LoadingState
import com.zy.multistatepage.state.SuccessState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 体系Fragment
 */
class SystemFragment : BaseFragment<FragmentSystemBinding, SystemViewModel>() {

    private var systemGroupAdapter by autoCleared<SystemGroupAdapter>()

    companion object {
        fun newInstance() = SystemFragment()
    }

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        systemGroupAdapter = SystemGroupAdapter{ systemVO, position ->
            // 点击内部的子栏目按钮跳转
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToSystemDetailArrFragment(systemVO, position)
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

        // 更新数据
        mViewModel.systemList.launchAndCollectIn(viewLifecycleOwner){
            it.onSuccess {
                uiRootState.show(SuccessState())
                systemGroupAdapter.setList(it)
            }.onLoading {
                uiRootState.show(LoadingState())
            }.onFailure { value, throwable ->
                val state = ErrorState()
                state.setErrorMsg(throwable.message ?: "")
                uiRootState.show(state)
            }
        }
    }

    override fun configImmersive(immersionBar: ImmersionBar): ImmersionBar? {
        return null
    }
}