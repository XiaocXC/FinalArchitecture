package com.zjl.finalarchitecture.module.toolbox.shoppingcart

import android.os.Bundle
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.launchAndRepeatWithViewLifecycle
import com.zjl.finalarchitecture.databinding.FragmentShoppingCartListBinding
import com.zjl.finalarchitecture.module.toolbox.shoppingcart.adapter.FoodGroupAdapter
import com.zjl.finalarchitecture.module.toolbox.shoppingcart.data.FoodItem
import com.zjl.finalarchitecture.module.toolbox.shoppingcart.provider.FinalShoppingCartProvider
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @author Xiaoc
 * @since  2022-08-08
 *
 * 购物车列表界面
 **/
class ShoppingCartListFragment: BaseFragment<FragmentShoppingCartListBinding, ShoppingCartListViewModel>() {

    private lateinit var foodGroupAdapter: FoodGroupAdapter
    private lateinit var finalShoppingCartProvider: FinalShoppingCartProvider

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        finalShoppingCartProvider = FinalShoppingCartProvider()
        foodGroupAdapter = FoodGroupAdapter()

        finalShoppingCartProvider.shoppingCardEventListener = object: FinalShoppingCartProvider.ShoppingCardEventListener{
            override fun afterAddSelectFood(
                addFoodItem: FoodItem,
                selectFoods: List<FoodItem>,
                shouldAddItem: Boolean,
                updatePosition: Int
            ) {
                if(shouldAddItem){

                } else {
                }
                val index = foodGroupAdapter.data.indexOfFirst {
                    it.foods.find { item ->
                        item.id == addFoodItem.id
                    } == null
                }

            }

            override fun afterRemoveSelectFood(
                removeFood: FoodItem,
                selectFoods: List<FoodItem>,
                shouldRemoveItem: Boolean,
                updatePosition: Int
            ) {

            }

            override fun afterClearAllSelectFood() {

            }

        }

        foodGroupAdapter.foodAddMinusCallback = { isAdd, food, position ->
            if(isAdd){
                finalShoppingCartProvider.addFood(food)
            } else {
                finalShoppingCartProvider.removeSelectFood(food)
            }

        }
        mBinding.rvFood.adapter = foodGroupAdapter
    }

    override fun createObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                mViewModel.shoppingFoods.collectLatest {

                }
            }
        }
    }
}