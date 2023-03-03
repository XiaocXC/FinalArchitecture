package com.zjl.library_skin.inflater

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * @author Xiaoc
 * @since 2023-01-16
 */
interface SkinViewInflater {

    /**
     * 创建视图的主题色
     * @param name 视图名
     * @param attributeSet 视图附带的属性
     */
    fun createView(context: Context, name: String?, attributeSet: AttributeSet?): View?
}