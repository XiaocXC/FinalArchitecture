package com.zjl.finalarchitecture.module.main.ui.fragment

import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zjl.base.fragment.BaseFragment
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentMainBinding
import com.zjl.finalarchitecture.module.discovery.ui.fragment.DiscoveryFragment
import com.zjl.finalarchitecture.module.home.ui.fragment.HomeFragment
import com.zjl.finalarchitecture.module.mine.ui.fragment.MineFragment
import com.zjl.finalarchitecture.module.navigation.ui.fragment.NavigationFragment
import com.zjl.finalarchitecture.module.system.ui.fragment.SystemFragment


/**
 * @description: 主页面
 * @author: zhou
 * @date : 2022/1/14 15:42
 */
class MainFragment : BaseFragment<FragmentMainBinding>(){

    private val mFragmentList = arrayListOf<Fragment>()

    override fun bindView(): FragmentMainBinding = FragmentMainBinding.inflate(layoutInflater)

    override fun initViewAndEvent() {

        binding.mViewPager2.isUserInputEnabled = false
        binding.mViewPager2.offscreenPageLimit = 5
        binding.mViewPager2.adapter = object : FragmentStateAdapter(this){
            override fun getItemCount() = 5
            override fun createFragment(position: Int) = when(position){
                0 -> HomeFragment()
                1 -> SystemFragment()
                2-> DiscoveryFragment()
                3-> NavigationFragment()
                4-> MineFragment()
                else -> HomeFragment()
            }
        }


        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
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
            true
        }

    }

    override fun createObserver() {
    }

    override fun lazyLoadData() {
    }

    private fun switchFragment(position: Int): Boolean {
        binding.mViewPager2.setCurrentItem(position, false)
        return true
    }



}