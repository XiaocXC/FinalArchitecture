package com.zjl.finalarchitecture.module.toolbox.bottomNaivgationBar

import android.os.Bundle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.ext.dp
import com.zjl.base.viewmodel.EmptyViewModel
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentSpecialBottomNavigationBarBinding
import com.zjl.finalarchitecture.widget.navigationBar.BaseCircleEdgeTreatment
import com.zjl.finalarchitecture.widget.navigationBar.HideBottomEdgeTreatmentOnScrollBehavior

/**
 * @author Xiaoc
 * @since  2023-04-11
 *
 * 一种中间插入了按钮的底部导航栏控件样式
 **/
class SpecialBottomNavigationBarFragment: BaseFragment<FragmentSpecialBottomNavigationBarBinding, EmptyViewModel>() {

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        mBinding.sliderFabMargin.valueTo = 5.dp.toFloat()
        mBinding.sliderFabDiameter.valueTo = 64.dp.toFloat()
        mBinding.sliderFabCornerRadius.valueTo = 16.dp.toFloat()

        val behavior = (mBinding.bottomNavigationBar.layoutParams as CoordinatorLayout.LayoutParams).behavior as? HideBottomEdgeTreatmentOnScrollBehavior

        mBinding.switchType.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                mBinding.switchType.text = "凹陷"
                mBinding.bottomNavigationBar.edgeTreatmentType = BaseCircleEdgeTreatment.SAG_TYPE
            } else {
                mBinding.switchType.text = "凸起"
                mBinding.bottomNavigationBar.edgeTreatmentType = BaseCircleEdgeTreatment.BULGE_TYPE
            }
        }

        mBinding.switchHideShow.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                mBinding.switchHideShow.text = "隐藏"
                behavior?.slideDown(mBinding.bottomNavigationBar, true)
            } else {
                mBinding.switchHideShow.text = "显示"
                behavior?.slideUp(mBinding.bottomNavigationBar, true)
            }
        }

        mBinding.sliderFabMargin.addOnChangeListener { slider, value, fromUser ->
            val scale = resources.displayMetrics.density
            val px = (value * scale + 0.5f)
            mBinding.bottomNavigationBar.fabMargin = px
        }

        mBinding.sliderFabDiameter.addOnChangeListener { slider, value, fromUser ->
            val scale = resources.displayMetrics.density
            val px = (value * scale + 0.5f).toInt()
            mBinding.bottomNavigationBar.setFabDiameter(px)
        }

        mBinding.sliderFabCornerRadius.addOnChangeListener { slider, value, fromUser ->
            val scale = resources.displayMetrics.density
            val px = (value * scale + 0.5f)
            mBinding.bottomNavigationBar.fabCornerRadius = px
        }

    }

    override fun createObserver() {

    }
}