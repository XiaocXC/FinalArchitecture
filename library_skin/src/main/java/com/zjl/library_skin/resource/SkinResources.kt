package com.zjl.library_skin.resource

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.content.res.XmlResourceParser
import android.graphics.drawable.Drawable
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import com.zjl.library_skin.provider.SkinProvider
import java.lang.ref.WeakReference
import kotlin.jvm.Throws

/**
 * @author Xiaoc
 * @since 2023-01-16
 *
 * 支持根据当前皮肤动态选择resId的Resources资源
 * 它重写了所有的获取颜色和Drawable的方法，并根据当前皮肤的类型，来选择应该加载的资源
 */
class SkinResources(
    private val skinProvider: SkinProvider,
    context: Context,
    private val resources: Resources
): Resources(resources.assets, resources.displayMetrics, resources.configuration) {

    private val contextRef = WeakReference(context)

    @RequiresApi(21)
    @Throws(NotFoundException::class)
    override fun getDrawable(id: Int, theme: Theme?): Drawable? {
        return ResourcesCompat.getDrawable(resources, resetResIdIfNeed(contextRef.get(), id), theme)
    }

    @RequiresApi(21)
    override fun getDrawableForDensity(id: Int, density: Int, theme: Theme?): Drawable? {
        return ResourcesCompat.getDrawableForDensity(resources, resetResIdIfNeed(contextRef.get(), id), density, theme)
    }

    override fun getDrawable(id: Int): Drawable {
        return super.getDrawable(resetResIdIfNeed(contextRef.get(), id))
    }

    @RequiresApi(15)
    @Throws(NotFoundException::class)
    override fun getDrawableForDensity(id: Int, density: Int): Drawable? {
        return ResourcesCompat.getDrawableForDensity(resources, resetResIdIfNeed(contextRef.get(), id), density, null)
    }

    override fun getColor(id: Int, theme: Theme?): Int {
        return super.getColor(resetResIdIfNeed(contextRef.get(), id), theme)
    }

    override fun getColorStateList(id: Int, theme: Theme?): ColorStateList {
        return super.getColorStateList(resetResIdIfNeed(contextRef.get(), id), theme)
    }

    override fun getColor(id: Int): Int {
        return super.getColor(resetResIdIfNeed(contextRef.get(), id))
    }

    override fun getColorStateList(id: Int): ColorStateList {
        return super.getColorStateList(resetResIdIfNeed(contextRef.get(), id))
    }

    override fun getLayout(id: Int): XmlResourceParser {
        return super.getLayout(resetResIdIfNeed(contextRef.get(), id))
    }

    private fun resetResIdIfNeed(context: Context?, resId: Int): Int{
        return skinProvider.resetResIdIfNeed(context, resId)
    }
}