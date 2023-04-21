package com.google.android.material.textview

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.zjl.library_skin.R
import com.zjl.library_skin.SkinManager

open class SkinMaterialTextViewHelper(
    private val view: TextView
) {

    var textColorResId: Int = View.NO_ID

    private var textColorHintResId = View.NO_ID

    private var drawableBottomResId = View.NO_ID
    private var drawableStartResId = View.NO_ID
    private var drawableEndResId = View.NO_ID
    private var drawableTopResId = View.NO_ID

    fun loadFromAttributes(attr: AttributeSet?, defStyleAttr: Int){
        var ta = view.context.obtainStyledAttributes(attr, R.styleable.AppCompatTextView, defStyleAttr, 0)
        val ap = ta.getResourceId(R.styleable.AppCompatTextView_android_textAppearance, View.NO_ID)

        if(ta.hasValue(R.styleable.AppCompatTextHelper_android_drawableLeft)){
            drawableStartResId = ta.getResourceId(R.styleable.AppCompatTextHelper_android_drawableLeft, View.NO_ID)
        }
        if(ta.hasValue(R.styleable.AppCompatTextHelper_android_drawableStart)){
            drawableStartResId = ta.getResourceId(R.styleable.AppCompatTextHelper_android_drawableStart, View.NO_ID)
        }
        if(ta.hasValue(R.styleable.AppCompatTextHelper_android_drawableRight)){
            drawableEndResId = ta.getResourceId(R.styleable.AppCompatTextHelper_android_drawableRight, View.NO_ID)
        }
        if(ta.hasValue(R.styleable.AppCompatTextHelper_android_drawableEnd)){
            drawableEndResId = ta.getResourceId(R.styleable.AppCompatTextHelper_android_drawableEnd, View.NO_ID)
        }
        if(ta.hasValue(R.styleable.AppCompatTextHelper_android_drawableTop)){
            drawableTopResId = ta.getResourceId(R.styleable.AppCompatTextHelper_android_drawableTop, View.NO_ID)
        }
        if(ta.hasValue(R.styleable.AppCompatTextHelper_android_drawableBottom)){
            drawableBottomResId = ta.getResourceId(R.styleable.AppCompatTextHelper_android_drawableBottom, View.NO_ID)
        }

        // 先检查style里面是否默认存在textColor等属性
        if(ap != 0){
            val a = view.context.obtainStyledAttributes(ap, R.styleable.TextAppearance)
            if(a.hasValue(R.styleable.TextAppearance_android_textColor)){
                textColorResId = a.getResourceId(R.styleable.TextAppearance_android_textColor, View.NO_ID)
            }
            if(a.hasValue(R.styleable.TextAppearance_android_textColorHint)){
                textColorHintResId = a.getResourceId(R.styleable.TextAppearance_android_textColorHint, View.NO_ID)
            }
            a.recycle()
        }

        // 再检查xml里面是否直接覆盖存在textColor等属性
        ta = view.context.obtainStyledAttributes(attr, R.styleable.TextAppearance, 0, View.NO_ID)
        if(ta.hasValue(R.styleable.TextAppearance_android_textColor)){
            textColorResId = ta.getResourceId(R.styleable.TextAppearance_android_textColor, View.NO_ID)
        }
        if(ta.hasValue(R.styleable.TextAppearance_android_textColorHint)){
            textColorHintResId = ta.getResourceId(R.styleable.TextAppearance_android_textColorHint, View.NO_ID)
        }
        ta.recycle()

        applySkin()
    }

    fun onSetCompoundDrawablesRelativeWithIntrinsicBounds(
        @DrawableRes start: Int, @DrawableRes top: Int, @DrawableRes end: Int, @DrawableRes bottom: Int
    ){
        drawableStartResId = start
        drawableTopResId = top
        drawableEndResId = end
        drawableBottomResId = bottom
        applyCompoundDrawablesRelativeResource()
    }

    fun onSetCompoundDrawablesWithIntrinsicBounds(
        @DrawableRes start: Int, @DrawableRes top: Int, @DrawableRes end: Int, @DrawableRes bottom: Int
    ){
        drawableStartResId = start
        drawableTopResId = top
        drawableEndResId = end
        drawableBottomResId = bottom
        applyCompoundDrawablesRelativeResource()
    }

    fun onSetTextAppearance(context: Context, resId: Int){
        val a = view.context.obtainStyledAttributes(resId, R.styleable.TextAppearance)
        if(a.hasValue(R.styleable.TextAppearance_android_textColor)){
            textColorResId = a.getResourceId(R.styleable.TextAppearance_android_textColor, View.NO_ID)
        }
        if(a.hasValue(R.styleable.TextAppearance_android_textColorHint)){
            textColorHintResId = a.getResourceId(R.styleable.TextAppearance_android_textColorHint, View.NO_ID)
        }
        a.recycle()
        applyTextColorResource()
        applyTextColorHintResource()
    }

    fun applyTextColorResource(){
        val provider = SkinManager.getInstance().provider ?: return
        val replaceTextColorResId = provider.resetResIdIfNeed(view.context, textColorResId)
        if(replaceTextColorResId != View.NO_ID){
            view.setTextColor(
                ContextCompat.getColorStateList(view.context, provider.resetResIdIfNeed(view.context, replaceTextColorResId))
            )
        }
    }

    fun applyTextColorHintResource(){
        val provider = SkinManager.getInstance().provider ?: return
        val replaceTextColorHintResId = provider.resetResIdIfNeed(view.context, textColorHintResId)
        if(replaceTextColorHintResId != View.NO_ID){
            view.setHintTextColor(
                ContextCompat.getColorStateList(view.context, provider.resetResIdIfNeed(view.context, replaceTextColorHintResId))
            )
        }
    }

    fun applyCompoundDrawablesRelativeResource(){
        applyCompoundDrawablesResource()
    }

    protected open fun applyCompoundDrawablesResource(){
        val provider = SkinManager.getInstance().provider ?: return
        var drawableStart: Drawable? = null
        var drawableEnd: Drawable? = null
        var drawableTop: Drawable? = null
        var drawableBottom: Drawable? = null
        val replacedDrawableStartResId = provider.resetResIdIfNeed(view.context, drawableStartResId)
        if(replacedDrawableStartResId != View.NO_ID){
            drawableStart = ContextCompat.getDrawable(view.context, replacedDrawableStartResId)
        }
        val replacedDrawableEndResId = provider.resetResIdIfNeed(view.context, drawableEndResId)
        if(replacedDrawableEndResId != View.NO_ID){
            drawableEnd = ContextCompat.getDrawable(view.context, replacedDrawableEndResId)
        }
        val replacedDrawableTopResId = provider.resetResIdIfNeed(view.context, drawableTopResId)
        if(replacedDrawableTopResId != View.NO_ID){
            drawableTop = ContextCompat.getDrawable(view.context, replacedDrawableTopResId)
        }
        val replacedDrawableBottomResId = provider.resetResIdIfNeed(view.context, drawableBottomResId)
        if(replacedDrawableBottomResId != View.NO_ID){
            drawableBottom = ContextCompat.getDrawable(view.context, replacedDrawableBottomResId)
        }
        if(replacedDrawableStartResId != View.NO_ID || replacedDrawableEndResId != View.NO_ID ||
                replacedDrawableTopResId != View.NO_ID || replacedDrawableBottomResId != View.NO_ID){
            view.setCompoundDrawablesWithIntrinsicBounds(drawableStart, drawableTop, drawableEnd, drawableBottom)
        }
    }

    fun applySkin(){
        applyCompoundDrawablesRelativeResource()
        applyTextColorResource()
        applyTextColorHintResource()
    }
}