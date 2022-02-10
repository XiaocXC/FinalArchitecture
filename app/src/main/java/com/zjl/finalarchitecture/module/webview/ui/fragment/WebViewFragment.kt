package com.zjl.finalarchitecture.module.webview.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zjl.base.fragment.BaseFragment
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentArticleBinding
import com.zjl.finalarchitecture.databinding.FragmentWebViewBinding

class WebViewFragment : BaseFragment<FragmentWebViewBinding>() {

    companion object {
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_URL = "extra_url"

        @JvmStatic
        fun newInstance(title: String, url: String) =
            WebViewFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_TITLE, title)
                    putString(EXTRA_URL, url)
                }
            }
    }

    //data
    private val mLoadUrl by lazy { arguments?.getString(EXTRA_URL) ?: "" }
    private val mTitle by lazy { arguments?.getString(EXTRA_TITLE) ?: "" }


    override fun bindView() = FragmentWebViewBinding.inflate(layoutInflater)

    override fun initViewAndEvent() {
    }

    override fun createObserver() {
    }


    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}