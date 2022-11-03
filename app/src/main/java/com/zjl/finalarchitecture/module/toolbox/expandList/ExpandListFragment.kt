package com.zjl.finalarchitecture.module.toolbox.expandList

import android.os.Bundle
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.viewmodel.EmptyViewModel
import com.zjl.finalarchitecture.databinding.FragmentExpandListBinding
import com.zjl.finalarchitecture.module.toolbox.expandList.adapter.ExpandListAdapter
import com.zjl.finalarchitecture.module.toolbox.expandList.data.ExpandListData

/**
 * @author Xiaoc
 * @since 2022-11-03
 *
 * 一个简单的二级展开折叠效果
 */
class ExpandListFragment: BaseFragment<FragmentExpandListBinding, EmptyViewModel>() {

    private lateinit var adapter: ExpandListAdapter

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        adapter = ExpandListAdapter()
        mBinding.rvExpandList.adapter = adapter

        val list = buildList {
            add(ExpandListData("标题1", "这是标题1的详情信息这是标题1的详情信息这是标题1的详情信息这是标题1的详情信息这是标题1的详情信息这是标题1的详情信息这是标题1的详情信息", "12s", false))
            add(ExpandListData("标题2", "这是标题2的详情信息这是标题2的详情信息这是标题2的详情信息", "14s", false))
            add(ExpandListData("标题3", "这是标题3的详情信息这是标题3的详情信息这是标题3的详情信息这是标题3的详情信息这是标题3的详情信息这是标题3的详情信息", "2s", false))
            add(ExpandListData("标题4", "这是标题4的详情信息这是标题4的详情信息这是标题4的详情信息这是标题4的详情信息这是标题4的详情信息这是标题4的详情信息这是标题4的详情信息这是标题4的详情信息这是标题4的详情信息这是标题4的详情信息这是标题4的详情信息", "22s", false))
            add(ExpandListData("标题5", "这是标题5的详情信息", "42s", false))
            add(ExpandListData("标题6", "这是标题6的详情信息", "15s", false))
            add(ExpandListData("标题7", "这是标题7的详情信息", "12min", false))
            add(ExpandListData("标题8", "这是标题8的详情信息", "13s", false))
            add(ExpandListData("标题9", "这是标题9的详情信息", "42min", false))
            add(ExpandListData("标题10", "这是标题10的详情信息", "32s", false))
            add(ExpandListData("标题11", "这是标题11的详情信息", "16s", false))
        }

        adapter.setList(list)
    }

    override fun createObserver() {

    }
}