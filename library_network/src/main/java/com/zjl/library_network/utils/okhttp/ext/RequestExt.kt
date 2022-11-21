package com.zjl.library_network.utils.okhttp.ext

import com.zjl.base.globalContext
import com.zjl.library_network.utils.okhttp.OkHttpUtils
import com.zjl.library_network.utils.okhttp.ProgressListener
import com.zjl.library_network.utils.okhttp.tag.NetworkTag
import okhttp3.Request
import okhttp3.Response
import java.net.URLDecoder
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * 下载文件目录
 */
fun Request.downloadFileDir(): String {
    return tagOf<NetworkTag.DownloadFileDir>()?.value ?: globalContext.filesDir.absolutePath
}

/**
 * 返回OkHttp的tag(通过Class区分的tag)
 */
inline fun <reified T> Request.tagOf(): T? {
    return tag(T::class.java)
}

/**
 * 设置OkHttp的tag(通过Class区分的tag)
 */
inline fun <reified T> Request.tagOf(value: T) = apply {
    tags()[T::class.java] = value
}

/**
 * 标签集合
 */
fun Request.tags(): MutableMap<Class<*>, Any?> {
    return OkHttpUtils.tags(this)
}

/**
 * 下载文件名
 */
fun Request.downloadFileName(): String? {
    return tagOf<NetworkTag.DownloadFileName>()?.value
}

/**
 * 下载的文件名称是否解码
 * 例如下载的文件名如果是中文, 服务器传输给你的会是被URL编码的字符串. 你使用URL解码后才是可读的中文名称
 */
fun Request.downloadFileNameDecode(): Boolean {
    return tagOf<NetworkTag.DownloadFileNameDecode>()?.value == true
}

/**
 * 是否进行校验文件md5, 如果校验则匹配上既马上返回文件而不会进行下载
 */
fun Request.downloadMd5Verify(): Boolean {
    return tagOf<NetworkTag.DownloadFileMD5Verify>()?.value == true
}

/**
 * 当指定下载目录存在同名文件是覆盖还是进行重命名, 重命名规则是: $文件名_($序号).$后缀
 */
fun Request.downloadConflictRename(): Boolean {
    return tagOf<NetworkTag.DownloadFileConflictRename>()?.value == true
}

/**
 * 下载是否使用临时文件
 * 避免下载失败后覆盖同名文件或者无法判别是否已下载完整, 仅在下载完整以后才会显示为原有文件名
 * 临时文件命名规则: 文件名 + .net-download
 *      下载文件名: install.apk, 临时文件名: install.apk.net-download
 */
fun Request.downloadTempFile(): Boolean {
    return tagOf<NetworkTag.DownloadTempFile>()?.value == true
}

/**
 * 全部的上传监听器
 */
fun Request.uploadListeners(): ConcurrentLinkedQueue<ProgressListener> {
    return tagOf<NetworkTag.UploadListeners>() ?: kotlin.run {
        val tag = NetworkTag.UploadListeners()
        tagOf(tag)
        tag
    }
}

/**
 * 全部的下载监听器
 */
fun Request.downloadListeners(): ConcurrentLinkedQueue<ProgressListener> {
    return tagOf<NetworkTag.DownloadListeners>() ?: kotlin.run {
        val tag = NetworkTag.DownloadListeners()
        tagOf(tag)
        tag
    }
}

/**
 * 按照以下顺序返回最终的下载文件的名称
 *
 * 1. 指定文件名
 * 2. 响应头文件名
 * 3. 请求URL路径
 * 4. 时间戳
 */
fun Response.fileName(): String {
    request.downloadFileName().takeUnless { it.isNullOrBlank() }?.let { return it }
    val disposition = header("Content-Disposition")
    if (disposition != null) {
        disposition.substringAfter("filename=", "").takeUnless { it.isBlank() }?.let { return it }
        disposition.substringAfter("filename*=", "").trimStart(*"UTF-8''".toCharArray())
            .takeUnless { it.isBlank() }?.let { return it }
    }

    var fileName: String = request.url.pathSegments.last().substringBefore("?")
    fileName = if (fileName.isBlank()) "unknown_${System.currentTimeMillis()}" else {
        if (request.downloadFileNameDecode()) {
            try {
                URLDecoder.decode(fileName, "UTF8")
            } catch (e: Exception) {
                fileName
            }
        } else fileName
    }
    return fileName
}