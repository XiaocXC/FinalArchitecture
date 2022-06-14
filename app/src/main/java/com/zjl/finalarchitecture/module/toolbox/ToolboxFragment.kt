package com.zjl.finalarchitecture.module.toolbox

import android.app.Activity
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

        mBinding.btnDropPop.setOnClickListener {


        }

        mBinding.btnTreeCheck.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToTreeCheckFragment())
        }

        mBinding.btnDragSimple.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToRecyclerViewDragFragment())
        }

    }

    override fun createObserver() {

    }



}