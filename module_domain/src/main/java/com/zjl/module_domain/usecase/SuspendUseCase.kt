package com.zjl.module_domain.usecase

import com.zjl.base.ApiResult
import com.zjl.base.ui.UiModel
import com.zjl.base.error.ApiError
import com.zjl.base.exception.ApiException
import com.zjl.base.transToUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * @author Xiaoc
 * @since 2021/2/2
 *
 * 在 ViewModel 与 Repository 之间加入了一个中间层 UseCase
 * 它的作用是做 Repository 与 ViewModel中的数据转换
 * 因为会出现 Repository 提供的数据与我们实际需要展示的数据结构有出入
 * 则可以在此进行转换后返回
 *
 * 这是基于Suspend的抽象，它主要使用于仅需要一次返回内容的情况
 * 因为 suspend 只适用于单次返回，而对于网络请求等是不适用的
 * 如果你返回的内容多次你也可以使用 [FlowUseCase]
 *
 * 你具体的UseCase继承该类后实现 [execute] 方法即可
 *
 * 该类不需要是单例类
 *
 * @param Parameters 需要给 Repository 传递的参数类型
 * @param Result 最后转换后返回的数据类型
 **/
abstract class SuspendUseCase<in Parameters, Result>(
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO) {

    suspend operator fun invoke(parameters: Parameters): UiModel<Result> {
        return try {
            withContext(coroutineDispatcher) {
                val result = execute(parameters)
                return@withContext result.transToUiModel()
            }
        } catch (e: Exception) {
            Timber.d(e)
            UiModel.Error(ApiException(ApiError.unknownError))
        }
    }

    /**
     * 真正执行的方法，也是需要重写的方法
     * 在该方法里你需要从 Repository 请求数据
     *
     * @param parameters 需要给 Repository 传递的参数
     */
    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(parameters: Parameters): ApiResult<Result>
}