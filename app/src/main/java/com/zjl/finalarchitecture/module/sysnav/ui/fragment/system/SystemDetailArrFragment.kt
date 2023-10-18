package com.zjl.finalarchitecture.module.sysnav.ui.fragment.system

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.gyf.immersionbar.ImmersionBar
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.ext.init
import com.zjl.base.utils.ext.reduceDragSensitivity
import com.zjl.base.utils.findNavController
import com.zjl.base.utils.launchAndCollectIn
import com.zjl.base.utils.navArgs
import com.zjl.finalarchitecture.data.model.ClassifyVO
import com.zjl.finalarchitecture.databinding.FragmentSystemDetailArrBinding
import com.zjl.finalarchitecture.module.sysnav.ui.fragment.system.child.SystemDetailChildFragment
import com.zjl.finalarchitecture.module.sysnav.viewmodel.SystemDetailArrViewModel

class SystemDetailArrFragment : BaseFragment<FragmentSystemDetailArrBinding, SystemDetailArrViewModel>() {

    private val args by navArgs<SystemDetailArrFragmentArgs>()

    private var mTitleArrayData : ArrayList<String> = arrayListOf()
    private var mFragmentList: ArrayList<Fragment> = arrayListOf()

//    private lateinit var systemDetailArrViewPagerAdapter: SystemDetailArrViewPagerAdapter

    /**
     * TabLayoutMediator可能需要在onDestroyView中手都detach，否则可能出现内存泄漏
     */
    private var tabLayoutMediator: TabLayoutMediator? = null

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        //标题
        mBinding.toolbarSystemArr.title = args.detailData.name

        mBinding.toolbarSystemArr.setNavigationOnClickListener {
            findNavController().navigateUp()
        }


        mBinding.vpSystemInner.reduceDragSensitivity()

//      用法2
//        systemDetailArrViewPagerAdapter = SystemDetailArrViewPagerAdapter(
//            emptyList(),
//            this.childFragmentManager,
//            viewLifecycleOwner.lifecycle
//        )
//        mBinding.vpSystemInner.adapter = systemDetailArrViewPagerAdapter

//        tabLayoutMediator = TabLayoutMediator(
//            mBinding.tabSystem,
//            mBinding.vpSystemInner
//        ) { tab, index ->
//            tab.text = systemDetailArrViewPagerAdapter.children[index].name
//        }.apply {
//            attach()
//        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun createObserver() {

        mViewModel.systemChild.launchAndCollectIn(viewLifecycleOwner){
            // 展示ViewPager内容
            val (index, ids) = it
            //用法1
            for (i in it.second) {
                mFragmentList.add(SystemDetailChildFragment.newInstance(i.id))
                mTitleArrayData.add(i.name)
            }
            mBinding.vpSystemInner.init(this@SystemDetailArrFragment,mFragmentList)

            tabLayoutMediator = TabLayoutMediator(
                mBinding.tabSystem,
                mBinding.vpSystemInner
            ) { tab, index ->
                tab.text = mTitleArrayData[index]
            }.apply {
                attach()
            }
            //用法2
//            systemDetailArrViewPagerAdapter.children = ids
//            systemDetailArrViewPagerAdapter.notifyDataSetChanged()
            mBinding.vpSystemInner.setCurrentItem(index, false)
        }
    }

    override fun onDestroyView() {
        // 我们在视图销毁时，将Tab绑定器还有ViewPager2的adapter手动置为空
        // 因为ViewPager2可能导致内存泄漏的BUG
        // https://issuetracker.google.com/issues/151212195
        // https://issuetracker.google.com/issues/154751401
        tabLayoutMediator?.detach()
        tabLayoutMediator = null
        mBinding.vpSystemInner.adapter = null
        super.onDestroyView()
    }

    class SystemDetailArrViewPagerAdapter(
        var children: List<ClassifyVO>,
        fragmentManager: FragmentManager,
        viewLifeCycle: Lifecycle
    ) : FragmentStateAdapter(fragmentManager, viewLifeCycle) {

        override fun getItemCount(): Int = children.size

        override fun createFragment(position: Int): Fragment {
            return SystemDetailChildFragment.newInstance(children[position].id)
        }
    }

    override fun configImmersive(immersionBar: ImmersionBar): ImmersionBar? {
        return null
    }

}