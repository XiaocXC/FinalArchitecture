package com.zjl.base

import android.app.Application
import com.tencent.mmkv.MMKV
import com.zjl.lib_base.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber
import android.content.Context
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.zjl.lib_base.R


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


    companion object {

        fun initSDK(){
            //设置全局的Header构建器
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                layout.setPrimaryColorsId(R.color.base_light_blue_800, R.color.white) //全局设置主题颜色
                ClassicsHeader(context) //.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
            // 设置全局的 Footer 构建器
            SmartRefreshLayout.setDefaultRefreshFooterCreator{ context: Context, layout: RefreshLayout ->
                ClassicsFooter(context)
            }
            // 设置全局初始化器
            SmartRefreshLayout.setDefaultRefreshInitializer { context: Context, layout: RefreshLayout ->
                // 刷新头部是否跟随内容偏移
                layout.setEnableHeaderTranslationContent(true)
                    // 刷新尾部是否跟随内容偏移
                    .setEnableFooterTranslationContent(true)
                    // 加载更多是否跟随内容偏移
                    .setEnableFooterFollowWhenNoMoreData(true)
                    // 内容不满一页时是否可以上拉加载更多
                    .setEnableLoadMoreWhenContentNotFull(false)
                    // 仿苹果越界效果开关
                    .setEnableOverScrollDrag(false)
            }
        }

    }

    override fun onCreate() {
        super.onCreate()
        globalApplication = this

        MMKV.initialize(this.filesDir.absolutePath + "/mmkv")

        /**
         * 配置Timber日志记录树
         * 当为debug模式时使用DebugTree
         */
        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }

        initSDK()

    }
}