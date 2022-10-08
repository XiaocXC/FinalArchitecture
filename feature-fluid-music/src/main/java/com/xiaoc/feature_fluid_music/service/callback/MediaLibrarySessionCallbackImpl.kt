package com.xiaoc.feature_fluid_music.service.callback

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.xiaoc.feature_fluid_music.service.tree.MediaItemTree

/**
 * @author Xiaoc
 * @since 2022-07-28
 */
class MediaLibrarySessionCallbackImpl(
    private val player: ExoPlayer,
    private val context: Context
): MediaLibraryService.MediaLibrarySession.Callback {

    private val mediaItemTree = MediaItemTree(context)

    override fun onGetLibraryRoot(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<MediaItem>> {
        val libraryResult = LibraryResult.ofItem(mediaItemTree.getRootMediaItem(), params)
        return Futures.immediateFuture(libraryResult)
    }

    override fun onGetChildren(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        parentId: String,
        page: Int,
        pageSize: Int,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
        return try {
            val data = mediaItemTree.getMediaItemByParentId(parentId)
            Futures.immediateFuture(LibraryResult.ofItemList(data, params))
        } catch (e: IllegalArgumentException){
            Futures.immediateFuture(LibraryResult.ofError(LibraryResult.RESULT_ERROR_BAD_VALUE))
        }
    }

    override fun onAddMediaItems(
        mediaSession: MediaSession,
        controller: MediaSession.ControllerInfo,
        mediaItems: MutableList<MediaItem>
    ): ListenableFuture<MutableList<MediaItem>> {
        val withUriMediaItems = mutableListOf<MediaItem>()
        mediaItems.forEach { noUriMedia ->
            val mediaIdUri = Uri.parse(noUriMedia.mediaId)
            val paths = mediaIdUri.pathSegments
            val resultParentIdUri = mediaIdUri.buildUpon().path(null)
            for(i in 0 until paths.size - 1){
                resultParentIdUri.appendPath(paths[i])
            }
            val withUriMediaItem = mediaItemTree.getMediaItemByParentId(resultParentIdUri.build().toString()).find {
                noUriMedia.mediaId == it.mediaId
            }
            if(withUriMediaItem != null){
                withUriMediaItems.add(withUriMediaItem)
            }
        }
        return Futures.immediateFuture(withUriMediaItems)
    }

    override fun onGetItem(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        mediaId: String
    ): ListenableFuture<LibraryResult<MediaItem>> {
        return super.onGetItem(session, browser, mediaId)
    }

}