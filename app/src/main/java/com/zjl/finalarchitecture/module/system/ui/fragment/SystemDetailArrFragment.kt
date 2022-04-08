package com.zjl.finalarchitecture.module.system.ui.fragment

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.zjl.base.fragment.BaseFragment
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

    override fun bindView(): FragmentSystemDetailArrBinding {
        return FragmentSystemDetailArrBinding.inflate(layoutInflater)
    }

    override fun initViewAndEvent() {
        mBinding.toolbarSystemArr.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        mBinding.toolbarSystemArr.title = args.detailData.name

        systemDetailArrViewPagerAdapter = SystemDetailArrViewPagerAdapter(emptyList(), this, viewLifecycleOwner.lifecycle)
        mBinding.vpSystemInner.adapter = systemDetailArrViewPagerAdapter

        TabLayoutMediator(mBinding.tabSystem, mBinding.vpSystemInner){ tab, index ->
            tab.text = systemDetailArrViewPagerAdapter.children[index].name
        }.attach()
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

    class SystemDetailArrViewPagerAdapter(
        var children: List<ClassifyVO>,
        fragment: Fragment, viewLifeCycle: Lifecycle
    ): FragmentStateAdapter(fragment.childFragmentManager, viewLifeCycle) {

        override fun getItemCount(): Int = children.size

        override fun createFragment(position: Int): Fragment {
            return SystemDetailInnerFragment.newInstance(children[position].id)
        }
    }

}