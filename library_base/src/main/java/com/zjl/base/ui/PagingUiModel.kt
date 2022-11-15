package com.zjl.base.ui


/**
 * @author Xiaoc
 * @since 2021/5/10
 *
 * 响应内容的分页包装类，它主要用于与UI层状态进行关联
 */
sealed class PagingUiModel<T> {

    /**
     * 当前数据是否是重新刷新的数据
     */
    abstract val refresh: Boolean

    /**
     * 当前数据的总分页List
     */
    abstract val totalList: MutableList<T>

    /**
     * 分页加载成功状态
     * @param data 数据
     * @param refresh 刷新
     * @param noMore 是否加载到头
     */
    data class Success<T>(
        val data: List<T>,
        override val refresh: Boolean,
        val noMore: Boolean,
        override val totalList: MutableList<T> = mutableListOf()
    ) : PagingUiModel<T>()

    /**
     * 分页加载中状态
     * @param data 数据
     * @param refresh 刷新
     */
    data class Loading<T>(
        override val refresh: Boolean,
        val data: List<T>? = null,
        override val totalList: MutableList<T> = mutableListOf()
    ) : PagingUiModel<T>()

    /**
     * 分页加载错误状态
     * @param error 错误信息
     * @param data 数据
     * @param refresh 刷新
     */
    data class Error<T>(
        val error: Throwable,
        override val refresh: Boolean,
        val data: List<T>? = null,
        override val totalList: MutableList<T> = mutableListOf()
    ) : PagingUiModel<T>()

}

/**
 * 更新PagingUiModel状态
 * @param pagingUiModel 新的PagingUiModel
 *
 * 由于Navigation的replace覆盖Fragment的问题，我们提供了该方法进行处理分页数据
 * 防止切换暗黑模式等情况时，总数据丢失的问题
 */
fun <T> PagingUiModel<T>.append(pagingUiModel: PagingUiModel<T>): PagingUiModel<T>{
    val oldList = this.totalList
    return when (pagingUiModel){
        is PagingUiModel.Success ->{
            return if(pagingUiModel.refresh){
                pagingUiModel.copy(totalList = pagingUiModel.data.toMutableList())
            } else {
                // 如果是加载更多，我们把总数据给放置正确
                val addList = pagingUiModel.data
                oldList.addAll(addList)
                pagingUiModel.copy(totalList = oldList)
            }
        }
        is PagingUiModel.Error -> {
            pagingUiModel.copy(totalList = oldList)
        }
        is PagingUiModel.Loading ->{
            pagingUiModel.copy(totalList = oldList)
        }
    }
}
