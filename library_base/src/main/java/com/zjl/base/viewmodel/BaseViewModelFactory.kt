package com.zjl.base.viewmodel

import androidx.lifecycle.ViewModelProvider
import com.zjl.base.globalApplication

/**
 * @author Xiaoc
 * @since 2022-01-07
 */
class BaseViewModelFactory() :
    ViewModelProvider.AndroidViewModelFactory(globalApplication) {
}