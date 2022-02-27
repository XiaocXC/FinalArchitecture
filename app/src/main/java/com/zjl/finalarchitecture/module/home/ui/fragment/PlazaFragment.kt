package com.zjl.finalarchitecture.module.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zjl.base.fragment.BaseFragment
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentPlazaBinding

/**
 * @description:
 * @author: zhou
 * @date : 2022/1/20 17:56
 */
class PlazaFragment : BaseFragment<FragmentPlazaBinding>(){

    companion object {
        fun newInstance() = PlazaFragment()
    }

    override fun bindView() = FragmentPlazaBinding.inflate(layoutInflater)

    override fun initViewAndEvent() {
    }

    override fun createObserver() {

    }

}