package com.zjl.finalarchitecture.module.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zjl.base.fragment.BaseFragment
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentProjectBinding
import com.zjl.finalarchitecture.databinding.FragmentWechatBinding

/**
 * @description:首页公众号
 * @author: zhou
 * @date : 2022/1/20 18:07
 */
class WeChatFragment: BaseFragment<FragmentWechatBinding>() {

    companion object {
        fun newInstance() = WeChatFragment()
    }

    override fun bindView() =  FragmentWechatBinding.inflate(layoutInflater)

    override fun initViewAndEvent() {
    }

    override fun createObserver() {
    }
}