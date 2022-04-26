package com.zjl.finalarchitecture.module.toolbox

import androidx.navigation.fragment.findNavController
import com.zjl.base.fragment.BaseFragment
import com.zjl.finalarchitecture.databinding.FragmentToolboxBinding
import com.zjl.finalarchitecture.module.main.ui.fragment.MainFragmentDirections

/**
 * @author Xiaoc
 * @since 2022-04-26
 *
 * 工具集Box Fragment
 */
class ToolboxFragment: BaseFragment<FragmentToolboxBinding>() {

    override fun bindView(): FragmentToolboxBinding {
        return FragmentToolboxBinding.inflate(layoutInflater)
    }

    override fun initViewAndEvent() {
        mBinding.btnMulti.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToMultiListFragment())
        }
    }

    override fun createObserver() {

    }
}