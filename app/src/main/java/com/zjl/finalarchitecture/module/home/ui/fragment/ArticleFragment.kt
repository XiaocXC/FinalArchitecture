package com.zjl.finalarchitecture.module.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zjl.base.fragment.BaseFragment
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentArticleBinding
import com.zjl.finalarchitecture.databinding.FragmentHomeBinding

/**
 * @description:
 * @author: zhou
 * @date : 2022/1/20 17:52
 */
class ArticleFragment : BaseFragment<FragmentArticleBinding>() {

    companion object {
        fun newInstance() = ArticleFragment()
    }

    override fun bindView() =  FragmentArticleBinding.inflate(layoutInflater)

    override fun initViewAndEvent() {
    }

    override fun createObserver() {
    }

    override fun lazyLoadData() {
    }

}