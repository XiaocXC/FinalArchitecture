package com.xiaoc.feature_fluid_music.service.ui.browser.music.adapter

import androidx.media3.common.MediaItem
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xiaoc.feature_fluid_music.R

/**
 * @author Xiaoc
 * @since  2022-07-30
 *
 * 音乐Item Adapter
 **/
class MusicItemAdapter: BaseQuickAdapter<MediaItem, BaseViewHolder>(
    R.layout.fluid_music_item_music
) {

    override fun convert(holder: BaseViewHolder, item: MediaItem) {
        holder.setText(R.id.tv_music_title, item.mediaMetadata.title)
    }
}