package com.zjl.base.utils

import androidx.annotation.IntDef
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

/**
 * @author Xiaoc
 * @since 2021/2/9
 *
 * 因 [AppBarLayout] 的监听过于繁琐，所以封装了一层以便监听它的各种状态
 **/
abstract class AppBarStateChangeListener: AppBarLayout.OnOffsetChangedListener {

    @IntDef(EXPANDED, COLLAPSED, IDLE)
    @Retention
    annotation class AppBarState

    companion object{
        /**
         * 展开状态
         */
        const val EXPANDED = 0

        /**
         * 折叠状态
         */
        const val COLLAPSED = 1

        /**
         * 默认状态
         */
        const val IDLE = 2
    }

    @AppBarState
    private var currentState: Int = IDLE

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        appBarLayout ?: return
        onOffsetChanged(appBarLayout)
        when {
            verticalOffset == 0 -> {
                if(currentState != EXPANDED){
                    onStateChanged(appBarLayout, EXPANDED, verticalOffset)
                }
                currentState = EXPANDED
            }
            abs(verticalOffset) >= appBarLayout.totalScrollRange -> {
                if(currentState != COLLAPSED){
                    onStateChanged(appBarLayout, COLLAPSED, verticalOffset)
                }
                currentState = COLLAPSED
            }
            else -> {
                if(currentState != IDLE){
                    onStateChanged(appBarLayout, IDLE, verticalOffset)
                }
                currentState = IDLE
            }
        }
    }

    abstract fun onStateChanged(appBarLayout: AppBarLayout,@AppBarState state: Int, verticalOffset: Int)

    abstract fun onOffsetChanged(appBarLayout: AppBarLayout)
}