package com.zjl.library_skin.inflater

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.theme.MaterialComponentsViewInflater
import com.zjl.library_skin.SkinManager

/**
 * @author Xiaoc
 * @since 2023-01-16
 *
 * 一些XML上配置的颜色等属性是不会被外部Resources替换的
 * 我们需要继承 [AppCompatViewInflater]或者[MaterialComponentsViewInflater] 来手动替换这些默认样式
 *
 * 这个
 */
open class SkinViewInflaterWrapper: MaterialComponentsViewInflater() {

    override fun createTextView(context: Context?, attrs: AttributeSet?): AppCompatTextView {
        val view = super.createTextView(context, attrs)
        resetViewAttrsIfNeed(context, view, "TextView", attrs)
        return view
    }

    override fun createButton(context: Context, attrs: AttributeSet): AppCompatButton {
        val view = super.createButton(context, attrs)
        resetViewAttrsIfNeed(context, view, "Button", attrs)
        return view
    }

    override fun createView(context: Context?, name: String?, attrs: AttributeSet?): View? {
        return resetViewAttrsIfNeed(context, null, name, attrs)
    }

    protected open fun resetViewAttrsIfNeed(context: Context?, view: View?, name: String?, attrs: AttributeSet?): View?{
        if(context == null){
            return null
        }
        // 判断当前主题是否需要变色
        val provider = SkinManager.getInstance().provider
        if(provider?.support(context) == false){
            return null
        }

        var changeView: View? = null
        // 替换我们配置好的一些公用属性
        for(skinInflater in SkinManager.getInstance().skinViewInflaters){
            changeView = view ?: skinInflater.createView(context, name, attrs)
            if(changeView != null){
                skinInflater.updateView(context, name, changeView, attrs)
            }
        }

        return changeView
    }

}