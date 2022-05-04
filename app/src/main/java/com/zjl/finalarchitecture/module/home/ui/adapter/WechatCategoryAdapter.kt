package com.zjl.finalarchitecture.module.home.ui.adapter

import android.widget.CheckedTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zjl.base.utils.ext.toHtml
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.data.model.CategoryVO
import timber.log.Timber

class WechatCategoryAdapter  :
    BaseQuickAdapter<CategoryVO, BaseViewHolder>(R.layout.item_category_sub) {

    private var checkPosition = 0

    var onCheckedListener: ((Int) -> Unit)? = null

    override fun convert(holder: BaseViewHolder, item: CategoryVO) {
        val ctvCategory = holder.getView<CheckedTextView>(R.id.ctvCategory)
        ctvCategory.run {
            text = item.name.toHtml()
            isChecked = checkPosition == holder.absoluteAdapterPosition
            setOnClickListener {
                val position = holder.absoluteAdapterPosition
                check(position)
                // 传入选中的id
                onCheckedListener?.invoke(item.id)
                Timber.e("选中的id是：${item.id}")
            }
        }
    }

    /**
     * 选中的id 回调到外面
     */
    fun setCheckClick(onChecked: ((Int) -> Unit)) {
        this.onCheckedListener = onChecked
    }

    /**
     * 选中的item
     */
    fun check(position: Int) {
        checkPosition = position
        notifyDataSetChanged()
    }

}