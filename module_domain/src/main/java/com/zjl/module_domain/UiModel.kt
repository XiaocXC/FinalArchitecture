package com.zjl.module_domain


/**
 * @author Xiaoc
 * @since 2021/5/10
 *
 * 响应内容的包装类，它主要用于与UI层状态进行关联
 *
 * @param data 数据
 * @param error 错误信息（用于UI显示对应错误内容策略）
 */
sealed class UiModel<T>{

    data class Success<T>(val data: T) : UiModel<T>()
    data class Loading<T>(val data: T? = null) : UiModel<T>()
    data class Error<T>(val error: Throwable, val data: T? = null) : UiModel<T>()

}

val <T> UiModel<T>.data: T?
get() {
    return when(this){
        is UiModel.Success ->{
            this.data
        }
        is UiModel.Loading ->{
            this.data
        }
        is UiModel.Error ->{
            this.data
        }
    }
}

