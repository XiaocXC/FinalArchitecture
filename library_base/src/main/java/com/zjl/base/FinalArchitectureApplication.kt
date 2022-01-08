package com.zjl.base

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob


/**
 * 全局Application对象
 */
lateinit var globalApplication: FinalArchitectureApplication

/**
 * 全局Context
 */
val globalContext get() = globalApplication.applicationContext

/**
 * APP全局协程
 * 该协程为单例，且作用于整个APP
 * 取消任务请取消对应Job任务
 */
val globalCoroutineScope by lazy {
    CoroutineScope(SupervisorJob() + Dispatchers.Default)
}

/**
 * @author Xiaoc
 * @since 2022-01-07
 */
class FinalArchitectureApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        globalApplication = this
    }
}