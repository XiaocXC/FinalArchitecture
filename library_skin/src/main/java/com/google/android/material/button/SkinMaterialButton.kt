package com.google.android.material.button

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.R
import com.google.android.material.SkinBackgroundHelper
import com.google.android.material.textview.SkinMaterialTextViewHelper

class SkinMaterialButton @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = R.attr.materialButtonStyle
): MaterialButton(context, attr, defStyleAttr) {

    private val backgroundHelper = SkinBackgroundHelper(this)
    private val materialButtonHelper = SkinMaterialButtonHelper(this)
    private val skinMaterialTextViewHelper = SkinMaterialTextViewHelper(this)

    init {
        backgroundHelper.loadFromAttributes(attr, defStyleAttr)
        materialButtonHelper.loadFromAttributes(attr, defStyleAttr)
        skinMaterialTextViewHelper.loadFromAttributes(attr, defStyleAttr)
    }

    override fun setBackgroundResource(backgroundResourceId: Int) {
        super.setBackgroundResource(backgroundResourceId)
        backgroundHelper.onSetBackgroundResource(backgroundResourceId)
    }

    override fun setTextAppearance(resId: Int) {
        super.setTextAppearance(resId)
        skinMaterialTextViewHelper.onSetTextAppearance(context, resId)
    }

//    override fun setTextAppearance(context: Context, resId: Int) {
//        super.setTextAppearance(context, resId)
//        skinMaterialTextViewHelper.onSetTextAppearance(context, resId)
//    }
}