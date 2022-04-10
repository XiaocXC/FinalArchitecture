package com.zjl.finalarchitecture.module.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zjl.base.fragment.BaseFragment
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentAskBinding

/**
 * @description:首页问答
 * @author: zhou
 * @date : 2022/1/20 17:58
 */
class AskFragment: BaseFragment<FragmentAskBinding>() {

    companion object {
        fun newInstance() = AskFragment()
    }

    override fun bindView() = FragmentAskBinding.inflate(layoutInflater)

    override fun initViewAndEvent() {

    }

    override fun createObserver() {
    }


}