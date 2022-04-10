package com.zjl.base.utils.ext

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import androidx.annotation.*
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.color.MaterialColors
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

fun View.getColor(@ColorRes id: Int): Int = resources.getColor(id, context.theme)

/**
 * 获取Attr属性的颜色值
 */
@ColorInt
fun View.getAttrColor(@AttrRes id: Int): Int = MaterialColors.getColor(this, id)


/**
 * 获取Attr属性的颜色值
 */
@ColorInt
fun Context.getAttrColor(@AttrRes id: Int): Int = MaterialColors.getColor(this, id, "")

/**
 * 获取尺寸值（根据屏幕适配）
 */
fun Context.getDimension(@DimenRes id: Int): Float = resources.getDimension(id)

/**
 * 获取尺寸值（根据屏幕适配）
 */
fun View.getDimension(@DimenRes id: Int): Float = resources.getDimension(id)

/**
 * 得到Drawable资源
 */
fun View.getDrawable(@DrawableRes id: Int): Drawable? = ResourcesCompat.getDrawable(resources,id,context.theme)

/**
 * 将dp转为对应px的像素
 */
val Int.dp: Int
    get() {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt()
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
 * 防止重复点击事件 默认0.5秒内不可重复点击
 * @param interval 时间间隔 默认0.5秒
 * @param action 执行方法
 */
var lastClickTime = 0L
fun View.clickNoRepeat(interval: Long = 500, action: (view: View) -> Unit) {
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (lastClickTime != 0L && (currentTime - lastClickTime < interval)) {
            return@setOnClickListener
        }
        lastClickTime = currentTime
        action(it)
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
    //设置适配器 这里传入fragment上下文，因为Navigation和ViewPager2的BUG，我们需要手动传入view的LifeCycle
    // https://issuetracker.google.com/issues/154751401
    this.adapter = object : FragmentStateAdapter(fragment.childFragmentManager, fragment.viewLifecycleOwner.lifecycle){
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

