package com.zjl.finalarchitecture.module.mine.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import coil.load
import com.blankj.utilcode.util.ToastUtils
import com.kongzue.dialogx.dialogs.MessageDialog
import com.kongzue.dialogx.dialogs.PopTip
import com.kongzue.dialogx.dialogs.TipDialog
import com.kongzue.dialogx.dialogs.WaitDialog
import com.kongzue.dialogx.interfaces.DialogXStyle
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.ui.UiModel
import com.zjl.base.ui.onFailure
import com.zjl.base.ui.onSuccess
import com.zjl.base.utils.findNavController
import com.zjl.base.utils.launchAndCollectIn
import com.zjl.finalarchitecture.BuildConfig
import com.zjl.finalarchitecture.NavMainDirections
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.data.model.user.CombineUserInfoVO
import com.zjl.finalarchitecture.data.respository.datasouce.UserAuthDataSource
import com.zjl.finalarchitecture.databinding.FragmentMineBinding
import com.zjl.finalarchitecture.module.main.ui.fragment.MainFragmentDirections
import com.zjl.finalarchitecture.module.mine.viewmodel.MineViewModel
import com.zjl.finalarchitecture.module.webview.viewmodel.WebDataUrl
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber


class MineFragment : BaseFragment<FragmentMineBinding, MineViewModel>(), View.OnClickListener {

    override fun initViewAndEvent(savedInstanceState: Bundle?) {

        /* 头像 */
        mBinding.ivUserPhoto.setOnClickListener(this)

        /* 积分 */
        mBinding.linearIntegral.setOnClickListener(this)

        /* 收藏 */
        mBinding.linearCollect.setOnClickListener(this)

        /* 分享 */
        mBinding.linearShare.setOnClickListener(this)

        /* 稍后阅读 */
        mBinding.linearReadLater.setOnClickListener(this)

        /* 阅读历史 */
        mBinding.linearReadRecord.setOnClickListener(this)

        /* 开源项目 */
        mBinding.linearOpen.setOnClickListener(this)

        /* 关于作者 */
        mBinding.linearAboutMe.setOnClickListener(this)

        /* 系统设置 */
        mBinding.linearSetting.setOnClickListener(this)

        /* 退出登录 */
        mBinding.txtLogout.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        v?.let {
            itemClick(it)
        }
    }

    private fun itemClick(view: View) {
        if (!UserAuthDataSource.isLogin) {
            tipAndJumpLogin()
            return
        }
        when (view.id) {
            R.id.ivUserPhoto -> {

            }

            R.id.linearIntegral -> {
                findNavController().navigate(R.id.action_mainFragment_to_integralFragment)
            }

            R.id.linearCollect -> {

            }

            R.id.linearShare -> {

            }

            R.id.linearReadLater -> {

            }

            R.id.linearReadRecord -> {

            }

            R.id.linearOpen -> {

            }

            R.id.linearAboutMe -> {

            }

            R.id.linearSetting -> {

            }

            R.id.txtLogout -> {
                displaySignOutDialog()
            }
        }
    }

    /**
     * 未登录提示并且跳转登录界面
     */
    private fun tipAndJumpLogin() {
        TipDialog.show(R.string.tip_message_to_login, WaitDialog.TYPE.WARNING)
        lifecycleScope.launch {
            delay(2500)
            findNavController().navigate(NavMainDirections.actionGlobalSignIn())
        }
    }


    /**
     * 退出登录弹框
     */
    private fun displaySignOutDialog() {
        MessageDialog.show(
            R.string.preference_sign_out,
            R.string.description_sign_out_des_dialog,
            R.string.description_confirm
        ).setOkButton { _, _ ->
            mViewModel.signOut()
            false
        }
    }

    override fun createObserver() {

        // 获取用户数据回调
        mViewModel.userInfo.launchAndCollectIn(viewLifecycleOwner) {
            // 判断是否登录更新不同视图
            handleUserInfoFlowCollect(it)
        }

        // 退出登录成功的回调
        mViewModel.signOutEvent.launchAndCollectIn(this) {
            // 退出登录
            showToastTip(it)
        }
    }

    /**
     * 根据 userInfo 收集的数据进行处理
     * @param dataBean 用户数据
     */
    private fun handleUserInfoFlowCollect(dataBean: CombineUserInfoVO?) {
        if (dataBean == null) {
            mBinding.txtLogout.visibility = View.GONE
            mBinding.txtUserName.text = getString(R.string.description_not_login_tip)
            mBinding.txtTip.isVisible = false
            mBinding.ivUserPhoto.load(R.drawable.ic_default_user_logo)
            mBinding.tvCoin.isVisible = false
        } else {
            mBinding.txtLogout.visibility = View.VISIBLE
            mBinding.txtUserName.text = dataBean.userInfo.nickname
            mBinding.txtTip.isVisible = true
            mBinding.tvCoin.isVisible = true
            mBinding.ivUserPhoto.load(dataBean.userInfo.icon) {
                placeholder(R.drawable.ic_default_user_photo)
                error(R.drawable.ic_default_user_logo)
            }
            // 排名积分信息
            mBinding.txtTip.text = getString(
                R.string.me_my_coin_num, dataBean.coinInfo.level.toString(), dataBean.coinInfo.rank
            )
            //当前积分
            mBinding.tvCoin.text =
                getString(R.string.my_rank_coin, "${dataBean.coinInfo.coinCount}")
        }
    }

    /**
     * 退出登录
     */
    private fun showToastTip(uiState: UiModel<Unit>) {
        uiState.onSuccess {
            // 清理数据
            handleUserInfoFlowCollect(null)
            // 退出登录成功我们弹出
            TipDialog.show(getString(R.string.description_sign_out_success))
        }.onFailure { _, throwable ->
            TipDialog.show(throwable.message)
        }
    }

}