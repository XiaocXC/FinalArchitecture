package com.zjl.finalarchitecture.module.toolbox.shoppingcart.adapter

import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.module.toolbox.shoppingcart.data.FoodGroup
import com.zjl.finalarchitecture.module.toolbox.shoppingcart.data.FoodItem

/**
 * @author Xiaoc
 * @since 2022-08-09
 *
 * 食物组 Adapter
 */
class FoodGroupAdapter: BaseQuickAdapter<FoodGroup, BaseViewHolder>(
    R.layout.item_food_group
) {

    var foodAddMinusCallback: (isAdd: Boolean, food: FoodItem, groupPosition: Int, itemPosition: Int) -> Unit = { _,_,_,_ ->

    }

    override fun convert(holder: BaseViewHolder, item: FoodGroup) {
        holder.setText(R.id.tvGroupName, item.groupName)
        val rvFoods = holder.getView<RecyclerView>(R.id.rvFoods)
        (rvFoods.adapter as FoodItemAdapter).setList(item.foods)
    }

    override fun convert(holder: BaseViewHolder, item: FoodGroup, payloads: List<Any>) {
        if(payloads.isEmpty()){
            return
        }

        val rvFoods = holder.getView<RecyclerView>(R.id.rvFoods)
        payloads.forEach {
            // 取到[ShoppingCartUtils]里调用notifyItemChanged时传入的Item的Position
            // 然后修改一下item的数据源
            if(it is Int){
                val adapter = rvFoods.adapter as FoodItemAdapter
                adapter.data[it] = item.foods[it]
                adapter.notifyItemChanged(it, item.foods[it].count)
            }
        }

    }

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        val rvFoods = viewHolder.getView<RecyclerView>(R.id.rvFoods)
        val adapter = FoodItemAdapter()
        // 设置食物Item的加减按钮的点击事件
        adapter.addChildClickViewIds(R.id.btnAdd, R.id.btnMinus)
        adapter.setOnItemChildClickListener { _, view, position ->
            if(view.id == R.id.btnAdd){
                foodAddMinusCallback(true, adapter.getItem(position), viewHolder.absoluteAdapterPosition, position)
            } else if(view.id == R.id.btnMinus){
                foodAddMinusCallback(false, adapter.getItem(position), viewHolder.absoluteAdapterPosition, position)
            }
        }

        rvFoods.adapter = adapter
    }
}