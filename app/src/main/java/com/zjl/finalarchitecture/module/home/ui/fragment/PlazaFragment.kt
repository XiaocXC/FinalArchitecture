package com.zjl.finalarchitecture.module.home.ui.fragment

import androidx.fragment.app.viewModels
import com.zjl.base.fragment.BaseFragment
import com.zjl.finalarchitecture.databinding.FragmentPlazaBinding
import com.zjl.finalarchitecture.module.home.viewmodel.PlazaViewModel

/**
 * @description:
 * @author: zhou
 * @date : 2022/1/20 17:56
 */
class PlazaFragment : BaseFragment<FragmentPlazaBinding>(){

    companion object {
        fun newInstance() = PlazaFragment()
    }

    private val articleViewModel by viewModels<PlazaViewModel>()

    override fun bindView() = FragmentPlazaBinding.inflate(layoutInflater)

    override fun initViewAndEvent() {
    }

    override fun createObserver() {

    }

}