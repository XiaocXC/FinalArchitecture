package com.xiaoc.feature_fluid_music.service.callback

import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.google.common.util.concurrent.ListenableFuture

/**
 * @author Xiaoc
 * @since 2022-07-28
 */
class MediaLibrarySessionCallbackImpl(
    private val player: ExoPlayer
): MediaLibraryService.MediaLibrarySession.Callback {

    override fun onGetLibraryRoot(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<MediaItem>> {
        return super.onGetLibraryRoot(session, browser, params)
    }
}