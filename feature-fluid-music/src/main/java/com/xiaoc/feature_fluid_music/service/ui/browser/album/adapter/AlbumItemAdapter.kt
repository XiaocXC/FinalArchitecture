package com.xiaoc.feature_fluid_music.service.ui.browser.album.adapter

import android.widget.ImageView
import androidx.media3.common.MediaItem
import coil.load
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.card.MaterialCardView
import com.xiaoc.feature_fluid_music.R
import com.xiaoc.feature_fluid_music.service.bean.UIMediaData

/**
 * @author Xiaoc
 * @since  2022-07-30
 *
 * 专辑Item Adapter
 **/
class AlbumItemAdapter: BaseQuickAdapter<MediaItem, BaseViewHolder>(
    R.layout.fluid_music_item_album
) {

    override fun convert(holder: BaseViewHolder, item: MediaItem) {
        holder.setText(R.id.tv_album_title, item.mediaMetadata.title)
        holder.setText(R.id.tv_album_subtitle, item.mediaMetadata.albumTitle)
        val ivThumbnailAlbumArt = holder.getView<ImageView>(R.id.iv_thumbnail_albumArt)
        ivThumbnailAlbumArt.load(item.mediaMetadata.artworkUri)
    }
}