package com.zjl.finalarchitecture.module.toolbox.shoppingcart.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.button.MaterialButton
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.module.toolbox.shoppingcart.data.FoodItem

/**
 * @author Xiaoc
 * @since  2022-08-08
 **/
class FoodItemAdapter: BaseQuickAdapter<FoodItem, BaseViewHolder>(
    R.layout.item_food_control_item
) {
    override fun convert(holder: BaseViewHolder, item: FoodItem) {
        holder.setText(R.id.tvFoodName, item.foodName)

        val btnFoodMinus = holder.getView<MaterialButton>(R.id.btnFoodMinus)
        btnFoodMinus.isEnabled = item.count > 0
    }

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        val btnFoodAdd = viewHolder.getView<MaterialButton>(R.id.btnFoodAdd)

        btnFoodAdd
    }
}