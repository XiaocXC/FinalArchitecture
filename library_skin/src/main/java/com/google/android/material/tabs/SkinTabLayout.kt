package com.google.android.material.tabs

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.zjl.library_skin.R
import com.zjl.library_skin.SkinManager

class SkinTabLayout @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = R.attr.tabStyle
): TabLayout(context, attr, defStyleAttr) {

    private var tabIndicatorColorResId: Int = View.NO_ID
    private var tabTextColorsResId: Int = View.NO_ID
    private var tabSelectedTextColorResId: Int = View.NO_ID

    init {
        val ta = context.obtainStyledAttributes(attr, R.styleable.TabLayout, defStyleAttr, 0)
        ta.use {
            tabIndicatorColorResId = ta.getResourceId(R.styleable.TabLayout_tabIndicatorColor, View.NO_ID)

            var a = context.obtainStyledAttributes(attr, R.styleable.AppCompatTextView, defStyleAttr, 0)
            val ap =  a.getResourceId(R.styleable.AppCompatTextView_android_textAppearance, View.NO_ID)
            a.recycle()

            // 先检查style里面是否默认存在textColor等属性
            if(ap != 0){
                a = context.obtainStyledAttributes(ap, R.styleable.TextAppearance)
                if(a.hasValue(R.styleable.TextAppearance_android_textColor)){
                    tabTextColorsResId = a.getResourceId(R.styleable.TextAppearance_android_textColor, View.NO_ID)
                }
                a.recycle()
            }

            if(ta.hasValue(R.styleable.TabLayout_tabTextColor)){
                tabTextColorsResId = ta.getResourceId(R.styleable.TabLayout_tabTextColor, View.NO_ID)
            }

            if(ta.hasValue(R.styleable.TabLayout_tabSelectedTextColor)){
                tabSelectedTextColorResId = ta.getResourceId(R.styleable.TabLayout_tabSelectedTextColor, View.NO_ID)
            }
        }
        applySkin()
    }

    fun applySkin(){
        val provider = SkinManager.getInstance().provider ?: return
        val replaceTabIndicatorColorResId = provider.resetResIdIfNeed(context, tabIndicatorColorResId)
        if(replaceTabIndicatorColorResId != View.NO_ID){
            setSelectedTabIndicatorColor(ContextCompat.getColor(context, replaceTabIndicatorColorResId))
        }

        val replaceTabTextColorsResId = provider.resetResIdIfNeed(context, tabTextColorsResId)
        if(replaceTabTextColorsResId != View.NO_ID){
            setTabTextColors(ContextCompat.getColorStateList(context, replaceTabTextColorsResId))
        }

        val replaceTabSelectedTextColorResId = provider.resetResIdIfNeed(context, tabSelectedTextColorResId)
        if(replaceTabSelectedTextColorResId != View.NO_ID){
            val selectColor = ContextCompat.getColor(context, replaceTabSelectedTextColorResId)
            if(getTabTextColors() != null){
                setTabTextColors(getTabTextColors()!!.defaultColor, selectColor)
            }
        }
    }
}