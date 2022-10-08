package com.xiaoc.feature_fluid_music.ui.bean

import androidx.media3.common.MediaItem

/**
 * @author Xiaoc
 * @since  2022-08-01
 **/
data class UIMediaData(
    val mediaItem: MediaItem,
    val isPlaying: Boolean
)