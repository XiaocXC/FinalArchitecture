package com.zjl.finalarchitecture.module.webview.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import com.just.agentweb.AgentWeb
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.launchAndRepeatWithViewLifecycle
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentArticleBinding
import com.zjl.finalarchitecture.databinding.FragmentWebViewBinding
import com.zjl.finalarchitecture.module.webview.viewmodel.WebViewModel
import kotlinx.coroutines.flow.collectLatest

class WebViewFragment : BaseFragment<FragmentWebViewBinding>() {

    private val webViewModel by viewModels<WebViewModel>()

    override fun bindView() = FragmentWebViewBinding.inflate(layoutInflater)

    private lateinit var preWeb: AgentWeb.PreAgentWeb

    private var mAgentWeb: AgentWeb? = null

    override fun initViewAndEvent() {
        preWeb = AgentWeb.with(this@WebViewFragment)
            .setAgentWebParent(mBinding.containerWeb, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .createAgentWeb()
            .ready()
    }

    override fun createObserver() {

        launchAndRepeatWithViewLifecycle {
            webViewModel.webUrl.collectLatest {
                if(it.isEmpty()){
                    return@collectLatest
                }
                //加载网页
                mAgentWeb = preWeb.go(it)
            }
        }
    }
}