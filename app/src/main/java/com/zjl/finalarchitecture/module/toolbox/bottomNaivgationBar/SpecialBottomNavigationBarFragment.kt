package com.zjl.finalarchitecture.module.toolbox.bottomNaivgationBar

import android.os.Bundle
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.ext.dp
import com.zjl.base.viewmodel.EmptyViewModel
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentSpecialBottomNavigationBarBinding
import com.zjl.finalarchitecture.widget.navigationBar.BaseCircleEdgeTreatment

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

        mBinding.switchType.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                mBinding.switchType.text = "凹陷"
                mBinding.bottomNavigationBar.edgeTreatmentType = BaseCircleEdgeTreatment.SAG_TYPE
            } else {
                mBinding.switchType.text = "凸起"
                mBinding.bottomNavigationBar.edgeTreatmentType = BaseCircleEdgeTreatment.BULGE_TYPE
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