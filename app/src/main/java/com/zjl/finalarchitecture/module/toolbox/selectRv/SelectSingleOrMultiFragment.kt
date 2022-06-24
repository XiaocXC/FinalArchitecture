package com.zjl.finalarchitecture.module.toolbox.selectRv

import com.zjl.base.fragment.BaseFragment
import com.zjl.finalarchitecture.databinding.FragmentSelectSingleOrMultiBinding
import com.zjl.finalarchitecture.module.toolbox.selectRv.adapter.SelectData
import com.zjl.finalarchitecture.module.toolbox.selectRv.adapter.SelectMultiAdapter
import com.zjl.finalarchitecture.module.toolbox.selectRv.adapter.SelectSingleAdapter

/**
 * @author Xiaoc
 * @since 2022-06-24
 *
 * 单选或多选的启动页面
 */
class SelectSingleOrMultiFragment: BaseFragment<FragmentSelectSingleOrMultiBinding>() {

    override fun bindView(): FragmentSelectSingleOrMultiBinding {
        return FragmentSelectSingleOrMultiBinding.inflate(layoutInflater)
    }

    override fun initViewAndEvent() {
        // 单选
        mBinding.btnShowSelectSingle.setOnClickListener {
            val dialog = SelectSingleDialog()
            dialog.confirmCallback = {
                mBinding.tvSelectData.text = "当前选中：$it"
            }
            dialog.show(childFragmentManager, null)
        }

        // 多选
        mBinding.btnShowSelectMulti.setOnClickListener {
            val dialog = SelectMultiDialog()
            dialog.confirmCallback = {
                mBinding.tvSelectData.text = "当前选中：$it"
            }
            dialog.show(childFragmentManager, null)
        }
    }

    override fun createObserver() {

    }
}