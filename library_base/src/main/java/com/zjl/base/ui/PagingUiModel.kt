package com.zjl.base.ui


/**
 * @author Xiaoc
 * @since 2021/5/10
 *
 * 响应内容的分页包装类，它主要用于与UI层状态进行关联
 *
 * @param refresh 刷新
 */
sealed class PagingUiModel<T>(open val refresh: Boolean) {

    /**
     * 是否需要加载更多
     * @param data 数据
     * @param refresh 刷新
     * @param hasMore 加载更多
     */
    data class Success<T>(
        val data: List<T>,
        override val refresh: Boolean,
        val hasMore: Boolean
    ) : PagingUiModel<T>(refresh) {

        override fun equals(other: Any?): Boolean {
            // 我们要把equals方法返回false，防止StateFlow判断值相同不更新的问题
            return false
        }

        override fun hashCode(): Int {
            var result = super.hashCode()
            result = 31 * result + data.hashCode()
            result = 31 * result + refresh.hashCode()
            result = 31 * result + hasMore.hashCode()
            return result
        }

    }

    data class Loading<T>(
        override val refresh: Boolean,
        val data: List<T>? = null
    ) : PagingUiModel<T>(refresh) {

        override fun equals(other: Any?): Boolean {
            // 我们要把equals方法返回false，防止StateFlow判断值相同不更新的问题
            return false
        }

        override fun hashCode(): Int {
            var result = refresh.hashCode()
            result = 31 * result + (data?.hashCode() ?: 0)
            return result
        }

    }

    data class Error<T>(
        val error: Throwable,
        override val refresh: Boolean,
        val data: List<T>? = null
    ) : PagingUiModel<T>(refresh) {

        override fun equals(other: Any?): Boolean {
            // 我们要把equals方法返回false，防止StateFlow判断值相同不更新的问题
            return false
        }

        override fun hashCode(): Int {
            var result = error.hashCode()
            result = 31 * result + refresh.hashCode()
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
//inline fun <T> PagingUiModel<T>.onSuccess(action: (successModel: PagingUiModel.Success<T>) -> Unit): PagingUiModel<T> {
//    if (this is PagingUiModel.Success) {
//        action(this)
//    }
//    return this
//}

/**
 * 如果UiModel为Failure时执行[action]自定义的操作
 * 否则不进行任何操作，返回本身
 * @param action 失败后的操作
 */
//inline fun <T> PagingUiModel<T>.onFailure(action: (value: List<T>?, throwable: Throwable) -> Unit): PagingUiModel<T> {
//    if (this is PagingUiModel.Error) {
//        action(this.data, this.error)
//    }
//    return this
//}

/**
 * 如果UiModel为Loading时执行[action]自定义的操作
 * 否则不进行任何操作，返回本身
 * @param action 加载中的操作
 */
//inline fun <T> PagingUiModel<T>.onLoading(action: (value: List<T>?) -> Unit): PagingUiModel<T> {
//    if (this is PagingUiModel.Loading) {
//        action(this.data)
//    }
//    return this
//}


//val <T> PagingUiModel<T>.data: List<T>?
//    get() {
//        return when (this) {
//            is PagingUiModel.Success -> {
//                this.data
//            }
//            is PagingUiModel.Loading -> {
//                this.data
//            }
//            is PagingUiModel.Error -> {
//                this.data
//            }
//        }
//    }

