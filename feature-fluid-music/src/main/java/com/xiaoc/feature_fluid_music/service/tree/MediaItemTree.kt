package com.xiaoc.feature_fluid_music.service.tree

import android.content.UriMatcher
import androidx.media3.common.MediaItem

/**
 * @author Xiaoc
 * @since 2022-07-29
 *
 * MediaItem存储树
 * 这里的数据按照规定格式存储，具体遵循以下规则：
 * 根订阅路径：content://fluidmusic.file/root
 * 全部歌曲：content://fluidmusic.file/music
 * 全部专辑：content://fluidmusic.file/album
 * 全部歌手：content://fluidmusic.file/artist
 * 某张专辑的歌曲：content://fluidmusic.file/album/albumId
 * 某位歌手的歌曲：content://fluidmusic.file/artist/artistId/music
 * 某位歌手的专辑：content://fluidmusic.file/artist/artistId/album
 */
class MediaItemTree {

    companion object {
        private const val TREE_AUTHORITY = "fluidmusic.file"

        private const val CODE_ROOT = 1
        private const val CODE_ALL_MUSIC = 2
        private const val CODE_ALL_ALBUM = 3
        private const val CODE_ALL_ARTIST = 4
        private const val CODE_MUSIC_BY_ALBUM = 5
        private const val CODE_MUSIC_BY_ARTIST = 6
        private const val CODE_ALBUM_BY_ARTIST = 7
    }

    private val artUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    init {
        // 添加不同的Uri匹配器
        artUriMatcher.addURI(TREE_AUTHORITY, "root", CODE_ROOT)
        artUriMatcher.addURI(TREE_AUTHORITY, "music", CODE_ALL_MUSIC)
        artUriMatcher.addURI(TREE_AUTHORITY, "album", CODE_ALL_ALBUM)
        artUriMatcher.addURI(TREE_AUTHORITY, "artist", CODE_ALL_ARTIST)
        artUriMatcher.addURI(TREE_AUTHORITY, "album/#", CODE_MUSIC_BY_ALBUM)
        artUriMatcher.addURI(TREE_AUTHORITY, "artist/#/music", CODE_MUSIC_BY_ARTIST)
        artUriMatcher.addURI(TREE_AUTHORITY, "artist/#/album", CODE_ALBUM_BY_ARTIST)
    }

    /**
     * 通过 parentId 拿到对应parentId下的媒体数据列表
     * @param parentId 父Id
     * @return 对应父Id下的媒体数据集
     */
    fun getMediaItemByParentId(parentId: String): List<MediaItem> {

    }

    fun getRootMediaItem(): MediaItem{

    }
}