package com.zjl.finalarchitecture.module.home.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.blankj.utilcode.util.LogUtils
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.ext.init
import com.zjl.base.utils.ext.reduceDragSensitivity
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentHomeBinding
import com.zjl.finalarchitecture.module.home.viewmodel.HomeViewModel


class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    //文章,广场,问答,项目,微信和对应Fragment
    private var mTitleArrayData = arrayListOf("文章", "广场", "问答", "项目","微信")
    private var mFragmentList: ArrayList<Fragment> = arrayListOf()

    //数据ViewModel
    private val mViewModel : HomeViewModel by viewModels()

    private var tabLayoutMediator: TabLayoutMediator? = null

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
        mBinding.mViewPager2.reduceDragSensitivity()

        //绑定TabLayout ViewPager2
        tabLayoutMediator = TabLayoutMediator(mBinding.mTabLayout,mBinding.mViewPager2
        ) { tab, position ->
            tab.text = mTitleArrayData[position]
        }.apply {
            attach()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        LogUtils.eTag("zhou::","onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtils.eTag("zhou::","onCreate")
    }

    override fun onStart() {
        super.onStart()
        LogUtils.eTag("zhou::","onStart")
    }

    override fun onResume() {
        super.onResume()
        LogUtils.eTag("zhou::","onResume")
    }

    override fun onPause() {
        super.onPause()
        LogUtils.eTag("zhou::","onPause")
    }

    override fun onStop() {
        super.onStop()
        LogUtils.eTag("zhou::","onStop")
    }

    override fun onDestroyView() {
        tabLayoutMediator?.detach()
        tabLayoutMediator = null

        super.onDestroyView()
        LogUtils.eTag("zhou::","onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtils.eTag("zhou::","onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        LogUtils.eTag("zhou::","onDetach")
    }

}