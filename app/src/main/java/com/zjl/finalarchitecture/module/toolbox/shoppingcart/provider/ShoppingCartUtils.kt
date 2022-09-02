package com.zjl.finalarchitecture.module.toolbox.shoppingcart.provider

import com.chad.library.adapter.base.BaseQuickAdapter
import com.zjl.finalarchitecture.module.toolbox.shoppingcart.data.FoodGroup
import com.zjl.finalarchitecture.module.toolbox.shoppingcart.data.FoodItem

/**
 * @author Xiaoc
 * @since 2022-08-09
 */
object ShoppingCartUtils {

    fun handleResult(
        result: ShoppingCartProvider.HandleResult<FoodItem>,
        adapter: BaseQuickAdapter<FoodGroup, *>,
        groupPosition: Int,
        itemPosition: Int
    ){
        if(result.updateFood != null){
            val data = adapter.getItem(groupPosition)
            data.foods[itemPosition] = result.updateFood
            // 我们把要更新的具体item的位置传入到adapter中
            adapter.notifyItemChanged(groupPosition, itemPosition)
        }
        /**
         * 当然如果你不想实现局部不闪烁更新单个Item，可以替换为以下内容：
         */
//        if(result.updateFood != null){
//            val data = adapter.getItem(groupPosition)
//            data.foods[itemPosition] = result.updateFood
//            adapter.notifyDataSetChanged()
//        }
    }
}