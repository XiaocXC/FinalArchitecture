package com.zjl.finalarchitecture.module.main.ui

import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation
import com.blankj.utilcode.util.ToastUtils
import com.zjl.base.activity.BaseActivity
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.module.main.viewmodel.MainViewModel
import com.zjl.finalarchitecture.databinding.ActivityMainBinding
import com.zjl.finalarchitecture.di.createBaseViewModel

/**
 * @description: 主界面
 * @author: zhou
 * @date : 2022/1/14 15:42
 */
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    var mExitTime = 0L

    override fun bindView(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun bindViewModel(): MainViewModel = createBaseViewModel()

    override fun initViewAndEvent() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val nav = Navigation.findNavController(this@MainActivity, R.id.mFragmentContainerView)
                if (nav.currentDestination != null && nav.currentDestination!!.id != R.id.mFragmentContainerView) {
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