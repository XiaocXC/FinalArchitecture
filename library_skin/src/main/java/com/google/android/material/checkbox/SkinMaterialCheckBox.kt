package com.google.android.material.checkbox

import android.content.Context
import android.util.AttributeSet
import com.zjl.library_skin.R

class SkinMaterialCheckBox @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = R.attr.checkboxStyle
): MaterialCheckBox(context, attr, defStyleAttr) {

    private val skinCompoundButtonHelper = SkinCompoundButtonHelper(this)
    init {
        skinCompoundButtonHelper.loadFromAttributes(attr, defStyleAttr)
    }

    override fun setButtonDrawable(resId: Int) {
        super.setButtonDrawable(resId)
        skinCompoundButtonHelper.onSetButtonDrawable(resId)
    }
}