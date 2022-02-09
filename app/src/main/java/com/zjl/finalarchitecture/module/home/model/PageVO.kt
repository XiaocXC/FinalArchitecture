package com.zjl.finalarchitecture.module.home.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author Xiaoc
 * @since 2022-01-08
 */
@Serializable
data class PageVO<T>(
    /**
     * 当前页码
     */
    @SerialName("curPage")
    val currentPage: Int,
    /**
     * 数据列表
     */
    @SerialName("datas")
    val dataList: MutableList<T>,
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
