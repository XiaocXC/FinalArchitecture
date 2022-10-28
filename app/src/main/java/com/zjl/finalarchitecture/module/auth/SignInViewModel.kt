package com.zjl.finalarchitecture.module.auth

import androidx.lifecycle.viewModelScope
import com.zjl.base.exception.ApiException
import com.zjl.base.onSuccess
import com.zjl.base.ui.UiModel
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.finalarchitecture.data.model.UserInfoVO
import com.zjl.finalarchitecture.data.respository.ApiRepository
import com.zjl.finalarchitecture.data.respository.datasouce.UserAuthDataSource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

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
    private val _eventSignInState = MutableSharedFlow<UiModel<UserInfoVO>>()
    val eventSignInState: SharedFlow<UiModel<UserInfoVO>> get() = _eventSignInState

    /**
     * 通过账号密码登录
     * @param account 账号
     * @param password 密码
     */
    fun signInByPassword(account: String, password: String){

        viewModelScope.launch {
            launchRequestByNormalWithUiState({
                apiRepository.login(account, password)
            }, _eventSignInState, isShowLoading = true, successBlock = {
                // 如果登录成功，我们把用户信息数据存储到本地
                UserAuthDataSource.signIn(it)
            })
        }
    }

    override fun initData() {

    }

}

