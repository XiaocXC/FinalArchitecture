package com.zjl.library_skin.inflater

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.zjl.library_skin.R
import com.zjl.library_skin.SkinManager

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
    ) {
        if(SkinManager.getInstance().provider?.support(context) == false){
            return
        }
        resetBackgroundColorIfNeed(context, view, attributeSet)
        when(name){
            "TextView" ->{
                resetTextColorIfNeed(context, view as TextView, attributeSet)
            }
        }
    }

    protected open fun resetBackgroundColorIfNeed(context: Context, view: View, attributeSet: AttributeSet?){
        val provider = SkinManager.getInstance().provider ?: return
        // 获取 对应 属性值的资源 id，未找到时返回 0
        val backgroundId = attributeSet?.getAttributeResourceValue(ANDROID_NAMESPACE, "background", 0) ?: 0
        if(backgroundId != 0){
            view.setBackgroundResource(provider.resetResIdIfNeed(context, backgroundId))
        }
    }

    protected open fun resetTextColorIfNeed(context: Context, view: TextView, attributeSet: AttributeSet?){
        val provider = SkinManager.getInstance().provider ?: return
        var textColorResId = 0
        val styleId = attributeSet?.styleAttribute
        if(styleId != 0){
            context.resources
        }

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

        if(textColorResId != 0){
            view.setTextColor(
                ContextCompat.getColor(context, provider.resetResIdIfNeed(context, textColorResId))
            )
        }


    }
}