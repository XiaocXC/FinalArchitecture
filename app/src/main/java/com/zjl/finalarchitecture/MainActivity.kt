package com.zjl.finalarchitecture

import com.zjl.base.BaseActivity
import com.zjl.finalarchitecture.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun initViewAndEvent() {
    }

    override fun bindView(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
}