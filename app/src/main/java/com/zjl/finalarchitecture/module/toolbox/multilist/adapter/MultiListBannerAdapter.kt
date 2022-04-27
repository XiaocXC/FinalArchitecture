package com.zjl.finalarchitecture.module.toolbox.multilist.adapter

import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedImageDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import coil.drawable.ScaleDrawable
import coil.load
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder
import com.zjl.finalarchitecture.R
import timber.log.Timber

/**
 * @author Xiaoc
 * @since 2022-02-02
 *
 * 多样式列表的Banner适配器
 */
class MultiListBannerAdapter: BaseBannerAdapter<String>() {

    override fun bindData(
        holder: BaseViewHolder<String>?,
        data: String?,
        position: Int,
        pageSize: Int
    ) {
        holder?.let {
            val ivBanner = it.findViewById<ImageView>(R.id.iv_banner)
            ivBanner.load(data)
        }
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.banner_item_article_top
    }
}