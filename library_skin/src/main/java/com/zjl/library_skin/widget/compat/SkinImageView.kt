package com.zjl.library_skin.widget.compat

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.SkinBackgroundHelper

class SkinImageView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = 0
): AppCompatImageView(context, attr, defStyleAttr){

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