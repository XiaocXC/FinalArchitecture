package com.zjl.finalarchitecture.module.toolbox.shoppingcart

import androidx.lifecycle.viewModelScope
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.finalarchitecture.module.toolbox.shoppingcart.data.FoodGroup
import com.zjl.finalarchitecture.module.toolbox.shoppingcart.data.FoodItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * @author Xiaoc
 * @since 2022-08-09
 */
class ShoppingCartViewModel: BaseViewModel() {

    private val _foodList = MutableStateFlow<List<FoodGroup>>(emptyList())
    val foodList: StateFlow<List<FoodGroup>> = _foodList

    init {
        initData()
    }

    override fun refresh() {
        viewModelScope.launch {
            val result = buildList<FoodGroup> {
                val item1 = mutableListOf(
                    FoodItem("f1-1", "吮指原味鸡", 13.9),
                    FoodItem("f1-2", "薯条（大份）", 13.5),
                    FoodItem("f1-3", "可乐（大杯）", 14.5),
                    FoodItem("f1-4", "香辣鸡腿堡", 16.9),
                    FoodItem("f1-5", "大鸡腿", 22.9)
                )
                add(FoodGroup("g1", "KFC", item1))

                val item2 = mutableListOf(
                    FoodItem("f2-1", "黄焖鸡米饭", 16.2),
                    FoodItem("f2-2", "黄焖鸡汤", 13.5),
                    FoodItem("f2-3", "油炸黄焖鸡", 22.5),
                    FoodItem("f2-4", "小吃饮料拼盘", 25.9)
                )
                add(FoodGroup("g2", "黄焖鸡", item2))

                val item3 = mutableListOf(
                    FoodItem("f3-1", "安格斯浓香双人餐", 79.9),
                    FoodItem("f3-2", "火烤菠萝", 49.9),
                    FoodItem("f3-3", "牛多多欢享盒", 64.9)
                )
                add(FoodGroup("g3", "汉堡王", item3))
            }
            _foodList.value = result
        }
    }
}