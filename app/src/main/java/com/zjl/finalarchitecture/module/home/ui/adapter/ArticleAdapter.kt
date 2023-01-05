package com.zjl.finalarchitecture.module.home.ui.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zjl.base.utils.ext.toHtml
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.data.model.ArticleListVO

/**
 * @author Xiaoc
 * @since 2022-02-02
 */
class ArticleAdapter: BaseQuickAdapter<ArticleListVO, BaseViewHolder>(R.layout.item_article_list_data), LoadMoreModule {

    override fun convert(
        holder: BaseViewHolder,
        item: ArticleListVO
    ) {
        item.run{
            holder.setText(R.id.item_home_author, if(author.isNotEmpty()) author else shareUser)
            holder.setText(R.id.item_home_content, title.toHtml())
            holder.setText(R.id.item_home_type2, "$superChapterName·$chapterName")
            holder.setText(R.id.item_home_date, niceDate)
            holder.setGone(R.id.item_home_new, !fresh)
            holder.setText(R.id.item_home_type1, niceDate)

            holder.setGone(R.id.item_home_top, type != 1)

            holder.setGone(R.id.item_home_type1, tags.isEmpty())
            if (tags.isNotEmpty()) {
                holder.setGone(R.id.item_home_type1, false)
                holder.setText(R.id.item_home_type1, tags[0].name)
            } else {
                holder.setGone(R.id.item_home_type1, true)
            }

            val itemHomeCollect = holder.getView<ImageView>(R.id.item_home_collect)
            // 是否点赞
            if(collect){
                itemHomeCollect.imageTintList = ColorStateList.valueOf(Color.RED)
            } else {
                itemHomeCollect.imageTintList = null
            }
        }
    }
}