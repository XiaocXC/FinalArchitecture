package com.zjl.finalarchitecture.data.respository.datasouce

import com.zjl.base.globalCoroutineScope
import com.zjl.finalarchitecture.data.model.UserInfoVO
import com.zjl.finalarchitecture.utils.auth.AuthStateManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * @author Xiaoc
 * @since 2022/5/9
 *
 * 用户认证数据源
 */
object UserAuthDataSource {

    private val authStateManager = AuthStateManager

    /**
     * 用户信息
     * 您可以观察此流来监听用户数据的变化
     */
    private val _basicUserInfo = MutableStateFlow(UserInfoVO.NOT_LOGIN_USER)
    val basicUserInfoVO: StateFlow<UserInfoVO> get() = _basicUserInfo

    /**
     * 是否登录标准
     * 依照用户信息是否为空来判定
     * true:已登录 | false:未登录
     */
    val isLogin: Boolean
    get() {
        return _basicUserInfo.value.id.isNotEmpty()
    }

    init {
        // 初始化时调用一次最新登录状态
        globalCoroutineScope.launch {
            refresh()
        }
    }

    /**
     * 登出退出登录
     * （启动全局作用域使用）
     */
    fun signOut(){
        globalCoroutineScope.launch {
            authStateManager.signOut()
            // 更新当前用户状态信息
            refresh()
        }
    }

    /**
     * 登录
     * （启动全局作用域使用）
     * @param userInfo 用户信息
     */
    fun signIn(userInfo: UserInfoVO){
        globalCoroutineScope.launch {
            authStateManager.signIn(userInfo)
            // 更新当前用户状态信息
            refresh()
        }
    }

    private suspend fun refresh() {
        _basicUserInfo.value = authStateManager.getUserInfo()
    }
}