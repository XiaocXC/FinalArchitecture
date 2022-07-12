package com.zjl.finalarchitecture.module.toolbox.selectRv.adapter

import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zjl.finalarchitecture.R

/**
 * @author Xiaoc
 * @since 2022-06-24
 *
 * 单选内容适配器
 */
class SelectSingleAdapter: BaseQuickAdapter<String, BaseViewHolder>(
    R.layout.item_single_or_multi_select
) {

    /**
     * 当前选择的数据下标
     */
    var currentSelect: Int = RecyclerView.NO_POSITION
    set(value) {
        // 通知更新前一个内容取消勾选
        getItemOrNull(currentSelect)?.let {
            notifyItemChanged(currentSelect, false)
        }
        field = value
        // 通知更新当前选中的状态
        getItemOrNull(currentSelect)?.let {
            notifyItemChanged(currentSelect, true)
        }
    }

    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.tv_title, item)
        handleSelectStatus(holder, holder.absoluteAdapterPosition == currentSelect)
    }

    /**
     * 局部更新，
     * 当调用 notifyItemChanged(pos, payloads) 方法时会调用到此处
     * 我们将payloads取出然后无损更新视图
     * @param payloads 额外参数值
     */
    override fun convert(holder: BaseViewHolder, item: String, payloads: List<Any>) {
        if(payloads.isEmpty()){
            return
        }
        payloads.forEach {
            handleSelectStatus(holder, it as Boolean)
        }
    }

    /**
     * 更新选中状态
     */
    private fun handleSelectStatus(holder: BaseViewHolder, selected: Boolean){
        val cbSelect = holder.getView<CheckBox>(R.id.cb_select)
        cbSelect.isChecked = selected
    }

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        val cbSelect = viewHolder.getView<CheckBox>(R.id.cb_select)
        // 禁用掉CheckBox自己的点击效果
        cbSelect.isClickable = false
    }

}

data class SelectData(
    val title: String,
    var selected: Boolean
)