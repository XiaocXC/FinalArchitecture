package com.zjl.finalarchitecture.module.toolbox.multilist

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewModelScope
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.zjl.base.globalApplication
import com.zjl.base.globalContext
import com.zjl.base.utils.ext.isNightMode
import com.zjl.base.utils.materialcolor.ColorContainerData
import com.zjl.base.utils.materialcolor.PaletteUtils
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.base.viewmodel.requestScope
import com.zjl.finalarchitecture.module.toolbox.multilist.entity.ExampleMultiEntity
import com.zjl.finalarchitecture.utils.ext.coil.toBitmapWithCoil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * @author Xiaoc
 * @since 2022-04-27
 *
 * 一个多ItemType的ViewModel
 */
class MultiListViewModel: BaseViewModel() {

    private val _multiList = MutableStateFlow<List<ExampleMultiEntity>>(emptyList())
    val multiList: StateFlow<List<ExampleMultiEntity>> get() = _multiList

    private val _toolbarColor = MutableStateFlow<ColorContainerData?>(null)
    val toolbarColor: StateFlow<ColorContainerData?> get() = _toolbarColor

    init {
        initData()
    }

    fun parseImageToPrimaryColor(imgUrl: String){
        requestScope {
            val request = ImageRequest.Builder(globalApplication)
                .size(100)
                .allowHardware(false)
                .data(imgUrl)
                .build()

            val bitmap = request.toBitmapWithCoil()
            // 进行色调提取
            val colorData = PaletteUtils.resolveByBitmap(bitmap, globalContext.resources.isNightMode())
            _toolbarColor.value = colorData

        }.catch {
            it.printStackTrace()
        }
    }

    fun initData() {
        viewModelScope.launch {
            // 模拟多样式内容数据
            _multiList.value = listOf(
                ExampleMultiEntity.MultiBannerData(
                    listOf(
                        "http://storage-image.ciangciang.top/0d23fb5ca091498db79e7ed4c7596cf6.jpg",
                        "http://storage-image.ciangciang.top/0dfafbd9f4e041e5bcbdc82bc438b22a.jpg",
                        "http://storage-image.ciangciang.top/30193b4e3a354a75913ce56e12fa9f8a.webp",
                        "http://storage-image.ciangciang.top/321187e921e6406788d85bf42c1e056e.jpg",
                        "http://storage-image.ciangciang.top/7553b942030e496581e896ca3f2573f1.jpg",
                        "http://storage-image.ciangciang.top/1e1d1d07b60948e78fc5a4be2f8bb154.jpg"
                    )
                ),
                ExampleMultiEntity.MultiExpandData(
                    listOf("新闻1","新闻2","新闻3","新闻4","新闻5","新闻6"),
                    false
                ),
                ExampleMultiEntity.MultiTextData("新闻1"),
                ExampleMultiEntity.MultiTextData("新闻2"),
                ExampleMultiEntity.MultiTextData("新闻3"),
                ExampleMultiEntity.MultiTextData("新闻4"),
                ExampleMultiEntity.MultiTextData("新闻5"),
                ExampleMultiEntity.MultiTextData("新闻6"),
                ExampleMultiEntity.MultiTextData("新闻7"),
                ExampleMultiEntity.MultiTextData("新闻8"),
                ExampleMultiEntity.MultiTextData("新闻9"),
                ExampleMultiEntity.MultiTextData("新闻10"),
                ExampleMultiEntity.MultiTextData("新闻11"),
                ExampleMultiEntity.MultiTextData("新闻12"),
                ExampleMultiEntity.MultiTextData("新闻13"),
                ExampleMultiEntity.MultiTextData("新闻14"),
                ExampleMultiEntity.MultiTextData("新闻15"),
                ExampleMultiEntity.MultiTextData("新闻16"),
                ExampleMultiEntity.MultiTextData("新闻17"),
                ExampleMultiEntity.MultiTextData("新闻18"),
                ExampleMultiEntity.MultiTextData("新闻19"),
                ExampleMultiEntity.MultiTextData("新闻20"),
                ExampleMultiEntity.MultiTextData("新闻21"),
                ExampleMultiEntity.MultiTextData("新闻22"),
                ExampleMultiEntity.MultiTextData("新闻23"),
                ExampleMultiEntity.MultiTextData("新闻24"),
                ExampleMultiEntity.MultiTextData("新闻25")
            )
        }
    }
}