package com.zjl.finalarchitecture.module.auth

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.transition.TransitionManager
import com.google.android.material.transition.MaterialSharedAxis
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.viewmodel.EmptyViewModel
import com.zjl.finalarchitecture.databinding.FragmentLoginBinding

/**
 * @author Xiaoc
 * @since 2021/5/22
 *
 * 登录Fragment（密码登录）
 */
class SignInFragment: BaseFragment<FragmentLoginBinding, EmptyViewModel>() {

//    private val signInViewModel by hiltNavGraphViewModels<SignInViewModel>(R.id.nav_auth)

    private lateinit var backCallback: OnBackPressedCallback

    private var agreePrivacyPass: Boolean = false
    private var phoneNumberPass: Boolean = false

    override fun initViewAndEvent(savedInstanceState: Bundle?) {

//        binding.toolbarSignIn.setNavigationOnClickListener {
//            requireActivity().onBackPressedDispatcher.onBackPressed()
//        }
//
//        binding.cbAgreePrivacy.setOnCheckedChangeListener { _, isChecked ->
//            phoneNumberPass = isChecked
//            updateGetVerifyButtonState()
//        }
//
//        binding.btnRetryGetVerify.setOnClickListener {
//            signInViewModel.getVerifyCode(binding.editPhone.editText.toString())
//        }
//
//        // 生成隐私协议等文字效果内容
//        val textColorSpan1 = ForegroundColorSpan(requireContext().getAttrColor(R.attr.colorPrimary))
//        val textColorSpan2 = ForegroundColorSpan(requireContext().getAttrColor(R.attr.colorPrimary))
//        val spanBuilder = SpannableStringBuilder(getString(R.string.description_sign_in_privacy_declare))
//        spanBuilder.setSpan(textColorSpan1, 6, 12, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
//        spanBuilder.setSpan(textColorSpan2, 13, 19, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
//        binding.cbAgreePrivacy.text = spanBuilder
//
//        // 监听输入手机内容更改获取验证码按钮状态
//        binding.editPhone.addOnEditTextAttachedListener {
//            it.editText?.doAfterTextChanged { text ->
//                agreePrivacyPass = PhoneUtil.isMobile(text)
//                updateGetVerifyButtonState()
//            }
//        }
//
//        // 监听验证码输入框
//        binding.editVerifyCode.addOnEditTextAttachedListener {
//            it.editText?.doAfterTextChanged { text ->
//                binding.btnSignIn.isEnabled = !text.isNullOrEmpty()
//            }
//        }
//
//        // 登录按钮
//        binding.btnSignIn.setOnClickListener {
//            signInViewModel.signInByVerify(
//                binding.editPhone.editText?.text.toString(),
//                binding.editVerifyCode.editText?.text.toString()
//            )
//        }
//
//        // 获取验证码按钮
//        binding.btnGetVerify.setOnClickListener {
//            // 打开返回重新监听
//            backCallback.isEnabled = true
//
//            // 如果没有正在计时，则去请求获取验证码，否则直接切换到输入验证码界面
//            if(!signInViewModel.isCounting){
//                signInViewModel.getVerifyCode(binding.editPhone.editText?.text.toString())
//            } else {
//                switchView()
//            }
//
//        }

    }

    private fun updateGetVerifyButtonState(){
//        binding.btnGetVerify.isEnabled = agreePrivacyPass and phoneNumberPass
    }

    override fun createObserver() {
//        backCallback = requireActivity().onBackPressedDispatcher.addCallback(this) {
//            if(!binding.containerEditPhoneView.isVisible){
//                switchView()
//                // 清除输入的验证码输入框
//                binding.editVerifyCode.editText?.text = null
//                isEnabled = true
//            } else {
//                isEnabled = false
//                requireActivity().onBackPressed()
//            }
//        }
//
//        launchAndRepeatWithViewLifecycle {
//            launch {
//                signInViewModel.countDownNumber.collectLatest {
//                    binding.btnRetryGetVerify.isEnabled = !it.isCounting
//                    if(it.isCounting){
//                        binding.btnRetryGetVerify.text = it.countDownNumber.toString()
//                    } else {
//                        binding.btnRetryGetVerify.setText(R.string.description_login_get_verify_retry)
//                    }
//                }
//            }
//
//            launch {
//                // 获取验证码状态
//                signInViewModel.eventShowVerifyState.collectLatest {
//                    it.onSuccess {
//                        signInViewModel.startCountDown()
//                        binding.btnGetVerify.postDelayed({
//                            // 动画效果后再更新 获取验证码的加载状态，防止出现闪现文字的清空
//                            binding.btnGetVerify.setLoading(false)
//                        },200L)
//                        binding.btnRetryGetVerify.setLoading(false)
//                        // 如果正在输入验证码页面，不切换
//                        if(binding.containerEditPhoneView.isVisible){
//                            switchView()
//                        }
//                    }.onLoading {
//                        if(binding.containerEditPhoneView.isVisible){
//                            binding.btnGetVerify.setLoading(true)
//                        } else {
//                            binding.btnRetryGetVerify.setLoading(true)
//                        }
//                    }.onFailure { _, throwable ->
//                        binding.btnGetVerify.setLoading(false)
//                        binding.btnRetryGetVerify.setLoading(false)
//                        snackbar(throwable).show()
//                    }
//                }
//            }
//
//            launch {
//                // 登录状态
//                signInViewModel.eventSignInState.collectLatest {
//                    it.onSuccess {
//                        binding.btnSignIn.setLoading(false)
//                        snackbar(R.string.description_login_success).show()
//                        findNavController().navigateUp()
//                    }.onLoading {
//                        binding.btnSignIn.setLoading(true)
//                    }.onFailure { _, throwable ->
//                        binding.btnSignIn.setLoading(false)
//                        snackbar(throwable).show()
//                    }
//                }
//            }
//
//        }
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