package com.zjl.finalarchitecture.module.toolbox.shoppingcart.data

/**
 * @author Xiaoc
 * @since 2022-08-09
 */
data class FoodItem(
    val id: String,
    val foodName: String,
    val price: Double,
    val count: Int = 0,
)