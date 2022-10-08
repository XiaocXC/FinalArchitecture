package com.xiaoc.feature_fluid_music.ui.browser.music.adapter

import android.widget.ImageView
import coil.load
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.card.MaterialCardView
import com.xiaoc.feature_fluid_music.R
import com.xiaoc.feature_fluid_music.ui.bean.UIMediaData

/**
 * @author Xiaoc
 * @since  2022-07-30
 *
 * 音乐Item Adapter
 **/
class MusicItemAdapter: BaseQuickAdapter<UIMediaData, BaseViewHolder>(
    R.layout.fluid_music_item_music
) {

    override fun convert(holder: BaseViewHolder, item: UIMediaData) {
        holder.setText(R.id.tv_music_title, item.mediaItem.mediaMetadata.title)
        holder.setText(R.id.tv_music_subtitle, item.mediaItem.mediaMetadata.albumTitle)
        val ivThumbnailAlbumArt = holder.getView<ImageView>(R.id.iv_thumbnail_albumArt)
        ivThumbnailAlbumArt.load(item.mediaItem.mediaMetadata.artworkUri)

        updatePlayingState(holder, item.isPlaying)
    }

    override fun convert(holder: BaseViewHolder, item: UIMediaData, payloads: List<Any>) {
        if(payloads.isEmpty()){
            return
        }

        payloads.forEach {
            if(it is Boolean){
                updatePlayingState(holder, it)
            }
        }
    }

    private fun updatePlayingState(holder: BaseViewHolder, isPlaying: Boolean){
        val containerMusicItem = holder.getView<MaterialCardView>(R.id.containerMusicItem)
        containerMusicItem.isChecked = isPlaying
    }
}