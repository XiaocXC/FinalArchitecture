package com.zjl.finalarchitecture.module.home.ui.adapter

import android.widget.ImageView
import coil.load
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zjl.base.utils.ext.toHtml
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.data.model.ArticleListVO


class ProjectAdapter : BaseQuickAdapter<ArticleListVO, BaseViewHolder>(
    R.layout.item_project
), LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: ArticleListVO) {
        item.run {
            holder.setText(R.id.item_project_author, if (author.isNotEmpty()) author else shareUser)
            holder.setGone(R.id.item_project_top, type != 1)
            holder.setGone(R.id.item_project_top, !fresh)
            if (tags.isNotEmpty()) {
                holder.setGone(R.id.item_project_type1, false)
                holder.setText(R.id.item_project_type1, tags[0].name)
            } else {
                holder.setGone(R.id.item_project_type1, true)
            }

            holder.setText(R.id.item_project_date, niceDate)
            val itemProjectImageView = holder.getView<ImageView>(R.id.item_project_imageview)

            if (envelopePic.isEmpty()) {
                itemProjectImageView.load(R.drawable.test)
            }else{
                itemProjectImageView.load(envelopePic) {
                    placeholder(R.drawable.test)
                }
            }



            holder.setText(R.id.item_project_title, title.toHtml())
            holder.setText(R.id.item_project_content, desc.toHtml())
            holder.setText(R.id.item_project_type2, "$superChapterName·$chapterName".toHtml())

        }
    }
}