package com.zjl.finalarchitecture.module.toolbox.shoppingcart.provider

import com.zjl.finalarchitecture.module.toolbox.shoppingcart.data.FoodItem

/**
 * @author Xiaoc
 * @since 2022-08-09
 */
class FinalShoppingCartProvider: ShoppingCartProvider<FoodItem>() {

    override fun addFood(food: FoodItem): HandleResult<FoodItem> {
        // 看已选择的列表里有没有该食物
        val index = _selectFoods.indexOfFirst {
            isFoodEquals(it, food)
        }

        val shouldAddToList = index < 0

        // 如果有，我们直接将数量 +1，如果没有，我们将其加入到已选择列表中
        val count = food.count + 1
        val newFood = food.copy(count = count)
        if(shouldAddToList){
            _selectFoods.add(newFood)
        } else {
            _selectFoods[index] = newFood
        }

        val type = if(shouldAddToList){
            TYPE_ADD_FOOD
        } else {
            TYPE_ADD_FOOD_ONLY_COUNT
        }

        // 计算总额
        val price = calculatePrice()

        return HandleResult(type, newFood, selectFoods, index, price)
    }

    override fun removeFood(food: FoodItem): HandleResult<FoodItem> {
        // 看已选择的列表里有没有该食物
        val index = _selectFoods.indexOfFirst {
            isFoodEquals(it, food)
        }
        if(index < 0){
            throw RuntimeException("没有这个食物，无法移除！")
        }

        val count = food.count - 1
        val shouldRemoveToList = count <= 0
        val newFood = food.copy(count = count)
        // 我们判断 -1 后的数量如果是 小于等于0 时，代表这个食物应该被移除了
        if(shouldRemoveToList){
            _selectFoods.removeAt(index)
        } else {
            _selectFoods[index] = newFood
        }

        val type = if(shouldRemoveToList){
            TYPE_ADD_FOOD
        } else {
            TYPE_ADD_FOOD_ONLY_COUNT
        }

        // 计算总额
        val price = calculatePrice()

        return HandleResult(type, newFood, selectFoods, index, price)
    }

    override fun clearAllFoods(): HandleResult<FoodItem> {
        _selectFoods.clear()
        return HandleResult(TYPE_CLEAR_ALL_FOOD, null, selectFoods, -1, 0.0)
    }

    override fun isFoodEquals(originFood: FoodItem, compareFood: FoodItem): Boolean {
        return originFood.id == compareFood.id
    }

    /**
     * 计算总金额
     */
    fun calculatePrice(): Double{
        return _selectFoods.sumOf {
            it.price * it.count
        }
    }
}