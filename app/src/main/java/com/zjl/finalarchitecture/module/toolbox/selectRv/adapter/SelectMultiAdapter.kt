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

    /**
     * 当前已选择的数据集
     */
    var currentSelectSet = mutableSetOf<String>()
    @SuppressLint("NotifyDataSetChanged")
    set(value) {
        currentSelectSet.clear()
        currentSelectSet.addAll(value)
        notifyDataSetChanged()
    }

    /**
     * 设置选中或取消选中某一项
     * @param item 这一项的ItemData
     * @param selected 是否选中
     * @param position Item的位置
     */
    fun setSelectContent(item: String, selected: Boolean, position: Int){
        // 如果是选中，将其加入到已选数据集，如果是取消选中，将其从里面移除
        if(selected){
            currentSelectSet.add(item)
        } else {
            currentSelectSet.remove(item)
        }
        // 通知对应Item进行局部更新是否选中状态
        notifyItemChanged(position, selected)
    }

    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.tv_title, item)
        // 判断该Item是否在已选择的数据集中，更新其选择状态
        handleSelectStatus(holder, currentSelectSet.contains(item))
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