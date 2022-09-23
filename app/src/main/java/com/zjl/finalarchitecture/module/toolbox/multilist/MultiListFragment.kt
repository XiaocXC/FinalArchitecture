package com.zjl.finalarchitecture.module.toolbox.multilist

import android.graphics.Color
import android.os.Bundle
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.autoCleared
import com.zjl.base.utils.findNavController
import com.zjl.base.utils.launchAndCollectIn
import com.zjl.finalarchitecture.databinding.FragmentMultiListBinding
import com.zjl.finalarchitecture.module.toolbox.multilist.adapter.MultiListAdapter

/**
 * @author Xiaoc
 * @since 2022-04-26
 *
 * 一个多ItemType支持的列表页
 * 并且支持Banner变色状态栏和展开折叠内容
 */
class MultiListFragment: BaseFragment<FragmentMultiListBinding, MultiListViewModel>() {

    private var multiAdapter by autoCleared<MultiListAdapter>()

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        mBinding.toolbarMulti.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        multiAdapter = MultiListAdapter {
            mViewModel.parseImageToPrimaryColor(it)
        }
        mBinding.rvMultiList.adapter = multiAdapter
    }

    override fun createObserver() {
        mViewModel.multiList.launchAndCollectIn(viewLifecycleOwner){
            multiAdapter.setList(it)
        }

        mViewModel.toolbarColor.launchAndCollectIn(viewLifecycleOwner){
            val colorData = it ?: return@launchAndCollectIn
            mBinding.toolbarMulti.setBackgroundColor(colorData.primaryColor)
            mBinding.toolbarMulti.setTitleTextColor(colorData.onPrimaryColor)
            mBinding.toolbarMulti.setNavigationIconTint(colorData.onPrimaryColor)

//          // 更改状态栏颜色
//             immersionBar {
//                navigationBarColorInt(Color.TRANSPARENT)
//                statusBarColorInt(colorData.primaryColor)
//                statusBarDarkFont(!resources.isNightMode())
//             }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()

//        // 恢复状态栏颜色
//        immersionBar {
//            navigationBarColorInt(Color.TRANSPARENT)
//            statusBarColorInt(Color.TRANSPARENT)
//            statusBarDarkFont(!resources.isNightMode())
//        }
    }
}