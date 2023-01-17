package com.zjl.finalarchitecture

import android.content.Context
import com.zjl.base.BaseApplication
import com.zjl.finalarchitecture.module.global.AppConfigViewModel
import com.zjl.finalarchitecture.theme.ThemeManager
import com.zjl.library_skin.SkinManager
import com.zjl.library_skin.inflater.MaterialDesignViewInflater
import com.zjl.library_skin.provider.SkinProvider

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

    override fun initSDK() {
        super.initSDK()


        // 初始化换肤器
        SkinManager
            .init(this)
            .setSkinProvider(object: SkinProvider(){
                override fun support(context: Context): Boolean {
                    return ThemeManager.currentTheme.value != ThemeManager.FinalTheme.DEFAULT
                }

                override fun replaceResIdPrefix(
                    context: Context,
                    resName: String,
                    resType: String
                ): String {
                    return when(ThemeManager.currentTheme.value){
                        ThemeManager.FinalTheme.GREEN ->{
                            "theme_green_$resName"
                        }
                        else ->{
                            resName
                        }
                    }
                }

            })
            .addSkinViewInflater(MaterialDesignViewInflater())
    }
}