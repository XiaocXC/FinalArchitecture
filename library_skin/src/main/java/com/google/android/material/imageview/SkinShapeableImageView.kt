package com.google.android.material.imageview

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.SkinBackgroundHelper
import com.zjl.library_skin.widget.compat.SkinImageViewHelper

class SkinShapeableImageView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = 0
): ShapeableImageView(context, attr, defStyleAttr) {

    private val backgroundHelper = SkinBackgroundHelper(this)
    private val imageViewHelper = SkinImageViewHelper(this)
    init {
        backgroundHelper.loadFromAttributes(attr, defStyleAttr)
        imageViewHelper.loadFromAttributes(attr, defStyleAttr)
    }

    override fun setBackgroundResource(resid: Int) {
        super.setBackgroundResource(resid)
        backgroundHelper.onSetBackgroundResource(resid)
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        imageViewHelper.onSetImageResource(resId)
    }
}