package com.zjl.finalarchitecture.module.mine.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import coil.load
import com.blankj.utilcode.util.ToastUtils
import com.kongzue.dialogx.dialogs.MessageDialog
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.ui.onFailure
import com.zjl.base.ui.onSuccess
import com.zjl.base.utils.findNavController
import com.zjl.base.utils.launchAndCollectIn
import com.zjl.finalarchitecture.BuildConfig
import com.zjl.finalarchitecture.NavMainDirections
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.data.respository.datasouce.UserAuthDataSource
import com.zjl.finalarchitecture.databinding.FragmentMineBinding
import com.zjl.finalarchitecture.module.main.ui.fragment.MainFragmentDirections
import com.zjl.finalarchitecture.module.mine.viewmodel.MineViewModel
import com.zjl.finalarchitecture.module.webview.viewmodel.WebDataUrl
import timber.log.Timber


class MineFragment : BaseFragment<FragmentMineBinding, MineViewModel>() {

    override fun initViewAndEvent(savedInstanceState: Bundle?) {

        val lp: ConstraintLayout.LayoutParams =
            mBinding.cl1.layoutParams as ConstraintLayout.LayoutParams
        mBinding.waveView.setOnWaveAnimationListener { y ->
            lp.setMargins(0, 0, 0, y.toInt() + 90)
            mBinding.cl1.layoutParams = lp
        }

        mBinding.ivUserPhoto.setOnClickListener {
            if (UserAuthDataSource.isLogin) {
                // TODO 个人主页
            } else {
                findNavController().navigate(NavMainDirections.actionGlobalSignIn())
            }
        }

        mBinding.sbIntegral.setOnClickListener {
            findNavController().navigate(NavMainDirections.actionGlobalToIntegralFragment())
        }

        mBinding.txtLogout.setOnClickListener {
            MessageDialog.show(R.string.preference_sign_out, R.string.description_sign_out_des_dialog, R.string.description_confirm)
                .setOkButton { _, _ ->
                    mViewModel.signOut()
                    false
                }
        }

        mBinding.sbOpenProject.setOnClickListener {
            findNavController().navigate(NavMainDirections.actionGlobalToWebFragment(
                WebDataUrl(getString(R.string.me_app_github_url), null))
            )
        }

        // 主题切换
        mBinding.sbTheme.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToThemeChangeFragment())
        }

        // 作者
        mBinding.sbAboutWe.setRightText(R.string.me_app_author)

        // 当前版本
        mBinding.sbVersion.setRightText(BuildConfig.VERSION_NAME)
    }

    override fun createObserver() {
        mViewModel.userInfo.launchAndCollectIn(viewLifecycleOwner) {
            // 判断是否登录更新不同视图
            if (it == null) {
                mBinding.txtLogout.visibility = View.GONE
                mBinding.userSetting.visibility = View.GONE
                mBinding.txtUserName.text = getString(R.string.description_not_login_tip)
                mBinding.txtTip.isVisible = false
                mBinding.ivUserPhoto.setImageResource(R.drawable.ic_default_user_logo)
            } else {
                mBinding.txtLogout.visibility = View.VISIBLE
                mBinding.userSetting.visibility = View.VISIBLE
                mBinding.txtUserName.text = it.userInfo.nickname
                mBinding.txtTip.isVisible = true
                mBinding.ivUserPhoto.load(it.userInfo.icon) {
                    placeholder(R.drawable.ic_default_user_photo)
                    error(R.drawable.ic_default_user_logo)
                }
                // 排名积分信息
                mBinding.txtTip.text = getString(
                    R.string.me_my_coin_num,
                    it.coinInfo.coinCount.toString(),
                    it.coinInfo.level.toString(),
                    it.coinInfo.rank
                )

                mBinding.sbIntegral.rightText = "当前积分:${it.coinInfo.coinCount}"
                mBinding.sbIntegral.setRightColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.base_blue_100
                    )
                )
            }
        }

        // 退出登录成功的通知
        mViewModel.signOutEvent.launchAndCollectIn(this){
            it.onSuccess {
                // 退出登录成功我们弹出
                ToastUtils.showShort(getString(R.string.description_sign_out_success))
            }.onFailure { _, throwable ->
                ToastUtils.showShort(throwable.message)
            }
        }
    }


}