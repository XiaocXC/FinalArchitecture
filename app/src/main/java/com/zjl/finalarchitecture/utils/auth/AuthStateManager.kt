package com.zjl.finalarchitecture.utils.auth

import com.tencent.mmkv.MMKV
import com.zjl.finalarchitecture.data.model.UserInfoVO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author Xiaoc
 * @since 2022/5/9
 *
 * 用户登录基本信息管理器
 * 此管理器会通过 [MMKV] 进行基本用户信息的存储和读取
 */
object AuthStateManager {

    /**
     * 用户信息存储文件名
     */
    private const val AUTH_FILE_NAME = "authState"

    /**
     * 用户详情Detail信息
     */
    private const val USER_DETAIL = "userDetail"

    /**
     * 获取用户信息
     * 协程
     */
    suspend fun getUserInfo(): UserInfoVO = withContext(Dispatchers.IO) {
        MMKV.mmkvWithID(AUTH_FILE_NAME)?.decodeParcelable(
            USER_DETAIL, UserInfoVO::class.java
        ) ?: UserInfoVO.NOT_LOGIN_USER
    }

    /**
     * 获取用户信息
     * 同步
     */
    fun getUserInfoSync(): UserInfoVO {
        return MMKV.mmkvWithID(AUTH_FILE_NAME)?.decodeParcelable(
            USER_DETAIL, UserInfoVO::class.java
        ) ?: UserInfoVO.NOT_LOGIN_USER
    }

    /**
     * 记录登陆用户信息
     */
    suspend fun signIn(userInfoVO: UserInfoVO) = withContext(Dispatchers.IO) {
        MMKV.mmkvWithID(AUTH_FILE_NAME)?.apply {
            encode(USER_DETAIL, userInfoVO)
        }
    }

    /**
     * 登出用户
     */
    suspend fun signOut() = withContext(Dispatchers.IO) {
        MMKV.mmkvWithID(AUTH_FILE_NAME)?.apply {
            encode(USER_DETAIL, UserInfoVO.NOT_LOGIN_USER)
        }
    }
}