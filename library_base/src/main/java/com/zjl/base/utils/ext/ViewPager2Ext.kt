package com.zjl.base.utils.ext

import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

/**
 * @author Xiaoc
 * @since 2022-04-08
 *
 * ViewPager2太敏感了，就像一个少女
 * 我们要用反射把少女的内部机制改了
 * @param f 敏感度：默认为测试的最佳值2
 */
fun ViewPager2.reduceDragSensitivity(f: Int = 2) {
    try {
        val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
        recyclerViewField.isAccessible = true
        val recyclerView = recyclerViewField.get(this) as RecyclerView

        val touchSlopField = RecyclerView::class.java.getDeclaredField("mTouchSlop")
        touchSlopField.isAccessible = true
        val touchSlop = touchSlopField.get(recyclerView) as Int
        touchSlopField.set(recyclerView, touchSlop*f)
    } catch (e: Exception){ }
}