package com.zjl.finalarchitecture

import com.zjl.base.activity.BaseActivity
import com.zjl.finalarchitecture.databinding.ActivityMainBinding
import com.zjl.finalarchitecture.di.createBaseViewModel

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override fun initViewAndEvent() {

    }

    override fun bindView(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun bindViewModel(): MainViewModel = createBaseViewModel()
}