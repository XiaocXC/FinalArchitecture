package com.zjl.library_network.utils.okhttp.download

import com.zjl.base.exception.DownloadFileException
import com.zjl.base.exception.HttpFailureException
import com.zjl.base.utils.ext.md5
import com.zjl.library_network.utils.okhttp.*
import com.zjl.library_network.utils.okhttp.ext.*
import com.zjl.library_network.utils.okhttp.interceptor.ProgressHandlerInterceptor
import com.zjl.library_network.utils.okhttp.tag.NetworkTag
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.closeQuietly
import okio.buffer
import okio.sink
import java.io.File
import java.net.SocketException
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * @author Xiaoc
 * @since  2022-11-17
 *
 * 由于Retrofit的下载功能非常脆弱，所以我们扩展该方法进行扩展
 * 该方法主要使用OkHttp4的功能进行下载，更加方便可靠
 *
 * @param url 下载地址
 * @param okHttpClient 可选，你可以使用你自己的OkHttpClient对象
 * @param block 一些自定义下载配置，例如你可以添加参数、监听并指定下载名称等内容
 *
 * //                lifecycleScope.launch {
 * //                    val fileResult = downloadFile<String>("https://www.wanandroid.com/resources/image/pc/logo.png") {
 * //                        setDownloadFileName("logo2.png")
 * //                        setDownloadDir(requireContext().getExternalFilesDir("final")?.absolutePath + "/")
 * //                        addDownloadListener(object : ProgressListener() {
 * //                            override fun onProgress(p: Progress) {
 * //                                LogUtils.e("下载进度：${p.progress()}")
 * //                            }
 * //                        })
 * //                    }
 * //                    val await: File = fileResult.await()
 * //                    LogUtils.e("下载完成的文件：${await.absolutePath}")
 * //                }
 *
 *
 **/
inline fun <reified T> CoroutineScope.downloadFile(
    url: String,
    okHttpClient: OkHttpClient = OkHttpClient(),
    noinline block: (DownloadConfig.(Request.Builder) -> Unit)? = null
): Deferred<File> = async(Dispatchers.IO + SupervisorJob()) {
    val requestBuilder = Request.Builder().apply {
        get().url(url)
    }
    val config = DownloadConfig(requestBuilder)
    block?.invoke(config, requestBuilder)
    val request = requestBuilder.build()

    // 给OkHttpClient加入进度监听拦截器
    val client = okHttpClient.newBuilder().addInterceptor(ProgressHandlerInterceptor()).build()
    val newCall = client.newCall(request)
    // 我们这里使用可取消的协程进行OkHttp请求
    // 因为OkHttp并不像Retrofit那样支持协程，我们需要手动让其支持协程
    val file = suspendCancellableCoroutine<File> {
        val response = newCall.execute()
        if(response.isSuccessful){
            // 如果下载成功，我们将其转为文件
            val file = response.file()
            if(file == null){
                it.resumeWithException(DownloadFileException(response = response))
            } else {
                it.resume(file)
            }
        } else {
            it.resumeWithException(HttpFailureException(request, message = response.message))
        }

        // 当协程取消后，我们取消网络请求
        it.invokeOnCancellation {
            newCall.cancel()
        }
    }

    file
}

/**
 * 下载到指定文件
 */
@Throws(DownloadFileException::class)
fun Response.file(): File? {
    var dir = request.downloadFileDir() // 下载目录
    val fileName: String // 下载文件名
    val dirFile = File(dir)
    // 判断downloadDir是否为目录
    var file = if (dirFile.isDirectory) {
        fileName = fileName()
        File(dir, fileName)
    } else {
        val temp = dir
        dir = dir.substringBeforeLast(File.separatorChar)
        fileName = temp.substringAfterLast(File.separatorChar)
        dirFile
    }
    try {
        if (file.exists()) {
            // MD5校验匹配文件
            if (request.downloadMd5Verify()) {
                val md5Header = header("Content-MD5")
                if (md5Header != null && file.md5(true) == md5Header) {
                    val downloadListeners = request.tagOf<NetworkTag.DownloadListeners>()
                    if (!downloadListeners.isNullOrEmpty()) {
                        val fileSize = file.length()
                        val progress = Progress().apply {
                            currentByteCount = fileSize
                            totalByteCount = fileSize
                            intervalByteCount = fileSize
                            finish = true
                        }
                        downloadListeners.forEach {
                            it.onProgress(progress)
                        }
                    }
                    return file
                }
            }
            // 命名冲突添加序列数字的后缀
            if (request.downloadConflictRename() && file.name == fileName) {
                val fileExtension = file.extension
                val fileNameWithoutExtension = file.nameWithoutExtension
                fun rename(index: Long): File {
                    file = File(dir, fileNameWithoutExtension + "_($index)" + fileExtension)
                    return if (file.exists()) {
                        rename(index + 1)
                    } else file
                }
                file = rename(1)
            }
        }

        // 临时文件
        if (request.downloadTempFile()) {
            file = File(dir, file.name + ".net-download")
        }
        val source = body?.source() ?: return null
        if (!file.exists()) file.createNewFile()
        file.sink().buffer().use {
            it.writeAll(source)
            source.closeQuietly()
        }
        // 下载完毕删除临时文件
        if (request.downloadTempFile()) {
            val fileFinal = File(dir, fileName)
            file.renameTo(fileFinal)
            return fileFinal
        }
        return file
    } catch (e: SocketException) {
        // 取消请求需要删除下载临时文件
        if (request.downloadTempFile()) file.delete()
        throw CancellationException(e)
    } catch (e: Exception) {
        throw DownloadFileException(this, cause = e)
    }
}
