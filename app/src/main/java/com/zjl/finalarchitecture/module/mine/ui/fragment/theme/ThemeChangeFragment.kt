package com.zjl.finalarchitecture.module.mine.ui.fragment.theme

import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.findNavController
import com.zjl.base.utils.launchAndCollectIn
import com.zjl.finalarchitecture.databinding.FragmentThemeChangeBinding
import com.zjl.finalarchitecture.module.mine.ui.fragment.theme.adapter.ThemeListAdapter
import com.zjl.finalarchitecture.theme.FinalTheme
import com.zjl.finalarchitecture.theme.ThemeManager

class ThemeChangeFragment: BaseFragment<FragmentThemeChangeBinding, ThemeChangeViewModel>() {

    private lateinit var themeListAdapter: ThemeListAdapter

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        mBinding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        themeListAdapter = ThemeListAdapter()
        mBinding.themeList.adapter = themeListAdapter

        themeListAdapter.setOnItemClickListener { _, _, position ->
            val item = themeListAdapter.getItem(position)
            val newTheme = FinalTheme.values().find {
                it.tag == item.title
            }
            if(newTheme != null){
                ThemeManager.changeTheme(newTheme)
            }
            requireActivity().recreate()
        }
    }

    override fun createObserver() {
        mViewModel.themeList.launchAndCollectIn(viewLifecycleOwner){
            themeListAdapter.setList(it)
        }
    }
}

data class ThemeUIState(
    val title: String,
    val previewUri: String,
    val isCurrent: Boolean
)