package com.zjl.base.activity

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.gyf.immersionbar.ImmersionBar
import com.kongzue.dialogx.dialogs.WaitDialog
import com.zjl.base.network.NetworkManager
import com.zjl.base.globalContext
import com.zjl.base.ui.onFailure
import com.zjl.base.ui.onLoading
import com.zjl.base.ui.onSuccess
import com.zjl.base.ui.state.EmptyState
import com.zjl.base.ui.state.ErrorState
import com.zjl.base.ui.state.LoadingState
import com.zjl.base.utils.ext.getVmClazz
import com.zjl.base.utils.ext.inflateBindingWithGeneric
import com.zjl.base.utils.ext.isNightMode
import com.zjl.base.utils.launchAndCollectIn
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.lib_base.R
import com.zjl.library_trace.base.IPageTrackNode
import com.zjl.library_trace.base.ITrackNode
import com.zjl.library_trace.base.TrackParams
import com.zjl.library_trace.ext.getReferrerParams
import com.zy.multistatepage.MultiStateContainer
import com.zy.multistatepage.MultiStatePage.bindMultiState
import com.zy.multistatepage.state.SuccessState
import timber.log.Timber

/**
 * @author Xiaoc
 * @since 2022-01-07
 *
 * 基类Activity，提供基本的封装内容进行统一
 * 规定一个Activity要基于ViewBinding且有一个统一的ViewModel作为支撑
 * 这一点在BaseFragment中没有强制
 */
abstract class BaseActivity<V : ViewBinding, VM : BaseViewModel> : AppCompatActivity(), IPageTrackNode {

    protected lateinit var mBinding: V

    protected lateinit var mViewModel: VM

    /**
     * 整个Activity的UiState状态控制器
     */
    protected val rootUiState by lazy {
        bindMultiState(this)
    }

    // 来自上个页码的数据埋点快照
    private var referrerSnapshot: ITrackNode? = null

    // 这是一个默认的埋点数据值，你可以自定义Activity默认自带的埋点数据
    protected open val defaultTrackParams by lazy {
        TrackParams()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 启动Activity后获取上个页码传来的埋点数据，如果存在，则设置快照数据
        getReferrerSnapshot()?.let { referrerParams ->
            referrerSnapshot = object: ITrackNode {
                override val parentNode: ITrackNode?
                    get() = null

                override fun fillTrackParams(trackParams: TrackParams) {
                    trackParams.merge(referrerParams)
                }

            }
        }


        mBinding = bindView()
        setContentView(mBinding.root)

        // 设置沉浸式状态栏，此操作会去掉透明遮罩等内容
        val defaultImmersionBar = ImmersionBar.with(this)
            .transparentBar()
            .statusBarDarkFont(!resources.isNightMode())
            .navigationBarDarkIcon(!resources.isNightMode())

        val immersionBar = configImmersive(defaultImmersionBar)
        immersionBar?.init()

        mViewModel = bindViewModel()
        initViewAndEvent(savedInstanceState)
        createDefObserver()
        createObserver()
    }

    // ---------------------------------------------------------------------------------------------
    // 基本设置
    // ---------------------------------------------------------------------------------------------

    /**
     * 配置沉浸式
     * 默认Fragment不进行沉浸式处理，如果你需要请返回对应沉浸式处理的immersionBar
     * @param immersionBar 默认的沉浸式处理，你可以自行创建，也可以在此基础上创建
     */
    open fun configImmersive(immersionBar: ImmersionBar): ImmersionBar?{
        return immersionBar
    }

    /**
     * 展示加载框弹窗（基于DialogX）
     */
    open fun showUiLoadingWithDialog(
        message: String = getString(R.string.base_ui_description_status_view_loading),
        cancelable: Boolean = false
    ){
        WaitDialog.show(message).apply {
            isCancelable = cancelable
        }
    }

    /**
     * 展示加载界面
     * @param message 加载提示
     * @param uiState MultiStateContainer的视图对象，如果不传，默认就把整个Activity界面根视图变成加载界面
     */
    open fun showUiLoading(
        message: String = getString(R.string.base_ui_description_status_view_loading),
        uiState: MultiStateContainer = rootUiState)
    {
        uiState.show<LoadingState> {
            it.setLoadingMsg(message)
        }
    }

    /**
     * 展示错误界面
     * 提供一个默认实现，根据UiModel展示具体错误和重试逻辑
     * @param throwable 错误信息
     * @param uiState MultiStateContainer的视图对象，如果不传，默认就把整个Activity界面根视图变成失败界面
     */
    open fun showUiError(throwable: Throwable, uiState: MultiStateContainer = rootUiState) {
        WaitDialog.dismiss()
        uiState.show<ErrorState> {
            it.setErrorMsg(throwable.message)
            it.retry { retryAll() }
        }
    }

    /**
     * 展示成功页面
     * 提供一个默认实现，及展示正确的视图，隐藏掉所有负面内容
     * @param uiState MultiStateContainer的视图对象，如果不传，默认就把整个Activity界面根视图变成成功界面
     */
    open fun showUiSuccess(uiState: MultiStateContainer = rootUiState) {
        WaitDialog.dismiss()
        uiState.show(SuccessState())
    }

    /**
     * 重试方法，默认不实现
     * 子类可以重写该方法
     */
    open fun retryAll() {

    }

    /**
     * 绑定ViewBinding
     * 提供一个默认实现，基于反射
     * @return 返回一个具体泛型的ViewBinding实例
     */
    open fun bindView(): V {
        return inflateBindingWithGeneric(layoutInflater)
    }

    /**
     * 绑定ViewModel
     * @return 返回一个具体泛型的ViewModel实例
     */
    open fun bindViewModel(): VM {
        return ViewModelProvider(this)[getVmClazz(this)]
    }

    open fun createDefObserver() {
        // 监听网络状态
        // 我们规定监听网络状态的内容在Activity创建时开始，避免恢复Activity时重新观察的问题
        NetworkManager.networkState.launchAndCollectIn(this, minActiveState = Lifecycle.State.CREATED){
            val hasNetwork = NetworkManager.isConnectNetwork(globalContext)
            Timber.i("Network：网络状态发生变化，是否有网络：%s", hasNetwork)
            networkStateChanged(hasNetwork)
        }
    }

    /**
     * 当网络状态发生变化时回调，你可以重写此方法来进行逻辑操作
     * @param hasNetwork 是否有网络
     */
    open fun networkStateChanged(hasNetwork: Boolean){}

    /**
     * 初始化视图和事件
     * 用于初始化需要的View相关内容，包括其点击事件，初始状态等
     */
    abstract fun initViewAndEvent(savedInstanceState: Bundle?)

    /**
     * 创建观察者
     */
    abstract fun createObserver()

    override fun onPause() {
        super.onPause()
        // 停止观察网络状态
        NetworkManager.unregisterNetworkCallback(this)
    }

    override fun onResume() {
        super.onResume()
        // 恢复观察网络状态
        NetworkManager.registerNetworkCallback(this)
    }

    // ---------------------------------------------------------------------------------------------
    // 埋点相关内容
    // ---------------------------------------------------------------------------------------------

    /**
     * 获取上一个页面来的快照埋点数据
     */
    fun getReferrerSnapshot(): TrackParams? = intent.getReferrerParams()?.let { referrerParams ->
        TrackParams().apply {
            // 补充页面来的数据
            fillReferrerKeyMap(referrerKeyMap(), referrerParams, this)
        }
    }

    /**
     * 通过映射值将上一个页面的值进行对应的更改
     */
    private fun fillReferrerKeyMap(
        map: Map<String, String>?, referrerParams: TrackParams, params: TrackParams
    ) {
        if (map.isNullOrEmpty()) {
            return
        }
        for ((fromKey, toKey) in map) {
            val toValue = referrerParams[fromKey]
            if (null != toValue) {
                params.setIfNull(toKey, toValue)
            }
        }
    }

    /**
     * 映射值Map
     * @return map
     * 跨页面后，上一个页面的埋点数据可能需要传递过来
     * 例如 上一个页面 cur_page = "main" 传递来后
     * 这个 cur_page Key可能需要变成 from_page
     * 所以这里返回的Map就是对应的映射
     */
    override fun referrerKeyMap(): Map<String, String>? {
        return null
    }

    override fun referrerSnapshot(): ITrackNode? {
        return referrerSnapshot
    }

    @CallSuper
    override fun fillTrackParams(trackParams: TrackParams) {
        trackParams.merge(defaultTrackParams)
    }


}