package com.zjl.finalarchitecture.module.auth.register

import com.zjl.base.ui.UiModel
import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.base.viewmodel.requestScope
import com.zjl.finalarchitecture.data.respository.ApiRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

/**
 * @author Xiaoc
 * @since  2022-09-06
 **/
class RegisterViewModel: BaseViewModel() {

    private val apiRepository = ApiRepository


    /**
     * 注册状态
     */
    private val _eventRegisterState = MutableSharedFlow<UiModel<Unit>>()
    val eventRegisterState: SharedFlow<UiModel<Unit>> get() = _eventRegisterState

    /**
     * 通过账号密码注册
     * @param account 账号
     * @param password 密码
     */
    fun registerByPassword(account: String, password: String){
        requestScope {
            requestApiResult(uiModel = _eventRegisterState) {
                apiRepository.requestRegister(account, password)
            }.await()
        }
    }
}