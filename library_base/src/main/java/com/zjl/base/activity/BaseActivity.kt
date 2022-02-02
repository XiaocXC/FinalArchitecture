package com.zjl.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.zjl.base.viewmodel.BaseViewModel
import com.zy.multistatepage.MultiStatePage.bindMultiState

/**
 * @author Xiaoc
 * @since 2022-01-07
 *
 * 基类Activity，提供基本的封装内容进行统一
 * 规定一个Activity要基于ViewBinding且有一个统一的ViewModel作为支撑
 * 这一点在BaseFragment中没有强制
 */
abstract class BaseActivity<V: ViewBinding, VM: BaseViewModel>: AppCompatActivity() {

    protected val mBinding: V by lazy {
        bindView()
    }

    protected val mViewModel: VM by lazy {
        bindViewModel()
    }

    /**
     * 整个Activity的UiState状态控制器
     */
    protected val rootUiState by lazy {
        bindMultiState(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        initViewAndEvent()
        createObserver()
    }

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
     * 初始化视图和事件
     * 用于初始化需要的View相关内容，包括其点击事件，初始状态等
     */
    abstract fun initViewAndEvent()

    /**
     * 创建观察者
     */
    abstract fun createObserver()

}