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
        // 通知前一个内容取消勾选
        getItemOrNull(currentSelect)?.let {
            notifyItemChanged(currentSelect, false)
        }
        field = value
        // 通知当前选中的更新
        getItemOrNull(currentSelect)?.let {
            notifyItemChanged(currentSelect, true)
        }
    }

    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.tv_title, item)
        handleSelectStatus(holder, holder.absoluteAdapterPosition == currentSelect)
    }

    /**
     * 局部更新
     * @param payloads 额外参数值
     */
    override fun convert(holder: BaseViewHolder, item: String, payloads: List<Any>) {
        if(payloads.isEmpty()){
            return
        }
        val selected = payloads[0] as Boolean
        handleSelectStatus(holder, selected)
    }

    private fun handleSelectStatus(holder: BaseViewHolder, selected: Boolean){
        val cbSelect = holder.getView<CheckBox>(R.id.cb_select)
        cbSelect.isChecked = selected
    }

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        val cbSelect = viewHolder.getView<CheckBox>(R.id.cb_select)
        // 监听CheckBox的点击事件（我们这不监听状态变化，因为会有问题BUG）
        cbSelect.setOnClickListener {
            val changedSelected = cbSelect.isChecked
            currentSelect = if(changedSelected){
                viewHolder.absoluteAdapterPosition
            } else {
                RecyclerView.NO_POSITION
            }
        }

        viewHolder.itemView.setOnClickListener {
            val changedSelected = !cbSelect.isChecked
            currentSelect = if(changedSelected){
                viewHolder.absoluteAdapterPosition
            } else {
                RecyclerView.NO_POSITION
            }
        }
    }

}

data class SelectData(
    val title: String,
    var selected: Boolean
)