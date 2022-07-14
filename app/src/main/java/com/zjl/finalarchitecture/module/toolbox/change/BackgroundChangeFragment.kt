package com.zjl.finalarchitecture.module.toolbox.change

import android.R
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.widget.LinearLayout
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.viewmodel.EmptyViewModel
import com.zjl.finalarchitecture.databinding.FragmentBgChangeBinding


/**
 * @description:
 * @author: zhou
 * @date : 2022/7/14 20:40
 */
class BackgroundChangeFragment : BaseFragment<FragmentBgChangeBinding, EmptyViewModel>() {

    override fun initViewAndEvent(savedInstanceState: Bundle?) {

        val animationDrawable = mBinding.ll.background as AnimationDrawable

        animationDrawable.setEnterFadeDuration(1500)
        animationDrawable.setExitFadeDuration(2000)
        animationDrawable.start()
    }

    override fun createObserver() {

    }
}