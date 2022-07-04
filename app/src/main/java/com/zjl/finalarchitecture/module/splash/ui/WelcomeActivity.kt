package com.zjl.finalarchitecture.module.splash.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.zjl.base.activity.BaseActivity
import com.zjl.finalarchitecture.utils.CacheUtil
import com.zjl.base.utils.ext.gone
import com.zjl.base.utils.ext.visible
import com.zjl.finalarchitecture.databinding.ActivityWelcomeBinding
import com.zjl.finalarchitecture.module.main.ui.activity.MainActivity
import com.zjl.finalarchitecture.module.splash.ui.adapter.WelcomeBannerAdapter
import com.zjl.finalarchitecture.module.splash.viewmodel.WelcomeViewModel

/**
 * @description: 欢迎页 or 启动页
 * @author: zhou
 * @date : 2022/1/14 15:42
 */
class WelcomeActivity : BaseActivity<ActivityWelcomeBinding, WelcomeViewModel>() {

    private var mItemTextArray = arrayOf("Z", "C", "love you")

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        //防止出现按Home键回到桌面时，再次点击重新进入该界面bug
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT !== 0) {
            finish()
            return
        }
        setUpViewPager()
        setClickListener()
    }

    override fun createObserver() {
    }

    private fun setUpViewPager() {
        //如果是第一次进入APP，则展示轮播图
        if (CacheUtil.isFirstEnterApp()) {
            mBinding.imgLogo.gone()
            mBinding.mViewPager.setAdapter(WelcomeBannerAdapter())
            mBinding.mViewPager.setLifecycleRegistry(lifecycle)
            mBinding.mViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        if(position == mItemTextArray.size -1){
                            mBinding.txtEnter.visible()
                            mBinding.imgLogo.visible()
                        }else{
                            mBinding.txtEnter.gone()
                            mBinding.imgLogo.gone()
                        }
                    }
                })
            mBinding.mViewPager.create(mItemTextArray.toList())
        } else {
            mBinding.txtEnter.gone()
            mBinding.imgLogo.visible()
            mBinding.mViewPager.postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                //带点渐变动画
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            },500)
        }
    }

    private fun setClickListener() {
        mBinding.txtEnter.setOnClickListener {
            CacheUtil.setFirstEnterApp(false)
            startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
            finish()
            //带点渐变动画
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

}