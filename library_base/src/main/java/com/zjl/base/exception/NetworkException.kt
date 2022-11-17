package com.zjl.base.exception

import okhttp3.Request
import java.io.IOException

/**
 * @author Xiaoc
 * @since 2021/5/10
 *
 * 网络请求异常类
 * 由于在Retrofit拦截器中要抛出此异常，必须让它为IOException的子类，否则Retrofit无法捕捉错误
 */
open class NetworkException constructor(
    open val request: Request,
    message: String? = null,
    cause: Throwable? = null,
): IOException(message, cause)