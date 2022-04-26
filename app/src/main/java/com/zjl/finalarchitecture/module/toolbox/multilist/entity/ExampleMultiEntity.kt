package com.zjl.finalarchitecture.module.toolbox.multilist.entity

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * @author Xiaoc
 * @since 2022-04-26
 */
interface ExampleMultiEntity: MultiItemEntity{

    companion object{
        const val TYPE_BANNER = 1
        const val TYPE_EXPAND = 2
        const val TYPE_OTHER = 3
    }
}