package com.zjl.finalarchitecture.module.toolbox.websocket

import android.os.Bundle
import com.zjl.base.fragment.BaseFragment
import com.zjl.finalarchitecture.databinding.FragmentWebsocketBinding
import com.zjl.lib_base.BuildConfig
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 * @author Xiaoc
 * @since  2023-03-31
 *
 * WebSocket连接
 **/
class WebSocketFragment: BaseFragment<FragmentWebsocketBinding, WebSocketViewModel>() {

    private var webSocket: WebSocket? = null

    private val okHttpClient by lazy {
        OkHttpClient.Builder().apply {
            // 添加Log日志记录
            if(BuildConfig.DEBUG){
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
            pingInterval(30000, TimeUnit.MILLISECONDS)
        }.build()
    }

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        /**
         * wss://yjgb.yd-data.com/connection/connection
            userType, APP
            areaId, 17
            centerId, 17
            userId, 13551272294
         后台：https://yjgb.yd-data.com:5678/web-command-screen/index.html#/?centerId=17&areaId=17
         */
        mBinding.btnConnect.setOnClickListener {
            val request = Request.Builder()
                .url("wss://yjgb.yd-data.com/connection/connection?userType=APP&areaId=17&centerId=17&userId=13551272294")
                .build()
            webSocket = okHttpClient.newWebSocket(request, object: WebSocketListener(){
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    // 连接成功
                    super.onOpen(webSocket, response)
                    requireActivity().runOnUiThread {
                        mBinding.tvInfo.append("连接成功：$response \n")
                    }
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    // 收到后台的消息
                    super.onMessage(webSocket, text)
                    requireActivity().runOnUiThread {
                        mBinding.tvInfo.append("收到消息：$text \n")
                    }
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    // 连接关闭
                    super.onClosed(webSocket, code, reason)
                    requireActivity().runOnUiThread {
                        mBinding.tvInfo.append("连接关闭：$code -- $reason \n")
                    }
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    // 失败
                    super.onFailure(webSocket, t, response)
                    requireActivity().runOnUiThread {
                        mBinding.tvInfo.append("连接失败：$t\n")
                    }
                }
            })

            mBinding.btnDisconnect.setOnClickListener {
                webSocket?.close(1000, null)
            }
        }
    }

    override fun createObserver() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        webSocket?.close(1000, null)
    }
}