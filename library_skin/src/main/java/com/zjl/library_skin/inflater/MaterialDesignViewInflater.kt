package com.zjl.library_skin.inflater

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.google.android.material.button.SkinMaterialButton
import com.google.android.material.checkbox.SkinMaterialCheckBox
import com.google.android.material.imageview.SkinShapeableImageView
import com.google.android.material.tabs.SkinTabLayout
import com.google.android.material.textfield.SkinMaterialTextInputEditText
import com.google.android.material.textfield.SkinMaterialTextInputLayout
import com.google.android.material.textview.SkinMaterialTextView

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
        return when(name){
            "com.google.android.material.button.MaterialButton" ->{
                SkinMaterialButton(context, attributeSet)
            }
            "com.google.android.material.textview.MaterialTextView" ->{
                SkinMaterialTextView(context, attributeSet)
            }
            "com.google.android.material.tabs.TabLayout" ->{
                SkinTabLayout(context, attributeSet)
            }
            "com.google.android.material.checkbox.MaterialCheckBox" ->{
                SkinMaterialCheckBox(context, attributeSet)
            }
            "com.google.android.material.checkbox.SkinMaterialCheckBox" ->{
                SkinMaterialCheckBox(context, attributeSet)
            }
            "com.google.android.material.imageview.ShapeableImageView" ->{
                SkinShapeableImageView(context, attributeSet)
            }
            "com.google.android.material.textfield.TextInputEditText" ->{
                SkinMaterialTextInputEditText(context, attributeSet)
            }
            "com.google.android.material.textfield.TextInputLayout" ->{
                SkinMaterialTextInputLayout(context, attributeSet)
            }
            else ->{
                null
            }
        }
    }
}