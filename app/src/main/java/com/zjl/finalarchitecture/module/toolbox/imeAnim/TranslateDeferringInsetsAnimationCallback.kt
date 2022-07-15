/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zjl.finalarchitecture.module.toolbox.imeAnim

import android.view.View
import androidx.core.graphics.Insets
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat

/**
 * 该类是 [WindowInsetsAnimationCompat.Callback] 的子类
 * 它主要负责给具体视图加入边衬变化的动画效果
 *
 * 该类需要配合 [RootViewDeferringInsetsCallback] 的支持才能正常工作
 *
 * 该类的实现原理很简单，在边衬发生改变时动态修改视图的 translationX 和 translationY 属性
 * 来改变视图的位置
 * 举个例子：当键盘弹起时，该类会回调边衬变化的动画回调，我们根据回调的数值，来计算视图应该处于什么位置
 *
 * @param view 要配合边衬动画的视图
 * @param dispatchMode 分发模式，如果子View也要设置setWindowInsetsAnimationCallback，那就需要 DISPATCH_MODE_CONTINUE_ON_SUBTREE
 */
class TranslateDeferringInsetsAnimationCallback(
    private val view: View,
    val persistentInsetTypes: Int,
    val deferredInsetTypes: Int,
    dispatchMode: Int = DISPATCH_MODE_STOP
) : WindowInsetsAnimationCompat.Callback(dispatchMode) {
    init {
        require(persistentInsetTypes and deferredInsetTypes == 0) {
            "persistentInsetTypes and deferredInsetTypes 不能一样！"
        }
    }

    override fun onProgress(
        insets: WindowInsetsCompat,
        runningAnimations: List<WindowInsetsAnimationCompat>
    ): WindowInsetsCompat {
        // onProgress() 在边衬动画进行时调用，例如键盘弹起时

        // 获取变化的边衬的Inset值
        val typesInset = insets.getInsets(deferredInsetTypes)
        // 获取固定不变的边衬的Inset值
        val otherInset = insets.getInsets(persistentInsetTypes)

        // 现在我们减去两个插入，来计算差值，计算平移距离
        val diff = Insets.subtract(typesInset, otherInset).let {
            Insets.max(it, Insets.NONE)
        }

        view.translationX = (diff.left - diff.right).toFloat()
        view.translationY = (diff.top - diff.bottom).toFloat()

        return insets
    }

    override fun onEnd(animation: WindowInsetsAnimationCompat) {
        // 动画结束后，由于根布局的Padding会被重新设置，我们这里会把位置还原
        view.translationX = 0f
        view.translationY = 0f
    }
}
