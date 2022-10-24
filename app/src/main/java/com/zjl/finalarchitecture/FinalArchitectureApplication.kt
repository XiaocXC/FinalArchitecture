package com.zjl.finalarchitecture

import com.zjl.base.BaseApplication
import com.zjl.finalarchitecture.module.global.AppConfigViewModel

/**
 * @author Xiaoc
 * @since 2022-10-24
 *
 * 我们在APP启动时初始化了标准SDK
 * 以及几个可能会使用的全局事件ViewModel
 */

// Application全局的ViewModel，用于APP配置变更处理
val appConfigViewModel: AppConfigViewModel by lazy { FinalArchitectureApplication.appConfigViewModelInstance }

class FinalArchitectureApplication: BaseApplication() {

    companion object {
        lateinit var appConfigViewModelInstance: AppConfigViewModel
    }

    override fun onCreate() {
        super.onCreate()

        // 初始化全局事件ViewModel
        getAppViewModelProvider()[AppConfigViewModel::class.java]
    }
}