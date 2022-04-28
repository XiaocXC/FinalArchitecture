package com.zjl.finalarchitecture.module.home.ui.adapter

import android.widget.CheckedTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zhpan.bannerview.BannerViewPager
import com.zjl.base.utils.ext.toHtml
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.data.model.BannerVO
import com.zjl.finalarchitecture.data.model.CategoryVO

/**
 * @description:
 * @author: zhou
 * @date : 2022/4/28 18:52
 */
class ProjectCategoryAdapter :
    BaseQuickAdapter<CategoryVO, BaseViewHolder>(R.layout.item_category_sub) {

    private var checkPosition = 0
    var onCheckedListener: ((Int) -> Unit)? = null

    override fun convert(holder: BaseViewHolder, item: CategoryVO) {
        val ctvCategory = holder.getView<CheckedTextView>(R.id.ctvCategory)
        ctvCategory.run {
            text = item.name.toHtml()
//            if (checkPosition == holder.absoluteAdapterPosition) {
//                isChecked
//            }

            isChecked = checkPosition == holder.absoluteAdapterPosition

            setOnClickListener {
                val position = holder.absoluteAdapterPosition
                check(position)
                onCheckedListener?.invoke(position)
            }
        }
    }

    fun setCheckClick(onChecked: ((Int) -> Unit)) {
        this.onCheckedListener = onChecked
    }

    fun check(position: Int) {
        checkPosition = position
        notifyDataSetChanged()
    }

}