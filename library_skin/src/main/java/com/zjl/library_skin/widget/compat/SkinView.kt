package com.zjl.library_skin.widget.compat

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.google.android.material.SkinBackgroundHelper

class SkinView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = 0
): View(context, attr, defStyleAttr) {

    private val backgroundHelper = SkinBackgroundHelper(this)
    init {
        backgroundHelper.loadFromAttributes(attr, defStyleAttr)
    }

    override fun setBackgroundResource(resid: Int) {
        super.setBackgroundResource(resid)
        backgroundHelper.onSetBackgroundResource(resid)
    }
}