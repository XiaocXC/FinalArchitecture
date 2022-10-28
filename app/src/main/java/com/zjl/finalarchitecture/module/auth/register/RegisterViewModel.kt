package com.zjl.finalarchitecture.module.auth.register

import androidx.lifecycle.viewModelScope
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

        viewModelScope.launch {
            launchRequestByNormalWithUiState({
                apiRepository.register(account, password)
            }, _eventRegisterState, isShowLoading = true)
        }
    }

    override fun initData() {

    }
}