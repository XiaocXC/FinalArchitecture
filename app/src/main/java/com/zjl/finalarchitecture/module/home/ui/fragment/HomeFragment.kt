package com.zjl.finalarchitecture.module.home.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.blankj.utilcode.util.LogUtils
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.ext.doOnApplyWindowInsets
import com.zjl.base.utils.ext.init
import com.zjl.base.utils.ext.reduceDragSensitivity
import com.zjl.base.utils.findNavController
import com.zjl.base.viewmodel.EmptyViewModel
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentHomeBinding
import com.zjl.finalarchitecture.module.home.viewmodel.HomeViewModel

/**
 * @description:首页父Fragment管理5个子fragment
 * @author: zhou
 * @date : 2022/1/20 17:52
 */
class HomeFragment : BaseFragment<FragmentHomeBinding, EmptyViewModel>() {

    //文章,广场,问答,项目,微信和对应Fragment
    private var mTitleArrayData = arrayListOf("文章", "广场", "问答", "项目","微信")
    private var mFragmentList: ArrayList<Fragment> = arrayListOf()

    private var tabLayoutMediator: TabLayoutMediator? = null

    //无参构造方法
    init {
        mFragmentList.add(ArticleFragment.newInstance())
        mFragmentList.add(PlazaFragment.newInstance())
        mFragmentList.add(AskFragment.newInstance())
        mFragmentList.add(ProjectFragment.newInstance())
        mFragmentList.add(WeChatFragment.newInstance())
    }

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        // 给整个布局加上一个状态栏的高度
        mBinding.root.doOnApplyWindowInsets { view, insets, _ ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.run {
                updatePadding(top = systemBars.top)
            }
        }

        //mAppBarLayout
        mBinding.mAppBarLayout.addOnOffsetChangedListener(object :
            AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
            }
        })
        //mSearchLayout
        mBinding.mSearchLayout.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                findNavController().navigate(R.id.action_mainFragment_to_searchFragment)
            }
        })
        initWidget()
    }

    override fun createObserver() {
    }

    private fun initWidget(){
        //ViewPager2初始化
        mBinding.mViewPager2.init(this, mFragmentList)
        mBinding.mViewPager2.reduceDragSensitivity()

        //绑定TabLayout ViewPager2
        tabLayoutMediator =
            TabLayoutMediator(mBinding.mTabLayout,mBinding.mViewPager2)
        { tab, position ->
            tab.text = mTitleArrayData[position]
        }.apply {
            attach()
        }
    }

    override fun onDestroyView() {
        tabLayoutMediator?.detach()
        tabLayoutMediator = null
        mBinding.mViewPager2.adapter = null
        super.onDestroyView()
        LogUtils.eTag("zhou::","onDestroyView")
    }

}