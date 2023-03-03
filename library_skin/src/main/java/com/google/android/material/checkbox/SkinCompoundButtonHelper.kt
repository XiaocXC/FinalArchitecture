package com.google.android.material.checkbox

import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.widget.CompoundButton
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.zjl.library_skin.R
import com.zjl.library_skin.SkinManager


class SkinCompoundButtonHelper(
    private val view: CompoundButton
) {

    private var buttonResourceId = View.NO_ID
    private var buttonTintResId = View.NO_ID

    fun loadFromAttributes(attr: AttributeSet?, defStyleAttr: Int){
        val ta: TypedArray = view.context.obtainStyledAttributes(
            attr, R.styleable.CompoundButton,
            defStyleAttr, 0
        )
        ta.use {
            if(ta.hasValue(R.styleable.CompoundButton_android_button)){
                buttonResourceId = ta.getResourceId(R.styleable.CompoundButton_android_button, View.NO_ID)
            }
            if(ta.hasValue(R.styleable.CompoundButton_buttonTint)){
                buttonTintResId = ta.getResourceId(R.styleable.CompoundButton_buttonTint, View.NO_ID)
            }
        }
        applySkin()
    }

    fun onSetButtonDrawable(resId: Int){
        buttonResourceId = resId
        applySkin()
    }

    fun applySkin(){
        val provider = SkinManager.getInstance().provider ?: return
        val replaceButtonResourceId = provider.resetResIdIfNeed(view.context, buttonResourceId)
        if(replaceButtonResourceId != View.NO_ID){
            view.buttonDrawable = ContextCompat.getDrawable(view.context, replaceButtonResourceId)
        }
        val replaceButtonTintResId = provider.resetResIdIfNeed(view.context, buttonTintResId)
        if(replaceButtonTintResId != View.NO_ID){
            view.buttonTintList = ContextCompat.getColorStateList(view.context, replaceButtonTintResId)
        }
    }
}