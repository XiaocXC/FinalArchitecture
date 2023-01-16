package com.zjl.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.zjl.base.utils.ext.getVmClazz
import com.zjl.base.utils.ext.inflateBindingWithGeneric
import com.zjl.base.viewmodel.BaseViewModel

/**
 * @author Xiaoc
 * @since 2022-01-07
 *
 * 基类Fragment，提供基本的封装内容进行统一
 * 规定一个Fragment要基于ViewBinding
 * 根据MVI或MVVM规定，一个界面由一个ViewModel所绑定，我们这里需要一个VM的绑定
 * 如果该界面需要用到多个VM，则请将负责界面状态的作为主要的VM，或者自行实现状态管理
 */
abstract class BaseFragment<V : ViewBinding, VM : BaseViewModel> : OriginFragment() {

    private var _mBinding: V? = null
    protected val mBinding get() = _mBinding!!

    protected lateinit var mViewModel: VM

    override fun generateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = bindView(inflater, container, savedInstanceState)
        return mBinding.root
    }

    /**
     * 创建ViewBinding视图
     * 默认实现通过反射实现
     * 你也可以重写该方法进行自行创建
     */
    open fun bindView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): V {
        return inflateBindingWithGeneric(inflater, container, false)
    }

    @CallSuper
    override fun bindOthers() {
        mViewModel = createViewModel()
    }

    /**
     * 利用反射创建ViewModel实例
     * 该ViewModel默认实现为当前Fragment作用域，如果你需要自定义作用域，请重写该方法
     */
    open fun createViewModel(): VM {
        return ViewModelProvider(this)[getVmClazz(this)]
    }

}