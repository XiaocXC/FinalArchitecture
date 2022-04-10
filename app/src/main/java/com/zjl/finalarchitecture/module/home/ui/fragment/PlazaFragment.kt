package com.zjl.finalarchitecture.module.home.ui.fragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.download.library.Executors.io
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.launchAndRepeatWithViewLifecycle
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentPlazaBinding
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleAdapter
import com.zjl.finalarchitecture.module.home.ui.adapter.PlazaArticleAdapter
import com.zjl.finalarchitecture.module.home.viewmodel.PlazaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

/**
 * @description:首页广场
 * @author: zhou
 * @date : 2022/1/20 17:56
 */
class PlazaFragment : BaseFragment<FragmentPlazaBinding>(){

    private var aslist : MutableList<String> =  mutableListOf()

    companion object {
        fun newInstance() = PlazaFragment()
    }

    private val mViewModel by viewModels<PlazaViewModel>()

    private lateinit var mPlazaArticleAdapter: PlazaArticleAdapter

    override fun bindView() = FragmentPlazaBinding.inflate(layoutInflater)

    override fun initViewAndEvent() {
        initRefresh()
        initAdapter()

//        lifecycleScope.launch() {
//            val data = withContext(Dispatchers.IO){
//                    var s = "jajja"
//                return@withContext s
//            }
//            aslist.add(data)
//        }
    }

    override fun createObserver() {
//        launchAndRepeatWithViewLifecycle {
//            launch {
//                mViewModel.mPlazaListFlow.collect {
//                    mPlazaArticleAdapter.addData(it)
//                }
//            }
//
//            launch {
//                mViewModel.mAddPlazaListFlow.collect {
//                    mPlazaArticleAdapter.addData(it)
//                }
//            }
//        }
    }

    private fun initRefresh(){
//        mBinding.refreshLayout.run {
//            //下拉样式的颜色
//            setColorSchemeResources(
//                R.color.base_blue_100,
//                R.color.base_blue_700,
//                R.color.base_light_blue_200
//            )
//            setOnRefreshListener {
//                mViewModel.toRefresh()
//            }
//        }
    }

    private fun initAdapter(){
        mPlazaArticleAdapter = PlazaArticleAdapter()
    }

}