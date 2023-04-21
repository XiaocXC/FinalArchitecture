package com.google.android.material.textfield

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.SkinBackgroundHelper
import com.google.android.material.textview.SkinMaterialTextViewHelper
import com.zjl.library_skin.R

class SkinMaterialTextInputEditText @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = R.attr.editTextStyle
): TextInputEditText(context, attr, defStyleAttr) {

    private val skinBackgroundHelper = SkinBackgroundHelper(this)
    private val skinMaterialTextViewHelper = SkinMaterialTextViewHelper(this)

    init {
        skinBackgroundHelper.loadFromAttributes(attr, defStyleAttr)
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

    fun getTextColorResId(): Int{
        return skinMaterialTextViewHelper.textColorResId
    }
}