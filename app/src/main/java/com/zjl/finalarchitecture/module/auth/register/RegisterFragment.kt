package com.zjl.finalarchitecture.module.auth.register

import android.os.Bundle
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.findNavController
import com.zjl.finalarchitecture.databinding.FragmentRegisterBinding

/**
 * @author Xiaoc
 * @since  2022-09-06
 *
 * 注册界面
 **/
class RegisterFragment: BaseFragment<FragmentRegisterBinding, RegisterViewModel>() {

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        mBinding.toolbarRegister.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        mBinding.btnRegister.setOnClickListener {

        }
    }

    override fun createObserver() {

    }
}