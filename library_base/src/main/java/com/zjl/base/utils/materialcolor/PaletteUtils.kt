package com.zjl.base.utils.materialcolor

import android.graphics.Bitmap
import android.graphics.Color
import com.zjl.base.utils.materialcolor.quantize.QuantizerCelebi
import com.zjl.base.utils.materialcolor.scheme.Scheme
import com.zjl.base.utils.materialcolor.score.ContainerScore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *
 * @author Xiaoc
 * @since 2021/1/10
 *
 * 调色板工具
 * 负责解析Bitmap色调
 */
object PaletteUtils {

    suspend fun resolveByBitmap(bitmap: Bitmap, isDarkMode: Boolean): ColorContainerData {
        return withContext(Dispatchers.Default){
            resolveByPixelsInner(bitmapToPixels(bitmap), isDarkMode)
        }
    }

    suspend fun resolveByPixels(pixels: IntArray, isDarkMode: Boolean): ColorContainerData {
        return withContext(Dispatchers.Default){
            resolveByPixelsInner(pixels, isDarkMode)
        }
    }

    private fun resolveByPixelsInner(pixels: IntArray, isDarkMode: Boolean): ColorContainerData {
        val quantize = QuantizerCelebi.quantize(pixels, 10)
        val primaryColorPair = ContainerScore.score(quantize, !isDarkMode)
        if(primaryColorPair.first){
            // 如果[primaryColorPair.first]为true
            // 说明返回的颜色不是最合适的，我们直接用该色作为主色调
            val color = primaryColorPair.second[0]
            return if(isDarkMode){
                val colorScheme = Scheme.dark(color)
                ColorContainerData(color, colorScheme.onPrimary)
            } else {
                val colorScheme = Scheme.light(color)
                ColorContainerData(color, colorScheme.onPrimary)
            }
        } else {
            // 如果[primaryColorPair.first]为false
            // 说明找到了最合适的颜色，我们计算其最应该展示的颜色
            val color = primaryColorPair.second[0]
            return if(isDarkMode){
                val colorScheme = Scheme.dark(color)
                ColorContainerData(colorScheme.primary, colorScheme.onPrimary)
            } else {
                val colorScheme = Scheme.light(color)
                ColorContainerData(colorScheme.primary, colorScheme.onPrimary)
            }
        }
    }

    private suspend fun bitmapToPixels(bitmap: Bitmap): IntArray{
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        return pixels
    }

    fun rgb2Argb(color: Int): Int{
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(46,red,green,blue)
    }
}

data class ColorContainerData(
    val primaryColor: Int,
    val onPrimaryColor: Int
)