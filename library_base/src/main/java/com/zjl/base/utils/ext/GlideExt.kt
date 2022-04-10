@file:Suppress("unused")
package com.zjl.base.utils.ext

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.zjl.lib_base.R
import java.io.File

/**
 * @author Xiaoc
 * @since 2021-11-01
 *
 * Glide图像处理工具集
 * 使用高阶函数进行图片内容自定义与加载
 * 该工具集包含了内置的一些内容处理，如失败图等处理，同时支持自定义
 */
val circleCrop by lazy {
    CircleCrop()
}

@JvmOverloads
inline fun ImageView.load(
    uri: String?,
    roundedCorners: Int = 0,
    builder: RequestOptions.() -> RequestOptions = {
        if(roundedCorners > 0) transform(RoundedCorners(roundedCorners)) else this
    }
) = loadAny(uri,builder, true)

@JvmOverloads
inline fun ImageView.load(
    @DrawableRes drawableResId: Int,
    roundedCorners: Int = 0,
    builder: RequestOptions.() -> RequestOptions = {
        if(roundedCorners > 0) transform(RoundedCorners(roundedCorners)) else this
    }
) = loadAny(drawableResId,builder, false)

@JvmOverloads
inline fun ImageView.load(
    uri: Uri?,
    roundedCorners: Int = 0,
    builder: RequestOptions.() -> RequestOptions = {
        if(roundedCorners > 0) transform(RoundedCorners(roundedCorners)) else this
    }
) = loadAny(uri,builder, true)

@JvmOverloads
inline fun ImageView.load(
    file: File?,
    roundedCorners: Int = 0,
    builder: RequestOptions.() -> RequestOptions = {
        if(roundedCorners > 0) transform(RoundedCorners(roundedCorners)) else this
    }
) = loadAny(file,builder, false)

@JvmOverloads
inline fun ImageView.load(
    bitmap: Bitmap?,
    roundedCorners: Int = 0,
    builder: RequestOptions.() -> RequestOptions = {
        if(roundedCorners > 0) transform(RoundedCorners(roundedCorners)) else this
    }
) = loadAny(bitmap,builder, false)

@JvmOverloads
inline fun ImageView.load(
    drawable: Drawable?,
    roundedCorners: Int = 0,
    builder: RequestOptions.() -> RequestOptions = {
        if(roundedCorners > 0) transform(RoundedCorners(roundedCorners)) else this
    }
) = loadAny(drawable,builder, false)

@JvmOverloads
inline fun ImageView.load(
    bytes: ByteArray?,
    roundedCorners: Int = 0,
    builder: RequestOptions.() -> RequestOptions = {
        if(roundedCorners > 0) transform(RoundedCorners(roundedCorners)) else this
    }
) = loadAny(bytes,builder, false)

@JvmOverloads
inline fun ImageView.loadCircleCrop(
    uri: String?,
    builder: RequestOptions.() -> RequestOptions = {
        transform(circleCrop)
    }
) = loadAny(uri,builder, true)

@JvmOverloads
inline fun ImageView.loadCircleCrop(
    @DrawableRes drawableResId: Int,
    builder: RequestOptions.() -> RequestOptions = {
        transform(circleCrop)
    }
) = loadAny(drawableResId,builder, false)

@JvmOverloads
inline fun ImageView.loadCircleCrop(
    uri: Uri?,
    builder: RequestOptions.() -> RequestOptions = {
        transform(circleCrop)
    }
) = loadAny(uri,builder, true)

@JvmOverloads
inline fun ImageView.loadCircleCrop(
    file: File?,
    builder: RequestOptions.() -> RequestOptions = {
        transform(circleCrop)
    }
) = loadAny(file,builder, false)

@JvmOverloads
inline fun ImageView.loadCircleCrop(
    drawable: Drawable?,
    builder: RequestOptions.() -> RequestOptions = {
        transform(circleCrop)
    }
) = loadAny(drawable,builder, false)

@JvmOverloads
inline fun ImageView.loadCircleCrop(
    bytes: ByteArray?,
    builder: RequestOptions.() -> RequestOptions = {
        transform(circleCrop)
    }
) = loadAny(bytes,builder, false)

inline fun ImageView.loadAny(
    data: Any?,
    builder: RequestOptions.() -> RequestOptions = {this},
    enablePlaceHolder: Boolean = true
) {

    // 默认图，放在调用高阶函数前，以便于如果自定义能够覆盖掉默认效果
    val result = if(enablePlaceHolder){
        RequestOptions()
            .placeholder(R.color.base_lightgray)
            .error(R.color.base_lightgray)
    } else {
        RequestOptions()
    }

    Glide.with(context)
        .load(data)
        .apply(builder(result))
        .into(this)
}