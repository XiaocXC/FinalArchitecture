package com.zjl.base.viewmodel

import androidx.lifecycle.AndroidViewModel
import com.zjl.base.globalApplication

/**
 * @author Xiaoc
 * @since 2022-01-07
 *
 * 基类ViewModel，提供基本的封装内容进行统一
 * 规定一个Fragment要基于ViewBinding
 * Fragment可能会同时依赖多个ViewModel或者干脆不使用ViewModel
 * 所以我们没有在Fragment中强制规定ViewModel的使用
 */
abstract class BaseViewModel: AndroidViewModel(globalApplication) {
}