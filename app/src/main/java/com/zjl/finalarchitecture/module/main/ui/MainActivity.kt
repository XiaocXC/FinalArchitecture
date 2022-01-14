package com.zjl.finalarchitecture.module.main.ui

import com.zjl.base.activity.BaseActivity
import com.zjl.finalarchitecture.module.main.viewmodel.MainViewModel
import com.zjl.finalarchitecture.databinding.ActivityMainBinding
import com.zjl.finalarchitecture.di.createBaseViewModel

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override fun bindView(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun bindViewModel(): MainViewModel = createBaseViewModel()

    override fun initViewAndEvent() {

    }

    override fun createObserver() {
//        mViewModel.bannerListUiModel.observe(this){
//            when (it) {
//                is UiModel.Success -> {
//                    rootUiState.show<SuccessState>()
//                }
//                is UiModel.Error -> {
//                    rootUiState.show<ErrorState>{ errorState ->
//                        errorState.setErrorMsg(it.error.message ?: "")
//                    }
//                }
//                is UiModel.Loading -> {
//                    rootUiState.show<LoadingState>()
//                }
//            }
//        }
//        mViewModel.bannerList.observe(this){
//            mBinding.tvJson.text = it.data.toString()
//        }
    }
}