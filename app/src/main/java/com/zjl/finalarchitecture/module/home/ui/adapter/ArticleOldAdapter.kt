package com.zjl.finalarchitecture.module.home.ui.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.zjl.base.adapter.ViewBoundPagingListAdapter
import com.zjl.base.adapter.ViewBoundViewHolder
import com.zjl.base.utils.ext.toHtml
import com.zjl.finalarchitecture.databinding.ItemArticleListDataBinding
import com.zjl.finalarchitecture.data.model.ArticleListVO

/**
 * @author Xiaoc
 * @since 2022-02-02
 */
@Deprecated("基于Paging3的Adapter目前已放弃使用")
class ArticleOldAdapter(
    val collectClicked: (ArticleListVO) -> Unit = {}
): ViewBoundPagingListAdapter<ArticleListVO, ItemArticleListDataBinding>(
    diffCallback = object: DiffUtil.ItemCallback<ArticleListVO>(){
        override fun areItemsTheSame(oldItem: ArticleListVO, newItem: ArticleListVO): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ArticleListVO, newItem: ArticleListVO): Boolean {
            return oldItem.title == newItem.title && oldItem.collect == newItem.collect
        }
    }) {

    override fun createBinding(
        parent: ViewGroup,
        viewType: Int
    ): ViewBoundViewHolder<ArticleListVO, ItemArticleListDataBinding> {
        return ViewBoundViewHolder<ArticleListVO, ItemArticleListDataBinding>(
            ItemArticleListDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ).apply {
            binding.itemHomeCollect.setOnClickListener {
                collectClicked(item)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun bind(
        binding: ItemArticleListDataBinding,
        item: ArticleListVO,
        position: Int,
        payloads: MutableList<Any>?
    ) {

        item.run{
            binding.itemHomeAuthor.text = if(author.isNotEmpty()) author else shareUser
            binding.itemHomeContent.text = title.toHtml()
            binding.itemHomeType2.text = "$superChapterName·$chapterName"
            binding.itemHomeDate.text = niceDate
            binding.itemHomeNew.visibility = if(fresh) View.VISIBLE else View.GONE
            binding.itemHomeType1.text = niceDate

            if(type == 1){
                binding.itemHomeTop.visibility = View.VISIBLE
            }else{
                binding.itemHomeTop.visibility = View.GONE
            }

            if (tags.isNotEmpty()) {
                binding.itemHomeType1.visibility = View.VISIBLE
                binding.itemHomeType1.text = tags[0].name
            } else {
                binding.itemHomeType1.visibility = View.GONE
            }

            // 是否点赞
            if(collect){
                binding.itemHomeCollect.imageTintList = ColorStateList.valueOf(Color.RED)
            } else {
                binding.itemHomeCollect.imageTintList = null
            }
        }

    }
}