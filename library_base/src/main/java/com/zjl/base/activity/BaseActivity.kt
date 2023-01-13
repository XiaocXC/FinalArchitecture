package com.zjl.base.activity

import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.zjl.base.utils.ext.getVmClazz
import com.zjl.base.utils.ext.inflateBindingWithGeneric
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.library_trace.base.IPageTrackNode

/**
 * @author Xiaoc
 * @since 2022-01-07
 *
 * 基类Activity，提供基本的封装内容进行统一
 * 规定一个Activity要基于ViewBinding且有一个统一的ViewModel作为支撑
 */
abstract class BaseActivity<V : ViewBinding, VM : BaseViewModel> : OriginActivity(), IPageTrackNode {

    protected lateinit var mBinding: V

    protected lateinit var mViewModel: VM

    override fun generateView() {
        mBinding = bindView()
        setContentView(mBinding.root)

        mViewModel = bindViewModel()
    }

    /**
     * 绑定ViewBinding
     * 提供一个默认实现，基于反射
     * @return 返回一个具体泛型的ViewBinding实例
     */
    open fun bindView(): V {
        return inflateBindingWithGeneric(layoutInflater)
    }

    /**
     * 绑定ViewModel
     * @return 返回一个具体泛型的ViewModel实例
     */
    open fun bindViewModel(): VM {
        return ViewModelProvider(this)[getVmClazz(this)]
    }


}