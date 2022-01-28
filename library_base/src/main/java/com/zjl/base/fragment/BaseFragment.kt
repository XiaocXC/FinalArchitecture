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

    lateinit var mActivity: AppCompatActivity

    var mBinding by autoCleared<V>()

    private val mHandler = Handler(Looper.myLooper()!!)

    //是否第一次加载
    private var isFirst: Boolean = true

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
        return mBinding.root
    }

    abstract fun bindView(): V

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

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
     * 懒加载
     */
    abstract fun lazyLoadData()


    override fun onResume() {
        super.onResume()
        onVisible()
    }

    /**
     * 是否需要懒加载
     */
    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst) {
            // 延迟加载 防止 切换动画还没执行完毕时数据就已经加载好了，这时页面会有渲染卡顿
            mHandler.postDelayed( {

                lazyLoadData()

                isFirst = false
            },lazyLoadTime())
        }
    }

    /**
     * 延迟加载 防止 切换动画还没执行完毕时数据就已经加载好了，这时页面会有渲染卡顿  bug
     * 这里传入你想要延迟的时间，延迟时间可以设置比转场动画时间长一点 单位： 毫秒
     * 不传默认 300毫秒
     * @return Long
     */
    open fun lazyLoadTime(): Long {
        return 300
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }

}