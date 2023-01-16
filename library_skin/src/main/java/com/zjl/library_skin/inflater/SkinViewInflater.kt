package com.zjl.library_skin.inflater

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * @author Xiaoc
 * @since 2023-01-16
 */
interface SkinViewInflater {

    fun support(context: Context, view: View): Boolean

    fun updateView(context: Context, view: View, attributeSet: AttributeSet?)
}