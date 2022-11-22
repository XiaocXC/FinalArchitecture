package com.zjl.finalarchitecture.module.sysnav.ui.fragment.navigation

import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.gyf.immersionbar.ImmersionBar
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.ui.data
import com.zjl.base.utils.autoCleared
import com.zjl.base.utils.launchAndCollectIn
import com.zjl.finalarchitecture.databinding.FragmentFuckNavigationBinding
import com.zjl.finalarchitecture.module.sysnav.ui.adapter.FuckNavigationGroupAdapter
import com.zjl.finalarchitecture.module.sysnav.viewmodel.NavigationViewModel


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
        mViewModel.fuckNavigationList.launchAndCollectIn(viewLifecycleOwner){ result ->
            mAdapter.setList(result.data)
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        // TODO:  
    }

    override fun configImmersive(immersionBar: ImmersionBar): ImmersionBar? {
        return null
    }

    override fun retryAll() {
        super.retryAll()
        mViewModel.initData()
    }

}