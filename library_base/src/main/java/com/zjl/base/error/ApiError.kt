package com.zjl.base.error

import com.zjl.lib_base.R

/**
 * @author Xiaoc
 * @since 2021/5/10
 *
 * 客户端本地定义的网络请求相关错误
 */
object ApiError {

    private const val dataIsNullCode = 0x000F
    private const val unknownCode = 0x0010
    private const val dataTypeCode = 0x0011
    private const val httpStatusCode = 0x0012
    private const val timeoutCode = 0x0016
    private const val connectionErrorCode = 0x0017

    val dataIsNull = Error(errorMsgId = R.string.base_network_description_data_is_null_error,errorCode = dataIsNullCode)
    val unknownError = Error(errorMsgId = R.string.base_network_description_unknown_error,errorCode = unknownCode)
    val connectionError = Error(errorMsgId = R.string.base_network_description_connection_error,errorCode = connectionErrorCode)
    val timeoutError = Error(errorMsgId = R.string.base_network_description_timeout_error,errorCode = timeoutCode)
    val dataTypeError = Error(errorMsgId = R.string.base_network_description_timeout_error,errorCode = dataTypeCode)
    val httpStatusError = Error(errorMsgId = R.string.base_network_description_http_status_error,errorCode = httpStatusCode)
}
