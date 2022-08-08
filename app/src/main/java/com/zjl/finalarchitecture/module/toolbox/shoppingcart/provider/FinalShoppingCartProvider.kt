package com.zjl.finalarchitecture.module.toolbox.shoppingcart.provider

import com.zjl.finalarchitecture.module.toolbox.shoppingcart.data.FoodItem
import kotlin.math.max

/**
 * @author Xiaoc
 * @since  2022-08-08
 **/
class FinalShoppingCartProvider: ShoppingCartProvider<FoodItem> {

    /**
     * 已选择的菜品列表
     */
    private val _selectFoods: MutableList<FoodItem> = mutableListOf()
    val selectFoods: List<FoodItem> = _selectFoods

    /**
     * 购物车事件监听器
     */
    var shoppingCardEventListener: ShoppingCardEventListener? = null

    override fun clearAllSelectFood() {
        // 清除已选择的菜品
        _selectFoods.clear()
        // 执行回调
        shoppingCardEventListener?.afterClearAllSelectFood()
    }

    override fun removeSelectFood(food: FoodItem) {
        // 先查找一下添加的菜品是否已经在已选择的菜品列表中
        // 如果在，我们直接把选择的数量改变
        // 如果数量已经为减了之后就没有了，我们就将它移除
        val index = _selectFoods.indexOfFirst {
            isFoodEquals(it, food)
        }

        // 如果没有找到对应菜品，直接返回不处理
        if(index < 0){
            return
        }

        val removeFood = _selectFoods[index]
        val count = max(removeFood.count - 1, 0)

        val removeNewFood = removeFood.copy(count = count)
        if(count <= 0){
            _selectFoods.removeAt(index)
        } else {
            _selectFoods[index] = removeNewFood
        }

        // 执行回调
        shoppingCardEventListener?.afterRemoveSelectFood(removeNewFood, selectFoods, count <= 0, index)

    }

    override fun addFood(food: FoodItem) {
        // 先查找一下添加的菜品是否已经在已选择的菜品列表中
        // 如果在，我们直接把选择的数量改变
        // 如果不在，我们add到List中
        val index = _selectFoods.indexOfFirst {
            isFoodEquals(it, food)
        }

        val shouldAddItem = index < 0

        val newFood: FoodItem
        val count = food.count + 1
        // 我们把原来的数量进行 +1
        newFood = food.copy(count = count)

        if(shouldAddItem){
            _selectFoods.add(food)
        } else {
            _selectFoods[index] = newFood
        }

        // 执行回调
        shoppingCardEventListener?.afterAddSelectFood(newFood, selectFoods, shouldAddItem, index)
    }

    override fun isFoodEquals(originFood: FoodItem, compareFood: FoodItem): Boolean {
        // 比对两者ID之间是否相同，如果ID相同就是同一个菜品
        return originFood.id == compareFood.id
    }

    interface ShoppingCardEventListener{

        fun afterAddSelectFood(addFoodItem: FoodItem, selectFoods: List<FoodItem>, shouldAddItem: Boolean, updatePosition: Int)
        fun afterRemoveSelectFood(removeFood: FoodItem, selectFoods: List<FoodItem>, shouldRemoveItem: Boolean, updatePosition: Int)
        fun afterClearAllSelectFood()
    }
}