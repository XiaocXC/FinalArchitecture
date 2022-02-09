package com.zjl.base.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zjl.base.ui.UiModel

/**
 * @author Xiaoc
 * @since 2022-01-07
 *
 * 基类ViewModel，提供基本的封装内容进行统一
 * 规定一个Fragment要基于ViewBinding
 * Fragment可能会同时依赖多个ViewModel或者干脆不使用ViewModel
 * 所以我们没有在Fragment中强制规定ViewModel的使用
 */
abstract class BaseViewModel: ViewModel(){

    /**
     * 根View状态值
     * 该值为当前viewModel控制视图的根View的状态值，它存储着整个页面当前的状态
     */
    protected val _rootViewState = MutableLiveData<UiModel<Any>>()
    val rootViewState: LiveData<UiModel<Any>> get() = _rootViewState
}