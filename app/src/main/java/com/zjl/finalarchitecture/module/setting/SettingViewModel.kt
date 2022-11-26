package com.zjl.finalarchitecture.module.setting

import com.zjl.base.ui.UiModel
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.base.viewmodel.requestScope
import com.zjl.finalarchitecture.api.wanAndroidCookieJar
import com.zjl.finalarchitecture.data.respository.ApiRepository
import com.zjl.finalarchitecture.data.respository.datasouce.UserAuthDataSource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * @author Xiaoc
 * @since  2022-11-22
 *
 * 设置ViewModel
 **/
class SettingViewModel: BaseViewModel() {

    val userAuthDataSource = UserAuthDataSource

    private val _signOutEvent = Channel<UiModel<Unit>>()
    val signOutEvent = _signOutEvent.receiveAsFlow()

    /**
     * 退出登录
     */
    fun signOut(){
        requestScope {
            try {
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