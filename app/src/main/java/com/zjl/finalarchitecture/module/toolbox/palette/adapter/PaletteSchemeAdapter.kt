package com.zjl.finalarchitecture.module.toolbox.palette.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.module.toolbox.palette.PaletteViewModel

/**
 * @author Xiaoc
 * @since 2022-12-02
 *
 * 色调Adapter
 */
class PaletteSchemeAdapter: BaseQuickAdapter<PaletteViewModel.PaletteData, BaseViewHolder>(
    R.layout.item_palette_card
) {

    override fun convert(holder: BaseViewHolder, item: PaletteViewModel.PaletteData) {
        holder.setText(R.id.tvSubtitle, item.subtitle)
        holder.setBackgroundColor(R.id.primary, item.scheme.primary)
            .setTextColor(R.id.tvPrimary, item.scheme.onPrimary)
            .setBackgroundColor(R.id.primaryContainer, item.scheme.primaryContainer)
            .setTextColor(R.id.tvPrimaryContainer, item.scheme.onPrimaryContainer)
            .setBackgroundColor(R.id.secondary, item.scheme.secondary)
            .setTextColor(R.id.tvSecondary, item.scheme.onSecondary)
            .setBackgroundColor(R.id.SecondaryContainer, item.scheme.secondaryContainer)
            .setTextColor(R.id.tvSecondaryContainer, item.scheme.onSecondaryContainer)
            .setBackgroundColor(R.id.Tertiary, item.scheme.tertiary)
            .setTextColor(R.id.tvTertiary, item.scheme.onTertiary)
            .setBackgroundColor(R.id.TertiaryContainer, item.scheme.tertiaryContainer)
            .setTextColor(R.id.tvTertiaryContainer, item.scheme.onTertiaryContainer)
            .setBackgroundColor(R.id.Error, item.scheme.error)
            .setTextColor(R.id.tvError, item.scheme.onError)
            .setBackgroundColor(R.id.ErrorContainer, item.scheme.errorContainer)
            .setTextColor(R.id.tvErrorContainer, item.scheme.onErrorContainer)
            .setBackgroundColor(R.id.Background, item.scheme.background)
            .setTextColor(R.id.tvBackground, item.scheme.onBackground)
            .setBackgroundColor(R.id.Surface, item.scheme.surface)
            .setTextColor(R.id.tvSurface, item.scheme.onSurface)
    }
}