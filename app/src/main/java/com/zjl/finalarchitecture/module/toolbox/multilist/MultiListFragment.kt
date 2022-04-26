package com.zjl.finalarchitecture.module.toolbox.multilist

import com.zjl.base.fragment.BaseFragment
import com.zjl.finalarchitecture.databinding.FragmentMultiListBinding

/**
 * @author Xiaoc
 * @since 2022-04-26
 *
 * 一个多ItemType支持的列表页
 * 并且支持Banner变色状态栏和展开折叠内容
 */
class MultiListFragment: BaseFragment<FragmentMultiListBinding>() {

    companion object {
        fun newInstance() = MultiListFragment()
    }

    override fun bindView(): FragmentMultiListBinding {
        return FragmentMultiListBinding.inflate(layoutInflater)
    }

    override fun initViewAndEvent() {

    }

    override fun createObserver() {

    }
}