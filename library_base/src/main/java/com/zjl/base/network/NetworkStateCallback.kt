package com.zjl.base.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import timber.log.Timber

/**
 * @author Xiaoc
 * @since  2022-07-17
 *
 * 监听网络变化情况，基于[ConnectivityManager.NetworkCallback]
 * 使用广播监听方法已废弃
 **/
class NetworkStateCallback(
    private val context: Context
): ConnectivityManager.NetworkCallback() {

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        Timber.i("Network：网络状态发生变化（新网络）：%s", network.toString())
        // 当某种网络被系统视为默认主要的网络时调用此回调
        NetworkManager.updateNetworkState(context, network)
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        Timber.i("Network：网络状态发生变化（失去网络）：%s", network.toString())
        // 当某种网络失去成为默认网络的资格时调用此回调，并不代表网络会被断开，因为一个设备可能支持多种网络连接同时开启
    }

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities)
    }

    override fun onUnavailable() {
        super.onUnavailable()
        // 没用适用的网络
        NetworkManager.updateNetworkState(context, null)
    }

}