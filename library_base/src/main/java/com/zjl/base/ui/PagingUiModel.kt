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
 * 由于Navigation的replace覆盖Fragment的问题，我们提供了该方法进行处理分页数据，它在赋值时会保留之前的页码数据
 * 防止切换暗黑模式等情况时，总数据丢失的问题
 * 注意数据丢失会出现在满足以下条件的情况下：
 * 1.你的分页请求初始化是放在ViewModel的初始化中，而不是Fragment中的
 * 2.你使用了LiveData、StateFlow之类的观察类
 * 3.你没有使用Paging库进行分页，而是使用adapter、smartRefresh等界面库的分页操作
 * 以上条件均满足，当切换界面重建则会出现数据丢失的情况
 */
fun <T> PagingUiModel<T>.append(pagingUiModel: PagingUiModel<T>): PagingUiModel<T>{
    val oldList = this.totalList
    return when (pagingUiModel){
        is PagingUiModel.Success ->{
            return if(pagingUiModel.refresh){
                pagingUiModel.copy(totalList = pagingUiModel.data.toMutableList())
            } else {
                // 如果是加载更多，我们把之前的总数据加上
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
