package com.zjl.finalarchitecture.module.toolbox.selectRv.adapter

import android.annotation.SuppressLint
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
 * 多选内容适配器
 */
class SelectMultiAdapter: BaseQuickAdapter<String, BaseViewHolder>(
    R.layout.item_single_or_multi_select
) {

    var currentSelectSet = mutableSetOf<String>()
    @SuppressLint("NotifyDataSetChanged")
    set(value) {
        currentSelectSet.clear()
        currentSelectSet.addAll(value)
        notifyDataSetChanged()
    }

    override fun convert(holder: BaseViewHolder, item: String) {
        holder.itemView.tag = item

        holder.setText(R.id.tv_title, item)
        handleSelectStatus(holder, currentSelectSet.contains(item))
    }

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
        // 监听CheckBox的点击事件（我们这不监听状态变化）
        cbSelect.setOnClickListener {
            val data = (viewHolder.itemView.tag as? String) ?: return@setOnClickListener
            val changedSelected = cbSelect.isChecked
            if(changedSelected){
                currentSelectSet.add(data)
            } else {
                currentSelectSet.remove(data)
            }
            notifyItemChanged(viewHolder.absoluteAdapterPosition, changedSelected)
        }

        viewHolder.itemView.setOnClickListener {
            val data = (viewHolder.itemView.tag as? String) ?: return@setOnClickListener
            val changedSelected = !cbSelect.isChecked

            if(changedSelected){
                currentSelectSet.add(data)
            } else {
                currentSelectSet.remove(data)
            }
            notifyItemChanged(viewHolder.absoluteAdapterPosition, changedSelected)
        }
    }

}