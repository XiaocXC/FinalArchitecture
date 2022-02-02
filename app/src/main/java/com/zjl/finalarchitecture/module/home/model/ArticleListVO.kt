package com.zjl.finalarchitecture.module.home.model

import kotlinx.serialization.Serializable

/**
 * @author Xiaoc
 * @since 2022-02-02
 *
 * 文章列表项的数据VO
 */
@Serializable
data class ArticleListVO(
    /**
     * 文章ID
     */
    val id: String,

    /**
     * 文章标题
     */
    val title: String?,

    /**
     * 文章作者
     */
    val author: String? = "",
)