package com.zjl.finalarchitecture.module.sysnav.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.LogUtils
import com.google.android.material.tabs.TabLayoutMediator
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.ext.init
import com.zjl.base.utils.ext.reduceDragSensitivity
import com.zjl.base.viewmodel.EmptyViewModel
import com.zjl.finalarchitecture.databinding.FragmentSysAndNavBinding


class SysAndNavFragment : BaseFragment<FragmentSysAndNavBinding, EmptyViewModel>() {

    //体系,导航
    private var mTitleArrayData = arrayListOf("体系", "导航")
    private var mFragmentList: ArrayList<Fragment> = arrayListOf()

    private var tabLayoutMediator: TabLayoutMediator? = null

    //无参构造方法
    init {
        mFragmentList.add(SystemFragment.newInstance())
//        mFragmentList.add(NavigationFragment.newInstance())
        mFragmentList.add(FuckNavigationFragment.newInstance())
    }

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        //初始化viewpager2
        initViewPager2()
    }

    override fun createObserver() {
    }

    private fun initViewPager2() {
        //ViewPager2初始化
        mBinding.mViewPager2.init(this, mFragmentList)
        mBinding.mViewPager2.reduceDragSensitivity()
        //绑定TabLayout ViewPager2
        tabLayoutMediator =
            TabLayoutMediator(mBinding.mTabLayout, mBinding.mViewPager2)
            { tab, position ->
                tab.text = mTitleArrayData[position]
            }.apply {
                attach()
            }

    }

    override fun onDestroyView() {
        tabLayoutMediator?.detach()
        tabLayoutMediator = null
        super.onDestroyView()
        LogUtils.eTag("zhou::", "onDestroyView")
    }

}