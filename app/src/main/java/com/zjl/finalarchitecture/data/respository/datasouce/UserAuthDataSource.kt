package com.zjl.finalarchitecture.data.respository.datasouce

import com.zjl.base.globalCoroutineScope
import com.zjl.finalarchitecture.data.model.user.CombineUserInfoVO
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
    private val _basicUserInfo = MutableStateFlow<CombineUserInfoVO?>(null)
    val basicUserInfoVO: StateFlow<CombineUserInfoVO?> = _basicUserInfo

    /**
     * 是否登录标准
     * 依照用户信息是否为空来判定
     * true:已登录 | false:未登录
     */
    val isLogin: Boolean
        get() {
            return _basicUserInfo.value != null && !_basicUserInfo.value?.userInfo?.id.isNullOrEmpty()
        }

    init {
        // 初始化时调用一次最新登录状态
        globalCoroutineScope.launch {
            refreshUserInfo()
        }
    }

    /**
     * 退出登录
     * （启动全局作用域使用）
     */
    fun signOut() {
        globalCoroutineScope.launch {
            authStateManager.signOut()
            // 更新当前用户状态信息
            refreshUserInfo()
        }
    }

    /**
     * 登录
     * （启动全局作用域使用）
     * @param userInfo 用户信息
     */
    fun signIn(userInfo: CombineUserInfoVO) {
        globalCoroutineScope.launch {
            authStateManager.signIn(userInfo)
            // 更新当前用户状态信息
            refreshUserInfo()
        }
    }

    private suspend fun refreshUserInfo() {
        _basicUserInfo.value = authStateManager.getUserInfo()
    }
}