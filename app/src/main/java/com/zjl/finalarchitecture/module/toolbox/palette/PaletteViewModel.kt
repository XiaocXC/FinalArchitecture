package com.zjl.finalarchitecture.module.toolbox.palette

import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.net.toFile
import coil.request.ImageRequest
import com.zjl.base.globalApplication
import com.zjl.base.globalContext
import com.zjl.base.ui.UiModel
import com.zjl.base.utils.ext.isNightMode
import com.zjl.base.utils.materialcolor.hct.Hct
import com.zjl.base.utils.materialcolor.quantize.QuantizerCelebi
import com.zjl.base.utils.materialcolor.scheme.SchemeTonalSpot
import com.zjl.base.utils.materialcolor.score.Score
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.base.viewmodel.requestScope
import com.zjl.finalarchitecture.utils.ext.bitmap.toIntArray
import com.zjl.finalarchitecture.utils.ext.coil.toBitmapWithCoil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

/**
 * @author Xiaoc
 * @since 2022-12-01
 *
 * 取色 ViewModel
 */
class PaletteViewModel: BaseViewModel() {

    private val _paletteList = MutableStateFlow<UiModel<List<PaletteData>>>(UiModel.Success(emptyList()))
    val paletteList: StateFlow<UiModel<List<PaletteData>>> = _paletteList

    /**
     * 将图片Uri解析为色调
     * @param uri 图片Uri（可接受本地或者远程）
     */
    fun resolvePalette(uri: Uri){
        requestScope(dispatcher = Dispatchers.IO) {
            _paletteList.value = UiModel.Loading()

            val scheme = uri.scheme
            // 如果是本地，我们直接解析
            val bitmap = if(scheme == ContentResolver.SCHEME_CONTENT || scheme == ContentResolver.SCHEME_FILE){
                val bitmap = globalContext.contentResolver.openInputStream(uri)?.use {
                    BitmapFactory.decodeStream(it)
                } ?: throw RuntimeException("无效的Uri")
                bitmap
            } else if(scheme == ContentResolver.SCHEME_FILE){
                val file = uri.toFile()
                val bitmap = BitmapFactory.decodeFile(file.path)
                bitmap
            } else {
                val request = ImageRequest.Builder(globalApplication)
                    .size(100)
                    .allowHardware(false)
                    .data(uri)
                    .build()
                val bitmap = request.toBitmapWithCoil()
                bitmap
            }

            // 进行图片色调解析，规定最大取色数为16
            val quantize = QuantizerCelebi.quantize(bitmap.toIntArray(), 16)
            // 取出后我们进行评分，将某些不符合规范的颜色给筛选掉，返回一个符合的颜色列表，第一个颜色是最符合的主色调
            val colors = Score.score(quantize)
            val mostSuitableColor = colors[0]
            // 将最符合的一个颜色进行划分，将其分为MD色系，包括主色调，变调，文字颜色等

            SchemeTonalSpot(Hct.fromInt(mostSuitableColor), globalContext.resources.isNightMode(), Constra)

        }.catch {
            _paletteList.value = UiModel.Error(it)
        }

    }

    data class PaletteData(
        val color: Int,
        val description: String
    )
}