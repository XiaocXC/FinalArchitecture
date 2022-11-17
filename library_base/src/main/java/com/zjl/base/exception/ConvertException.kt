package com.zjl.base.exception

import okhttp3.Response

/**
 * @author Xiaoc
 * @since 2022-11-17
 *
 * 转换数据异常
 * @param response 响应内容
 * @param message 错误描述信息
 * @param cause 错误原因
 * @param tag 可携带任意内容，用于携带其他信息
 */
class ConvertException(
    response: Response,
    message: String? = null,
    cause: Throwable? = null,
    var tag: Any? = null
) : HttpResponseException(response, message, cause)