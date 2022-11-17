package com.zjl.finalarchitecture.module.sysnav.ui.fragment

import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.gyf.immersionbar.ImmersionBar
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.ui.onFailure
import com.zjl.base.ui.onLoading
import com.zjl.base.ui.onSuccess
import com.zjl.base.utils.autoCleared
import com.zjl.base.utils.launchAndCollectIn
import com.zjl.finalarchitecture.databinding.FragmentTutorialBinding
import com.zjl.finalarchitecture.module.sysnav.ui.adapter.SystemGroupAdapter
import com.zjl.finalarchitecture.module.sysnav.ui.adapter.TutorialAdapter
import com.zjl.finalarchitecture.module.sysnav.viewmodel.TutorialViewModel
import com.zy.multistatepage.state.ErrorState
import com.zy.multistatepage.state.LoadingState
import com.zy.multistatepage.state.SuccessState

/**
 * @description:教程Fragment
 * @author: zhou
 * @date : 2022/11/16 17:16
 */
class TutorialFragment : BaseFragment<FragmentTutorialBinding, TutorialViewModel>(),
    OnItemClickListener {

    private var tutorialAdapter by autoCleared<TutorialAdapter>()

    companion object {
        fun newInstance() = TutorialFragment()
    }

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        tutorialAdapter = TutorialAdapter()
        mBinding.rv.adapter = tutorialAdapter
        tutorialAdapter.setOnItemClickListener(this)
    }

    override fun createObserver() {

        /* 教程列表数据回调 */
        mViewModel.tutorialList.launchAndCollectIn(viewLifecycleOwner) {
            it.onSuccess {
                uiRootState.show(SuccessState())
                tutorialAdapter.setList(it)
            }.onLoading {
                uiRootState.show(LoadingState())
            }.onFailure { value, throwable ->
                val state = ErrorState()
                state.setErrorMsg(throwable.message ?: "")
                uiRootState.show(state)
            }
        }

    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        ToastUtils.showShort("小${position}")
    }

    override fun configImmersive(immersionBar: ImmersionBar): ImmersionBar? {
        return null
    }
}