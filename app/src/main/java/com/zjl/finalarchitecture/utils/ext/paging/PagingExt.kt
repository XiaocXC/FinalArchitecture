package com.zjl.finalarchitecture.utils.ext.paging

import com.zjl.base.ApiResult
import com.zjl.base.exception.NetworkException
import com.zjl.base.ui.AppendState
import com.zjl.base.ui.ErrorState
import com.zjl.base.ui.LoadingState
import com.zjl.base.ui.PagerState
import com.zjl.base.ui.PagingUiModel
import com.zjl.base.ui.RefreshState
import com.zjl.base.ui.UiModel
import com.zjl.base.ui.append
import com.zjl.base.ui.data
import com.zjl.base.ui.onFailure
import com.zjl.base.ui.onLoading
import com.zjl.base.ui.onSuccess
import com.zjl.finalarchitecture.data.model.PageVO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlin.jvm.Throws

/**
 * 这是一个扩展出来的快速请求分页数据的方法
 *
 * 由于我们需要告诉UI层具体加载分页的状态
 * 例如是否加载到底、是否是重新刷新等状态，我们需要组装为[PagingUiModel]来统一处理
 *
 * 注意数据丢失会出现在满足以下条件的情况下：
 * 1.你的分页请求初始化是放在ViewModel的初始化中，而不是Fragment中的
 * 2.你使用了LiveData、StateFlow之类的观察类
 * 3.你没有使用Paging库进行分页，而是使用adapter、smartRefresh等界面库的分页操作
 * 以上条件均满足，当切换界面重建则会出现数据丢失的情况
 *
 * example 1.
 * 我需要请求一个分页数据，并将数据填充到stateFlow中
 *  // 文章列表
 *  private val _articleList = MutableStateFlow<PagingUiModel<ArticleListVO>>(PagingUiModel.Loading(true))
 *  val articleList: StateFlow<PagingUiModel<ArticleListVO>> = _articleList
 *
 *  fun test(){
 *      // 在协程作用域下启动（这里推荐requestScope，它能处理一切异常）
 *      requestScope {
 *          requestPagingApiResult(isRefresh = XXX, pagingUiModel = _articleList){
 *              ApiService.requestList(currentPage)
 *          }.await()
 *      }
 *  }
 *
 *
 * example 2.
 * 我需要请求一个分页数据，但我不需要自行填充，我自己处理返回数据
 *
 *  fun test(){
 *      // 在协程作用域下启动（这里推荐requestScope，它能处理一切异常）
 *      requestScope {
 *          val resultPageList = requestPagingApiResult(isRefresh = XXX){
 *              ApiService.requestList(currentPage)
 *          }.await()
 *
 *          // 自行处理返回的分页数据 resultPageList
 *      }
 *  }
 *
 * @param isRefresh 是否是重新刷新
 * @param pagingUiModel PagingUiModel的状态值，如果不传，你可以通过返回值自行处理
 * @param block 请求的接口调用
 *
 */
@Throws(NetworkException::class)
inline fun <reified T> CoroutineScope.requestPagingApiResult(
    isRefresh: Boolean,
    pagingUiModel: MutableStateFlow<PagingUiModel<T>>? = null,
    crossinline block: suspend (CoroutineScope.() -> ApiResult<PageVO<T>>)
): Deferred<PageVO<T>> {
    return async {
        ensureActive()
        // 如果传入了uiModel，则请求前更改为加载中状态
        if(pagingUiModel != null){
            pagingUiModel.value = pagingUiModel.value.append(PagingUiModel.Loading(isRefresh))
        }
        // 进行请求，并解析返回结果
        return@async when(val result = block(this)){
            is ApiResult.Success ->{
                val data = result.data
                // 如果传入了uiModel，则给它赋值成功状态
                if(pagingUiModel != null){
                    pagingUiModel.value = pagingUiModel.value.append(PagingUiModel.Success(data.dataList, isRefresh, data.over))
                }

                // 脱壳返回里面的数据
                data
            }
            is ApiResult.Failure ->{
                val throwable = result.throwable
                // 如果传入了uiModel，则给它赋值失败状态
                if(pagingUiModel != null){
                    pagingUiModel.value = pagingUiModel.value.append(PagingUiModel.Error(throwable, isRefresh))
                }
                // 抛出错误，让外部去捕捉
                throw throwable
            }
        }
    }
}

@Throws(NetworkException::class)
inline fun <reified T> CoroutineScope.requestPagingApiResult(
    pager: Pager<T>,
    crossinline block: suspend (CoroutineScope.() -> ApiResult<PageVO<T>>)
): Deferred<PageVO<T>> {
    return async {
        ensureActive()
        // 进行请求，并解析返回结果
        return@async when(val result = block(this)){
            is ApiResult.Success ->{
                val data = result.data
                pager.append(data.dataList, data.over)
                // 脱壳返回里面的数据
                data
            }
            is ApiResult.Failure ->{
                val throwable = result.throwable
                // 如果传入了uiModel，则给它赋值失败状态
                pager.error(throwable)
                // 抛出错误，让外部去捕捉
                throw throwable
            }
        }
    }
}

class Pager<T>(
    /**
     * 初始页码
     */
    val initPageIndex: Int = 0
) {

    /**
     * 当前页码
     */
    var currentIndex = 0

    /**
     * 是否滑动到底部
     */
    var endOfPaginationReached: Boolean = false

    private val _addListState = Channel<PagerState<T>>()
    private val addListState = _addListState.receiveAsFlow()

    /**
     * 内部存储的总List数据
     */
    val totalList = mutableListOf<T>()

    /**
     * 观察的变化数据
     */
    val observePager = flow {
        emit(RefreshState(endOfPaginationReached, totalList))

        addListState.collect {
            emit(it)
        }
    }

    /**
     * 添加分页数据
     * 内部会根据调用次数，自动进行页码添加等操作
     */
    suspend fun append(data: List<T>, endOfPaginationReached: Boolean){
        val index = currentIndex
        if(index == initPageIndex){
            totalList.clear()
        }
        totalList.addAll(data)
        currentIndex ++
        this.endOfPaginationReached = endOfPaginationReached

        if(index == initPageIndex){
            _addListState.send(RefreshState(endOfPaginationReached, data))
        } else {
            _addListState.send(AppendState(endOfPaginationReached, data))
        }
    }


    /**
     * 刷新数据
     */
    suspend fun loading(refresh: Boolean){
        if(refresh){
            currentIndex = initPageIndex
        }
        _addListState.send(LoadingState())
    }

    /**
     * 分页请求失败
     */
    suspend fun error(throwable: Throwable){
        _addListState.send(ErrorState(throwable))
    }
}