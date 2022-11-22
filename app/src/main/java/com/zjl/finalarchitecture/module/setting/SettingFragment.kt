package com.zjl.finalarchitecture.module.setting

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.preference.*
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.appbar.MaterialToolbar
import com.kongzue.dialogx.dialogs.MessageDialog
import com.zjl.base.ui.onFailure
import com.zjl.base.ui.onSuccess
import com.zjl.base.utils.findNavController
import com.zjl.base.utils.launchAndCollectIn
import com.zjl.finalarchitecture.NavMainDirections
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.module.webview.viewmodel.WebDataUrl

/**
 * @author Xiaoc
 * @since 2022-04-24
 *
 * 设置界面
 */
class SettingFragment: PreferenceFragmentCompat() {

    private var toolbarView: MaterialToolbar? = null
    private var containerView: FrameLayout? = null

    private val settingViewModel by viewModels<SettingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 加入Toolbar
        view.fitsSystemWindows = true
        //这里重写根据PreferenceFragmentCompat 的布局 ，往他的根布局插入了一个toolbar
        containerView = view.findViewById(android.R.id.list_container)
        containerView?.let {
            //转为线性布局
            val linearLayout = it.parent as? LinearLayout
            linearLayout?.run {
                toolbarView = MaterialToolbar(requireContext()).apply {
                    title = getString(R.string.description_app_setting)
                    isTitleCentered = true
                    setNavigationIcon(R.drawable.ic_arrow_back)
                    setNavigationOnClickListener {
                        findNavController().navigateUp()
                    }
                }
                addView(toolbarView, 0)
            }
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // 设置XML的设置内容
        setPreferencesFromResource(R.xml.setting, null)

        findPreference<Preference>(getString(R.string.key_preference_github_website))?.apply {
            setOnPreferenceClickListener {
                findNavController().navigate(NavMainDirections.actionGlobalToWebFragment(WebDataUrl(summary.toString(), null)))
                true
            }
        }

        settingViewModel.signOutEvent.launchAndCollectIn(this){
            it.onSuccess {
                // 退出登录成功我们弹出
                ToastUtils.showShort(getString(R.string.description_sign_out_success))
                findNavController().navigateUp()
            }.onFailure { _, throwable ->
                ToastUtils.showShort(throwable.message)
            }
        }

        settingViewModel.userAuthDataSource.basicUserInfoVO.launchAndCollectIn(this) {
            // 只有登录了才显示退出登录按钮
            findPreference<Preference>(getString(R.string.key_preference_sign_out))?.isVisible = settingViewModel.userAuthDataSource.isLogin
        }
    }

    override fun onCreateAdapter(preferenceScreen: PreferenceScreen): RecyclerView.Adapter<*> {
        // 我们有自定义的退出登录按钮，我们重写Setting内置的Adapter
        // 来初始化我们自定义布局的点击事件
        return object : PreferenceGroupAdapter(preferenceScreen) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
                super.onCreateViewHolder(parent, viewType)
                    .apply(this@SettingFragment::initSignOutButton)

        }
    }

    private fun initSignOutButton(holder: PreferenceViewHolder){
        when (holder.itemView.id) {
            R.id.preferenceSignOut -> {
                holder.itemView.findViewById<View>(R.id.btnSignOut).setOnClickListener {
                    MessageDialog.show(R.string.preference_sign_out, R.string.description_sign_out_des_dialog, R.string.description_confirm)
                        .setOkButton { _, _ ->
                            settingViewModel.signOut()
                            false
                        }
                }
            }
        }
    }

}