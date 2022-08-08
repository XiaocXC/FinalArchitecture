package com.zjl.finalarchitecture.module.toolbox.shoppingcart.data

/**
 * @author Xiaoc
 * @since  2022-08-08
 **/
data class FoodItem(
    val id: String,
    val foodName: String,
    val count: Int = 0
)