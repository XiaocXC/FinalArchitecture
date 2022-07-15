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
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat

/**
 * 该类是 [WindowInsetsAnimationCompat.Callback] 和 [View.OnApplyWindowInsetsListener] 的子类
 * 它应该是用在你的根布局上的
 *
 * 该类主要作用是 可选择地处理匹配 [deferredInsetTypes] 与 rootView 的效果，让它们看起来更和谐
 *
 * 举个例子：当输入法弹起时，也就是 [WindowInsetsAnimationCompat] 启动时
 * 系统会调用 [WindowInsetsCompat] 的 [OnApplyWindowInsetsListener] 告诉键盘弹起后结束状态的 Insets 内容，
 * 里面包含了键盘弹起后的高度
 * 而如果我们仅仅只是在收到 [OnApplyWindowInsetsListener] 去改变 rootView 的 padding 以达到适配的IME高度的目的，
 * 会导致整个视图会在键盘弹起一瞬间还没有完全弹起时，就被更新到了最终的状态
 * 所以我们要做的处理就是需要使用 [WindowInsetsAnimationCompat.Callback]，在键盘完全弹出时才给 rootView 更新最终状态
 *
 * 你可以按照以下方式去给根视图创建一个 [RootViewDeferringInsetsCallback]
 *
 * ```
 * val callback = RootViewDeferringInsetsCallback(
 *     persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
 *     deferredInsetTypes = WindowInsetsCompat.Type.ime()
 * )
 * ```
 *
 * 这个类可以使用与任何 [WindowInsetsCompat.Type]，不仅限于 IME 的Type
 *
 * @param persistentInsetTypes 这个Type会始终被处理
 * @param deferredInsetTypes 这个Type会在 [WindowInsetsAnimationCompat] 动画处理完毕后进行处理
 */
class RootViewDeferringInsetsCallback(
    val persistentInsetTypes: Int,
    val deferredInsetTypes: Int
) : WindowInsetsAnimationCompat.Callback(DISPATCH_MODE_CONTINUE_ON_SUBTREE),
    OnApplyWindowInsetsListener {
    init {
        require(persistentInsetTypes and deferredInsetTypes == 0) {
            "persistentInsetTypes 和 deferredInsetTypes 不能一样！"
        }
    }

    private var view: View? = null
    private var lastWindowInsets: WindowInsetsCompat? = null

    private var deferredInsets = false

    override fun onApplyWindowInsets(
        v: View,
        windowInsets: WindowInsetsCompat
    ): WindowInsetsCompat {
        // 存储这个View和结束后的insets边衬，以便于动画结束后使用
        view = v
        lastWindowInsets = windowInsets

        val types = if(deferredInsets){
            // 当动画还没处理完时，我们只管 [persistentInsetTypes]
            persistentInsetTypes
        } else {
            // 当动画结束后，我们两种Type [persistentInsetTypes] 和 [deferredInsetTypes] 一起管
            persistentInsetTypes or deferredInsetTypes
        }

        // 我们获取到对应Type的边衬，根据Insets边衬来给整个视图设置padding
        val typeInsets = windowInsets.getInsets(types)
        v.setPadding(typeInsets.left, typeInsets.top, typeInsets.right, typeInsets.bottom)

        // 我们这里返回 WindowInsetsCompat.CONSUMED 来停止继续消耗这里的 WindowInsets 边衬
        return WindowInsetsCompat.CONSUMED
    }

    override fun onPrepare(animation: WindowInsetsAnimationCompat) {
        if (animation.typeMask and deferredInsetTypes != 0) {
            // 这在动画开始时会调用，我们将flag置为true，代表动画进行中
            deferredInsets = true
        }
    }

    override fun onProgress(
        insets: WindowInsetsCompat,
        runningAnims: List<WindowInsetsAnimationCompat>
    ): WindowInsetsCompat {
        // 动画进行时，我们不做任何操作
        return insets
    }

    override fun onEnd(animation: WindowInsetsAnimationCompat) {
        if (deferredInsets && (animation.typeMask and deferredInsetTypes) != 0) {
            // 动画结束时我们将flag置为false
            deferredInsets = false

            // 最后我们重新给整个视图View分发一下边衬，让其更新最新的内容
            // 理想状态下调用 view.requestApplyInsets() 就能够执行
            // 但是这会导致界面闪烁
            if (lastWindowInsets != null && view != null) {
                ViewCompat.dispatchApplyWindowInsets(view!!, lastWindowInsets!!)
            }
        }
    }
}
