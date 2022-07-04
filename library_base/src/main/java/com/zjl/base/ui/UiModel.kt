package com.zjl.base.ui


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

    data class Success<T>(val data: T) : UiModel<T>(){
        override fun equals(other: Any?): Boolean {
            // 我们要把equals方法返回false，防止StateFlow判断值相同不更新的问题
            return false
        }

        override fun hashCode(): Int {
            return data?.hashCode() ?: 0
        }
    }
    data class Loading<T>(val data: T? = null) : UiModel<T>(){
        override fun equals(other: Any?): Boolean {
            // 我们要把equals方法返回false，防止StateFlow判断值相同不更新的问题
            return false
        }

        override fun hashCode(): Int {
            return data?.hashCode() ?: 0
        }
    }
    data class Error<T>(val error: Throwable, val data: T? = null) : UiModel<T>(){
        override fun equals(other: Any?): Boolean {
            // 我们要把equals方法返回false，防止StateFlow判断值相同不更新的问题
            return false
        }

        override fun hashCode(): Int {
            var result = error.hashCode()
            result = 31 * result + (data?.hashCode() ?: 0)
            return result
        }
    }

}

/**
 * 如果UiModel为Success时执行[action]自定义的操作
 * 否则不进行任何操作，返回本身
 * @param action 成功后的操作
 */
inline fun <T> UiModel<T>.onSuccess(action: (value: T) -> Unit): UiModel<T>{
    if(this is UiModel.Success){
        action(this.data)
    }
    return this
}

/**
 * 如果UiModel为Failure时执行[action]自定义的操作
 * 否则不进行任何操作，返回本身
 * @param action 失败后的操作
 */
inline fun <T> UiModel<T>.onFailure(action: (value: T?, throwable: Throwable) -> Unit): UiModel<T>{
    if(this is UiModel.Error){
        action(this.data, this.error)
    }
    return this
}

/**
 * 如果UiModel为Loading时执行[action]自定义的操作
 * 否则不进行任何操作，返回本身
 * @param action 加载中的操作
 */
inline fun <T> UiModel<T>.onLoading(action: (value: T?) -> Unit): UiModel<T>{
    if(this is UiModel.Loading){
        action(this.data)
    }
    return this
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

