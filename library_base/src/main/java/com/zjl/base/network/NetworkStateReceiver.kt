package com.zjl.base.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * @author Xiaoc
 * @since 2022-07-15
 *
 * 监听网络变化情况
 */
class NetworkStateReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        NetworkManager.updateNetworkState(context)
    }
}