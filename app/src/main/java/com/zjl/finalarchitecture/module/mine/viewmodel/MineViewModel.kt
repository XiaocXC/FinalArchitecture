package com.zjl.finalarchitecture.module.mine.viewmodel

import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.base.viewmodel.requestScope
import com.zjl.finalarchitecture.data.model.coin.CoinVO
import com.zjl.finalarchitecture.data.respository.ApiRepository
import com.zjl.finalarchitecture.data.respository.datasouce.UserAuthDataSource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow

/**
 * @author Xiaoc
 * @since  2022-05-08
 *
 * 我的 ViewModel
 **/
class MineViewModel: BaseViewModel() {

    val userInfo = UserAuthDataSource.basicUserInfoVO

    fun initData() {
        getUserInfo()
    }

    private fun getUserInfo(){
        requestScope {
            val data = requestApiResult {
                ApiRepository.requestUserInfo()
            }.await()

            // 刷新用户信息
            UserAuthDataSource.signIn(data)
        }
    }
}