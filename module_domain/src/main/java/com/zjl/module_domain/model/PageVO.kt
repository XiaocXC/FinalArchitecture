package com.zjl.module_domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author Xiaoc
 * @since 2022-01-08
 */
@Serializable
data class PageVO<out T>(
    /**
     * 当前页码
     */
    @SerialName("curPage")
    val currentPage: Long,
    /**
     * 数据列表
     */
    @SerialName("datas")
    val dataList: List<T>,
    val offset: Int,
    /**
     * 是否到头
     */
    val over: Boolean,
    /**
     * 页码总数
     */
    val pageCount: Long,
    val size: Long,
    val total: Long
)
