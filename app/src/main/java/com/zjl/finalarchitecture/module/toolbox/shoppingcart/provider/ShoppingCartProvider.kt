package com.zjl.finalarchitecture.module.toolbox.shoppingcart.provider

import java.math.BigDecimal

/**
 * @author Xiaoc
 * @since 2022-08-09
 */
abstract class ShoppingCartProvider<T> {

    companion object {
        const val TYPE_ADD_FOOD = 1
        const val TYPE_ADD_FOOD_ONLY_COUNT = 2
        const val TYPE_REMOVE_FOOD = 3
        const val TYPE_REMOVE_FOOD_TOTAL = 4
        const val TYPE_CLEAR_ALL_FOOD = 5
    }

    protected val _selectFoods = mutableListOf<T>()
    val selectFoods: List<T> = _selectFoods

    abstract fun addFood(food: T): HandleResult<T>

    abstract fun removeFood(food: T): HandleResult<T>

    abstract fun clearAllFoods(): HandleResult<T>

    abstract fun isFoodEquals(originFood: T, compareFood: T): Boolean

    data class HandleResult<T>(
        val type: Int,
        val updateFood: T?,
        val selectFoods: List<T>,
        val updatePosition: Int,
        val totalPrice: Double
    )
}