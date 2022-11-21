package com.zjl.library_network.utils.okhttp.ext

import com.zjl.library_network.utils.okhttp.OkHttpUtils
import com.zjl.library_network.utils.okhttp.ProgressListener
import com.zjl.library_network.utils.okhttp.tag.NetworkTag
import okhttp3.Request
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * 返回OkHttp的tag(通过Class区分的tag)
 */
inline fun <reified T> Request.Builder.tagOf(): T? {
    return tags()[T::class.java] as? T
}

/**
 * 设置OkHttp的tag(通过Class区分的tag)
 */
inline fun <reified T> Request.Builder.tagOf(value: T) = apply {
    tag(T::class.java, value)
}

/**
 * 标签集合
 */
fun Request.Builder.tags(): MutableMap<Class<*>, Any?> {
    return OkHttpUtils.tags(this)
}

/**
 * 全部的下载监听器
 */
fun Request.Builder.downloadListeners(): ConcurrentLinkedQueue<ProgressListener> {
    return tagOf<NetworkTag.DownloadListeners>() ?: run {
        val tag = NetworkTag.DownloadListeners()
        tagOf(tag)
        tag
    }
}