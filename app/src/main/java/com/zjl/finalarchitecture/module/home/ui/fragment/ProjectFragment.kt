package com.zjl.finalarchitecture.module.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.autoCleared
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentProjectBinding
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleAdapter
import com.zjl.finalarchitecture.module.home.viewmodel.AskViewModel
import com.zjl.finalarchitecture.module.home.viewmodel.ProjectViewModel

/**
 * @description:首页项目
 * @author: zhou
 * @date : 2022/1/20 18:04
 */
class ProjectFragment: BaseFragment<FragmentProjectBinding>() {


    companion object {
        fun newInstance() = ProjectFragment()
    }

    private val mProjectViewModel by viewModels<ProjectViewModel>()

    private var mArticleAdapter by autoCleared<ArticleAdapter>()


    override fun bindView() = FragmentProjectBinding.inflate(layoutInflater)

    override fun initViewAndEvent() {

    }

    override fun createObserver() {

    }

}