package com.zjl.base.network

import android.content.Context
import android.net.*
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.IntDef
import androidx.core.net.ConnectivityManagerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * @author Xiaoc
 * @since 2022-07-28
 */
object NetworkManager {
    @IntDef(
        NO_NETWORK,
        NETWORK_WIFI,
        NETWORK_MOBILE,
        NETWORK_OTHERS
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class NetworkStatus

    /**
     * 无网络
     */
    const val NO_NETWORK = 0

    /**
     * WIFI网络
     */
    const val NETWORK_WIFI = 1

    /**
     * 移动网络
     */
    const val NETWORK_MOBILE = 2

    /**
     * 其他的网络状态
     */
    const val NETWORK_OTHERS = -1

    private val availableNetworkStates = mutableSetOf<String>()

    /**
     * 网络状态LiveData
     */
    private val _networkState = MutableStateFlow(NO_NETWORK)
    val networkState: StateFlow<Int> = _networkState

    private val networkCallback = object: ConnectivityManager.NetworkCallback(){

        /**
         * 当默认网络丢失时回调
         */
        override fun onLost(network: Network) {
            super.onLost(network)
            Log.i("NetworkManager", "onLost ---$network")

            // 在 Android N 以后使用默认网络进行监听，策略不一样
            // Android N 以后我们直接使用回调回来的最新网络状态
            // 在 Android N 以前我们需要用一个列表存储起来判断当前默认网络是不是已经没有了，代表无网络状态
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                availableNetworkStates.clear()
                setNetworkState(NO_NETWORK)
            } else {
                availableNetworkStates.remove(network.toString())

                if(availableNetworkStates.isEmpty()){
                    setNetworkState(NO_NETWORK)
                }
            }

        }

        override fun onUnavailable() {
            super.onUnavailable()
            Log.i("NetworkManager", "onUnavailable")

            availableNetworkStates.clear()
        }

        /**
         * 当默认网络更改时回调
         */
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            Log.i("NetworkManager", "onAvailable ---$network")

            availableNetworkStates.add(network.toString())
        }

        /**
         * 当默认网络的信息更改时回调
         * 我们在此处更新最新网络状态数据
         */
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            Log.i("NetworkManager", "onCapabilitiesChanged $network --- $networkCapabilities")

            val networkStatus = getNetworkInner(networkCapabilities)
            setNetworkState(networkStatus)
        }
    }

    /**
     * 注册网络变化监听
     * 开启监听后，可以调用[networkState]进行观察变化
     *
     * 建议在Activity的 onResume 中注册
     * 然后在Activity的 onPause 中取消注册
     * 避免在后台监听网络状态
     */
    @JvmStatic
    fun registerNetworkCallback(context: Context){
        val connectivityManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // 手动获取一次当前网络状态
        val networkType = getNetworkState(context)
        setNetworkState(networkType)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            val networkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        }
    }


    /**
     * 取消注册网络变化监听
     */
    @JvmStatic
    fun unregisterNetworkCallback(context: Context){
        val connectivityManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    /**
     * 获取网络状态
     * @return type 网络类型状态
     */
    @NetworkStatus
    @JvmStatic
    fun getNetworkState(context: Context): Int{
        val connectivityManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return NO_NETWORK
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return NO_NETWORK
            return getNetworkInner(capabilities)
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            // 找到当前活跃的network
            val network = connectivityManager.allNetworks.find {
                connectivityManager.getNetworkInfo(it)?.type == activeNetworkInfo?.type
            } ?: return NO_NETWORK

            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return NO_NETWORK
            return getNetworkInner(capabilities)
        }
    }

    /**
     * 是否是弱网环境
     * @param context 上下文
     * @param minKbps 最小的Kbps值，超过该值则不是弱网（默认100）
     */
    @JvmStatic
    @JvmOverloads
    fun isWeakNetwork(context: Context, minKbps: Int = 100): Boolean{
        val connectivityManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return true
            connectivityManager.getNetworkCapabilities(network) ?: return true
        } else {
            // 找到当前活跃的network
            val network = connectivityManager.allNetworks.find {
                val networkInfo = connectivityManager.getNetworkInfo(it)
                networkInfo?.type == connectivityManager.activeNetworkInfo?.type
            } ?: return true
            connectivityManager.getNetworkCapabilities(network)
        }
        if(capabilities == null){
            return true
        }

        val bandwidthKbps = capabilities.linkDownstreamBandwidthKbps
        Log.i("NetworkManager", "下行带宽：${bandwidthKbps}")
        return minKbps >= bandwidthKbps
    }

    /**
     * 判断是否成功连接网络
     * @return 是否有网络，有网络返回true，否则返回false
     */
    @JvmStatic
    fun isConnectNetwork(context: Context): Boolean{
        val status = getNetworkState(context)
        return status != NO_NETWORK
    }

    /**
     * 判断是否成功连接Wifi网络
     * @return 是否是Wifi网络，是返回true，否则返回false
     */
    @JvmStatic
    fun isConnectWifi(context: Context): Boolean{
        val status = getNetworkState(context)
        return  status == NETWORK_WIFI
    }

    /**
     * 判断是否成功连接移动网络
     * @return 是否是移动网络，是返回true，否则返回false
     */
    @JvmStatic
    fun isConnectMobileNetwork(context: Context): Boolean{
        val status = getNetworkState(context)
        return  status == NETWORK_MOBILE
    }

    /**
     * 判断该网络是否计费
     * 计费网络一般为移动网络，部分WIFI内容也可能为计费网络
     * @return 是否为计费网络，true则为计费
     */
    @JvmStatic
    fun isConnectMetered(context: Context): Boolean{
        val connectivityManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return ConnectivityManagerCompat.isActiveNetworkMetered(connectivityManager)
    }

    private fun getNetworkInner(networkCapabilities: NetworkCapabilities): Int{

        // 是否有访问互联网功能
        if(networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)){

            return if(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                // WIFI
                NETWORK_WIFI
            } else if(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
                // Mobile（GPRS）
                NETWORK_MOBILE
            } else {
                // 其他可联网的网络类型，例如以太网
                NETWORK_OTHERS
            }

        } else {
            return NO_NETWORK
        }
    }

    private fun setNetworkState(@NetworkStatus state: Int){
        Log.i("NetworkManager", "NetworkStatus Changed --- $state -- ${getNetworkStateName(state)}")
        if(state == _networkState.value){
            return
        }
        _networkState.value = state

    }

    private fun getNetworkStateName(@NetworkStatus state: Int): String{
       return when(state){
           NO_NETWORK ->{
               "NO_NETWORK"
           }
           NETWORK_WIFI ->{
               "WIFI"
           }
           NETWORK_MOBILE ->{
                "GPRS"
           }
           NETWORK_OTHERS ->{
               "Others"
           }
           else ->{
               "Unknown"
           }
        }
    }
}