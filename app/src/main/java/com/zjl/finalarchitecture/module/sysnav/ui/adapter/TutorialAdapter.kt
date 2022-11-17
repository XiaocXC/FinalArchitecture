package com.zjl.finalarchitecture.module.sysnav.ui.adapter

import android.widget.ImageView
import coil.load
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zjl.base.utils.ext.toHtml
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.data.model.TutorialVO

/**
 * @description:教程RV列表适配器
 * @author: zhou
 * @date : 2022/11/16 17:22
 */
class TutorialAdapter : BaseQuickAdapter<TutorialVO, BaseViewHolder>(R.layout.item_tutorial) {
    override fun convert(holder: BaseViewHolder, item: TutorialVO) {
        //左侧图片封面
        holder.getView<ImageView>(R.id.sivCover).load(item.cover)
        //标题,作者,简洁
        holder.setText(R.id.tvTitle, item.name)
        holder.setText(R.id.tvAuthor, item.author)
        holder.setText(R.id.tvContent, item.desc)
    }
}