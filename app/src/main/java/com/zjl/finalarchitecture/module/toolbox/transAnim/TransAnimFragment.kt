package com.zjl.finalarchitecture.module.toolbox.transAnim

import android.os.Bundle
import androidx.transition.Scene
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import androidx.transition.TransitionManager
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.viewmodel.EmptyViewModel
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentTransAnimContainerBinding

/**
 * @author Xiaoc
 * @since 2023/4/4
 *
 * 该Demo展示了简单的View之间的过度动画
 */
class TransAnimFragment: BaseFragment<FragmentTransAnimContainerBinding, EmptyViewModel>() {

    private lateinit var aScene: Scene
    private lateinit var bScene: Scene

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        aScene = Scene.getSceneForLayout(
            mBinding.layoutAnim, R.layout.fragment_trans_anim_layout_1, requireContext()
        )

        bScene = Scene.getSceneForLayout(mBinding.layoutAnim, R.layout.fragment_trans_anim_layout_2, requireContext())

        mBinding.btnTrans.setOnClickListener {
            TransitionManager.go(bScene)
        }

    }

    override fun createObserver() {
    }
}