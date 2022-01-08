package com.zjl.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.zjl.base.viewmodel.BaseViewModel

/**
 * @author Xiaoc
 * @since 2022-01-07
 *
 * 基类Activity，提供基本的封装内容进行统一
 * 规定一个Activity要基于ViewBinding且有一个统一的ViewModel作为支撑
 * 这一点在BaseFragment中没有强制
 */
abstract class BaseActivity<V: ViewBinding, VM: BaseViewModel>: AppCompatActivity() {

    protected val binding: V by lazy {
        bindView()
    }

    protected val viewModel: VM by lazy {
        bindViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViewAndEvent()
        createObserver()
    }

    /**
     * 初始化视图和事件
     * 用于初始化需要的View相关内容，包括其点击事件，初始状态等
     */
    abstract fun initViewAndEvent()

    /**
     * 绑定ViewBinding
     * @return 返回一个具体泛型的ViewBinding实例
     */
    abstract fun bindView(): V

    /**
     * 绑定ViewModel
     * @return 返回一个具体泛型的ViewModel实例
     */
    abstract fun bindViewModel(): VM


    /**
     * 创建观察者
     */
    abstract fun createObserver()

}