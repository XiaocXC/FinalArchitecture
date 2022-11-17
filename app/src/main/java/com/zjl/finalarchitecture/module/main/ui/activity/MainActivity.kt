package com.zjl.finalarchitecture.module.main.ui.activity

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.navigation.findNavController
import com.blankj.utilcode.util.ToastUtils
import com.zjl.base.activity.BaseActivity
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.module.main.viewmodel.MainViewModel
import com.zjl.finalarchitecture.databinding.ActivityMainBinding
import org.json.JSONObject
import timber.log.Timber

/**
 * @description: 主界面
 * @author: zhou
 * @date : 2022/1/14 15:42
 */
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    var mExitTime = 0L

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        val jsonStr = "{\"msg\":\"error\",\"code\":20,\"data\":null}"
        val obj = JSONObject(jsonStr)
        val data = obj.optJSONObject("data")
        Timber.i("TEST: ${data == null}")
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