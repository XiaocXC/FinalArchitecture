package com.xiaoc.feature_fluid_music.service.scanner

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import androidx.core.os.bundleOf
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.xiaoc.feature_fluid_music.service.tree.MediaItemTree
import java.util.concurrent.TimeUnit

/**
 * @author Xiaoc
 * @since 2020.12.25
 *
 * 基于MediaStore的本地扫描器
 * 使用 [android.provider.MediaStore] 媒体库，扫描其中的内容
 * 扫描媒体库中的所有歌曲内容
 */
class MediaStoreScanner constructor(
    private val context: Context
){

    companion object {
        /**
         * 兼容性字符串，因为安卓Q以下MediaStore不带
         */
        const val DURATION: String = "duration"
    }

    private val mediaProjection: Array<String> = arrayOf(
            Media._ID,
            Media.TITLE,
            Media.ALBUM_ID,
            Media.ALBUM,
            DURATION,
            Media.TRACK,
            Media.ARTIST_ID,
            Media.ARTIST,
            Media.YEAR,
            Media.DATA
    )

    private val albumProjection: Array<String> = arrayOf(
        MediaStore.Audio.Albums.ALBUM_ID,
        MediaStore.Audio.Albums.ALBUM,
        MediaStore.Audio.Albums.ARTIST,
        MediaStore.Audio.Albums.FIRST_YEAR,
        MediaStore.Audio.Albums.NUMBER_OF_SONGS
    )

    private val artistProjection = arrayOf(
        MediaStore.Audio.Artists._ID,
        MediaStore.Audio.Artists.ARTIST,
        MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
        MediaStore.Audio.Artists.NUMBER_OF_ALBUMS
    )

    private val mediaSelection = "${Media.IS_MUSIC} != 0 AND $DURATION > ?"

    /**
     * 检索规则中排除小于5秒的歌曲
     */
    private val mediaSelectionArgs = arrayOf(
        TimeUnit.MILLISECONDS.convert(5, TimeUnit.SECONDS).toString())

    /**
     * 从 MediaStore 媒体库中扫描所有音乐
     */
    fun scanAllFromMediaStore(parentId: String): List<MediaItem> {
        val cursor = context.contentResolver.query(
                Media.EXTERNAL_CONTENT_URI,
                mediaProjection,
                mediaSelection,
                mediaSelectionArgs,
                Media.DEFAULT_SORT_ORDER
        )

        return handleMediaMusicCursor(cursor, true, parentId)
    }

    private fun getAlbumUri(albumId: String): Uri{
        val artworkUri = Uri.parse("content://media/external/audio/albumart")
        return Uri.withAppendedPath(artworkUri,albumId)
    }

    /**
     * 从 MediaStore 媒体库中扫描专辑
     */
    fun scanAlbumFromMediaStore(parentId: String): List<MediaItem>{
        val cursor = context.contentResolver.query(
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
            albumProjection,
            null,
            null,
            MediaStore.Audio.Albums.DEFAULT_SORT_ORDER
        )
        return handleAlbumCursor(cursor, parentId)
    }

    /**
     * 从 MediaStore 媒体库中根据专辑Id扫描对应专辑的音乐
     * @param albumId 专辑ID
     */
    fun scanAlbumMusic(albumId: String, parentId: String): List<MediaItem>{
        val cursor = context.contentResolver.query(
            Media.EXTERNAL_CONTENT_URI,
            mediaProjection,
            "${Media.IS_MUSIC} != 0 AND $DURATION > ? AND ${Media.ALBUM_ID} = ?",
            arrayOf(
                TimeUnit.MILLISECONDS.convert(5, TimeUnit.SECONDS).toString(),albumId),
            "${Media.TRACK} ASC" /* 专辑歌曲默认按照曲目顺序排序 */
        )
        return handleMediaMusicCursor(cursor,false, parentId)
    }

    /**
     * 从 MediaStore 媒体库中根据歌手Id扫描对应歌手的音乐
     * @param artistId 歌手ID
     */
    fun scanArtistMusic(artistId: String, parentId: String): List<MediaItem>{
        val cursor = context.contentResolver.query(
            Media.EXTERNAL_CONTENT_URI,
            mediaProjection,
            "${Media.IS_MUSIC} != 0 AND $DURATION > ? AND ${Media.ARTIST_ID} = ?",
            arrayOf(
                TimeUnit.MILLISECONDS.convert(5, TimeUnit.SECONDS).toString(),artistId),
            Media.DEFAULT_SORT_ORDER /* 专辑歌曲默认按照曲目顺序排序 */
        )
        return handleMediaMusicCursor(cursor, true, parentId)
    }

    /**
     * 从 MediaStore 媒体库中根据歌手Id扫描歌手的专辑
     * @param artistId 歌手ID
     */
    fun scanArtistAlbum(artistId: String, parentId: String): List<MediaItem>{
        val cursor = context.contentResolver.query(
            MediaStore.Audio.Artists.Albums.getContentUri("external",artistId.toLong()),
            albumProjection,
            null,
            null,
            null
        )
        return handleAlbumCursor(cursor, parentId)
    }

    /**
     * 从 MediaStore 媒体库中扫描歌手
     */
    fun scanArtistFromMediaStore(parentId: String): List<MediaItem>{
        val artistList = mutableListOf<MediaItem>()
        val cursor = context.contentResolver.query(
            MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
            artistProjection,
            null,
            null,
            MediaStore.Audio.Artists.DEFAULT_SORT_ORDER
        )
        cursor?.use {
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST)
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID)
            val albumNumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS)
            val trackColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_TRACKS)

            while (it.moveToNext()){
                val id = it.getLong(idColumn)
                val artistName = it.getString(artistColumn)
                val albumNum = it.getLong(albumNumColumn)
                val trackNum = it.getInt(trackColumn)

                // 获得当前歌手的头像（取该歌手第一张专辑封面为头像）
                val albumId = getArtistAlbumArtByAlbumId(MediaStore.Audio.Artists.Albums.getContentUri("external",id))
                val artistAlbumArtUri = if(albumId != null){
                    getAlbumUri(albumId.toString())
                } else {
                    null
                }

                val extras = bundleOf(
                    MediaItemTree.KEY_METADATA_ARTIST_ID to id.toString()
                )
                val metadataBuilder = MediaMetadata.Builder().apply {
                    setTitle(artistName)
                    setArtist(artistName)
                    setArtworkUri(artistAlbumArtUri)
                    setTrackNumber(trackNum)
                    setFolderType(MediaMetadata.FOLDER_TYPE_ARTISTS)
                    setIsPlayable(false)
                    setExtras(extras)
                }

                val mediaId = Uri.parse(parentId).buildUpon().appendPath(id.toString()).build().toString()
                val mediaItem = MediaItem.Builder()
                    .setMediaId(mediaId)
                    .setMediaMetadata(metadataBuilder.build())
                    .setUri(artistAlbumArtUri)
                    .build()

                artistList.add(mediaItem)

            }
        }
        // 进行拼音排序
//        artistList.sortBy {
//            PinyinUtil.getPinyin(it.title)
//        }
        return artistList
    }

    private fun getArtistAlbumArtByAlbumId(albumsUri: Uri): Long?{
        val cursor = context.contentResolver.query(albumsUri,
            arrayOf(MediaStore.Audio.Artists.Albums.ALBUM_ID),null,null,null)
        cursor?.use {
            return if(it.moveToNext()){
                it.getLong(it.getColumnIndexOrThrow(MediaStore.Audio.Artists.Albums.ALBUM_ID))
            } else {
                null
            }
        }
        return null
    }

    private fun handleAlbumCursor(cursor: Cursor?, parentId: String): List<MediaItem>{
        val albumList = mutableListOf<MediaItem>()

        cursor?.use {
            val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ID)
            val albumTitleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST)
            val trackNumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.NUMBER_OF_SONGS)
            val yearColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.FIRST_YEAR)

            while (it.moveToNext()){
                val albumId = it.getLong(albumIdColumn)
                val albumTitle = it.getString(albumTitleColumn)
                val artist = it.getString(artistColumn)
                val trackNum = it.getInt(trackNumColumn)
                val year = it.getInt(yearColumn)

                val extras = bundleOf(
                    MediaItemTree.KEY_METADATA_ALBUM_ID to albumId
                )
                val metadataBuilder = MediaMetadata.Builder().apply {
                    setTitle(albumTitle)
                    setArtist(artist)
                    setReleaseYear(year)
                    setAlbumTitle(albumTitle)
                    setTrackNumber(trackNum)
                    setFolderType(MediaMetadata.FOLDER_TYPE_ALBUMS)
                    setIsPlayable(false)
                    setExtras(extras)
                }

                val albumUri = getAlbumUri(albumId.toString())
                metadataBuilder.setArtworkUri(albumUri)

                val mediaId = Uri.parse(parentId).buildUpon().appendPath(albumId.toString()).build().toString()
                val mediaItem = MediaItem.Builder()
                    .setMediaId(mediaId)
                    .setMediaMetadata(metadataBuilder.build())
                    .setUri(albumUri)
                    .build()

                albumList.add(mediaItem)
            }

        }
        // 进行拼音排序
//        albumList.sortBy {
//            PinyinUtil.getPinyin(it.title)
//        }
        return albumList
    }

    private fun handleMediaMusicCursor(cursor: Cursor?, handlePinyin: Boolean = true, parentId: String): List<MediaItem>{
        val localMusicList = mutableListOf<MediaItem>()
        cursor?.use {
            val colId = it.getColumnIndexOrThrow(Media._ID)
            // 歌曲标题
            val colTitle = it.getColumnIndexOrThrow(Media.TITLE)
            val colAlbum = it.getColumnIndexOrThrow(Media.ALBUM)
            val colAlbumId = it.getColumnIndexOrThrow(Media.ALBUM_ID)
            val colArtist = it.getColumnIndexOrThrow(Media.ARTIST)
            val colArtistId = it.getColumnIndexOrThrow(Media.ARTIST_ID)
            val colDuration = it.getColumnIndexOrThrow(DURATION)
            val colTrack = it.getColumnIndexOrThrow(Media.TRACK)
            val colData = it.getColumnIndexOrThrow(Media.DATA)

            while (it.moveToNext()) {
                val id = it.getLong(colId)
                val title = it.getString(colTitle)
                val album = it.getString(colAlbum)
                val artist = it.getString(colArtist)
                val duration = it.getLong(colDuration)
                val track = it.getInt(colTrack)
                val albumId = it.getLong(colAlbumId)
                val artistId = it.getLong(colArtistId)
                val data = it.getString(colData)

                val musicUri: Uri = ContentUris.withAppendedId(Media.EXTERNAL_CONTENT_URI, id)

                val extras = bundleOf(
                    MediaItemTree.KEY_METADATA_DURATION to duration,
                    MediaItemTree.KEY_METADATA_ALBUM_ID to albumId,
                    MediaItemTree.KEY_METADATA_ARTIST_ID to artistId
                )
                val metadataBuilder = MediaMetadata.Builder().apply {
                    setTitle(title)
                    setArtist(artist)
                    setAlbumTitle(album)
                    setAlbumArtist(artist)
                    setTrackNumber(track)
                    setExtras(extras)
                    setIsPlayable(true)
                    setFolderType(MediaMetadata.FOLDER_TYPE_NONE)
                }

                val albumUri = getAlbumUri(albumId.toString())
                metadataBuilder.setArtworkUri(albumUri)

                val mediaId = Uri.parse(parentId).buildUpon().appendPath(id.toString()).build().toString()
                val mediaItem = MediaItem.Builder()
                    .setMediaId(mediaId)
                    .setMediaMetadata(metadataBuilder.build())
                    .setUri(musicUri)
                    .build()

                localMusicList.add(mediaItem)
            }
        }
//        if(handlePinyin){
//            // 进行拼音排序
//            localMusicList.sortBy {
//                PinyinUtil.getPinyin(it.title)
//            }
//        }
        return localMusicList
    }


}