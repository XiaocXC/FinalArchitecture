package com.zjl.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.zjl.base.utils.autoCleared

/**
 * @author Xiaoc
 * @since 2022-01-07
 *
 * 基类Fragment，提供基本的封装内容进行统一
 * 规定一个Fragment要基于ViewBinding
 * Fragment可能会同时依赖多个ViewModel或者干脆不使用ViewModel
 * 所以我们没有在Fragment中强制规定ViewModel的使用
 */
abstract class BaseFragment<V: ViewBinding>: Fragment() {

    protected var binding by autoCleared<V>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = bindView()
        return binding.root
    }

    abstract fun bindView(): V
}