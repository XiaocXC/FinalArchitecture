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

    fun updateView(context: Context, name: String?, view: View, attributeSet: AttributeSet?)
}