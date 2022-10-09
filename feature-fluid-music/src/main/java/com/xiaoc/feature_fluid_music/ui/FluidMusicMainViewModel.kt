package com.xiaoc.feature_fluid_music.ui

import android.graphics.drawable.BitmapDrawable
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.xiaoc.feature_fluid_music.connection.MusicServiceConnection
import com.zjl.base.globalApplication
import com.zjl.base.globalContext
import com.zjl.base.utils.ext.isNightMode
import com.zjl.base.utils.materialcolor.ColorContainerData
import com.zjl.base.utils.materialcolor.PaletteUtils
import com.zjl.base.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * @author Xiaoc
 * @since  2022-07-30
 **/
class FluidMusicMainViewModel: BaseViewModel() {

    private val musicServiceConnection = MusicServiceConnection.getInstance(globalContext)

    private val _currentPlayInfo = MutableStateFlow<MediaItem?>(null)
    val currentPlayInfo: StateFlow<MediaItem?> = _currentPlayInfo

    val currentPlaybackState = musicServiceConnection.playbackState

    private val _musicColor = MutableStateFlow<ColorContainerData?>(null)
    val musicColor: StateFlow<ColorContainerData?> = _musicColor

    init {
        initData()
    }

    override fun refresh() {
        viewModelScope.launch {
            musicServiceConnection.nowPlaying.collectLatest {
                _currentPlayInfo.value = it

                Timber.d("FluidMusic musicChanged", it.mediaMetadata.title)
                // 1.获得图片Bitmap
                val request = ImageRequest.Builder(globalApplication)
                    .size(80)
                    .allowHardware(false)
                    .data(it.mediaMetadata.artworkUri)
                    .build()

                val result = globalApplication.imageLoader.execute(request)
                Timber.d("FluidMusic musicColor-Uri: %s", it.mediaMetadata.artworkUri)
                if(result !is SuccessResult){
                    _musicColor.value = null
                    return@collectLatest
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
                Timber.d("FluidMusic musicColor: %s", colorData)

                _musicColor.value = colorData
            }
        }
    }
}