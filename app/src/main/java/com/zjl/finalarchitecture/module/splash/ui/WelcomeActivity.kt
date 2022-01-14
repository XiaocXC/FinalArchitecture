package com.zjl.finalarchitecture.module.splash.ui

import android.content.Intent
import com.zjl.base.activity.BaseActivity
import com.zjl.base.utils.CacheUtil
import com.zjl.base.utils.ext.gone
import com.zjl.finalarchitecture.databinding.ActivityWelcomeBinding
import com.zjl.finalarchitecture.di.createBaseViewModel
import com.zjl.finalarchitecture.module.splash.ui.adapter.WelcomeBannerAdapter
import com.zjl.finalarchitecture.module.splash.viewmodel.WelcomeViewModel

class WelcomeActivity : BaseActivity<ActivityWelcomeBinding, WelcomeViewModel>() {

    private var mItemTextArray = arrayOf("唱", "跳", "r a p")

    override fun bindView(): ActivityWelcomeBinding {
        return ActivityWelcomeBinding.inflate(layoutInflater)
    }

    override fun bindViewModel() : WelcomeViewModel = createBaseViewModel()

    override fun initViewAndEvent() {

        //防止出现按Home键回到桌面时，再次点击重新进入该界面bug
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT !== 0) {
            finish()
            return
        }

        //如果是第一次进入APP，则展示轮播图
        if(CacheUtil.isFirstEnterApp()){
            mBinding.imgLogo.gone()

            mBinding.mViewPager.setAdapter(WelcomeBannerAdapter())
            mBinding.mViewPager.setLifecycleRegistry(lifecycle)

            mBinding.mViewPager.apply {
                adapter = WelcomeBannerAdapter()
                setLifecycleRegistry(lifecycle)
            }

        }else{

        }

    }

    override fun createObserver() {
    }
}