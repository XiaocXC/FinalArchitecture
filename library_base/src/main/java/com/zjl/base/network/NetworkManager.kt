package com.zjl.base.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.telephony.TelephonyManager
import androidx.annotation.IntDef
import com.zjl.base.globalCoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

/**
 * @author Xiaoc
 * @since 2021-11-24
 *
 * 网络状态监测工具集
 */
object NetworkManager {

    @IntDef(
        NO_NETWORK,
        NETWORK_METERED,
        NETWORK_NOT_METERED
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class NetworkStatus

    /**
     * 无网络
     */
    const val NO_NETWORK = 0

    /**
     * 网络不计费（例如Wifi等内容）
     */
    const val NETWORK_METERED = 1

    /**
     * 网络计费（例如移动流量）
     */
    const val NETWORK_NOT_METERED = 2

    private val _networkState = MutableSharedFlow<Int>()
    val networkState: SharedFlow<Int> = _networkState

    private var networkStateCallback: NetworkStateCallback? = null

    /**
     * 注册网络状态监听事件
     */
    fun registerNetworkStateChanged(context: Context){
        val connectMgr = context.applicationContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val callback = NetworkStateCallback(context)
        networkStateCallback = callback
        connectMgr.registerDefaultNetworkCallback(callback)
    }

    /**
     * 取消注册网络状态监听事件
     */
    fun unregisterNetworkStateChanged(context: Context){
        val connectMgr = context.applicationContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkStateCallback?.let {
            connectMgr.unregisterNetworkCallback(it)
        }
    }

    /**
     * 判断当前网络类型
     */
    @NetworkStatus
    @JvmStatic
    fun getNetworkType(context: Context): Int {

        // 改为applicationContext，防止发生内存泄漏
        val connectMgr = context.applicationContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectMgr.activeNetwork ?: return NO_NETWORK
            val capabilities = connectMgr.getNetworkCapabilities(network) ?: return NO_NETWORK

            return if(capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)){
                NETWORK_NOT_METERED
            } else {
                NETWORK_METERED
            }
        } else {
            val networkInfo = connectMgr.activeNetworkInfo
                ?: // 没有任何网络
                return NO_NETWORK
            if (!networkInfo.isConnected) {
                // 网络断开或关闭
                return NO_NETWORK
            }
            when (networkInfo.type) {
                ConnectivityManager.TYPE_WIFI, ConnectivityManager.TYPE_ETHERNET -> {
                    // wifi网络或以太网
                    return NETWORK_NOT_METERED
                }
                ConnectivityManager.TYPE_MOBILE -> {
                    // 移动数据连接，视为可计费
                    return NETWORK_METERED
                }
            }
            // 未知网络，视为不计费
            return NETWORK_METERED
        }

    }

    private fun parseNetwork(connectMgr: ConnectivityManager, network: Network): Int{
        val capabilities = connectMgr.getNetworkCapabilities(network) ?: return NO_NETWORK

        return if(capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)){
            NETWORK_NOT_METERED
        } else {
            NETWORK_METERED
        }
    }

    /**
     * 判断是否成功连接网络
     * @return 是否有网络，有网络返回true，否则返回false
     */
    @JvmStatic
    fun isConnectNetwork(context: Context): Boolean{
        val status = getNetworkType(context)
        return status != NO_NETWORK
    }

    /**
     * 判断是否成功连接Wifi网络
     * @return 是否是Wifi网络，是返回true，否则返回false
     */
    @JvmStatic
    fun isConnectWifi(context: Context): Boolean{
        val status = getNetworkType(context)
        return  status == NETWORK_NOT_METERED
    }

    /**
     * 判断是否成功连接移动网络
     * @return 是否是移动网络，是返回true，否则返回false
     */
    @JvmStatic
    fun isConnectMobileNetwork(context: Context): Boolean{
        val status = getNetworkType(context)
        return  status == NETWORK_METERED
    }

    /**
     * 更新网络状态全局值
     */
    internal fun updateNetworkState(context: Context){
        val networkType = getNetworkType(context)
        globalCoroutineScope.launch {
            _networkState.emit(networkType)
        }
    }

    /**
     * 更新网络状态全局值
     */
    internal fun updateNetworkState(context: Context, network: Network?){
        val connectMgr = context.applicationContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkType = if(network == null){
            NO_NETWORK
        } else {
            parseNetwork(connectMgr, network)
        }
        globalCoroutineScope.launch {
            _networkState.emit(networkType)
        }
    }
}