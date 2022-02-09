package com.zjl.base.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


/**
 * 启动一个新的协程并且在Fragment的viewLifecycleOwner规定的activeState中执行
 * 该内容与LiveData效果一致，但注意基于观察的StateFlow等内容的处理
 * @param minActiveState 最小活跃状态，默认为Lifecycle为STARTED时收集，在PAUSE时停止收集
 */
inline fun Fragment.launchAndRepeatWithViewLifecycle(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(minActiveState) {
            block()
        }
    }
}

/**
 * 启动一个新的协程并且在Activity的viewLifecycleOwner规定的activeState中执行
 * 该内容与LiveData效果一致，但注意基于观察的StateFlow等内容的处理
 * @param minActiveState 最小活跃状态，默认为Lifecycle为STARTED时收集，在PAUSE时停止收集
 */
inline fun AppCompatActivity.launchAndRepeatWithViewLifecycle(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(minActiveState){
            block()
        }
    }
}