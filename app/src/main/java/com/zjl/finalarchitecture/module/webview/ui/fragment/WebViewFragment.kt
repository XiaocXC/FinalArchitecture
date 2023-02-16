package com.zjl.finalarchitecture.module.webview.ui.fragment

import android.os.Bundle
import android.widget.LinearLayout
import com.google.android.material.transition.MaterialSharedAxis
import com.gyf.immersionbar.ImmersionBar
import com.just.agentweb.AgentWeb
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.ui.state.EmptyState
import com.zjl.base.utils.findNavController
import com.zjl.base.utils.launchAndCollectIn
import com.zjl.finalarchitecture.databinding.FragmentWebViewBinding
import com.zjl.finalarchitecture.module.webview.viewmodel.WebViewModel
import kotlinx.coroutines.flow.collectLatest

class WebViewFragment : BaseFragment<FragmentWebViewBinding, WebViewModel>() {

    private lateinit var preWeb: AgentWeb.PreAgentWeb

    private var mAgentWeb: AgentWeb? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X,true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X,false)
    }
    override fun initViewAndEvent(savedInstanceState: Bundle?) {

        mBinding.toolbarWeb.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        preWeb = AgentWeb.with(this@WebViewFragment)
            .setAgentWebParent(mBinding.containerWeb, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .createAgentWeb()
            .ready()
    }

    override fun createObserver() {

        mViewModel.webUrl.launchAndCollectIn(viewLifecycleOwner){
            if(it.isNullOrEmpty()){
                uiRootState.show<EmptyState>()
            } else {
                //加载网页
                mAgentWeb = preWeb.go(it)
            }

        }
    }

    override fun configImmersive(immersionBar: ImmersionBar): ImmersionBar? {
        return null
    }
}