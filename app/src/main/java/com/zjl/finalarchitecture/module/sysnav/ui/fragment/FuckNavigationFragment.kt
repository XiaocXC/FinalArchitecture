package com.zjl.finalarchitecture.module.sysnav.ui.fragment

import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.ui.data
import com.zjl.base.utils.autoCleared
import com.zjl.base.utils.launchAndRepeatWithViewLifecycle
import com.zjl.finalarchitecture.databinding.FragmentFuckNavigationBinding
import com.zjl.finalarchitecture.module.sysnav.ui.adapter.FuckNavigationGroupAdapter
import com.zjl.finalarchitecture.module.sysnav.viewmodel.NavigationViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FuckNavigationFragment : BaseFragment<FragmentFuckNavigationBinding, NavigationViewModel>(),
    OnItemClickListener {

    private var mAdapter by autoCleared<FuckNavigationGroupAdapter>()

    companion object {
        fun newInstance() = FuckNavigationFragment()
    }

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        mAdapter = FuckNavigationGroupAdapter(this)
        mBinding.rv.adapter = mAdapter
        mAdapter.setOnItemClickListener(this)
    }

    override fun createObserver() {
        launchAndRepeatWithViewLifecycle {
            // Banner数据
            launch {
                mViewModel.fuckNavigationList.collectLatest { result ->
                    mAdapter.setList(result.data)
                }
            }
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        // TODO:  
    }

}