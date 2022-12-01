package com.zjl.finalarchitecture.utils.ext.coil

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.core.graphics.drawable.toBitmap
import coil.imageLoader
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.zjl.base.globalApplication

/**
 * Coil快速将内容解析为Bitmap
 * 如果是动图，我们会取动图的第一帧
 */
suspend fun ImageRequest.toBitmapWithCoil(): Bitmap{
    val result = globalApplication.imageLoader.execute(this)
    if(result is ErrorResult){
        throw result.throwable
    } else if(result is SuccessResult){
        // 1.将Bitmap获得
        val bitmap = when(val drawable = result.drawable){
            // 2.解析图片主色调
            is BitmapDrawable ->{
                drawable.bitmap
            }
            else ->{
                result.drawable.current.toBitmap()
            }
        }
        return bitmap
    }
    throw IllegalArgumentException("未知的请求操作")
}