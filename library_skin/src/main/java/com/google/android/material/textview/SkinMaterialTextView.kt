package com.google.android.material.textview

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import com.google.android.material.SkinBackgroundHelper

class SkinMaterialTextView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
): MaterialTextView(context, attr, defStyleAttr) {

    private val skinMaterialTextViewHelper = SkinMaterialTextViewHelper(this)
    private val skinBackgroundHelper = SkinBackgroundHelper(this)

    init {
        skinMaterialTextViewHelper.loadFromAttributes(attr, defStyleAttr)
    }

    override fun setBackgroundResource(resId: Int) {
        super.setBackgroundResource(resId)
        skinBackgroundHelper.onSetBackgroundResource(resId)
    }

    override fun setTextAppearance(resId: Int) {
        super.setTextAppearance(resId)
        skinMaterialTextViewHelper.onSetTextAppearance(context, resId)
    }

    override fun setTextAppearance(context: Context, resId: Int) {
        super.setTextAppearance(context, resId)
        skinMaterialTextViewHelper.onSetTextAppearance(context, resId)
    }

    override fun setCompoundDrawablesRelativeWithIntrinsicBounds(
        start: Int,
        top: Int,
        end: Int,
        bottom: Int
    ) {
        super.setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom)
        skinMaterialTextViewHelper.onSetCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom)
    }

    override fun setCompoundDrawablesWithIntrinsicBounds(
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
        skinMaterialTextViewHelper.onSetCompoundDrawablesRelativeWithIntrinsicBounds(left, top, right, bottom)
    }
}