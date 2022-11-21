package com.zjl.library_network.utils.okhttp.tag

import com.zjl.library_network.utils.okhttp.ProgressListener
import java.io.File
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * @author Xiaoc
 * @since  2022-11-17
 **/
sealed class NetworkTag {

    class UploadListeners : ConcurrentLinkedQueue<ProgressListener>()
    class DownloadListeners : ConcurrentLinkedQueue<ProgressListener>()

    @JvmInline
    value class DownloadFileMD5Verify(val value: Boolean = true)

    @JvmInline
    value class DownloadFileNameDecode(val value: Boolean = true)

    @JvmInline
    value class DownloadTempFile(val value: Boolean = true)

    @JvmInline
    value class DownloadFileConflictRename(val value: Boolean = true)

    @JvmInline
    value class DownloadFileName(val value: String)

    @JvmInline
    value class DownloadFileDir(val value: String) {
        constructor(fileDir: File) : this(fileDir.absolutePath)
    }
}