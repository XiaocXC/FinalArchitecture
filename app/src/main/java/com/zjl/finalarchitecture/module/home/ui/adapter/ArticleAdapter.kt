package com.zjl.finalarchitecture.module.home.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.DifferCallback
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.zjl.base.adapter.ViewBoundPagingListAdapter
import com.zjl.base.adapter.ViewBoundViewHolder
import com.zjl.finalarchitecture.databinding.ItemArticleListDataBinding
import com.zjl.finalarchitecture.module.home.model.ArticleListVO

/**
 * @author Xiaoc
 * @since 2022-02-02
 */
class ArticleAdapter: ViewBoundPagingListAdapter<ArticleListVO,ItemArticleListDataBinding>(
    object: DiffUtil.ItemCallback<ArticleListVO>(){
        override fun areItemsTheSame(oldItem: ArticleListVO, newItem: ArticleListVO): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ArticleListVO, newItem: ArticleListVO): Boolean {
            return oldItem.title == newItem.title
        }

    }
) {
    override fun createBinding(
        parent: ViewGroup,
        viewType: Int
    ): ViewBoundViewHolder<ArticleListVO, ItemArticleListDataBinding>? {
        return ViewBoundViewHolder(
            ItemArticleListDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun bind(
        binding: ItemArticleListDataBinding,
        item: ArticleListVO,
        position: Int,
        payloads: MutableList<Any>?
    ) {
        binding.tvTitle.text = item.title
    }
}