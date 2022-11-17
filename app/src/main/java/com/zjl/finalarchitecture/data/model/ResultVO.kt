package com.zjl.finalarchitecture.data.model

import kotlinx.serialization.Serializable

/**
 * @author Xiaoc
 * @since 2021/5/10
 *
 * 从服务端返回信息包装总类
 */
@Serializable
data class ResultVO<out T>(
    val data: T,
    val errorCode: Int,
    val errorMsg: String
)