package com.google.android.material.textfield

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import com.google.android.material.SkinBackgroundHelper
import com.zjl.library_skin.R
import com.zjl.library_skin.SkinManager
import java.lang.reflect.Field
import java.lang.reflect.Method


class SkinMaterialTextInputLayout @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = R.attr.textInputStyle
): TextInputLayout(context, attr, defStyleAttr) {

    private var passwordToggleResId: Int = View.NO_ID
    private var counterTextColorResId: Int = View.NO_ID
    private var errorTextColorResId: Int = View.NO_ID
    private var focusedTextColorResId: Int = View.NO_ID
    private var defaultTextColorResId: Int = View.NO_ID

    private val skinBackgroundHelper = SkinBackgroundHelper(this)

    init {
        skinBackgroundHelper.loadFromAttributes(attr, defStyleAttr)

        val ta = context.obtainStyledAttributes(attr, R.styleable.TextInputLayout, defStyleAttr, R.style.Widget_Design_TextInputLayout)
        ta.use {
            if(ta.hasValue(R.styleable.TextInputLayout_android_textColorHint)){
                val textColorHintResId = ta.getResourceId(R.styleable.TextInputLayout_android_textColorHint, View.NO_ID)
                defaultTextColorResId = textColorHintResId
                focusedTextColorResId = textColorHintResId
                applyFocusedTextColorResource()
            }
            val errorTextAppearance: Int =
                ta.getResourceId(R.styleable.TextInputLayout_errorTextAppearance, View.NO_ID)
            loadErrorTextColorResFromAttributes(errorTextAppearance)

            val counterTextAppearance: Int =
                ta.getResourceId(R.styleable.TextInputLayout_counterTextAppearance, View.NO_ID)
            loadCounterTextColorResFromAttributes(counterTextAppearance)
            passwordToggleResId =
                ta.getResourceId(R.styleable.TextInputLayout_passwordToggleDrawable, View.NO_ID)
        }
    }

    private fun loadErrorTextColorResFromAttributes(@StyleRes resId: Int) {
        if (resId != View.NO_ID) {
            val errorTA =
                context.obtainStyledAttributes(resId, R.styleable.TextAppearance)
            if (errorTA.hasValue(R.styleable.TextAppearance_android_textColor)) {
                errorTextColorResId = errorTA.getResourceId(
                    R.styleable.TextAppearance_android_textColor,
                    View.NO_ID
                )
            }
            errorTA.recycle()
        }
        applyErrorTextColorResource()
    }

    private fun loadCounterTextColorResFromAttributes(@StyleRes resId: Int) {
        if (resId != View.NO_ID) {
            val counterTA =
                context.obtainStyledAttributes(resId, R.styleable.TextAppearance)
            if (counterTA.hasValue(R.styleable.TextAppearance_android_textColor)) {
                counterTextColorResId = counterTA.getResourceId(
                    R.styleable.TextAppearance_android_textColor,
                    View.NO_ID
                )
            }
            counterTA.recycle()
        }
        applyCounterTextColorResource()
    }

    private fun applyCounterTextColorResource() {
        val provider = SkinManager.getInstance().provider ?: return
        val replaceCounterTextColorResId = provider.resetResIdIfNeed(context, counterTextColorResId)
        if (replaceCounterTextColorResId != View.NO_ID) {
            val counterView: TextView? = getCounterView()
            if (counterView != null) {
                counterView.setTextColor(
                    ContextCompat.getColor(context, replaceCounterTextColorResId)
                )
                updateEditTextBackgroundInternal()
            }
        }
    }

    private fun getCounterView(): TextView? {
        try {
            val counterView = TextInputLayout::class.java.getDeclaredField("mCounterView")
            counterView.isAccessible = true
            return counterView[this] as TextView
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun applyErrorTextColorResource() {
        val provider = SkinManager.getInstance().provider ?: return
        val replaceErrorTextColorResId = provider.resetResIdIfNeed(context, errorTextColorResId)
        if (replaceErrorTextColorResId != View.NO_ID && replaceErrorTextColorResId != R.color.design_error) {
            val errorView: TextView? = getErrorView()
            if (errorView != null) {
                errorView.setTextColor(ContextCompat.getColor(context, replaceErrorTextColorResId))
                updateEditTextBackgroundInternal()
            }
        }
    }

    private fun updateEditTextBackgroundInternal() {
        updateEditTextBackground()
    }

    private fun applyFocusedTextColorResource() {
        val provider = SkinManager.getInstance().provider ?: return
        val replaceFocusedTextColorResId = provider.resetResIdIfNeed(context, focusedTextColorResId)
        if (replaceFocusedTextColorResId != View.NO_ID && replaceFocusedTextColorResId != R.color.abc_hint_foreground_material_light) {
            setFocusedTextColor(
                ContextCompat.getColorStateList(context, replaceFocusedTextColorResId)
            )
        } else if (getEditText() != null) {
            var textColorResId: Int = View.NO_ID
            if (getEditText() is SkinMaterialTextInputEditText) {
                textColorResId =
                    (getEditText() as SkinMaterialTextInputEditText).getTextColorResId()
            }
            val replaceTextColorResId = provider.resetResIdIfNeed(context, textColorResId)
            if (replaceTextColorResId != View.NO_ID) {
                val colors: ColorStateList? = ContextCompat.getColorStateList(context, replaceTextColorResId)
                setFocusedTextColor(colors)
            }
        }
    }

    private fun setFocusedTextColor(colors: ColorStateList?) {
        try {
            val focusedTextColor = TextInputLayout::class.java.getDeclaredField("mFocusedTextColor")
            focusedTextColor.isAccessible = true
            focusedTextColor[this] = colors
            updateLabelState()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun updateLabelState() {
        try {
            val updateLabelState =
                TextInputLayout::class.java.getDeclaredMethod(
                    "updateLabelState",
                    Boolean::class.javaPrimitiveType
                )
            updateLabelState.isAccessible = true
            updateLabelState.invoke(this, false)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun getErrorView(): TextView? {
        try {
            val errorView: Field = TextInputLayout::class.java.getDeclaredField("mErrorView")
            errorView.isAccessible = true
            return errorView.get(this) as TextView
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun setBackgroundResource(resId: Int) {
        super.setBackgroundResource(resId)
        skinBackgroundHelper.onSetBackgroundResource(resId)
    }

    override fun setCounterTextAppearance(counterTextAppearance: Int) {
        super.setCounterTextAppearance(counterTextAppearance)
        loadCounterTextColorResFromAttributes(counterTextAppearance)
    }

    override fun setErrorTextAppearance(errorTextAppearance: Int) {
        super.setErrorTextAppearance(errorTextAppearance)
        loadErrorTextColorResFromAttributes(errorTextAppearance)
    }

    override fun setErrorEnabled(enabled: Boolean) {
        super.setErrorEnabled(enabled)
        if(enabled){
            applyErrorTextColorResource()
        }
    }

    override fun setCounterEnabled(enabled: Boolean) {
        super.setCounterEnabled(enabled)
        if(enabled){
            applyCounterTextColorResource()
        }
    }

    fun applySkin(){
        applyErrorTextColorResource()
        applyCounterTextColorResource()
        applyFocusedTextColorResource()
        skinBackgroundHelper.applySkin()
    }
}