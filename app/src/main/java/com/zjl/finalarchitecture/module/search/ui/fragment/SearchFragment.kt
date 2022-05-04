package com.zjl.finalarchitecture.module.search.ui.fragment

import android.graphics.Color
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.gyf.immersionbar.ktx.immersionBar
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.ext.isNightMode
import com.zjl.base.utils.launchAndRepeatWithViewLifecycle
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.data.model.SearchHotVO
import com.zjl.finalarchitecture.databinding.FragmentSearchBinding
import com.zjl.finalarchitecture.module.search.viewmodel.SearchViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    private val searchViewModel by viewModels<SearchViewModel>()

    override fun bindView(): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(layoutInflater)
    }

    override fun initViewAndEvent() {



        mBinding.editSearch.addOnEditTextAttachedListener { inputLayout ->
            inputLayout.setEndIconOnClickListener {
                if(inputLayout.editText?.text.isNullOrEmpty()){
                    return@setEndIconOnClickListener
                }
                // 存储搜索的内容
                searchViewModel.saveSearchKey(inputLayout.editText?.text.toString())

                // 进入下一个页面
                findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToSearchResultFragment(
                    inputLayout.editText?.text.toString(), inputLayout.editText?.text.toString())
                )
            }
        }
    }

    override fun createObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                // 热门搜索
                searchViewModel.searchHotKeys.collectLatest {
                    generateSearchHotChip(it)
                }
            }
            launch {
                // 历史记录
                searchViewModel.searchHistoryKeys.collectLatest {
                    generateSearchHistoryChip(it)
                }
            }
        }
    }

    /**
     * 生成搜索热词Chip
     */
    private fun generateSearchHotChip(data: List<SearchHotVO>){
        mBinding.chipSearchHot.removeAllViews()
        data.forEach { searchHot ->
            val chip =
                layoutInflater.inflate(R.layout.view_search_chip, mBinding.chipSearchHot, false) as Chip
            chip.text = searchHot.name
            chip.tag = searchHot
            // 每一项的点击事件
            chip.setOnClickListener {
                // 存储搜索的内容
                searchViewModel.saveSearchKey(chip.text.toString())

                // 进入下一个页面
                findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToSearchResultFragment(chip.text.toString(), chip.text.toString()))
            }

            mBinding.chipSearchHot.addView(chip)
        }
    }

    private fun generateSearchHistoryChip(data: List<String>){
        mBinding.chipSearchHistory.removeAllViews()
        data.forEach { text ->
            val chip = layoutInflater.inflate(R.layout.view_search_chip, mBinding.chipSearchHistory, false) as Chip
            chip.text = text
            chip.isCloseIconVisible = true
            chip.setOnClickListener {
                // 存储搜索的内容
                searchViewModel.saveSearchKey(chip.text.toString())

                // 进入下一个页面
                findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToSearchResultFragment(chip.text.toString(), chip.text.toString()))
            }
            chip.setOnCloseIconClickListener {
                searchViewModel.removeSearchKey(chip.text.toString())
            }
            mBinding.chipSearchHistory.addView(chip)
        }
    }
}