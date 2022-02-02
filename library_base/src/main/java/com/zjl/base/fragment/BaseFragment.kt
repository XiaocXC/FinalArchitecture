package com.zjl.base.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding
import com.zjl.base.utils.autoCleared
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

    var mBinding by autoCleared<V>()
    private set

    private val mHandler = Handler(Looper.myLooper()!!)

    //是否第一次加载
    private var isFirst: Boolean = true

    /**
     * 整个Fragment的状态控制器
     */
    protected lateinit var uiRootState: MultiStateContainer

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
        mBinding = _binding
        uiRootState = _binding.root.bindMultiState()
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

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }

}