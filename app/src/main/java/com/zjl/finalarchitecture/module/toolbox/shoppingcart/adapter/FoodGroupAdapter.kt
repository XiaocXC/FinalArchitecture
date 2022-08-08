package com.zjl.finalarchitecture.module.toolbox.shoppingcart.adapter

import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.module.toolbox.shoppingcart.data.FoodGroup
import com.zjl.finalarchitecture.module.toolbox.shoppingcart.data.FoodItem

/**
 * @author Xiaoc
 * @since  2022-08-08
 **/
class FoodGroupAdapter: BaseQuickAdapter<FoodGroup, BaseViewHolder>(
    R.layout.item_food_group
) {

    var foodAddMinusCallback: (isAdd: Boolean, food: FoodItem, position: Int,) -> Unit = { _, _, _ ->

    }

    override fun convert(holder: BaseViewHolder, item: FoodGroup) {
        holder.setText(R.id.tv_title, item.groupName)
        val rvGoods = holder.getView<RecyclerView>(R.id.rv_goods)

        (rvGoods.adapter as FoodItemAdapter).setList(item.foods)
    }

    override fun convert(holder: BaseViewHolder, item: FoodGroup, payloads: List<Any>) {
        val rvGoods = holder.getView<RecyclerView>(R.id.rv_goods)
        val adapter = FoodItemAdapter()
        adapter.addChildClickViewIds(R.id.btnFoodMinus, R.id.btnFoodAdd)
        adapter.setOnItemChildClickListener { _, view, position ->
            if(view.id == R.id.btnFoodAdd){
                foodAddMinusCallback(true, adapter.getItem(position), holder.absoluteAdapterPosition)
            } else if(view.id == R.id.btnFoodMinus){
                foodAddMinusCallback(false, adapter.getItem(position), holder.absoluteAdapterPosition)
            }
        }
        rvGoods.adapter = adapter
    }
}