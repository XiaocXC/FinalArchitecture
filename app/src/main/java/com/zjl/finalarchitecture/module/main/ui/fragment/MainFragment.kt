package com.zjl.finalarchitecture.module.main.ui.fragment

import androidx.navigation.fragment.findNavController
import com.zjl.base.fragment.BaseFragment
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentMainBinding


/**
 * @description: 主页面
 * @author: zhou
 * @date : 2022/1/14 15:42
 */
class MainFragment : BaseFragment<FragmentMainBinding>() {

    override fun bindView(): FragmentMainBinding = FragmentMainBinding.inflate(layoutInflater)

    override fun initViewAndEvent() {
        findNavController().navigate(R.id.mainFragment)
    }

    override fun createObserver() {
    }

    override fun lazyLoadData() {
    }

}