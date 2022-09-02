package com.zjl.finalarchitecture.module.mine.ui.fragment

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.findNavController
import com.zjl.base.utils.launchAndRepeatWithViewLifecycle
import com.zjl.base.viewmodel.EmptyViewModel
import com.zjl.finalarchitecture.NavMainDirections
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.data.model.UserInfoVO.Companion.NOT_LOGIN_USER
import com.zjl.finalarchitecture.data.respository.datasouce.UserAuthDataSource
import com.zjl.finalarchitecture.databinding.FragmentMineBinding
import com.zjl.finalarchitecture.module.mine.viewmodel.MineViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.io.File


class MineFragment : BaseFragment<FragmentMineBinding, MineViewModel>() {

    override fun initViewAndEvent(savedInstanceState: Bundle?) {

        mBinding.containerUserInfo.setOnClickListener {
            if(UserAuthDataSource.isLogin){
                // TODO 个人主页
            } else {
                findNavController().navigate(NavMainDirections.actionGlobalSignIn())
            }
        }
    }

    override fun createObserver() {

        launchAndRepeatWithViewLifecycle {

            launch {
                mViewModel.userInfo.collectLatest {
                    // 判断是否登录更新不同视图
                    if(it == NOT_LOGIN_USER){
                        mBinding.tvUserName.text = getString(R.string.description_not_login_tip)
                    } else {
                        mBinding.tvUserName.text = it.nickname
                    }
                }
            }
        }
    }

}