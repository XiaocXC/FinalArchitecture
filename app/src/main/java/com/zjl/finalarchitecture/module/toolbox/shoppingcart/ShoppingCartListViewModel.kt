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
 * @since  2022-08-08
 *
 * 购物车列表 ViewModel
 * 模拟数据生成
 **/
class ShoppingCartListViewModel: BaseViewModel() {

    private val _shoppingFoods = MutableStateFlow<List<FoodGroup>>(emptyList())
    val shoppingFoods: StateFlow<List<FoodGroup>> = _shoppingFoods

    init {
        initData()
    }

    override fun refresh() {
        viewModelScope.launch {
            val dataDemo = buildList<FoodGroup> {
                val g1Items = listOf<FoodItem>(
                    FoodItem(id = "I1-1", "KFC全家桶"),
                    FoodItem(id = "I1-2", "香辣鸡翅"),
                    FoodItem(id = "I1-3", "吮指原味鸡"),
                    FoodItem(id = "I1-4", "牛肉堡"),
                    FoodItem(id = "I1-5", "薯条（大）")
                )
                add(FoodGroup(id = "G1", "KFC", g1Items))


                val g2Items = listOf<FoodItem>(
                    FoodItem(id = "I2-1", "黄焖鸡米饭（香辣）"),
                    FoodItem(id = "I2-2", "蛋炒饭（香辣）"),
                    FoodItem(id = "I2-3", "牛肉盖浇饭"),
                    FoodItem(id = "I2-4", "超级牛腩饭"),
                    FoodItem(id = "I2-5", "土豆炒肉丝盖浇")
                )
                add(FoodGroup(id = "G2", "黄焖鸡", g2Items))
            }
            _shoppingFoods.value = dataDemo
        }
    }
}