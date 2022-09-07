package com.zjl.finalarchitecture.module.auth.register

import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.ui.onFailure
import com.zjl.base.ui.onLoading
import com.zjl.base.ui.onSuccess
import com.zjl.base.utils.findNavController
import com.zjl.base.utils.launchAndRepeatWithViewLifecycle
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentRegisterBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @author Xiaoc
 * @since  2022-09-06
 *
 * 注册界面
 **/
class RegisterFragment: BaseFragment<FragmentRegisterBinding, RegisterViewModel>() {

    private var accountNumberPass: Boolean = false
    private var passwordPass: Boolean = false
    private var passwordAgainPass: Boolean = false

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        mBinding.toolbarRegister.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // 账号输入监听
        mBinding.editAccount.addOnEditTextAttachedListener {
            it.editText?.doAfterTextChanged { text ->
                accountNumberPass = !text.isNullOrEmpty()
                updateRegisterButtonState()
            }
        }

        // 密码输入监听
        mBinding.editPassword.addOnEditTextAttachedListener {
            it.editText?.doAfterTextChanged { text ->
                passwordPass = !text.isNullOrEmpty()
                updateRegisterButtonState()
            }
        }

        // 重新输入密码监听
        mBinding.editPasswordAgain.addOnEditTextAttachedListener {
            it.editText?.doAfterTextChanged { text ->
                passwordAgainPass = text.toString() == mBinding.editPassword.editText?.text.toString()
                updateRegisterButtonState()
            }
        }

        // 注册按钮
        mBinding.btnRegister.setOnClickListener {
            mViewModel.registerByPassword(
                mBinding.editAccount.editText?.text.toString(),
                mBinding.editPassword.editText?.text.toString()
            )
        }
    }

    override fun createObserver() {
        launchAndRepeatWithViewLifecycle {

            launch {
                // 注册状态
                mViewModel.eventRegisterState.collectLatest {
                    it.onSuccess {
                        mBinding.btnRegister.setLoading(false)
                        Toast.makeText(requireContext(), R.string.description_register_success, Toast.LENGTH_SHORT).show()
                        // 登录成功则返回上一页
                        findNavController().navigateUp()
                    }.onLoading {
                        mBinding.btnRegister.setLoading(true)
                    }.onFailure { _, throwable ->
                        mBinding.btnRegister.setLoading(false)
                        Toast.makeText(requireContext(), throwable.message ?: "未知错误", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }

    private fun updateRegisterButtonState(){
        mBinding.btnRegister.isEnabled = accountNumberPass and passwordPass and passwordAgainPass
    }
}