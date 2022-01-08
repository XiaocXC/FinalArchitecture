package com.zjl.library_network.exception

import com.zjl.base.error.Error
import java.io.IOException

/**
 * @author Xiaoc
 * @since 2021/5/10
 *
 * 自定义异常类，支持从 [Error] 中进行调用
 * 由于在Retrofit拦截器中要抛出此异常，必须让它为IOException的子类，否则Retrofit无法捕捉错误
 */
class ApiException constructor(val error: Error): IOException(error.errorMsg)