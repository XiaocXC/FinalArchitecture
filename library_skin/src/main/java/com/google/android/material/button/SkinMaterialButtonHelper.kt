package com.google.android.material.button

import android.annotation.SuppressLint
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.zjl.library_skin.R
import com.zjl.library_skin.SkinManager

class SkinMaterialButtonHelper(
    private val view: MaterialButton
) {

    private var rippleColorResId: Int = View.NO_ID
    private var backgroundTintResId: Int = View.NO_ID

    @SuppressLint("RestrictedApi")
    fun loadFromAttributes(attr: AttributeSet?, defStyleAttr: Int){
        val ta = view.context.obtainStyledAttributes(attr, R.styleable.MaterialButton, defStyleAttr, 0)
        ta.use {
            if(it.hasValue(R.styleable.MaterialButton_backgroundTint)){
                backgroundTintResId = ta.getResourceId(R.styleable.MaterialButton_backgroundTint, View.NO_ID)
            }
            if(it.hasValue(R.styleable.MaterialButton_rippleColor)){
                rippleColorResId = ta.getResourceId(R.styleable.MaterialButton_rippleColor, View.NO_ID)
            }
        }
        applySkin()
    }

    fun applySkin(){
        val provider = SkinManager.getInstance().provider ?: return
        val backgroundId = provider.resetResIdIfNeed(view.context, backgroundTintResId)
        if(backgroundId != View.NO_ID){
            view.backgroundTintList = AppCompatResources.getColorStateList(view.context, backgroundId)
        }
        val replaceRippleColorId = provider.resetResIdIfNeed(view.context, rippleColorResId)
        if(replaceRippleColorId != View.NO_ID){
            view.rippleColor = AppCompatResources.getColorStateList(view.context, replaceRippleColorId)
        }

    }
}