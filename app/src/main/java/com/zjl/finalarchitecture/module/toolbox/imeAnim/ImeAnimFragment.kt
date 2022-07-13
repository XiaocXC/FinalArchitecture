package com.zjl.finalarchitecture.module.toolbox.imeAnim

import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.findNavController
import com.zjl.base.viewmodel.EmptyViewModel
import com.zjl.finalarchitecture.databinding.FragmentImeAnimBinding
import com.zjl.finalarchitecture.module.toolbox.imeAnim.adapter.ImeAnimMessageAdapter

/**
 * @author Xiaoc
 * @since 2022-07-13
 *
 * Ime输入法动画监听示例
 */
class ImeAnimFragment: BaseFragment<FragmentImeAnimBinding, EmptyViewModel>() {

    override fun initViewAndEvent(savedInstanceState: Bundle?) {

        mBinding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        mBinding.rvImeMessage.adapter = ImeAnimMessageAdapter()

        // 分以下3个步骤使用IME动画监听动作：

        /**
         * 1) 我们所有操作需要在开启了沉浸式状态栏后使用，例如在Activity中使用了 `window.setDecorFitsSystemWindows(false)`
         * 或者使用其他方式设置了view.setSystemUiVisibility(LAYOUT_STABLE | LAYOUT_FULLSCREEN | LAYOUT_FULLSCREEN)
         * 以便能够让 [WindowInsetsCompat] 能够正确处理内容。不开启沉浸式则无法正确处理
         *
         * 在弹出软键盘后，视图的部分内容会被遮盖，我们需要监听整个window的变化，通过 window的insets边衬 来取得对应软键盘(IME)的
         * 高度，取得软键盘高度后我们去设置整个布局的Padding来达到让布局能够完整显示出来的目的。并且更新整个布局的Padding后我们
         * 我们会分发window的变化让子视图去适应变化后的布局界面（更详细的内容见具体类）
         */
        val deferringInsetsListener = RootViewDeferringInsetsCallback(
            persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
            deferredInsetTypes = WindowInsetsCompat.Type.ime()
        )
        // RootViewDeferringInsetsCallback is both an WindowInsetsAnimation.Callback and an
        // OnApplyWindowInsetsListener, so needs to be set as so.
        ViewCompat.setWindowInsetsAnimationCallback(mBinding.root, deferringInsetsListener)
        ViewCompat.setOnApplyWindowInsetsListener(mBinding.root, deferringInsetsListener)

        ViewCompat.setWindowInsetsAnimationCallback(
            mBinding.messageHolder,
            TranslateDeferringInsetsAnimationCallback(
                view = mBinding.messageHolder,
                persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
                deferredInsetTypes = WindowInsetsCompat.Type.ime(),
                // We explicitly allow dispatch to continue down to binding.messageHolder's
                // child views, so that step 2.5 below receives the call
                dispatchMode = WindowInsetsAnimationCompat.Callback.DISPATCH_MODE_CONTINUE_ON_SUBTREE
            )
        )

        ViewCompat.setWindowInsetsAnimationCallback(
            mBinding.rvImeMessage,
            TranslateDeferringInsetsAnimationCallback(
                view = mBinding.rvImeMessage,
                persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
                deferredInsetTypes = WindowInsetsCompat.Type.ime()
            )
        )
    }

    override fun createObserver() {

    }
}