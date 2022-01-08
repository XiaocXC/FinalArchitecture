package com.zjl.finalarchitecture

import com.zjl.base.activity.BaseActivity
import com.zjl.finalarchitecture.databinding.ActivityMainBinding
import com.zjl.finalarchitecture.di.createBaseViewModel
import com.zjl.module_domain.UiModel
import com.zy.multistatepage.state.EmptyState
import com.zy.multistatepage.state.ErrorState
import com.zy.multistatepage.state.LoadingState
import com.zy.multistatepage.state.SuccessState

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override fun initViewAndEvent() {

    }

    override fun bindView(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun bindViewModel(): MainViewModel = createBaseViewModel()

    override fun createObserver() {
        viewModel.bannerListUiModel.observe(this){
            when (it) {
                is UiModel.Success -> {
                    rootUiState.show<SuccessState>()
                }
                is UiModel.Error -> {
                    rootUiState.show<ErrorState>{ errorState ->
                        errorState.setErrorMsg(it.error.message ?: "")
                    }
                }
                is UiModel.Loading -> {
                    rootUiState.show<LoadingState>()
                }
            }
        }
        viewModel.bannerList.observe(this){
            binding.tvJson.text = it.data.toString()
        }
    }
}