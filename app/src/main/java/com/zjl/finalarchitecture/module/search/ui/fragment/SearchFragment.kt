package com.zjl.finalarchitecture.module.search.ui.fragment

import android.os.Bundle
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ResourceUtils
import com.google.android.material.chip.Chip
import com.google.android.material.transition.MaterialContainerTransform
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.ktx.immersionBar
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.ext.doOnApplyWindowInsets
import com.zjl.base.utils.ext.isNightMode
import com.zjl.base.utils.findNavController
import com.zjl.base.utils.launchAndCollectIn
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.data.model.SearchHotVO
import com.zjl.finalarchitecture.databinding.FragmentSearchBinding
import com.zjl.finalarchitecture.module.search.viewmodel.SearchViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>() {

    override fun initViewAndEvent(savedInstanceState: Bundle?) {

        mBinding.toolbarSearch.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        mBinding.editSearch.addOnEditTextAttachedListener { inputLayout ->
            inputLayout.setEndIconOnClickListener {
                if (inputLayout.editText?.text.isNullOrEmpty()) {
                    return@setEndIconOnClickListener
                }
                // 存储搜索的内容
                mViewModel.saveSearchKey(inputLayout.editText?.text.toString())

                // 进入下一个页面
                findNavController().navigate(
                    SearchFragmentDirections.actionSearchFragmentToSearchResultFragment(
                        inputLayout.editText?.text.toString(),
                        inputLayout.editText?.text.toString()
                    )
                )

            }
        }
    }

    override fun createObserver() {
        // 热门搜索
        mViewModel.searchHotKeys.launchAndCollectIn(viewLifecycleOwner){
            generateSearchHotChip(it)
        }

        // 历史记录
        mViewModel.searchHistoryKeys.launchAndCollectIn(viewLifecycleOwner){
            generateSearchHistoryChip(it)
        }
    }

    /**
     * 生成搜索热词Chip
     */
    private fun generateSearchHotChip(data: List<SearchHotVO>) {
        mBinding.chipSearchHot.removeAllViews()
        data.forEach { searchHot ->
            val chip =
                layoutInflater.inflate(
                    R.layout.view_search_chip,
                    mBinding.chipSearchHot,
                    false
                ) as Chip
            chip.text = searchHot.name
            chip.tag = searchHot
            // 每一项的点击事件
            chip.setOnClickListener {
                // 存储搜索的内容
                mViewModel.saveSearchKey(chip.text.toString())

                // 进入下一个页面
                findNavController().navigate(
                    SearchFragmentDirections.actionSearchFragmentToSearchResultFragment(
                        chip.text.toString(),
                        chip.text.toString()
                    )
                )
            }

            mBinding.chipSearchHot.addView(chip)
        }
    }

    private fun generateSearchHistoryChip(data: List<String>) {
        mBinding.chipSearchHistory.removeAllViews()
        data.forEach { text ->
            val chip = layoutInflater.inflate(
                R.layout.view_search_chip,
                mBinding.chipSearchHistory,
                false
            ) as Chip
            chip.text = text
            chip.isCloseIconVisible = true
            chip.setOnClickListener {
                // 存储搜索的内容
                mViewModel.saveSearchKey(chip.text.toString())

                // 进入下一个页面
                findNavController().navigate(
                    SearchFragmentDirections.actionSearchFragmentToSearchResultFragment(
                        chip.text.toString(),
                        chip.text.toString()
                    )
                )
            }
            chip.setOnCloseIconClickListener {
                mViewModel.removeSearchKey(chip.text.toString())
            }
            mBinding.chipSearchHistory.addView(chip)
        }
    }


    override fun configImmersive(immersionBar: ImmersionBar): ImmersionBar? {
        return immersionBar.titleBar(mBinding.toolbarSearch)
    }

    override fun onDestroyView() {
        // 隐藏输入法
        KeyboardUtils.hideSoftInput(mBinding.editSearch)

        super.onDestroyView()

    }
}