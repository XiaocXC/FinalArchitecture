package com.zjl.finalarchitecture.module.toolbox.shoppingcart

import android.os.Bundle
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.launchAndCollectIn
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentShoppingCartBinding
import com.zjl.finalarchitecture.module.toolbox.shoppingcart.adapter.FoodGroupAdapter
import com.zjl.finalarchitecture.module.toolbox.shoppingcart.provider.FinalShoppingCartProvider
import com.zjl.finalarchitecture.module.toolbox.shoppingcart.provider.ShoppingCartUtils

/**
 * @author Xiaoc
 * @since 2022-08-09
 */
class ShoppingCartFragment: BaseFragment<FragmentShoppingCartBinding, ShoppingCartViewModel>() {

    private lateinit var foodGroupAdapter: FoodGroupAdapter
    private val finalShoppingCartProvider = FinalShoppingCartProvider()

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        foodGroupAdapter = FoodGroupAdapter()

        // 设置加减按钮点击后的回调
        foodGroupAdapter.foodAddMinusCallback = { isAdd, food, groupPosition, itemPosition ->
            val result = if(isAdd){
                // 处理添加食物
                finalShoppingCartProvider.addFood(food)
            } else {
                // 处理减去食物
                finalShoppingCartProvider.removeFood(food)
            }

            // 处理适配器更新数据
            ShoppingCartUtils.handleResult(result, foodGroupAdapter, groupPosition, itemPosition)

            // 更新总价格和数量
            mBinding.tvPrice.text = result.totalPrice.toString()
            mBinding.tvNum.text = getString(R.string.shopping_cart_select_num, result.selectFoods.size)

        }
        mBinding.rvFood.adapter = foodGroupAdapter
    }

    override fun createObserver() {
        mViewModel.foodList.launchAndCollectIn(viewLifecycleOwner){
            foodGroupAdapter.setList(it)
        }
    }
}