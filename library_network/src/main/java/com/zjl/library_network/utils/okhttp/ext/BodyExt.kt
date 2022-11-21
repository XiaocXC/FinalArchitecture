package com.zjl.library_network.utils.okhttp.ext

import com.zjl.library_network.utils.okhttp.ProgressListener
import com.zjl.library_network.utils.okhttp.body.ExtRequestBody
import com.zjl.library_network.utils.okhttp.body.ExtResponseBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.util.concurrent.ConcurrentLinkedQueue

fun RequestBody.toExtRequestBody(listeners: ConcurrentLinkedQueue<ProgressListener>? = null) = run {
    ExtRequestBody(this, listeners)
}

fun ResponseBody.toExtResponseBody(
    listeners: ConcurrentLinkedQueue<ProgressListener>? = null,
    complete: (() -> Unit)? = null
) = run { ExtResponseBody(this, listeners, complete) }