package com.zjl.finalarchitecture.module.toolbox.shoppingcart.data

/**
 * @author Xiaoc
 * @since  2022-08-08
 **/
data class FoodGroup(
    val id: String,
    val groupName: String,
    val foods: List<FoodItem>
)