package com.zjl.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

/**
 * @author Xiaoc
 * @since 2022-01-07
 *
 * 基类Activity，提供基本的封装内容进行统一
 */
abstract class BaseActivity<T: ViewBinding>: AppCompatActivity() {

    protected val binding: T by lazy {
        bindView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViewAndEvent()
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
    abstract fun bindView(): T

}