package com.zjl.finalarchitecture.module.home.ui.fragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.blankj.utilcode.util.ToastUtils
import com.download.library.Executors.io
import com.zjl.base.adapter.DefaultLoadStateAdapter
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.launchAndRepeatWithViewLifecycle
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.FragmentPlazaBinding
import com.zjl.finalarchitecture.module.home.ui.adapter.ArticleAdapter
import com.zjl.finalarchitecture.module.home.ui.adapter.PlazaArticleAdapter
import com.zjl.finalarchitecture.module.home.viewmodel.PlazaViewModel
import com.zjl.finalarchitecture.utils.multistate.handleWithPaging3
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
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

    private val mPlazaViewModel by viewModels<PlazaViewModel>()

    //这里用的也是 ArticleAdapter
    private lateinit var mArticleAdapter: ArticleAdapter

    override fun bindView() = FragmentPlazaBinding.inflate(layoutInflater)

    override fun initViewAndEvent() {
//        lifecycleScope.launch() {
//            val data = withContext(Dispatchers.IO){
//                    var s = "jajja"
//                return@withContext s
//            }
//            aslist.add(data)
//        }
        mArticleAdapter = ArticleAdapter()

        mBinding.recyclerView.adapter = mArticleAdapter.withLoadStateFooter(DefaultLoadStateAdapter{
            mArticleAdapter.retry()
        })


        mBinding.floatBar.setOnClickListener {
            ToastUtils.showShort("尿我嘴里！")
            findNavController().navigate(R.id.action_mainFragment_to_addPlazaArticleFragment)
        }
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

        viewLifecycleOwner.lifecycleScope.launch {
            mPlazaViewModel.plazaPagingFlow.collectLatest {
                mArticleAdapter.submitData(it)
            }
        }

        // 下拉刷新,上拉分页,LEC状态观察
        viewLifecycleOwner.lifecycleScope.launch {
            mArticleAdapter.loadStateFlow.collectLatest {
                // 处理Paging3状态与整个布局状态相关联动
                uiRootState.handleWithPaging3(it,mArticleAdapter.itemCount <= 0){
                    refresh()
                }
            }
        }

    }

    private fun refresh(){
        // 刷新Paging
        mArticleAdapter.refresh()
    }
}