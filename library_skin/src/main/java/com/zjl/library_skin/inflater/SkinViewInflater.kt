package com.zjl.library_skin.inflater

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * @author Xiaoc
 * @since 2023-01-16
 */
interface SkinViewInflater {

    fun createView(context: Context, name: String?, attributeSet: AttributeSet?): View?

    /**
     * 更新视图的主题色
     * @param name 视图名
     * @param view 视图实例
     * @param attributeSet 视图附带的属性
     * @return 由于我们可以加入多个视图ViewInflater，所以我们为了提高渲染速率，处理过的视图我们就可以返回true，不会再交由后面的处理器处理
     */
    fun updateView(context: Context, name: String?, view: View, attributeSet: AttributeSet?): Boolean
}