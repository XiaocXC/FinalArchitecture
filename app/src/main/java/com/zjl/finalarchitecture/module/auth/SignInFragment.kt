package com.zjl.finalarchitecture.module.auth

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.transition.TransitionManager
import com.google.android.material.transition.MaterialSharedAxis
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.ui.onFailure
import com.zjl.base.ui.onLoading
import com.zjl.base.ui.onSuccess
import com.zjl.base.utils.findNavController
import com.zjl.base.utils.launchAndRepeatWithViewLifecycle
import com.zjl.base.viewmodel.EmptyViewModel
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentLoginBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @author Xiaoc
 * @since 2021/5/22
 *
 * 登录Fragment（密码登录）
 */
class SignInFragment: BaseFragment<FragmentLoginBinding, SignInViewModel>() {

//    private val signInViewModel by hiltNavGraphViewModels<SignInViewModel>(R.id.nav_auth)

    private lateinit var backCallback: OnBackPressedCallback

    private var accountNumberPass: Boolean = false

    override fun initViewAndEvent(savedInstanceState: Bundle?) {

        mBinding.toolbarSignIn.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        mBinding.btnNextStep.setOnClickListener {
            mBinding.btnNextStep.postDelayed({
                // 动画效果后再更新，防止出现闪现文字的清空
                mBinding.btnNextStep.setLoading(false)
            },200L)
            mBinding.btnSignIn.setLoading(false)
            // 如果正在输入密码页面，不切换
            if(mBinding.containerEditPhoneView.isVisible){
                switchView()
            }
        }


        // 监听输入手机内容更改获取验证码按钮状态
        mBinding.editAccount.addOnEditTextAttachedListener {
            it.editText?.doAfterTextChanged { text ->
                accountNumberPass = !text.isNullOrEmpty()
                updateNextStepButtonState()
            }
        }

        // 监听密码输入框
        mBinding.editPassword.addOnEditTextAttachedListener {
            it.editText?.doAfterTextChanged { text ->
                mBinding.btnSignIn.isEnabled = !text.isNullOrEmpty()
            }
        }

        // 登录按钮
        mBinding.btnSignIn.setOnClickListener {
            mViewModel.signInByPassword(
                mBinding.editAccount.editText?.text.toString(),
                mBinding.editPassword.editText?.text.toString()
            )
        }

        // 下一步的按钮
        mBinding.btnNextStep.setOnClickListener {
            // 打开返回重新监听
            backCallback.isEnabled = true

            // 如果没有正在计时，则去请求获取验证码，否则直接切换到输入验证码界面
            switchView()

        }

    }

    private fun updateNextStepButtonState(){
        mBinding.btnNextStep.isEnabled = accountNumberPass
    }

    override fun createObserver() {
        backCallback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            if(!mBinding.containerEditPhoneView.isVisible){
                switchView()
                // 清除输入的验证码输入框
                mBinding.editPassword.editText?.text = null
                isEnabled = true
            } else {
                isEnabled = false
                requireActivity().onBackPressed()
            }
        }

        launchAndRepeatWithViewLifecycle {

            launch {
                // 登录状态
                mViewModel.eventSignInState.collectLatest {
                    it.onSuccess {
                        mBinding.btnSignIn.setLoading(false)
                        Toast.makeText(requireContext(), R.string.description_login_success, Toast.LENGTH_SHORT).show()
                        findNavController().navigateUp()
                    }.onLoading {
                        mBinding.btnSignIn.setLoading(true)
                    }.onFailure { _, throwable ->
                        mBinding.btnSignIn.setLoading(false)
                        Toast.makeText(requireContext(), throwable.message ?: "未知错误", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }

    private fun createTransition(entering: Boolean): MaterialSharedAxis {
        val transition = MaterialSharedAxis(MaterialSharedAxis.X, entering)

        transition.addTarget(mBinding.containerEditPhoneView)
        transition.addTarget(mBinding.containerEditPasswordView)
        return transition
    }

    private fun switchView(){
        val startViewShowing = mBinding.containerEditPhoneView.isVisible
        val transition = createTransition(startViewShowing)
        TransitionManager.beginDelayedTransition(mBinding.root, transition)
        mBinding.containerEditPhoneView.isVisible = !startViewShowing
        mBinding.containerEditPasswordView.isVisible = startViewShowing
    }

}