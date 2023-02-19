package com.zjl.finalarchitecture.module.toolbox.progressList

import android.os.Bundle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.findNavController
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentProgressListBinding
import timber.log.Timber

/**
 * @author Xiaoc
 * @since 2022-09-20
 *
 * 进度更新的RecyclerView内容实践
 *
 * 对于这种动态更新进度的Item列表来讲，最重要的其实就是数据一致性
 * 我们把进度等数据存储到ItemData中，然后动态更新Item的视图即可
 *
 * 我们提供两个案例：
 * 1.Timer倒计时更新（这个是本地收到数据后，自行进行简单的倒计时）
 * 2.下载进度更新（这个是根据下载进度更新）
 */
class ProgressListFragment: BaseFragment<FragmentProgressListBinding, ProgressListViewModel>() {

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        mBinding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        childFragmentManager.beginTransaction()
            .replace(R.id.container_progress_list, ProgressListHomeFragment())
            .commit()
    }

    override fun createObserver() {

    }
}