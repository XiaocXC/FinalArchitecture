package com.zjl.library_skin.inflater

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.zjl.library_skin.R
import com.zjl.library_skin.SkinManager
import com.zjl.library_skin.inflater.helper.MaterialColorHelper

/**
 * @author Xiaoc
 * @since 2023-01-17
 *
 * MD设计中的一些代替的视图膨胀器
 * 它会在创建视图完毕后，将对应视图所需要的视图的View的属性值进行主题替换
 */
open class MaterialDesignViewInflater: SkinViewInflater {

    companion object {
        const val ANDROID_NAMESPACE = "http://schemas.android.com/apk/res/android"
    }

    override fun createView(context: Context, name: String?, attributeSet: AttributeSet?): View? {
        return null
    }

    override fun updateView(
        context: Context,
        name: String?,
        view: View,
        attributeSet: AttributeSet?
    ): Boolean {
        if(SkinManager.getInstance().provider?.support(context) == false){
            return false
        }
        // 统一处理控件背景
        resetBackgroundColorIfNeed(context, view, attributeSet)
        return when(name){
            "TextView" ->{
                resetTextColorIfNeed(context, view as TextView, attributeSet)
                true
            }
            "Button" ->{
                resetTextColorIfNeed(context, view as TextView, attributeSet)
                resetButtonColorIfNeed(context, view as MaterialButton, attributeSet)
                true
            }
            else -> {
                false
            }
        }
    }

    protected open fun resetBackgroundColorIfNeed(context: Context, view: View, attributeSet: AttributeSet?){
        val provider = SkinManager.getInstance().provider ?: return

        MaterialColorHelper.updateMaterialBackground(context, provider, view, attributeSet)
    }

    protected open fun resetTextColorIfNeed(context: Context, view: TextView, attributeSet: AttributeSet?){
        val provider = SkinManager.getInstance().provider ?: return

        MaterialColorHelper.updateMaterialTextColor(context, provider, view, attributeSet)
    }

    protected open fun resetButtonColorIfNeed(context: Context, view: MaterialButton, attributeSet: AttributeSet?){
        val provider = SkinManager.getInstance().provider ?: return

        MaterialColorHelper.updateMaterialButton(context, provider, view, attributeSet)
    }
}