package com.zjl.finalarchitecture.module.auth

import com.zjl.base.ui.UiModel
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.base.viewmodel.requestScope
import com.zjl.finalarchitecture.data.model.UserInfoVO
import com.zjl.finalarchitecture.data.respository.ApiRepository
import com.zjl.finalarchitecture.data.respository.datasouce.UserAuthDataSource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * @author Xiaoc
 * @since 2021/6/18
 *
 * 登录界面ViewModel
 */
class SignInViewModel : BaseViewModel(){

    private val apiRepository = ApiRepository

    /**
     * 登录状态
     */
    private val _eventSignInState = Channel<UiModel<UserInfoVO>>()
    val eventSignInState = _eventSignInState.receiveAsFlow()

    /**
     * 通过账号密码登录
     * @param account 账号
     * @param password 密码
     */
    fun signInByPassword(account: String, password: String){
        requestScope {
            // 请求中
            _eventSignInState.send(UiModel.Loading())

            try {
                val data = requestApiResult {
                    apiRepository.requestLogin(account, password)
                }.await()
                // 如果登录成功，我们把用户信息数据存储到本地
                UserAuthDataSource.signIn(data)
                // 更新为成功信息
                _eventSignInState.send(UiModel.Success(data))
            } catch (throwable: Throwable){
                // 更新为失败信息
                _eventSignInState.send(UiModel.Error(throwable))
            }

        }
    }

}

