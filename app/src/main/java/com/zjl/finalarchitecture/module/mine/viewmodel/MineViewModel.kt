package com.zjl.finalarchitecture.module.mine.viewmodel

import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.finalarchitecture.data.respository.datasouce.UserAuthDataSource

/**
 * @author Xiaoc
 * @since  2022-05-08
 *
 * 我的 ViewModel
 **/
class MineViewModel: BaseViewModel() {

    val userInfo = UserAuthDataSource.basicUserInfoVO

    fun initData() {
    }
}