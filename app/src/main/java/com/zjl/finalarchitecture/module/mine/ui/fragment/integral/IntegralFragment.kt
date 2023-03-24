package com.zjl.finalarchitecture.module.mine.ui.fragment.integral

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.StringUtils
import com.gyf.immersionbar.ImmersionBar
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.findNavController
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentIntegralBinding
import com.zjl.finalarchitecture.module.mine.viewmodel.IntegralViewModel


/**
 * @description: 我的积分页面
 * @author: zhou
 * @date : 2022/11/30 20:35
 */
class IntegralFragment : BaseFragment<FragmentIntegralBinding,IntegralViewModel>() {

    override fun initViewAndEvent(savedInstanceState: Bundle?) {

        //标题
        mBinding.toolbar.title = StringUtils.getString(R.string.my_rank_coin)

        mBinding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

    }

    override fun createObserver() {
    }


    override fun configImmersive(immersionBar: ImmersionBar): ImmersionBar? {
        return immersionBar.titleBar(mBinding.toolbar)
    }
}