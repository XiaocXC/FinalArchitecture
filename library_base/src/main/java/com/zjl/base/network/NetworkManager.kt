package com.zjl.base.network

import android.content.Context
import android.net.ConnectivityManager
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
        NETWORK_CLOSED,
        NETWORK_ETHERNET,
        NETWORK_WIFI,
        NETWORK_MOBILE,
        NETWORK_UNKNOWN
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class NetworkStatus

    /**
     * 无网络
     */
    const val NO_NETWORK = 0

    /**
     * 网络断开或关闭
     */
    const val NETWORK_CLOSED = 1

    /**
     * 以太网网络
     */
    const val NETWORK_ETHERNET = 2

    /**
     * WIFI网络
     */
    const val NETWORK_WIFI = 3

    /**
     * 移动网络
     */
    const val NETWORK_MOBILE = 4

    /**
     * 未知的网络状态
     */
    const val NETWORK_UNKNOWN = -1

    private val _networkState = MutableSharedFlow<Int>()
    val networkState: SharedFlow<Int> = _networkState

    /**
     * 判断当前网络类型
     */
    @NetworkStatus
    @JvmStatic
    fun getNetworkType(context: Context): Int {

        // 改为applicationContext，防止发生内存泄漏
        val connectMgr = context.applicationContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return NO_NETWORK
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectMgr.activeNetwork ?: return NO_NETWORK
            val capabilities = connectMgr.getNetworkCapabilities(network) ?: return NO_NETWORK

            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return NETWORK_MOBILE
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return NETWORK_WIFI
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return NETWORK_ETHERNET
                }
                !capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) -> {
                    return NETWORK_CLOSED
                }
            }
            return NETWORK_UNKNOWN
        } else {
            val networkInfo = connectMgr.activeNetworkInfo
                ?: // 没有任何网络
                return NO_NETWORK
            if (!networkInfo.isConnected) {
                // 网络断开或关闭
                return NETWORK_CLOSED
            }
            when (networkInfo.type) {
                ConnectivityManager.TYPE_ETHERNET -> {
                    // 以太网网络
                    return NETWORK_ETHERNET
                }
                ConnectivityManager.TYPE_WIFI -> {
                    // wifi网络，当激活时，默认情况下，所有的数据流量将使用此连接
                    return NETWORK_WIFI
                }
                ConnectivityManager.TYPE_MOBILE -> {
                    // 移动数据连接,不能与连接共存,如果wifi打开，则自动关闭
                    when (networkInfo.subtype) {
                        TelephonyManager.NETWORK_TYPE_GPRS,
                        TelephonyManager.NETWORK_TYPE_EDGE,
                        TelephonyManager.NETWORK_TYPE_CDMA,
                        TelephonyManager.NETWORK_TYPE_1xRTT,
                        TelephonyManager.NETWORK_TYPE_IDEN,
                        TelephonyManager.NETWORK_TYPE_UMTS,
                        TelephonyManager.NETWORK_TYPE_EVDO_0,
                        TelephonyManager.NETWORK_TYPE_EVDO_A,
                        TelephonyManager.NETWORK_TYPE_HSDPA,
                        TelephonyManager.NETWORK_TYPE_HSUPA,
                        TelephonyManager.NETWORK_TYPE_HSPA,
                        TelephonyManager.NETWORK_TYPE_EVDO_B,
                        TelephonyManager.NETWORK_TYPE_EHRPD,
                        TelephonyManager.NETWORK_TYPE_HSPAP,
                        TelephonyManager.NETWORK_TYPE_LTE,
                        TelephonyManager.NETWORK_TYPE_NR ->
                            return NETWORK_MOBILE
                    }
                }
            }
            // 未知网络
            return NETWORK_UNKNOWN
        }

    }

    /**
     * 判断是否成功连接网络
     * @return 是否有网络，有网络返回true，否则返回false
     */
    @JvmStatic
    fun isConnectNetwork(context: Context): Boolean{
        val status = getNetworkType(context)
        return !(status == NO_NETWORK || status == NETWORK_CLOSED)
    }

    /**
     * 判断是否成功连接Wifi网络
     * @return 是否是Wifi网络，是返回true，否则返回false
     */
    @JvmStatic
    fun isConnectWifi(context: Context): Boolean{
        val status = getNetworkType(context)
        return  status == NETWORK_WIFI
    }

    /**
     * 判断是否成功连接移动网络
     * @return 是否是移动网络，是返回true，否则返回false
     */
    @JvmStatic
    fun isConnectMobileNetwork(context: Context): Boolean{
        val status = getNetworkType(context)
        return  status == NETWORK_MOBILE
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
}