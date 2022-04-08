package com.zjl.base.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @author Xiaoc
 * @since 2021/5/10
 *
 * 一个基于委托模式的当Fragment被销毁时自动清除属性的工具
 * 由于Fragment的生命周期特殊性与Navigation结合时可能导致回收View后的内存泄漏
 * 所以此工具基于fragment的lifecycle生命周期进行监管，并在其生命周期销毁后置空依赖的视图对象防止出现内存泄漏
 *
 * 使用方式如下：
 * private var XXX by autoCleared<XXX>()
 * 注意对应ViewBinding这类特殊的不能使用此autoCleared工具，因为当使用添加viewLifecycleOwner的Observer时，视图还没有创建，会直接报错
 *
 * @param isViewLifeCycle 如果为true，使用Fragment的ViewLifeCycle，在视图销毁时就进行清理，默认为true
 *
 */
class AutoClearedValue<T: Any>(fragment: Fragment, isViewLifeCycle: Boolean = true): ReadWriteProperty<Fragment,T>{
    private var _value: T? = null

    init {
        if(isViewLifeCycle){
            fragment.viewLifecycleOwner.lifecycle.addObserver(object: DefaultLifecycleObserver{
                override fun onDestroy(owner: LifecycleOwner) {
                    _value = null
                    fragment.viewLifecycleOwner.lifecycle.removeObserver(this)
                }
            })
        } else {
            fragment.lifecycle.addObserver(object: DefaultLifecycleObserver{
                override fun onDestroy(owner: LifecycleOwner) {
                    _value = null
                    fragment.lifecycle.removeObserver(this)
                }
            })
        }
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return _value ?: throw IllegalStateException(
            "已被自动清除无法调用"
        )
    }

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        _value = value
    }

}

fun <T : Any> Fragment.autoCleared(isViewLifeCycle: Boolean = true) = AutoClearedValue<T>(this, isViewLifeCycle)