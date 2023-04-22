package com.zjl.base

import android.app.Application
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber
import android.content.Context
import android.os.Build
import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout


/**
 * 全局Application对象
 */
lateinit var globalApplication: BaseApplication

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
 *
 * 这是一个基础Application类，它提供了基本的初始化SDK功能
 * 并且提供了公共的全局公用ViewModel的内容
 */
open class BaseApplication: Application(), ImageLoaderFactory {


    companion object {

        internal fun initSDKInternal(application: Application){

            MMKV.initialize(application, application.filesDir.absolutePath + "/mmkv")

            /**
             * 配置Timber日志记录树
             * 当为debug模式时使用DebugTree
             */
            if(BuildConfig.DEBUG){
                Timber.plant(Timber.DebugTree())
            }

            //设置全局的Header构建器
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                layout.setPrimaryColorsId(R.color.base_blue_100, R.color.white) //全局设置主题颜色
                ClassicsHeader(context) //.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
            // 设置全局的 Footer 构建器
            SmartRefreshLayout.setDefaultRefreshFooterCreator{ context: Context, _: RefreshLayout ->
                ClassicsFooter(context)
            }
            // 设置全局初始化器
            SmartRefreshLayout.setDefaultRefreshInitializer { _: Context, layout: RefreshLayout ->
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

    /**
     * 全局保持的ViewModel提供者
     * 我们可以在自己的Application中初始化ViewModel
     * 保证这个ViewModel在Application级别均存在
     * 全局ViewModel可以用作在UI层的配置更改等内容时进行通知
     * 效果类似EventBus
     */
    private var mFactory: ViewModelProvider.Factory? = null
//    private lateinit var mAppViewModelStore: ViewModelStore

    @CallSuper
    override fun onCreate() {
        super.onCreate()
        globalApplication = this
//        mAppViewModelStore = ViewModelStore()

        initSDKInternal(this)

        initSDK()
    }

    /**
     * 可以重写该方法，初始化自己的第三方SDK
     */
    open fun initSDK(){}

    override fun newImageLoader(): ImageLoader {
        // Coil配置
        return ImageLoader.Builder(this)
            .components {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
    }

//    /**
//     * 获取一个全局的ViewModel
//     */
//    fun getAppViewModelProvider(): ViewModelProvider {
//        return ViewModelProvider(this, this.getAppFactory())
//    }
//
//    override fun getViewModelStore(): ViewModelStore {
//        return mAppViewModelStore
//    }

//    private fun getAppFactory(): ViewModelProvider.Factory {
//        if (mFactory == null) {
//            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
//        }
//        return mFactory as ViewModelProvider.Factory
//    }


}