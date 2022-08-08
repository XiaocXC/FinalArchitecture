package com.zjl.finalarchitecture.module.toolbox.shoppingcart.provider

/**
 * @author Xiaoc
 * @since  2022-08-08
 *
 * 购物车数据处理和视图联动 Provider
 **/
interface ShoppingCartProvider<F> {

    /**
     * 清除所有已选择的菜品
     */
    abstract fun clearAllSelectFood()

    /**
     * 移除一个菜品
     * @param food 菜品实例
     */
    abstract fun removeSelectFood(food: F)

    /**
     * 添加一个菜品
     * @param food 菜品实例
     */
    abstract fun addFood(food: F)

    /**
     * 对比菜品是不是相同的
     * 这个需要你自行重写，来告诉Provider如何比对菜品是否相同
     */
    abstract fun isFoodEquals(originFood: F, compareFood: F): Boolean
}