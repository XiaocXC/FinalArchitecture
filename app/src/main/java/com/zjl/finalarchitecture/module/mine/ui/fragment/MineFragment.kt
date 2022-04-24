package com.zjl.finalarchitecture.module.mine.ui.fragment

import com.zjl.base.fragment.BaseFragment
import com.zjl.finalarchitecture.databinding.FragmentMineBinding


class MineFragment : BaseFragment<FragmentMineBinding>() {

    override fun bindView(): FragmentMineBinding {
        return FragmentMineBinding.inflate(layoutInflater)
    }

    override fun initViewAndEvent() {
    }

    override fun createObserver() {

    }

}