package com.zjl.library_network

/**
 * @author Xiaoc
 * @since 2021/5/10
 *
 * 从服务端返回信息包装总类
 */
data class ResultVO<out T>(
    val data: T,
    val errorCode: Int,
    val errorMsg: String
)