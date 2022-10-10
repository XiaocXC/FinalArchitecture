package com.zjl.base.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.gyf.immersionbar.ImmersionBar
import com.kongzue.dialogx.dialogs.WaitDialog
import com.zjl.base.activity.BaseActivity
import com.zjl.base.globalContext
import com.zjl.base.network.NetworkManager
import com.zjl.base.ui.onFailure
import com.zjl.base.ui.onLoading
import com.zjl.base.ui.onSuccess
import com.zjl.base.ui.state.ErrorState
import com.zjl.base.ui.state.LoadingState
import com.zjl.base.utils.ext.getVmClazz
import com.zjl.base.utils.ext.inflateBindingWithGeneric
import com.zjl.base.utils.ext.isNightMode
import com.zjl.base.utils.launchAndCollectIn
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.lib_base.R
import com.zjl.library_trace.base.ITrackModel
import com.zjl.library_trace.base.TrackParams
import com.zjl.library_trace.ext.trackModel
import com.zy.multistatepage.MultiStateContainer
import com.zy.multistatepage.bindMultiState
import com.zy.multistatepage.state.SuccessState
import timber.log.Timber

/**
 * @author Xiaoc
 * @since 2022-01-07
 *
 * 基类Fragment，提供基本的封装内容进行统一
 * 规定一个Fragment要基于ViewBinding
 * 根据MVI或MVVM规定，一个界面由一个ViewModel所绑定，我们这里需要一个VM的绑定
 * 如果该界面需要用到多个VM，则请将负责界面状态的作为主要的VM，或者自行实现状态管理
 */
abstract class BaseFragment<V : ViewBinding, VM : BaseViewModel> : Fragment() {

    protected var isUserHintVisible: Boolean = false

    private var _mBinding: V? = null
    protected val mBinding get() = _mBinding!!

    protected lateinit var mViewModel: VM

    /**
     * 整个Fragment的状态控制器
     */
    private var _uiRootState: MultiStateContainer? = null
    protected val uiRootState get() = _uiRootState!!


    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = bindView(inflater, container, savedInstanceState)
        _uiRootState = mBinding.root.bindMultiState()
        return uiRootState
    }

    private fun configImmersiveInternal(){
        if(isUserHintVisible){
            val activity = requireActivity()
            if(activity is BaseActivity<*,*>){
                // 设置沉浸式状态栏，此操作会去掉透明遮罩等内容
                val defaultImmersionBar = ImmersionBar.with(this)
                    .transparentBar()
                    .statusBarDarkFont(!resources.isNightMode())
                    .navigationBarDarkIcon(!resources.isNightMode())
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

    /**
     * 创建ViewBinding视图
     * 默认实现通过反射实现
     * 你也可以重写该方法进行自行创建
     */
    open fun bindView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): V {
        return inflateBindingWithGeneric(inflater, container, false)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 给Fragment的根View设置埋点
        view.trackModel = ITrackModel {
            fillTrackParams(it)
        }

        configImmersiveInternal()

        mViewModel = createViewModel()

        initViewAndEvent(savedInstanceState)
        createDefObserver()
        createObserver()
    }

    /**
     * 展示加载界面
     * 提供一个默认实现，展示弹窗加载或者是界面加载
     * @param cancelable 是否支持取消，默认为true可取消
     */
    open fun showUiLoading(cancelable: Boolean = true) {
        // 如果可取消，显示的是替换整个界面的加载内容
        // 如果不可取消，则显示的是加载弹出，禁止关闭
        if (cancelable) {
            uiRootState.show(LoadingState())
        } else {
            WaitDialog.show(getString(R.string.base_ui_description_status_view_loading)).apply {
                isCancelable = false
            }
        }
    }

    /**
     * 展示错误界面
     * 提供一个默认实现，根据UiModel展示具体错误和重试逻辑
     * @param uiModel 错误状态信息
     */
    open fun showUiError(throwable: Throwable) {
        WaitDialog.dismiss()
        uiRootState.show<ErrorState> {
            it.setErrorMsg(throwable.message)
            it.retry { retryAll() }
        }
    }

    /**
     * 展示成功页面
     * 提供一个默认实现，及展示正确的视图，隐藏掉所有负面内容
     */
    open fun showUiSuccess() {
        WaitDialog.dismiss()
        uiRootState.show(SuccessState())
    }

    /**
     * 重试刷新全部内容的方法，默认直接调用viewModel的初始化方法
     */
    open fun retryAll() {
        mViewModel.initData()
    }

    /**
     * 创建默认的监听器内容
     * 主要用于界面状态的监听
     */
    open fun createDefObserver() {
        // 默认监听根视图状态
        mViewModel.rootViewState.launchAndCollectIn(viewLifecycleOwner){ uiModel ->
            uiModel.onSuccess {
                showUiSuccess()
            }.onLoading {
                showUiLoading()
            }.onFailure { _, throwable ->
                showUiError(throwable)
            }
        }

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
     * 利用反射创建ViewModel实例
     * 该ViewModel默认实现为当前Fragment作用域，如果你需要自定义作用域，请重写该方法
     */
    open fun createViewModel(): VM {
        return ViewModelProvider(this)[getVmClazz(this)]
    }

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

    /**
     * 当Fragment视图销毁时使用
     * 如果Fragment需要手动释放部分视图内容时，我们使用请确保在调用 super.onDestroyView() 之前清除视图
     */
    override fun onDestroyView() {
        super.onDestroyView()
        // 保持ViewBinding在 onCreateView 和 onDestroyView 生命周期之间
        // 这里不自动使用autoCleared原因是因为有个先后顺序，因为部分adapter的原因，需要重写onDestroyView来手动设置为Null
        // 而此时如果获取mBinding可能由于清除了mBinding导致报错，所以我们会手动处理
        _uiRootState = null
        _mBinding = null
    }

    protected open fun fillTrackParams(trackParams: TrackParams) {
        // 默认不实现任何埋点数据填充
    }

}