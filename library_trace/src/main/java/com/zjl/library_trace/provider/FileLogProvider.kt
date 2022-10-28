package com.zjl.library_trace.provider

import android.content.Context
import android.util.Log
import com.zjl.library_trace.base.ITrackProvider
import com.zjl.library_trace.base.TrackParams
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File

/**
 * @author Xiaoc
 * @since 2022-10-11
 *
 * 埋点库自带一个存储在本地缓存中的埋点数据记录
 */
class FileLogProvider(
    private val context: Context,
    private val scope: CoroutineScope
): ITrackProvider {
    override var enabled: Boolean = true
    override var tag: String = "FileLogProvider"

    private val fileName = "trace-log.txt"

    /**
     * 协程锁
     * 在协程中使用 synchronized 会引起线程阻塞
     * 推荐使用 Mutex 锁来操作
     */
    private val mutex = Mutex()

    override fun onInit() {
        Log.d(tag, "Init FileLog provider.")
    }

    override fun onEvent(eventName: String, params: TrackParams) {

        scope.launch(Dispatchers.IO) {
            mutex.withLock {
                val cacheDir = context.externalCacheDir ?: return@launch
                // 日志txt文件对象
                val cacheFile = File(cacheDir, fileName)

                // 追加写入文本到末尾
                cacheFile.appendText(params.toString() + "\n")
            }
        }
    }
}