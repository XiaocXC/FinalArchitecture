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
class ArticleAdapter: BaseQuickAdapter<ArticleListVO, BaseViewHolder>(R.layout.item_article_list_data) {
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
        item.run{
//                        binding.itemHomeAuthor.text = if(author.isNotEmpty()) author else shareUser
//                        binding.itemHomeContent.text = title.toHtml()
//                        binding.itemHomeType2.text = "$superChapterName·$chapterName"
//                        binding.itemHomeDate.text = niceDate
//                        binding.itemHomeNew.visibility = if(fresh) View.VISIBLE else View.GONE
//                        binding.itemHomeType1.text = niceDate
//                        if (tags.isNotEmpty()) {
//                            binding.itemHomeType1.visibility = View.VISIBLE
//                            binding.itemHomeType1.text = tags[0].name
//                        } else {
//                            binding.itemHomeType1.visibility = View.GONE
//                        }
            holder.setText(
                R.id.item_home_author,
                if (author.isNotEmpty()) author else shareUser
            )
            holder.setText(R.id.item_home_content, title.toHtml())
            holder.setText(R.id.item_home_type2, "$superChapterName·$chapterName".toHtml())
            holder.setText(R.id.item_home_date, niceDate)

//            if(fresh){
//                holder.setGone(R.id.item_home_new, false)
//            }else{
//                holder.setGone(R.id.item_home_new, true)
//            }
            holder.setGone(R.id.item_home_new, !fresh)


//            if(type == 1){
//                holder.setGone(R.id.item_home_new, false)
//            }else{
//                holder.setGone(R.id.item_home_new, true)
//            }
            holder.setGone(R.id.item_home_top, type != 1)

            if (tags.isNotEmpty()) {
                holder.setGone(R.id.item_home_type1, false)
                holder.setText(R.id.item_home_type1, tags[0].name)
            } else {
                holder.setGone(R.id.item_home_type1, true)
            }

        }
    }
}