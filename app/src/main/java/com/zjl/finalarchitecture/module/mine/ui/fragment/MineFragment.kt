package com.zjl.finalarchitecture.module.mine.ui.fragment

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.zjl.base.fragment.BaseFragment
import com.zjl.finalarchitecture.NavMainDirections
import com.zjl.finalarchitecture.databinding.FragmentMineBinding
import com.zjl.finalarchitecture.module.mine.viewmodel.MineViewModel
import java.io.File


class MineFragment : BaseFragment<FragmentMineBinding>() {

    private val mineViewModel by viewModels<MineViewModel>()

    override fun bindView(): FragmentMineBinding {
        return FragmentMineBinding.inflate(layoutInflater)
    }

    override fun initViewAndEvent() {
        mBinding.containerUserInfo.setOnClickListener {
            findNavController().navigate(NavMainDirections.actionGlobalSignIn())
        }
    }

    override fun createObserver() {

    }

}