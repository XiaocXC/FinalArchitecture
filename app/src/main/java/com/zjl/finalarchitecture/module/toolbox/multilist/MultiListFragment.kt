package com.zjl.finalarchitecture.module.toolbox.multilist

import android.graphics.Color
import androidx.fragment.app.viewModels
import com.gyf.immersionbar.ktx.immersionBar
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.autoCleared
import com.zjl.base.utils.ext.isNightMode
import com.zjl.base.utils.launchAndRepeatWithViewLifecycle
import com.zjl.finalarchitecture.databinding.FragmentMultiListBinding
import com.zjl.finalarchitecture.module.toolbox.multilist.adapter.MultiListAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @author Xiaoc
 * @since 2022-04-26
 *
 * 一个多ItemType支持的列表页
 * 并且支持Banner变色状态栏和展开折叠内容
 */
class MultiListFragment: BaseFragment<FragmentMultiListBinding>() {

    private val multiListViewModel by viewModels<MultiListViewModel>()

    private var multiAdapter by autoCleared<MultiListAdapter>()

    override fun bindView(): FragmentMultiListBinding {
        return FragmentMultiListBinding.inflate(layoutInflater)
    }

    override fun initViewAndEvent() {
        multiAdapter = MultiListAdapter {
            multiListViewModel.parseImageToPrimaryColor(it)
        }
        mBinding.rvMultiList.adapter = multiAdapter
    }

    override fun createObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                multiListViewModel.multiList.collectLatest {
                    multiAdapter.setList(it)
                }
            }

            launch {
                multiListViewModel.toolbarColor.collectLatest {
                    val colorData = it ?: return@collectLatest
                    mBinding.toolbarMulti.setBackgroundColor(colorData.primaryColor)
                    mBinding.toolbarMulti.setTitleTextColor(colorData.onPrimaryColor)
                    mBinding.toolbarMulti.setNavigationIconTint(colorData.onPrimaryColor)

                    // 更改状态栏颜色
                    immersionBar {
                        statusBarColorInt(colorData.primaryColor)
                        statusBarDarkFont(!resources.isNightMode())
                    }
                }
            }

            launch {
                multiListViewModel.bitmap.collectLatest {
                    mBinding.ivTest.setImageBitmap(it)
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()

        // 恢复状态栏颜色
        immersionBar {
            statusBarColorInt(Color.TRANSPARENT)
            statusBarDarkFont(!resources.isNightMode())
        }
    }
}