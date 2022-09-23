package com.zjl.finalarchitecture.module.toolbox.progressList

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.viewmodel.EmptyViewModel
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentProgressListHomeBinding
import com.zjl.finalarchitecture.module.toolbox.progressList.download.DownloadProgressListFragment
import com.zjl.finalarchitecture.module.toolbox.progressList.timer.TimerProgressListFragment

/**
 * @author Xiaoc
 * @since 2022-09-20
 *
 * 进度更新的RecyclerView内容 首页导航
 */
class ProgressListHomeFragment: BaseFragment<FragmentProgressListHomeBinding, EmptyViewModel>() {

    override fun initViewAndEvent(savedInstanceState: Bundle?) {

        val drawable = BadgeDrawable.create(requireContext())
        mBinding.ivBadge.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                BadgeUtils.attachBadgeDrawable(
                    drawable, mBinding.ivBadge
                )
                drawable.isVisible = false
                mBinding.ivBadge.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }

        })

        // 倒计时案例
        mBinding.btnTimer.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container_progress_list, TimerProgressListFragment())
                .addToBackStack("progressList")
                .commit()
        }

        // 下载案例
        mBinding.btnDownload.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container_progress_list, DownloadProgressListFragment())
                .addToBackStack("progressList")
                .commit()
        }
    }

    override fun createObserver() {

    }
}