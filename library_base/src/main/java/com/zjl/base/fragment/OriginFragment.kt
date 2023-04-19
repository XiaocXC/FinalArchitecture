package com.zjl.base.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.gyf.immersionbar.ImmersionBar
import com.kongzue.dialogx.dialogs.WaitDialog
import com.zjl.base.activity.BaseActivity
import com.zjl.base.globalContext
import com.zjl.base.network.NetworkManager
import com.zjl.base.ui.state.ErrorState
import com.zjl.base.ui.state.LoadingState
import com.zjl.base.utils.ext.isNightMode
import com.zjl.base.utils.launchAndCollectIn
import com.zjl.base.R
import com.zjl.library_trace.base.ITrackModel
import com.zjl.library_trace.base.TrackParams
import com.zjl.library_trace.ext.trackModel
import com.zy.multistatepage.MultiStateContainer
import com.zy.multistatepage.bindMultiState
import com.zy.multistatepage.state.SuccessState
import timber.log.Timber

/**
 * @author Xiaoc
 * @since 2023-01-13
 *
 * 基类Fragment，提供基本的封装内容进行统一
 * 它与[BaseFragment]不同点在于，BaseFragment需要VB和VM的支撑，而 OriginFragment 不需要
 */
abstract class OriginFragment : Fragment() {

    /**
     * 当前Fragment是否可见
     */
    protected var isUserHintVisible: Boolean = false

    /**
     * 整个Fragment的状态控制器
     */
    protected lateinit var uiRootState: MultiStateContainer


    /**
     * 默认的ImmersionBar配置
     */
    private val defaultImmersionBar by lazy {
        ImmersionBar.with(this)
            .transparentBar()
            .statusBarDarkFont(!resources.isNightMode())
            .navigationBarDarkIcon(!resources.isNightMode())
    }

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = generateView(inflater, container, savedInstanceState)
        uiRootState = view.bindMultiState()
        return uiRootState
    }

    abstract fun generateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View

    private fun configImmersiveInternal(){
        if(isUserHintVisible){
            val activity = requireActivity()
            if(activity is BaseActivity<*, *>){
                val immersionBar = configImmersive(defaultImmersionBar)
                immersionBar?.init()
            }
        }
    }

    /**
     * 配置沉浸式
     * Fragment进行默认沉浸式处理
     * @param immersionBar 默认的沉浸式处理，你可以自行创建，也可以在此基础上创建
     */
    open fun configImmersive(immersionBar: ImmersionBar): ImmersionBar?{
        return immersionBar
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 给Fragment的根View设置埋点
        view.trackModel = ITrackModel {
            fillTrackParams(it)
        }

        configImmersiveInternal()

        initViewAndEvent(savedInstanceState)
        createDefObserver()
        createObserver()
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
     * @param uiState MultiStateContainer的视图对象，如果不传，默认就把整个Fragment界面根视图变成加载界面
     */
    open fun showUiLoading(
        message: String = getString(R.string.base_ui_description_status_view_loading),
        uiState: MultiStateContainer = uiRootState
    ){
        uiState.show<LoadingState> {
            it.setLoadingMsg(message)
        }
    }

    /**
     * 展示错误界面
     * 提供一个默认实现，根据UiModel展示具体错误和重试逻辑
     * @param throwable 错误信息
     * @param uiState MultiStateContainer的视图对象，如果不传，默认就把整个Fragment界面根视图变成失败界面
     */
    open fun showUiError(throwable: Throwable, uiState: MultiStateContainer = uiRootState) {
        WaitDialog.dismiss()
        uiState.show<ErrorState> {
            it.setErrorMsg(throwable.message)
            it.retry { retryAll() }
        }
    }

    /**
     * 展示成功页面
     * 提供一个默认实现，及展示正确的视图，隐藏掉所有负面内容
     * @param uiState MultiStateContainer的视图对象，如果不传，默认就把整个Fragment界面根视图变成成功界面
     */
    open fun showUiSuccess(uiState: MultiStateContainer = uiRootState) {
        WaitDialog.dismiss()
        uiState.show(SuccessState())
    }

    /**
     * 重试刷新全部内容的方法
     * 子类可以重写该方法
     */
    open fun retryAll() {

    }

    /**
     * 创建默认的监听器内容
     * 主要用于界面状态的监听
     */
    open fun createDefObserver() {
        // 网络状态监听
        NetworkManager.networkState.launchAndCollectIn(viewLifecycleOwner, minActiveState = Lifecycle.State.CREATED){
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

    override fun onResume() {
        super.onResume()
        isUserHintVisible = true
        configImmersiveInternal()
    }

    override fun onPause() {
        super.onPause()
        isUserHintVisible = false
        configImmersiveInternal()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        configImmersiveInternal()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        configImmersiveInternal()
    }

    protected open fun fillTrackParams(trackParams: TrackParams) {
        // 默认不实现任何埋点数据填充
    }

}