package com.zjl.finalarchitecture.module.system.ui.fragment

import androidx.fragment.app.viewModels
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.autoCleared
import com.zjl.base.utils.launchAndRepeatWithViewLifecycle
import com.zjl.finalarchitecture.databinding.FragmentNavigationBinding
import com.zy.multistatepage.state.ErrorState
import com.zy.multistatepage.state.LoadingState
import com.zy.multistatepage.state.SuccessState
import kotlinx.coroutines.launch


class NavigationFragment : BaseFragment<FragmentNavigationBinding>() {

    private var navigationGroupAdapter by autoCleared<NavigationGroupAdapter>()

    private val navigationViewModel by viewModels<NavigationViewModel>()

    companion object {
        fun newInstance() = NavigationFragment()
    }

    override fun bindView(): FragmentNavigationBinding {
        return FragmentNavigationBinding.inflate(layoutInflater)
    }

    override fun initViewAndEvent() {
        navigationGroupAdapter = NavigationGroupAdapter{ vo, position ->

        }
        mBinding.rvNavigation.adapter = navigationGroupAdapter
    }

    override fun createObserver() {

        launchAndRepeatWithViewLifecycle {

            // 监听状态
            launch {
                navigationViewModel.rootViewState.collectLatest {
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
                navigationViewModel.navigationList.collectLatest {
                    navigationGroupAdapter.setList(it)
                }
            }
        }
    }
}