package com.zjl.finalarchitecture.utils.ext.bitmap

import android.graphics.Bitmap


/**
 * 将Bitmap转换为Int数组
 */
fun Bitmap.toIntArray(): IntArray{
    val width = this.width
    val height = this.height
    val pixels = IntArray(width * height)
    this.getPixels(pixels, 0, width, 0, 0, width, height)
    return pixels
}