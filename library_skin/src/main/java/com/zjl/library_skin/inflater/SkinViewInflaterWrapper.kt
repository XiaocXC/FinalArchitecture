package com.zjl.library_skin.inflater

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.theme.MaterialComponentsViewInflater
import com.zjl.library_skin.SkinManager
import com.google.android.material.button.SkinMaterialButton
import com.google.android.material.textview.SkinMaterialTextView
import com.zjl.library_skin.widget.compat.SkinCheckBox
import com.zjl.library_skin.widget.compat.SkinImageView

/**
 * @author Xiaoc
 * @since 2023-01-16
 *
 * 一些XML上配置的颜色等属性是不会被外部Resources替换的
 * 我们需要继承 [MaterialComponentsViewInflater] 来手动替换这些默认样式
 *
 * 这个类不需要手动在代码中配置，你可以在主题样式XML中配置 viewInflaterClass 属性配置上该类路径即可
 * 在APP启动后，就会自动处理其中的逻辑
 *
 * 我们现在的类是[MaterialDesignViewInflater]的子类，如果你的项目不是MD主题，请继承[AppCompatViewInflater]
 */
open class SkinViewInflaterWrapper: MaterialComponentsViewInflater() {

    override fun createTextView(context: Context, attrs: AttributeSet?): AppCompatTextView {
        return SkinMaterialTextView(context, attrs)
    }

    override fun createButton(context: Context, attrs: AttributeSet): AppCompatButton {
        return SkinMaterialButton(context, attrs)
    }

    override fun createImageView(context: Context, attrs: AttributeSet?): AppCompatImageView {
        return SkinImageView(context, attrs)
    }

    override fun createCheckBox(context: Context, attrs: AttributeSet?): AppCompatCheckBox {
        return SkinCheckBox(context, attrs)
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
                break
            }
        }

        return changeView
    }

}