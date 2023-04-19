package com.zjl.base.viewmodel

import android.util.Log
import com.zjl.base.BuildConfig
import kotlinx.coroutines.*
import java.io.Closeable
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * @author Xiaoc
 * @since  2022-11-14
 *
 * 请求网络数据的协程作用域
 * 该灵感来源于 Net 库：https://liangjingkanji.github.io/
 *
 * 该协程域与普通的ViewModel中的协程域区别如下：
 * 1.提供了全局兜底抓错的问题，防止作用域中执行的代码出现任何错误导致的闪退
 *
 * 与原来BaseViewModel的 [launchRequestByNormal] 相比，它更加灵活，并且能够完美解决并发任务等带来的写法困难问题
 *
 * 用法：
 * class EmptyViewModel: BaseViewModel() {
 *
 *     fun test(){
 *          requestScope {
 *
 *          }.catch{ e ->
 *             // 当作用域中出现任何错误时跑到这里（可选）
 *          }.finally {
 *             // 当作用域内部执行完毕后跑到这里（可选）
 *          }
 *     }
 * }
 *
 **/
open class RequestCoroutineScope(
    val dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
) : CoroutineScope, Closeable {

    companion object {
        const val TAG = "RequestCoroutineScope"
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        catch(throwable)
    }

    protected open var catch: (RequestCoroutineScope.(Throwable) -> Unit)? = null
    protected open var finally: (RequestCoroutineScope.(Throwable?) -> Unit)? = null

    /**
     * 这里我们把全局协程异常处理器加上，做一个全局兜底
     */
    override val coroutineContext: CoroutineContext = dispatcher + exceptionHandler + SupervisorJob()

    open fun launch(block: suspend CoroutineScope.() -> Unit): RequestCoroutineScope {
        launch(EmptyCoroutineContext) {
            block()
        }.invokeOnCompletion {
            finally(it)
        }
        return this
    }

    /**
     * 当作用域内部执行结束后回调
     * @param e 如果发生异常导致作用域执行完毕, 则该参数为该异常对象, 正常结束则为null
     */
    protected open fun finally(e: Throwable?) {
        finally?.invoke(this@RequestCoroutineScope, e)
    }

    /**
     * 当作用域出现错误时回调
     * @param e 错误信息
     */
    protected open fun catch(e: Throwable) {
        catch?.invoke(this@RequestCoroutineScope, e)
        if(BuildConfig.DEBUG){
            // Debug模式打印更详细的信息
            val adjustMessage = e.stackTraceToString()
            Log.d(TAG, adjustMessage)
        }
    }

    /**
     * 当作用域内发生异常时回调
     */
    open fun catch(block: RequestCoroutineScope.(Throwable) -> Unit = {}): RequestCoroutineScope {
        this.catch = block
        return this
    }

    /**
     * 当作用域内部执行结束后回调
     * 无论正常或者异常结束都将最终执行
     */
    open fun finally(block: RequestCoroutineScope.(Throwable?) -> Unit = {}): RequestCoroutineScope {
        this.finally = block
        return this
    }

    override fun close() {
        coroutineContext.cancel()
    }
}

/**
 * BaseViewModel的获取requestScope的扩展方法
 */
fun BaseViewModel.requestScope(
    dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
    block: suspend CoroutineScope.() -> Unit
): RequestCoroutineScope{
    val scope = RequestCoroutineScope(dispatcher = dispatcher).launch(block)
    // 将该协程加入到ViewModel可销毁清单中
    addCloseable(scope)
    return scope
}