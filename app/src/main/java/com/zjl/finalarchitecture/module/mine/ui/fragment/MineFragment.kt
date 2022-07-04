package com.zjl.finalarchitecture.module.mine.ui.fragment

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.findNavController
import com.zjl.base.viewmodel.EmptyViewModel
import com.zjl.finalarchitecture.NavMainDirections
import com.zjl.finalarchitecture.databinding.FragmentMineBinding
import com.zjl.finalarchitecture.module.mine.viewmodel.MineViewModel
import java.io.File


class MineFragment : BaseFragment<FragmentMineBinding, EmptyViewModel>() {

    private val mineViewModel by viewModels<MineViewModel>()

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        mBinding.containerUserInfo.setOnClickListener {
            findNavController().navigate(NavMainDirections.actionGlobalSignIn())
        }
    }

    override fun createObserver() {

    }

}