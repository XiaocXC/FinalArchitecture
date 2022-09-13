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
@Deprecated("现在请使用NetworkStateCallback代替广播事件监听")
class NetworkStateReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
//        NetworkManager.updateNetworkState(context)
    }
}