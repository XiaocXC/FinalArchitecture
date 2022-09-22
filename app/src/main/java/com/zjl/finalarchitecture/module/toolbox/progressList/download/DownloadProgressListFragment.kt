package com.zjl.finalarchitecture.module.toolbox.progressList.download

import android.os.Bundle
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.launchAndRepeatWithViewLifecycle
import com.zjl.finalarchitecture.databinding.FragmentDownloadProgressListBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @author Xiaoc
 * @since 2022-09-20
 *
 * 下载进度列表案例 Fragment
 */
class DownloadProgressListFragment: BaseFragment<FragmentDownloadProgressListBinding, DownloadProgressListViewModel>() {

    private lateinit var adapter: DownloadProgressAdapter

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        adapter = DownloadProgressAdapter()
        mBinding.rvTimer.adapter = adapter
    }

    override fun createObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                mViewModel.downloadList.collectLatest {
                    adapter.setList(it)
                }
            }

            launch {
                mViewModel.updateProgress.collect { updateData ->
                    // 在Adapter中找到对应Item的索引
                    val index = adapter.data.indexOfFirst {
                        it.id == updateData.id
                    }
                    // 更新数据源和视图
                    if(index >= 0){
                        adapter.data[index] = updateData
                        adapter.notifyItemChanged(index, updateData)
                    }
                }
            }
        }
    }
}