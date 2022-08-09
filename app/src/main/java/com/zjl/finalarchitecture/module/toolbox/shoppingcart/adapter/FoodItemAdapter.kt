package com.zjl.finalarchitecture.module.toolbox.shoppingcart.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.button.MaterialButton
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.module.toolbox.shoppingcart.data.FoodItem

/**
 * @author Xiaoc
 * @since 2022-08-09
 *
 * 食物Item Adapter
 */
class FoodItemAdapter: BaseQuickAdapter<FoodItem, BaseViewHolder>(
    R.layout.item_food_control_item
) {
    override fun convert(holder: BaseViewHolder, item: FoodItem) {
        holder.setText(R.id.tvFoodName, item.foodName)

        handleCount(holder, item.count)
    }

    override fun convert(holder: BaseViewHolder, item: FoodItem, payloads: List<Any>) {
        if(payloads.isEmpty()){
            return
        }
        payloads.forEach {
            if(it is Int){
                handleCount(holder, it)
            }
        }

    }

    /**
     * 设置食物选择数量的视图
     */
    private fun handleCount(holder: BaseViewHolder, count: Int){
        holder.setText(R.id.tvNum, count.toString())
        val btnMinus = holder.getView<MaterialButton>(R.id.btnMinus)
        // 如果 count > 0 我们才启用减去按钮
        btnMinus.isEnabled = count > 0
    }
}