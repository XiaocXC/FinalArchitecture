package com.zjl.finalarchitecture.module.home.ui.fragment

import androidx.fragment.app.viewModels
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.launchAndRepeatWithViewLifecycle
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentPlazaBinding
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleAdapter
import com.zjl.finalarchitecture.module.home.viewmodel.PlazaViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * @description:
 * @author: zhou
 * @date : 2022/1/20 17:56
 */
class PlazaFragment : BaseFragment<FragmentPlazaBinding>(){

    companion object {
        fun newInstance() = PlazaFragment()
    }

    private val mViewModel by viewModels<PlazaViewModel>()

    private lateinit var mArticleAdapter: ArticleAdapter

    override fun bindView() = FragmentPlazaBinding.inflate(layoutInflater)

    override fun initViewAndEvent() {
        initRefresh()
        initAdapter()
    }

    override fun createObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                mViewModel.mPlazaListFlow.collect {

                }
            }
        }
    }

    private fun initRefresh(){
        mBinding.refreshLayout.run {
            //下拉样式的颜色
            setColorSchemeResources(
                R.color.base_blue_100,
                R.color.base_blue_700,
                R.color.base_light_blue_200
            )
            setOnRefreshListener {
                mViewModel.requestPlazaData()
            }
        }
    }

    private fun initAdapter(){

    }

}