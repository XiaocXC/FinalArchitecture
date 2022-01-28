package com.zjl.base.utils.ext

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * @description:
 * @author: zhou
 * @date : 2022/1/13 23:50
 */

/**
 * 设置view显示
 */
fun View.visible() {
    visibility = View.VISIBLE
}

/**
 * 设置view占位隐藏
 */
fun View.invisible() {
    visibility = View.INVISIBLE
}

/**
 * 设置view隐藏
 */
fun View.gone() {
    visibility = View.GONE
}

/**
 * 根据条件设置view显示隐藏 为true 显示，为false 隐藏
 */
fun View.visibleOrGone(flag: Boolean) {
    visibility = if (flag) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

/**
 * 根据条件设置view显示隐藏 为true 显示，为false 隐藏
 */
fun View.visibleOrInvisible(flag: Boolean) {
    visibility = if (flag){
        View.VISIBLE
    }else{
        View.INVISIBLE
    }
}

/**
 * ViewPager2初始化
 */
fun ViewPager2.init(
    fragment:Fragment,
    fragmentList: ArrayList<Fragment>,
    isUserInputEnabled: Boolean = true
): ViewPager2{
    //全部缓存,避免切换回重新加载
    this.offscreenPageLimit = fragmentList.size
    //是否可以滑动
    this.isUserInputEnabled = isUserInputEnabled
    //设置适配器 这里传入fragment上下文
    this.adapter = object : FragmentStateAdapter(fragment){
        override fun getItemCount() = fragmentList.size
        //when另一种写法
        override fun createFragment(position: Int) = fragmentList[position]
    }
    return this
}

fun TabLayout.bindViewPager2(
    mTab : TabLayout,
    mViewPager2 : ViewPager2,
    title : ArrayList<String>,
){
    TabLayoutMediator(mTab,mViewPager2,object :
        TabLayoutMediator.TabConfigurationStrategy {
        override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
            tab.text = title[position]
        }
    }).attach()

    mViewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
//            mTab.s
        }

    })
}

