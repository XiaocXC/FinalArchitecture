package com.zjl.base.activity

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.gyf.immersionbar.ImmersionBar
import com.kongzue.dialogx.dialogs.WaitDialog
import com.zjl.base.globalContext
import com.zjl.base.network.NetworkManager
import com.zjl.base.network.NetworkStateReceiver
import com.zjl.base.ui.onFailure
import com.zjl.base.ui.onLoading
import com.zjl.base.ui.onSuccess
import com.zjl.base.ui.state.ErrorState
import com.zjl.base.ui.state.LoadingState
import com.zjl.base.utils.ext.getVmClazz
import com.zjl.base.utils.ext.inflateBindingWithGeneric
import com.zjl.base.utils.launchAndRepeatWithViewLifecycle
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.lib_base.R
import com.zy.multistatepage.MultiStatePage.bindMultiState
import com.zy.multistatepage.state.SuccessState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * @author Xiaoc
 * @since 2022-01-07
 *
 * 基类Activity，提供基本的封装内容进行统一
 * 规定一个Activity要基于ViewBinding且有一个统一的ViewModel作为支撑
 * 这一点在BaseFragment中没有强制
 */
abstract class BaseActivity<V : ViewBinding, VM : BaseViewModel> : AppCompatActivity() {

    protected lateinit var mBinding: V

    protected lateinit var mViewModel: VM

    /**
     * 整个Activity的UiState状态控制器
     */
    protected val rootUiState by lazy {
        bindMultiState(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = bindView()
        setContentView(mBinding.root)

        // 设置沉浸式状态栏，此操作会去掉透明遮罩等内容
        // 类似于使用了 view.setSystemUiVisibility(LAYOUT_STABLE | LAYOUT_FULLSCREEN | LAYOUT_FULLSCREEN)
//        WindowCompat.setDecorFitsSystemWindows(window, false)

        mViewModel = bindViewModel()
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
            WaitDialog.show(getString(R.string.base_ui_description_status_view_empty)).apply {
                isCancelable = false
            }
        } else {
            rootUiState.show(LoadingState())
        }
    }

    /**
     * 展示错误界面
     * 提供一个默认实现，根据UiModel展示具体错误和重试逻辑
     * @param uiModel 错误状态信息
     */
    open fun showUiError(throwable: Throwable) {
        WaitDialog.dismiss()
        rootUiState.show<ErrorState> {
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
        rootUiState.show(SuccessState())
    }

    /**
     * 重试方法，默认不实现
     */
    open fun retryAll() {
        mViewModel.initData()
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
        launchAndRepeatWithViewLifecycle {
            launch {
                // 默认监听根视图状态
                mViewModel.rootViewState.collectLatest { uiModel ->
                    uiModel.onSuccess {
                        showUiSuccess()
                    }.onLoading {
                        showUiLoading()
                    }.onFailure { _, throwable ->
                        showUiError(throwable)
                    }
                }
            }

            launch {
                // 监听网络状态
                NetworkManager.networkState.collectLatest {
                    val hasNetwork = NetworkManager.isConnectNetwork(globalContext)
                    Timber.i("Network：网络状态发生变化，是否有网络：%s", hasNetwork)
                    networkStateChanged(hasNetwork)
                }
            }
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
        NetworkManager.unregisterNetworkStateChanged(this)
    }

    override fun onResume() {
        super.onResume()
        // 恢复观察网络状态
        NetworkManager.registerNetworkStateChanged(this)
    }


}