package com.zjl.finalarchitecture.test

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * @description:
 * @author: zhou
 * @date : 2022/3/9 10:56
 */
class TestViewModelFactory(var savedInstanceState: Bundle?) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Bundle::class.java).newInstance(savedInstanceState)
    }
}