package com.zjl.finalarchitecture.utils.ext.paging

import com.zjl.base.ApiResult
import com.zjl.base.exception.ApiException
import com.zjl.base.ui.PagingUiModel
import com.zjl.base.ui.append
import com.zjl.finalarchitecture.data.model.PageVO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.jvm.Throws

/**
 * 这是一个扩展出来的快速请求分页数据的方法
 *
 * 由于我们需要告诉UI层具体加载分页的状态
 * 例如是否加载到底、是否是重新刷新等状态，我们需要组装为[PagingUiModel]来统一处理
 *
 * @param isRefresh 是否是重新刷新
 * @param pagingUiModel PagingUiModel的状态值，如果不传，你可以通过返回值自行处理
 * @param block 请求的接口调用
 *
 * example 1.
 * 我需要请求一个分页数据，并将数据填充到stateFlow中
 *  // 文章列表
 *  private val _articleList = MutableStateFlow<PagingUiModel<ArticleListVO>>(PagingUiModel.Loading(true))
 *  val articleList: StateFlow<PagingUiModel<ArticleListVO>> = _articleList
 *
 *  fun test(){
 *      requestPagingApiResult(isRefresh = XXX, pagingUiModel = _articleList){
 *          ApiService.requestList(currentPage)
 *      }.await()
 *  }
 *
 * example 2.
 * 我需要请求一个分页数据，但我不需要自行填充，我自己处理返回数据
 *
 *  fun test(){
 *      val resultPageList = requestPagingApiResult(isRefresh = XXX){
 *          ApiService.requestList(currentPage)
 *      }.await()
 *
 *      // 自行处理返回的分页数据 resultPageList
 *  }
 *
 */
@Throws(ApiException::class)
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
                val exception = ApiException(result.error)
                // 如果传入了uiModel，则给它赋值失败状态
                if(pagingUiModel != null){
                    pagingUiModel.value = pagingUiModel.value.append(PagingUiModel.Error(exception, isRefresh))
                }
                // 抛出错误
                throw exception
            }
        }
    }
}