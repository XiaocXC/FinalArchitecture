package com.zjl.library_skin.widget.compat

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatCheckBox
import com.google.android.material.checkbox.SkinCompoundButtonHelper
import com.zjl.library_skin.R

class SkinCheckBox @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = R.attr.checkboxStyle
): AppCompatCheckBox(context, attr, defStyleAttr){

    private val skinCompoundButtonHelper = SkinCompoundButtonHelper(this)
    init {
        skinCompoundButtonHelper.loadFromAttributes(attr, defStyleAttr)
    }

    override fun setButtonDrawable(resId: Int) {
        super.setButtonDrawable(resId)
        skinCompoundButtonHelper.onSetButtonDrawable(resId)
    }
}