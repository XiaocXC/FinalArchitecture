package com.zjl.finalarchitecture.module.system.ui.fragment

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.ext.reduceDragSensitivity
import com.zjl.base.utils.launchAndRepeatWithViewLifecycle
import com.zjl.finalarchitecture.databinding.FragmentSystemDetailArrBinding
import com.zjl.finalarchitecture.module.home.model.system.ClassifyVO
import com.zjl.finalarchitecture.module.system.ui.viewmodel.SystemDetailArrViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SystemDetailArrFragment: BaseFragment<FragmentSystemDetailArrBinding>() {

    private val args by navArgs<SystemDetailArrFragmentArgs>()

    private val systemDetailArrViewModel by viewModels<SystemDetailArrViewModel>()

    private lateinit var systemDetailArrViewPagerAdapter: SystemDetailArrViewPagerAdapter

    /**
     * TabLayoutMediator可能需要在onDestroyView中手都detach，否则可能出现内存泄漏
     */
    private var tabLayoutMediator: TabLayoutMediator? = null

    override fun bindView(): FragmentSystemDetailArrBinding {
        return FragmentSystemDetailArrBinding.inflate(layoutInflater)
    }

    override fun initViewAndEvent() {
        mBinding.toolbarSystemArr.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        mBinding.toolbarSystemArr.title = args.detailData.name

        mBinding.vpSystemInner.reduceDragSensitivity()
        systemDetailArrViewPagerAdapter = SystemDetailArrViewPagerAdapter(emptyList(), this.childFragmentManager, viewLifecycleOwner.lifecycle)
        mBinding.vpSystemInner.adapter = systemDetailArrViewPagerAdapter

        tabLayoutMediator = TabLayoutMediator(mBinding.tabSystem, mBinding.vpSystemInner){ tab, index ->
            tab.text = systemDetailArrViewPagerAdapter.children[index].name
        }.apply {
            attach()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun createObserver() {

        launchAndRepeatWithViewLifecycle {
            launch {
                systemDetailArrViewModel.systemChild.collectLatest {
                    // 展示ViewPager内容
                    val (index, ids) = it
                    systemDetailArrViewPagerAdapter.children = ids
                    systemDetailArrViewPagerAdapter.notifyDataSetChanged()
                    mBinding.vpSystemInner.setCurrentItem(index, false)
                }
            }
        }
    }

    override fun onDestroyView() {
        // 我们在视图销毁时，将Tab绑定器还有ViewPager2的adapter手动置为空
        // 因为ViewPager2可能导致内存泄漏的BUG
        // https://issuetracker.google.com/issues/151212195
        // https://issuetracker.google.com/issues/154751401
        tabLayoutMediator?.detach()
        tabLayoutMediator = null
        mBinding.vpSystemInner.adapter = null
        super.onDestroyView()

    }

    class SystemDetailArrViewPagerAdapter(
        var children: List<ClassifyVO>,
        fragmentManager: FragmentManager, viewLifeCycle: Lifecycle
    ): FragmentStateAdapter(fragmentManager, viewLifeCycle) {

        override fun getItemCount(): Int = children.size

        override fun createFragment(position: Int): Fragment {
            return SystemDetailInnerFragment.newInstance(children[position].id)
        }
    }

}