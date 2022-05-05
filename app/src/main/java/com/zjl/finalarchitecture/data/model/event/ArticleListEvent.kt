package com.zjl.finalarchitecture.data.model.event

/**
 * @author Xiaoc
 * @since 2022-05-05
 */
sealed class ArticleListEvent{

    data class ArticleCollectEvent(
        val id: Int,
        val isCollect: Boolean
    ): ArticleListEvent()
}
