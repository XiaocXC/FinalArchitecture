package com.zjl.finalarchitecture.module.toolbox.progressList.download

import android.os.Bundle
import com.zjl.base.fragment.BaseFragment
import com.zjl.finalarchitecture.databinding.FragmentDownloadProgressListBinding

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

    }
}