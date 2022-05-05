package com.zjl.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.zy.multistatepage.MultiStateContainer
import com.zy.multistatepage.bindMultiState

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

    private var _mBinding: V? = null
    protected val mBinding get() = _mBinding!!

    //是否第一次加载
    private var isFirst: Boolean = true

    /**
     * 整个Fragment的状态控制器
     */
    private var _uiRootState: MultiStateContainer? = null
    protected val uiRootState get() = _uiRootState!!

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val _binding = bindView()
        // 如果是DataBinding，加入lifecycleOwner
        if(_binding is ViewDataBinding){
            _binding.lifecycleOwner = viewLifecycleOwner
        }
        _mBinding = _binding
        _uiRootState = _binding.root.bindMultiState()
        return uiRootState
    }

    abstract fun bindView(): V

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFirst = true
        initViewAndEvent()
        createObserver()
    }

    /**
     * 初始化视图和事件
     * 用于初始化需要的View相关内容，包括其点击事件，初始状态等
     */
    abstract fun initViewAndEvent()

    /**
     * 创建观察者
     */
    abstract fun createObserver()

    /**
     * 当Fragment视图销毁时使用
     * 如果Fragment需要手动释放部分视图内容时，我们使用请确保在调用 super.onDestroyView() 之前清除视图
     */
    override fun onDestroyView() {
        super.onDestroyView()
        // 保持ViewBinding在 onCreateView 和 onDestroyView 生命周期之间
        // 这里不自动使用autoCleared原因是因为有个先后顺序，因为部分adapter的原因，需要重写onDestroyView来手动设置为Null
        // 而此时如果获取mBinding可能由于清除了mBinding导致报错，所以我们会手动处理
        _uiRootState = null
        _mBinding = null
    }

}