package com.zjl.finalarchitecture.module.mine.ui.fragment.theme.adapter

import android.widget.ImageView
import coil.load
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.card.MaterialCardView
import com.zjl.base.utils.ext.dp
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.module.mine.ui.fragment.theme.ThemeUIState

class ThemeListAdapter: BaseQuickAdapter<ThemeUIState, BaseViewHolder>(
    R.layout.item_theme_preview
) {

    override fun convert(holder: BaseViewHolder, item: ThemeUIState) {
        holder.setText(R.id.themeTitle, item.title)
        val themeImage = holder.getView<ImageView>(R.id.themeImage)
        themeImage.load(item.previewUri){
            placeholder(R.drawable.base_ui_ic_empty)
            error(R.drawable.base_ui_ic_empty)
        }

        val containerThemePreview = holder.getView<MaterialCardView>(R.id.containerThemePreview)
        if(item.isCurrent){
            containerThemePreview.strokeWidth = 1.dp
        } else {
            containerThemePreview.strokeWidth = 0
        }
    }
}