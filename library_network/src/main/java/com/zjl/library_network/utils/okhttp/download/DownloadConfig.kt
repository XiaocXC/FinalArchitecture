package com.zjl.library_network.utils.okhttp.download

import com.zjl.library_network.utils.okhttp.ProgressListener
import com.zjl.library_network.utils.okhttp.ext.downloadListeners
import com.zjl.library_network.utils.okhttp.tag.NetworkTag
import com.zjl.library_network.utils.okhttp.ext.tagOf
import okhttp3.Request
import java.io.File

/**
 * @author Xiaoc
 * @since  2022-11-17
 **/
class DownloadConfig(
    private val requestBuilder: Request.Builder
) {

    /**
     * 下载文件名
     * 如果[setDownloadDir]函数使用完整路径(包含文件名的参数)作为参数则将无视本函数设置
     * 如果不调用本函数则默认是读取服务器返回的文件名
     * @see setDownloadFileNameDecode
     * @see setDownloadFileNameConflict
     * @see setDownloadDir
     */
    fun setDownloadFileName(name: String) {
        requestBuilder.tagOf(NetworkTag.DownloadFileName(name))
    }

    /**
     * 下载保存的目录, 也支持包含文件名称的完整路径, 如果使用完整路径则无视setDownloadFileName设置
     */
    fun setDownloadDir(name: String) {
        requestBuilder.tagOf(NetworkTag.DownloadFileDir(name))
    }

    /**
     * 下载保存的目录, 也支持包含文件名称的完整路径, 如果使用完整路径则无视setDownloadFileName设置
     */
    fun setDownloadDir(name: File) {
        requestBuilder.tagOf(NetworkTag.DownloadFileDir(name))
    }

    /**
     * 如果服务器返回 "Content-MD5"响应头和制定路径已经存在的文件MD5相同是否直接返回File
     */
    fun setDownloadMd5Verify(enabled: Boolean = true) {
        requestBuilder.tagOf(NetworkTag.DownloadFileMD5Verify(enabled))
    }

    /**
     * 假设下载文件路径已存在同名文件是否重命名, 例如`file_name(1).apk`
     */
    fun setDownloadFileNameConflict(enabled: Boolean = true) {
        requestBuilder.tagOf(NetworkTag.DownloadFileConflictRename(enabled))
    }

    /**
     * 文件名称是否使用URL解码
     * 例如下载的文件名如果是中文, 服务器传输给你的会是被URL编码的字符串. 你使用URL解码后才是可读的中文名称
     */
    fun setDownloadFileNameDecode(enabled: Boolean = true) {
        requestBuilder.tagOf(NetworkTag.DownloadFileNameDecode(enabled))
    }

    /**
     * 下载是否使用临时文件
     * 避免下载失败后覆盖同名文件或者无法判别是否已下载完整, 仅在下载完整以后才会显示为原有文件名
     * 临时文件命名规则: 文件名 + .net-download
     *      下载文件名: install.apk, 临时文件名: install.apk.net-download
     */
    fun setDownloadTempFile(enabled: Boolean = true) {
        requestBuilder.tagOf(NetworkTag.DownloadTempFile(enabled))
    }

    /**
     * 下载监听器
     * [setDownloadMd5Verify] 启用MD5文件校验且匹配本地文件MD5值成功会直接返回本地文件对象, 不会触发下载监听器
     */
    fun addDownloadListener(progressListener: ProgressListener) {
        requestBuilder.downloadListeners().add(progressListener)
    }
}