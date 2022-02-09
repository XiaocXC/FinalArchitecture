package com.zjl.finalarchitecture.module.home.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zjl.base.utils.ext.toHtml
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.module.home.model.ArticleListVO

/**
 * @author Xiaoc
 * @since 2022-02-02
 */
class ArticleAdapter: BaseQuickAdapter<ArticleListVO, BaseViewHolder>(
    R.layout.item_article_list_data
) {
//    override fun createBinding(
//        parent: ViewGroup,
//        viewType: Int
//    ): ViewBoundViewHolder<ArticleListVO, ItemArticleListDataBinding>? {
//        return ViewBoundViewHolder(
//            ItemArticleListDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        )
//    }
//
//    @SuppressLint("SetTextI18n")
//    override fun bind(
//        binding: ItemArticleListDataBinding,
//        item: ArticleListVO,
//        position: Int,
//        payloads: MutableList<Any>?
//    ) {
////        binding.tvTitle.text = item.title
////        binding.itemHomeAuthor.text = if(item.author.isNotEmpty()) author else
//
//        item.run{
//            binding.itemHomeAuthor.text = if(author.isNotEmpty()) author else shareUser
//            binding.itemHomeContent.text = title.toHtml()
//            binding.itemHomeType2.text = "$superChapterName·$chapterName"
//            binding.itemHomeDate.text = niceDate
//            binding.itemHomeNew.visibility = if(fresh) View.VISIBLE else View.GONE
//            binding.itemHomeType1.text = niceDate
//            if (tags.isNotEmpty()) {
//                binding.itemHomeType1.visibility = View.VISIBLE
//                binding.itemHomeType1.text = tags[0].name
//            } else {
//                binding.itemHomeType1.visibility = View.GONE
//            }
//
//        }
//
//    }

    override fun convert(holder: BaseViewHolder, item: ArticleListVO) {
        holder.setText(R.id.item_home_content, item.title.toHtml())
    }
}