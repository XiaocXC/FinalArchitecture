package com.xiaoc.feature_fluid_music.service.tree

import android.content.Context
import android.content.UriMatcher
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.xiaoc.feature_fluid_music.service.scanner.MediaStoreScanner

/**
 * @author Xiaoc
 * @since 2022-07-29
 *
 * MediaItem存储树
 * 这里的数据按照规定格式存储，具体遵循以下规则：
 * 根订阅路径：fluidmusic://file/root
 * 全部歌曲：fluidmusic://file/music
 * 全部专辑：fluidmusic://file/album
 * 全部歌手：fluidmusic://file/artist
 * 某张专辑的歌曲：fluidmusic://file/album/albumId
 * 某位歌手的歌曲：fluidmusic://file/artist/artistId/music
 * 某位歌手的专辑：fluidmusic://file/artist/artistId/album
 */
class MediaItemTree(
    private val context: Context
) {

    companion object {
        const val TREE_FILE_AUTHORITY = "file"
        const val TREE_FILE_URI_HEADER = "fluidmusic://$TREE_FILE_AUTHORITY"

        const val TREE_PATH_ROOT = "root"
        const val TREE_PATH_MUSIC = "music"
        const val TREE_PATH_ALBUM = "album"
        const val TREE_PATH_ARTIST = "artist"

        private const val CODE_ROOT = 1
        private const val CODE_ALL_MUSIC = 2
        private const val CODE_ALL_ALBUM = 3
        private const val CODE_ALL_ARTIST = 4
        private const val CODE_MUSIC_BY_ALBUM = 5
        private const val CODE_MUSIC_BY_ARTIST = 6
        private const val CODE_ALBUM_BY_ARTIST = 7

        const val KEY_METADATA_DURATION = "duration"
        const val KEY_METADATA_ALBUM_ID = "albumId"
        const val KEY_METADATA_ARTIST_ID = "artistId"
    }

    private val treeUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    private val mediaStoreScanner = MediaStoreScanner(context)

    init {
        // 添加不同的Uri匹配器
        treeUriMatcher.addURI(TREE_FILE_AUTHORITY, TREE_PATH_ROOT, CODE_ROOT)
        treeUriMatcher.addURI(TREE_FILE_AUTHORITY, TREE_PATH_MUSIC, CODE_ALL_MUSIC)
        treeUriMatcher.addURI(TREE_FILE_AUTHORITY, TREE_PATH_ALBUM, CODE_ALL_ALBUM)
        treeUriMatcher.addURI(TREE_FILE_AUTHORITY, TREE_PATH_ARTIST, CODE_ALL_ARTIST)
        treeUriMatcher.addURI(TREE_FILE_AUTHORITY, "$TREE_PATH_ALBUM/#", CODE_MUSIC_BY_ALBUM)
        treeUriMatcher.addURI(TREE_FILE_AUTHORITY, "$TREE_PATH_ARTIST/#/$TREE_PATH_MUSIC", CODE_MUSIC_BY_ARTIST)
        treeUriMatcher.addURI(TREE_FILE_AUTHORITY, "$TREE_PATH_ARTIST/#/$TREE_PATH_ALBUM", CODE_ALBUM_BY_ARTIST)
    }

    private val treeMediaItemMap = mutableMapOf<String, List<MediaItem>>()

    /**
     * 通过 parentId 拿到对应parentId下的媒体数据列表
     * @param parentId 父Id
     * @return 对应父Id下的媒体数据集
     */
    @Throws(IllegalArgumentException::class)
    fun getMediaItemByParentId(parentId: String): List<MediaItem> {
        val data = treeMediaItemMap[parentId]
        if(data != null){
            return data
        }
        val parentIdUri = Uri.parse(parentId)
        val code = treeUriMatcher.match(parentIdUri)
        val scanResult =  when(code){
            CODE_ROOT ->{
                generateRootChildren()
            }
            CODE_ALL_MUSIC ->{
                // 所有音乐
                mediaStoreScanner.scanAllFromMediaStore(parentId)
            }
            CODE_ALL_ALBUM ->{
                mediaStoreScanner.scanAlbumFromMediaStore(parentId)
            }
            CODE_ALL_ARTIST ->{
                mediaStoreScanner.scanArtistFromMediaStore(parentId)
            }
            CODE_MUSIC_BY_ALBUM ->{
                mediaStoreScanner.scanAlbumMusic(parentIdUri.lastPathSegment ?: "", parentId)
            }
//            CODE_ALBUM_BY_ARTIST ->{
//                mediaStoreScanner.scanAlbumMusic(parentIdUri.lastPathSegment ?: "", parentId)
//            }
//            CODE_MUSIC_BY_ARTIST ->{
//
//            }
            else ->{
                throw IllegalArgumentException("不支持的类型")
            }
        }
        treeMediaItemMap[parentId] = scanResult
        return scanResult
    }

    /**
     * 得到根媒体订阅数据
     */
    fun getRootMediaItem(): MediaItem{
        val metadata = MediaMetadata.Builder()
            .setTitle(TREE_PATH_ROOT)
            .setFolderType(MediaMetadata.FOLDER_TYPE_MIXED)
            .setIsPlayable(false)
            .build()
        return MediaItem.Builder()
            .setMediaId("$TREE_FILE_URI_HEADER/$TREE_PATH_ROOT")
            .setMediaMetadata(metadata)
            .build()
    }

    private fun generateRootChildren(): List<MediaItem>{
        val allMusicMetadata = MediaMetadata.Builder()
            .setTitle(TREE_PATH_MUSIC)
            .setFolderType(MediaMetadata.FOLDER_TYPE_TITLES)
            .setIsPlayable(false)
            .build()
        val allAlbumMetadata = MediaMetadata.Builder()
            .setTitle(TREE_PATH_ALBUM)
            .setFolderType(MediaMetadata.FOLDER_TYPE_ALBUMS)
            .setIsPlayable(false)
            .build()
        val allArtistMetadata = MediaMetadata.Builder()
            .setTitle(TREE_PATH_ARTIST)
            .setFolderType(MediaMetadata.FOLDER_TYPE_ARTISTS)
            .setIsPlayable(false)
            .build()

        return listOf(
            MediaItem.Builder()
                .setMediaId("$TREE_FILE_URI_HEADER/$TREE_PATH_MUSIC")
                .setMediaMetadata(allMusicMetadata)
                .build(),

            MediaItem.Builder()
                .setMediaId("$TREE_FILE_URI_HEADER/$TREE_PATH_ALBUM")
                .setMediaMetadata(allAlbumMetadata)
                .build(),

            MediaItem.Builder()
                .setMediaId("$TREE_FILE_URI_HEADER/$TREE_PATH_ARTIST")
                .setMediaMetadata(allArtistMetadata)
                .build()
        )
    }

}