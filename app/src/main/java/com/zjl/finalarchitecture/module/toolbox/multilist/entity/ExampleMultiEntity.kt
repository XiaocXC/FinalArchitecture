package com.zjl.finalarchitecture.module.toolbox.multilist.entity

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * @author Xiaoc
 * @since 2022-04-26
 *
 * 多样式的接口
 */
interface ExampleMultiEntity: MultiItemEntity{

    companion object{

        /**
         * Banner样式
         */
        const val TYPE_BANNER = 1

        /**
         * 展开折叠样式
         */
        const val TYPE_EXPAND = 2

        /**
         * 其他普通文本样式
         */
        const val TYPE_OTHER_TEXT = 3
    }

    data class MultiBannerData(
        val imgUrls: List<String>, override val itemType: Int = TYPE_BANNER
    ): ExampleMultiEntity

    data class MultiExpandData(
        val expandList: List<String>,

        /**
         * 是否展开的状态，默认收起
         */
        var expanded: Boolean = false,
        override val itemType: Int = TYPE_EXPAND
    ): ExampleMultiEntity

    data class MultiTextData(
        val text: String, override val itemType: Int = TYPE_OTHER_TEXT
    ): ExampleMultiEntity
}