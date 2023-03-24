package com.zjl.finalarchitecture.module.mine.ui.fragment.integral

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.StringUtils
import com.google.android.material.appbar.AppBarLayout
import com.gyf.immersionbar.ImmersionBar
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.AppBarStateChangeListener
import com.zjl.base.utils.AppBarStateChangeListener.Companion.COLLAPSED
import com.zjl.base.utils.ext.getAttrColor
import com.zjl.base.utils.findNavController
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentIntegralBinding
import com.zjl.finalarchitecture.module.mine.viewmodel.IntegralViewModel
import timber.log.Timber
import kotlin.math.min


/**
 * @description: 我的积分页面
 * @author: zhou
 * @date : 2022/11/30 20:35
 */
class IntegralFragment : BaseFragment<FragmentIntegralBinding,IntegralViewModel>() {

    private val appBarStateChangeListener =  object: AppBarStateChangeListener(){

        override fun onOffsetChanged(appBarLayout: AppBarLayout) {
        }

        override fun onStateChanged(appBarLayout: AppBarLayout, state: Int, verticalOffset: Int) {
//            if(state == COLLAPSED){
//                // 处于收缩状态
//                mBinding.toolbar.setNavigationIconTint(requireContext().getAttrColor(R.attr.colorOnSurface))
//                mBinding.toolbar.setTitleTextColor(requireContext().getAttrColor(R.attr.colorOnSurface))
//            } else {
//                mBinding.toolbar.setNavigationIconTint(requireContext().getAttrColor(R.attr.colorSurface))
//                mBinding.toolbar.setTitleTextColor(requireContext().getAttrColor(R.attr.colorSurface))
//            }
        }

    }

    override fun initViewAndEvent(savedInstanceState: Bundle?) {

        //标题
        mBinding.toolbar.title = StringUtils.getString(R.string.my_rank_coin)

        mBinding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        mBinding.appBarLayout.addOnOffsetChangedListener(appBarStateChangeListener)
    }

    override fun createObserver() {
    }

}