package com.zjl.base.exception

import okhttp3.Response

/**
 * >= 500 服务器异常
 * @param response 响应信息
 * @param message 错误描述信息
 * @param cause 错误原因
 * @param tag 可携带任意内容，用于携带其他信息
 */
class ServerResponseException(
    response: Response,
    message: String? = null,
    cause: Throwable? = null,
    var tag: Any? = null
) : HttpResponseException(response, message, cause)