package com.zjl.base.utils

import kotlinx.coroutines.flow.SharingStarted

/**
 * @author Xiaoc
 * @since 2021/6/14
 */
private const val StopTimeoutMillis: Long = 5000

/**
 * 对于[StateFlow]，如果视图停止观察后，又返回视图重新开始观察，这段时间可能会很短，
 * 但是默认 [SharingStarted.WhileSubscribed] 会立刻停止收集流数据
 * 当在短时间内回到视图时，会频繁进行流的重新收集，浪费过多资源
 * 所以将停止等待时间延长
 */
val WhileViewSubscribed: SharingStarted = SharingStarted.WhileSubscribed(StopTimeoutMillis)