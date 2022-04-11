package com.zjl.finalarchitecture.module.main.ui.fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.zjl.base.fragment.BaseFragment
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentMainBinding
import com.zjl.finalarchitecture.module.discovery.ui.fragment.DiscoveryFragment
import com.zjl.finalarchitecture.module.mine.ui.fragment.MineFragment
import com.zjl.finalarchitecture.module.navigation.ui.fragment.BlankFragment
import com.zjl.finalarchitecture.module.sysnav.ui.fragment.SysAndNavFragment
import com.zjl.finalarchitecture.module.home.ui.fragment.HomeFragment


/**
 * @description: 主页面
 * @author: zhou
 * @date : 2022/1/14 15:42
 */
class MainFragment : BaseFragment<FragmentMainBinding>() {

    override fun bindView(): FragmentMainBinding = FragmentMainBinding.inflate(layoutInflater)

    override fun initViewAndEvent() {
        //viewpager2是否可以滑动
        mBinding.mViewPager2.isUserInputEnabled = false
        //全部缓存,避免切换回重新加载
        mBinding.mViewPager2.offscreenPageLimit = 5
        mBinding.mViewPager2.adapter = object : FragmentStateAdapter(childFragmentManager, viewLifecycleOwner.lifecycle) {
            override fun getItemCount() = 5
            override fun createFragment(position: Int) = when (position) {
                0 -> HomeFragment()
                1 -> SysAndNavFragment()
                2 -> DiscoveryFragment()
                3 -> BlankFragment()
                4 -> MineFragment()
                else -> HomeFragment()
            }
        }

        //初始化底部导航栏
        mBinding.mBottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    switchFragment(0)
                }
                R.id.system -> {
                    switchFragment(1)
                }
                R.id.discovery -> {
                    switchFragment(2)
                }
                R.id.navigation -> {
                    switchFragment(3)
                }
                R.id.mine -> {
                    switchFragment(4)
                }
            }
            //这里注意返回true,否则点击失效
            true
        }

        //viewpager2联动bottomNavigationView
        mBinding.mViewPager2.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mBinding.mBottomNavigationView.menu.getItem(position).isChecked = true
            }
        })

    }

    override fun createObserver() {
    }

    private fun switchFragment(position: Int): Boolean {
        //smoothScroll设置为false 不然会有转场效果
        mBinding.mViewPager2.setCurrentItem(position, false)
        return true
    }


}