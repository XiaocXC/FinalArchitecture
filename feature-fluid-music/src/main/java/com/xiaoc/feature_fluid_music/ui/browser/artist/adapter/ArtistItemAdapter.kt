package com.xiaoc.feature_fluid_music.ui.browser.artist.adapter

import android.widget.ImageView
import androidx.media3.common.MediaItem
import coil.load
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xiaoc.feature_fluid_music.R

/**
 * @author Xiaoc
 * @since  2022-07-30
 *
 * 专辑Item Adapter
 **/
class ArtistItemAdapter: BaseQuickAdapter<MediaItem, BaseViewHolder>(
    R.layout.fluid_music_item_artist
) {

    override fun convert(holder: BaseViewHolder, item: MediaItem) {
        holder.setText(R.id.tv_artist_title, item.mediaMetadata.title)
        val ivThumbnailAlbumArt = holder.getView<ImageView>(R.id.iv_thumbnail_albumArt)
        ivThumbnailAlbumArt.load(item.mediaMetadata.artworkUri)
    }
}