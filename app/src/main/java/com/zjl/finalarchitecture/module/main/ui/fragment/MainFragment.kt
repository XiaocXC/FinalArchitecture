package com.zjl.finalarchitecture.module.main.ui.fragment

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.wwdablu.soumya.lottiebottomnav.FontBuilder
import com.wwdablu.soumya.lottiebottomnav.ILottieBottomNavCallback
import com.wwdablu.soumya.lottiebottomnav.MenuItemBuilder
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.ext.getAttrColor
import com.zjl.base.viewmodel.EmptyViewModel
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentMainBinding
import com.zjl.finalarchitecture.module.discovery.ui.fragment.DiscoveryFragment
import com.zjl.finalarchitecture.module.home.ui.fragment.HomeFragment
import com.zjl.finalarchitecture.module.mine.ui.fragment.MineFragment
import com.zjl.finalarchitecture.module.sysnav.ui.fragment.SysAndNavAndTutorFragment
import com.zjl.finalarchitecture.module.toolbox.ToolboxFragment


/**
 * @description: 主页面
 * @author: zhou
 * @date : 2022/1/14 15:42
 */
class MainFragment : BaseFragment<FragmentMainBinding, EmptyViewModel>() {

    private val lottieResWithMenu = listOf(
        "newtab/Tab_Shop_Active.json",
        "newtab/Tab_Community_Active.json",
        "newtab/Tab_Discovery_Active.json",
        "newtab/Tab_Knowledge_Active.json",
        "newtab/Tab_Mine_Active.json"
    )

    private val bottomNavlist: ArrayList<com.wwdablu.soumya.lottiebottomnav.MenuItem> =
        arrayListOf()

    private var lastNavPosition = 0

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        //viewpager2是否可以滑动
        mBinding.mViewPager2.isUserInputEnabled = false
        //全部缓存,避免切换回重新加载
        mBinding.mViewPager2.offscreenPageLimit = 5
        mBinding.mViewPager2.adapter =
            object : FragmentStateAdapter(childFragmentManager, viewLifecycleOwner.lifecycle) {
                override fun getItemCount() = 5
                override fun createFragment(position: Int) = when (position) {
                    0 -> HomeFragment()
                    1 -> SysAndNavAndTutorFragment()
                    2 -> DiscoveryFragment()
                    3 -> ToolboxFragment()
                    4 -> MineFragment()
                    else -> HomeFragment()
                }
            }

        createBottomNav()

        // 不按照默认BottomNavigation的Tint色调显示
//        mBinding.mBottomNavigationView.itemIconTintList = null
//        mBinding.mBottomNavigationView.setBackgroundColor(requireContext().getAttrColor(R.attr.colorSurface))

        // 动态生成Lottie的样式的底部Menu
//        mBinding.mBottomNavigationView.menu.run {
//            for (index in lottieResWithMenu.indices) {
//                add(Menu.NONE, index, Menu.NONE, index.toString())
//                generateLottieDrawable(index, lottieResWithMenu[index])
//            }
//        }
        //初始化底部导航栏
//        mBinding.mBottomNavigationView.setOnItemSelectedListener {
//            switchFragment(it.itemId)
//            applyNavigationItem(it)
//            lastNavPosition = it.itemId
//            //这里注意返回true,否则点击失效
//            true
//        }

        //viewpager2联动bottomNavigationView
//        mBinding.mViewPager2.registerOnPageChangeCallback(object :
//            ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                mBinding.mBottomNavigationView.menu.getItem(position).isChecked = true
//            }
//        })

    }

    override fun createObserver() {
    }


    private fun createBottomNav() {

        val fontItem = FontBuilder.create("首页")
            .selectedTextColor(ContextCompat.getColor(requireContext(), R.color.md_colorPrimary))
            .unSelectedTextColor(ContextCompat.getColor(requireContext(), R.color.base_panda))
            .selectedTextSize(11) //SP
            .unSelectedTextSize(11) //SP
            .build()

//        val fontItemSystem = FontBuilder.create("体系")
//            .selectedTextColor(ContextCompat.getColor(requireContext(), R.color.md_colorPrimary))
//            .unSelectedTextColor(ContextCompat.getColor(requireContext(), R.color.base_panda))
//            .selectedTextSize(13) //SP
//            .unSelectedTextSize(12) //SP
//            .build()
//
//        val fontItemFind = FontBuilder.create("发现")
//            .selectedTextColor(ContextCompat.getColor(requireContext(), R.color.md_colorPrimary))
//            .unSelectedTextColor(ContextCompat.getColor(requireContext(), R.color.base_panda))
//            .selectedTextSize(13) //SP
//            .unSelectedTextSize(12) //SP
//            .build()
//
//        val fontItemTool = FontBuilder.create("工具")
//            .selectedTextColor(ContextCompat.getColor(requireContext(), R.color.md_colorPrimary))
//            .unSelectedTextColor(ContextCompat.getColor(requireContext(), R.color.base_panda))
//            .selectedTextSize(13) //SP
//            .unSelectedTextSize(12) //SP
//            .build()
//
//        val fontItemMine = FontBuilder.create("我的")
//            .selectedTextColor(ContextCompat.getColor(requireContext(), R.color.md_colorPrimary))
//            .unSelectedTextColor(ContextCompat.getColor(requireContext(), R.color.base_panda))
//            .selectedTextSize(13) //SP
//            .unSelectedTextSize(12) //SP
//            .build()

        val item1: com.wwdablu.soumya.lottiebottomnav.MenuItem = MenuItemBuilder.create(
            "newtab/Tab_Shop_Active.json",
            com.wwdablu.soumya.lottiebottomnav.MenuItem.Source.Assets,
            fontItem,
            "fontItemHome"
        ).pausedProgress(0f).loop(false)
            .unSelectedLottieName("newtab/Tab_Shop_Active.json").build()

        val item2: com.wwdablu.soumya.lottiebottomnav.MenuItem = MenuItemBuilder.create(
            "newtab/Tab_Knowledge_Active.json",
            com.wwdablu.soumya.lottiebottomnav.MenuItem.Source.Assets,
            FontBuilder.create(fontItem).setTitle("体系").build(),
            "fontItemSystem"
        ).pausedProgress(0f).loop(false)
            .unSelectedLottieName("newtab/Tab_Knowledge_Active.json").build()

        val item3: com.wwdablu.soumya.lottiebottomnav.MenuItem = MenuItemBuilder.create(
            "newtab/Tab_Discovery_Active.json",
            com.wwdablu.soumya.lottiebottomnav.MenuItem.Source.Assets,
            FontBuilder.create(fontItem).setTitle("发现").build(),
            "fontItemFind"
        ).pausedProgress(0f).loop(false)
            .unSelectedLottieName("newtab/Tab_Discovery_Active.json").build()

        val item4: com.wwdablu.soumya.lottiebottomnav.MenuItem = MenuItemBuilder.create(
            "newtab/Tab_Community_Active.json",
            com.wwdablu.soumya.lottiebottomnav.MenuItem.Source.Assets,
            FontBuilder.create(fontItem).setTitle("工具").build(),
            "fontItemTool"
        ).pausedProgress(0f).loop(false)
            .unSelectedLottieName("newtab/Tab_Community_Active.json").build()

        val item5: com.wwdablu.soumya.lottiebottomnav.MenuItem = MenuItemBuilder.create(
            "newtab/Tab_Mine_Active.json",
            com.wwdablu.soumya.lottiebottomnav.MenuItem.Source.Assets,
            FontBuilder.create(fontItem).setTitle("我的").build(),
            "fontItemMine"
        ).pausedProgress(0f)
            .loop(false).unSelectedLottieName("newtab/Tab_Mine_Active.json").build()

        bottomNavlist.add(item1)
        bottomNavlist.add(item2)
        bottomNavlist.add(item3)
        bottomNavlist.add(item4)
        bottomNavlist.add(item5)

        mBinding.lottieBottomNav.setMenuItemList(bottomNavlist)
        mBinding.lottieBottomNav.selectedIndex = lastNavPosition

        mBinding.lottieBottomNav.setCallback(object : ILottieBottomNavCallback {
            override fun onMenuSelected(
                oldIndex: Int,
                newIndex: Int,
                menuItem: com.wwdablu.soumya.lottiebottomnav.MenuItem
            ) {
                lastNavPosition = newIndex
                switchFragment(lastNavPosition)
            }

            override fun onAnimationStart(
                index: Int,
                menuItem: com.wwdablu.soumya.lottiebottomnav.MenuItem?
            ) {

            }

            override fun onAnimationEnd(
                index: Int,
                menuItem: com.wwdablu.soumya.lottiebottomnav.MenuItem?
            ) {

            }

            override fun onAnimationCancel(
                index: Int,
                menuItem: com.wwdablu.soumya.lottiebottomnav.MenuItem?
            ) {

            }

        })
    }

    /**
     * 切换fragment
     */
    private fun switchFragment(
        position: Int
    ): Boolean {
        //smoothScroll设置为false 不然会有转场效果
        mBinding.mViewPager2.setCurrentItem(position, false)
        return true
    }


//    private fun applyNavigationItem(item: MenuItem) {
//        playLottieAnimation(item)
//    }
//
//    private fun playLottieAnimation(item: MenuItem) {
//        val currentIcon = item.icon as? LottieDrawable
//        currentIcon?.start()
//
//        if (item.itemId != lastNavPosition) {
////            val lastLottieDrawable = mBinding.mBottomNavigationView.menu.getItem(lastNavPosition)?.icon as? LottieDrawable
////            // 将上次的动画复原
////            lastLottieDrawable?.stop()
////            lastLottieDrawable?.frame = lastLottieDrawable?.minFrame?.toInt() ?: 0
//            val lastItem = mBinding.mBottomNavigationView.menu.getItem(lastNavPosition)
//            lastItem?.icon =
//                generateLottieDrawable(lastNavPosition, lottieResWithMenu[lastNavPosition])
//        }
//
//    }
//
//    private fun generateLottieDrawable(index: Int, jsonKey: String): LottieDrawable {
//        val menuItem = mBinding.mBottomNavigationView.menu.getItem(index)
//        val lottieDrawable = LottieDrawable().apply {
//            val result = LottieCompositionFactory.fromAssetSync(requireContext(), jsonKey)
//            callback = mBinding.mBottomNavigationView
//            composition = result.value
//        }
//        menuItem.icon = lottieDrawable
//        return lottieDrawable
//    }

}