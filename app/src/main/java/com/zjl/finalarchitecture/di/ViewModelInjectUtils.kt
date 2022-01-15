package com.zjl.finalarchitecture.di

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.zjl.base.viewmodel.BaseViewModel

/**
 * @author Xiaoc
 * @since 2022-01-07
 */
inline fun <reified T: BaseViewModel> AppCompatActivity.createBaseViewModel(): T{
    val vm by viewModels<T>()
    return vm
}