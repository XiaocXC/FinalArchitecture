package com.zjl.finalarchitecture.module.home.ui.fragment

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.ext.init
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentHomeBinding
import com.zjl.finalarchitecture.module.home.viewmodel.HomeViewModel


class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    //文章,广场,问答,项目,微信和对应Fragment
    private var mTitleArrayData = arrayListOf("文章", "广场", "问答", "项目","微信")
    private var mFragmentList: ArrayList<Fragment> = arrayListOf()

    //数据ViewModel
    private val mViewModel : HomeViewModel by viewModels()

    //无参构造方法
    init {
        mFragmentList.add(ArticleFragment.newInstance())
        mFragmentList.add(PlazaFragment.newInstance())
        mFragmentList.add(AskFragment.newInstance())
        mFragmentList.add(ProjectFragment.newInstance())
        mFragmentList.add(WeChatFragment.newInstance())
    }

    override fun bindView() = FragmentHomeBinding.inflate(layoutInflater)

    override fun initViewAndEvent() {
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
        //绑定TabLayout ViewPager2
        TabLayoutMediator(mBinding.mTabLayout,mBinding.mViewPager2,object :
            TabLayoutMediator.TabConfigurationStrategy {
            override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                tab.text = mTitleArrayData[position]
            }
        }).attach()
    }

}