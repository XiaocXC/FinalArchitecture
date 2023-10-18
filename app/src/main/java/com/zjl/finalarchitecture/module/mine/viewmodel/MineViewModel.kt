package com.zjl.finalarchitecture.module.mine.viewmodel

import com.zjl.base.ui.UiModel
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.base.viewmodel.requestScope
import com.zjl.finalarchitecture.api.wanAndroidCookieJar
import com.zjl.finalarchitecture.data.model.coin.CoinVO
import com.zjl.finalarchitecture.data.respository.ApiRepository
import com.zjl.finalarchitecture.data.respository.datasouce.UserAuthDataSource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * @author Xiaoc
 * @since  2022-05-08
 *
 * 我的 ViewModel
 **/
class MineViewModel: BaseViewModel() {

    val userInfo = UserAuthDataSource.basicUserInfoVO

    val userAuthDataSource = UserAuthDataSource

    private val _signOutEvent = Channel<UiModel<Unit>>()
    val signOutEvent = _signOutEvent.receiveAsFlow()

    init {
        initData()
    }

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

    /**
     * 退出登录
     */
    fun signOut(){
        requestScope {
            try {
                // 请求后台的退出登陆接口
                requestApiResult {
                    ApiRepository.requestLogout()
                }.await()
                // 清除本地记录
                userAuthDataSource.signOut()
                // 清除网络Cookies的数据
                wanAndroidCookieJar.clear()
                _signOutEvent.send(UiModel.Success(Unit))
            } catch (throwable: Throwable){
                _signOutEvent.send(UiModel.Error(throwable))
            }
        }
    }
}