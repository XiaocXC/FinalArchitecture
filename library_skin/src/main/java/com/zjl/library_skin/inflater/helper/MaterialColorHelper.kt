package com.zjl.library_skin.inflater.helper

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.zjl.library_skin.R
import com.zjl.library_skin.inflater.MaterialDesignViewInflater
import com.zjl.library_skin.provider.SkinProvider

internal object MaterialColorHelper {

    private val defaultMaterialTextColors = listOf<Int>(
        R.color.m3_default_color_primary_text,
        R.color.m3_dark_default_color_primary_text,
        R.color.m3_default_color_secondary_text,
        R.color.m3_dark_default_color_secondary_text,
        R.color.m3_primary_text_disable_only,
        R.color.m3_dark_primary_text_disable_only,
        R.color.m3_hint_foreground,
        R.color.m3_dark_hint_foreground,
        R.color.m3_highlighted_text,
        R.color.m3_dark_highlighted_text
    )

    fun updateMaterialTextColor(context: Context,
                                provider: SkinProvider,
                                view: TextView,
                                attributeSet: AttributeSet?){
        var textColorResId = 0

        var ta = context.obtainStyledAttributes(attributeSet, R.styleable.AppCompatTextView, 0, 0)
        var ap = 0
        ap = ta.getResourceId(R.styleable.AppCompatTextView_android_textAppearance, 0)
        ta.recycle()

        // 先检查style里面是否默认存在textColor等属性
        if(ap != 0){
            ta = context.obtainStyledAttributes(ap, R.styleable.TextAppearance)
            if(ta.hasValue(R.styleable.TextAppearance_android_textColor)){
                textColorResId = ta.getResourceId(R.styleable.TextAppearance_android_textColor, 0)
            }
            ta.recycle()
        }

        // 再检查xml里面是否直接覆盖存在textColor等属性
        ta = context.obtainStyledAttributes(attributeSet, R.styleable.TextAppearance, 0, 0)
        if(ta.hasValue(R.styleable.TextAppearance_android_textColor)){
            textColorResId = ta.getResourceId(R.styleable.TextAppearance_android_textColor, 0)
        }
        ta.recycle()

        // 排除掉默认的M3的相关颜色属性
        if(textColorResId == 0){
            return
        }
        if(defaultMaterialTextColors.contains(textColorResId)){
            return
        }
        view.setTextColor(
            ContextCompat.getColor(context, provider.resetResIdIfNeed(context, textColorResId))
        )
    }

    fun updateMaterialBackground(context: Context,
                                 provider: SkinProvider,
                                 view: View,
                                 attributeSet: AttributeSet?){
        // 获取 对应 属性值的资源 id，未找到时返回 0
        val backgroundId = attributeSet?.getAttributeResourceValue(MaterialDesignViewInflater.ANDROID_NAMESPACE, "background", 0) ?: 0
        if(backgroundId != 0){
            view.setBackgroundResource(provider.resetResIdIfNeed(context, backgroundId))
        }
    }

    fun updateMaterialButton(context: Context,
                             provider: SkinProvider,
                             view: MaterialButton,
                             attributeSet: AttributeSet?){
        var backgroundTintResId = 0
        // 获取 对应 属性值的资源 id，未找到时返回 0
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.MaterialButton, 0, 0)
        if(ta.hasValue(R.styleable.MaterialButton_backgroundTint)){
            backgroundTintResId = ta.getResourceId(R.styleable.MaterialButton_backgroundTint, 0)
        }
        ta.recycle()
        if(backgroundTintResId != 0){
            view.backgroundTintList = context.getColorStateList(provider.resetResIdIfNeed(context, backgroundTintResId))
        }
    }
}