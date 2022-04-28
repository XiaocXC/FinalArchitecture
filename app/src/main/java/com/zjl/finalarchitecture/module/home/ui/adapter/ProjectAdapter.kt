package com.zjl.finalarchitecture.module.home.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import coil.load
import com.zjl.base.adapter.ViewBoundPagingListAdapter
import com.zjl.base.adapter.ViewBoundViewHolder
import com.zjl.base.utils.ext.toHtml
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.ItemArticleListDataBinding
import com.zjl.finalarchitecture.data.model.ArticleListVO
import com.zjl.finalarchitecture.databinding.ItemProjectBinding


class ProjectAdapter : ViewBoundPagingListAdapter<ArticleListVO, ItemProjectBinding>(
    diffCallback = object : DiffUtil.ItemCallback<ArticleListVO>() {
        override fun areItemsTheSame(oldItem: ArticleListVO, newItem: ArticleListVO): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ArticleListVO, newItem: ArticleListVO): Boolean {
            return oldItem.title == newItem.title
        }
    }) {

    override fun createBinding(
        parent: ViewGroup,
        viewType: Int
    ): ViewBoundViewHolder<ArticleListVO, ItemProjectBinding> {
        return ViewBoundViewHolder(
            ItemProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun bind(
        binding: ItemProjectBinding,
        item: ArticleListVO,
        position: Int,
        payloads: MutableList<Any>?
    ) {

        item.run {
            binding.itemProjectAuthor.text = if (author.isNotEmpty()) author else shareUser


            if (type == 1) {
                binding.itemProjectTop.visibility = View.VISIBLE
            } else {
                binding.itemProjectTop.visibility = View.GONE
            }

            binding.itemProjectNew.visibility = if (fresh) View.VISIBLE else View.GONE

            if (tags.isNotEmpty()) {
                binding.itemProjectType1.visibility = View.VISIBLE
                binding.itemProjectType1.text = tags[0].name
            } else {
                binding.itemProjectType1.visibility = View.GONE
            }

            binding.itemProjectDate.text = niceDate

            binding.itemProjectImageview.load(envelopePic) {
                placeholder(R.drawable.default_project_img)
            }
            binding.itemProjectTitle.text = title.toHtml()
            binding.itemProjectContent.text = desc.toHtml()
            binding.itemProjectType2.text = "$superChapterName·$chapterName".toHtml()



        }

    }
}