package com.zjl.base.utils.materialcolor

import android.graphics.Bitmap
import android.graphics.Color
import com.zjl.base.utils.materialcolor.quantize.QuantizerCelebi
import com.zjl.base.utils.materialcolor.scheme.Scheme
import com.zjl.base.utils.materialcolor.score.Score
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

    suspend fun resolveByBitmap(bitmap: Bitmap, isDarkMode: Boolean, originalColor: Boolean = false): ColorContainerData {
        return withContext(Dispatchers.Default){
            resolveByPixelsInner(bitmapToPixels(bitmap), isDarkMode)
        }
    }

    suspend fun resolveByPixels(pixels: IntArray, isDarkMode: Boolean, originalColor: Boolean = false): ColorContainerData {
        return withContext(Dispatchers.Default){
            resolveByPixelsInner(pixels, isDarkMode)
        }
    }

    private fun resolveByPixelsInner(pixels: IntArray, isDarkMode: Boolean, originalColor: Boolean = false): ColorContainerData {
        val quantize = QuantizerCelebi.quantize(pixels, 10)
        val result = Score.score(quantize, 4, Color.BLUE, originalColor)
        val firstColor = result[0]
        val colorScheme = if(isDarkMode){
            Scheme.dark(firstColor)
        } else {
            Scheme.light(firstColor)
        }
        return if(originalColor){
            ColorContainerData(firstColor, colorScheme.onPrimary)
        } else {
            ColorContainerData(colorScheme.primary, colorScheme.onPrimary)
        }

    }

    private suspend fun bitmapToPixels(bitmap: Bitmap): IntArray{
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        return pixels
    }

    fun rgb2Argb(color: Int, alpha: Int): Int{
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(alpha,red,green,blue)
    }
}

data class ColorContainerData(
    val primaryColor: Int,
    val onPrimaryColor: Int
)