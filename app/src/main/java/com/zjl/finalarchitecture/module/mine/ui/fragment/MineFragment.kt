package com.zjl.finalarchitecture.module.mine.ui.fragment

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import coil.load
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.findNavController
import com.zjl.base.utils.launchAndCollectIn
import com.zjl.finalarchitecture.NavMainDirections
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.data.respository.datasouce.UserAuthDataSource
import com.zjl.finalarchitecture.databinding.FragmentMineBinding
import com.zjl.finalarchitecture.module.mine.viewmodel.MineViewModel


class MineFragment : BaseFragment<FragmentMineBinding, MineViewModel>(), SwipeRefreshLayout.OnRefreshListener {

    override fun initViewAndEvent(savedInstanceState: Bundle?) {

        mBinding.containerUserInfo.setOnClickListener {
            if(UserAuthDataSource.isLogin){
                // TODO 个人主页
            } else {
                findNavController().navigate(NavMainDirections.actionGlobalSignIn())
            }
        }

        mBinding.containerRefresh.setOnRefreshListener(this)

        mBinding.containerAppSetting.setOnClickListener {
            findNavController().navigate(NavMainDirections.actionGlobalToSettingFragment())
        }
    }

    override fun createObserver() {

        mViewModel.userInfo.launchAndCollectIn(viewLifecycleOwner){
            mBinding.containerRefresh.isRefreshing = false

            // 判断是否登录更新不同视图
            if(it == null){
                mBinding.tvUserName.text = getString(R.string.description_not_login_tip)
                mBinding.tvTip.isVisible = false
                mBinding.ivUserPhoto.setImageResource(R.drawable.ic_default_user_photo)
            } else {
                mBinding.tvUserName.text = it.userInfo.nickname
                mBinding.tvTip.isVisible = true
                mBinding.ivUserPhoto.load(it.userInfo.icon){
                    placeholder(R.drawable.ic_default_user_photo)
                    error(R.drawable.ic_default_user_photo)
                }
                // 排名积分信息
                mBinding.tvTip.text = getString(R.string.me_my_coin_num, it.coinInfo.coinCount.toString(), it.coinInfo.level.toString(), it.coinInfo.rank.toString())
            }
        }
    }

    override fun onRefresh() {
        // 更新用户信息
        mViewModel.initData()
    }

}