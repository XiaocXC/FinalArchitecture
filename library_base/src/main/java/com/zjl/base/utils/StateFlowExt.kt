package com.zjl.base.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


/**
 * 启动一个新的协程并且在Fragment的viewLifecycleOwner规定的activeState中执行
 * 该内容与LiveData效果一致，但注意基于观察的StateFlow等内容的处理
 * @param minActiveState 最小活跃状态，默认为Lifecycle为STARTED时收集，在PAUSE时停止收集
 */
@Deprecated("由于语义问题，导致过程过于复杂，可以使用更简单的launchAndCollectIn",
    replaceWith = ReplaceWith("Flow<T>.launchAndCollectIn(owner, state, block)")
)
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
@Deprecated("由于语义问题，导致过程过于复杂，可以使用更简单的launchAndCollectIn",
    replaceWith = ReplaceWith("Flow<T>.launchAndCollectIn(owner, state, block)")
)
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

/**
 * 启动对应LifeCycle协程作用域并在正确的生命周期状态下收集流的变化
 * 该API仿照了LiveData的用法，可以无损失迁移
 * @param minActiveState 最小收集状态，默认为Lifecycle的STARTED，也就是代表在STARTED时重新收集，在PAUSE时停止收集
 */
inline fun <T> Flow<T>.launchAndCollectIn(
    owner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.(T) -> Unit
){
    owner.lifecycleScope.launch {
        owner.repeatOnLifecycle(minActiveState){
            collect {
                block(it)
            }
        }
    }
}