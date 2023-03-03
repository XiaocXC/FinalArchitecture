package com.google.android.material

import android.util.AttributeSet
import android.view.View
import com.zjl.library_skin.R
import com.zjl.library_skin.SkinManager

class SkinBackgroundHelper(
    private val view: View
) {

    private var backgroundResId = View.NO_ID

    fun loadFromAttributes(attr: AttributeSet?, defStyleAttr: Int){
        val ta = view.context.obtainStyledAttributes(attr, R.styleable.View, defStyleAttr, 0)
        ta.use {
            if(ta.hasValue(R.styleable.ViewBackgroundHelper_android_background)){
                backgroundResId = ta.getResourceId(R.styleable.ViewBackgroundHelper_android_background, View.NO_ID)
            }
        }
        applySkin()
    }

    fun applySkin(){
        if(backgroundResId == View.NO_ID){
            return
        }
        val provider = SkinManager.getInstance().provider ?: return
        val backgroundId = provider.resetResIdIfNeed(view.context, backgroundResId)
        view.setBackgroundResource(backgroundId)
    }

    fun onSetBackgroundResource(resId: Int){
        backgroundResId = resId
        // 更新背景
        applySkin()
    }
}