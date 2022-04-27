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
import com.zjl.finalarchitecture.module.toolbox.multilist.entity.ExampleMultiEntity
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

    val bitmap = MutableStateFlow<Bitmap?>(null)

    init {
        toRefresh()
    }

    fun parseImageToPrimaryColor(imgUrl: String){
        viewModelScope.launch {
            // 1.获得图片Bitmap
            val request = ImageRequest.Builder(globalApplication)
                .size(100)
                .allowHardware(false)
                .data(imgUrl)
                .build()

            val result = globalApplication.imageLoader.execute(request)
            if(result !is SuccessResult){
                return@launch
            }
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
            // 2.进行色调提取
            val colorData = PaletteUtils.resolveByBitmap(bitmap, globalContext.resources.isNightMode())
            _toolbarColor.value = colorData
        }
    }

    override fun refresh() {
        viewModelScope.launch {
            // 模拟多样式内容数据
            _multiList.value = listOf(
                ExampleMultiEntity.MultiBannerData(
                    listOf(
                        "https://t7.baidu.com/it/u=963301259,1982396977&fm=193&f=GIF",
                        "https://t7.baidu.com/it/u=737555197,308540855&fm=193&f=GIF",
                        "https://t7.baidu.com/it/u=3655946603,4193416998&fm=193&f=GIF",
                        "https://t7.baidu.com/it/u=12235476,3874255656&fm=193&f=GIF",
                        "http://f.sinaimg.cn/sinakd20220121s/96/w540h356/20220121/96c6-c994bc45927a02a9dc5379a9869ff202.gif"
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