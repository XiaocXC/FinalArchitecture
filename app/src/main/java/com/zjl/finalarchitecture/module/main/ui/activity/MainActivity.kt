package com.zjl.finalarchitecture.module.main.ui.activity

import android.app.Activity
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.navigation.findNavController
import com.blankj.utilcode.util.ToastUtils
import com.zjl.base.activity.BaseActivity
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.module.main.viewmodel.MainViewModel
import com.zjl.finalarchitecture.databinding.ActivityMainBinding

/**
 * @description: 主界面
 * @author: zhou
 * @date : 2022/1/14 15:42
 */
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    var mExitTime = 0L

    override fun bindView(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun bindViewModel(): MainViewModel{
        val vm by viewModels<MainViewModel>()
        return vm
    }

    override fun initViewAndEvent() {
        // 配置Navigation
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val nav = findNavController(R.id.mFragmentContainerView)
                if (nav.currentDestination != null && nav.currentDestination?.id != R.id.mainFragment) {
                    //如果当前界面不是主页，那么直接调用返回即可
                    nav.navigateUp()
                } else {
                    //是主页
                    if (System.currentTimeMillis() - mExitTime > 2000) {
                        ToastUtils.showShort("再按一次退出程序")
                        mExitTime = System.currentTimeMillis()
                    } else {
                        finish()
                    }
                }
            }
        })
    }

    override fun createObserver() {
    }
}