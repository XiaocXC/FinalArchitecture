package com.zjl.finalarchitecture.data.event

import com.zjl.finalarchitecture.data.model.event.ArticleListEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

/**
 * @author Xiaoc
 * @since 2022-05-05
 *
 * 一个事件处理的单例处理类
 */
object EventRepository {

    /**
     * 收藏事件
     */
    private val _collectEvent = MutableSharedFlow<ArticleListEvent>()
    val articleListEvent: SharedFlow<ArticleListEvent> get() = _collectEvent
}