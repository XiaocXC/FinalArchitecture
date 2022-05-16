package com.zjl.finalarchitecture.module.auth

import com.zjl.base.viewmodel.BaseViewModel

/**
 * @author Xiaoc
 * @since 2021/6/18
 *
 * 登录界面ViewModel
 */
class SignInViewModel constructor(
): BaseViewModel(){

//    /**
//     * 验证码倒计时
//     */
//    private val countDownTimer = CountDownTimer(60L * 1000, 1000L)
//    private var countDownJob: Job? = null

//    /**
//     * 倒计时数据
//     */
//    private val _countDownNumber = MutableStateFlow(CountDownTimer.CountDownData(0, false))
//    val countDownNumber: StateFlow<CountDownTimer.CountDownData> get() = _countDownNumber
//
//    val isCounting get() = countDownTimer.isCounting
//
//    /**
//     * 获取验证码状态
//     */
//    private val _eventShowVerifyState = MutableSharedFlow<UiModel<Unit>>()
//    val eventShowVerifyState: SharedFlow<UiModel<Unit>> get() = _eventShowVerifyState
//
//    /**
//     * 登录状态
//     */
//    private val _eventSignInState = MutableSharedFlow<UiModel<SignInVO>>()
//    val eventSignInState: SharedFlow<UiModel<SignInVO>> get() = _eventSignInState
//
//    /**
//     * 通过验证码登录
//     * @param phone 手机号
//     * @param code 验证码
//     */
//    fun signInByVerify(phone: String, code: String){
//
//        viewModelScope.launch {
//            _eventSignInState.emit(UiModel.Loading())
//
//            val result = userAuthRepository.userSignIn(SignInDTO(phone, code))
//            result.onSuccess { sign ->
//                // 登录成功我们获取一次用户详情作为缓存
//                val userInfoResult = userRepository.getUserRepository(sign.userId)
//
//                userInfoResult.onSuccess {
//                    val backupUserInfo = it.copy(id = sign.userId, phone = phone)
//                    // 如果成功，我们将数据存到本地
//                    emitSignIn(GatherUserInfo(sign.userId, sign.tokenName, sign.tokenValue, backupUserInfo))
//                    _eventSignInState.emit(UiModel.Success(sign))
//                }.onFailure {
//                    _eventSignInState.emit(UiModel.Error(ApiException(it)))
//                }
//            }.onFailure {
//                _eventSignInState.emit(UiModel.Error(ApiException(it)))
//            }
//        }
//    }
//
//    /**
//     * 获取手机验证码
//     * @param phone 手机号
//     */
//    fun getVerifyCode(phone: String){
//
//        viewModelScope.launch {
//            _eventShowVerifyState.emit(UiModel.Loading())
//
////            delay(2000L)
////            _eventShowVerifyState.emit(UiModel.Success(Unit))
//            launchRequestByNormalOnlyResult({
//                userAuthRepository.sendPhoneVerify(UserAuthDataSource.LOGIN_CODE, phone)
//            }, resultBlock = {
//                _eventShowVerifyState.emit(it)
//            })
//        }
//
//    }
//
//    /**
//     * 开始倒计时
//     */
//    fun startCountDown(){
//        // 取消前一次的倒计时Job
//        countDownJob.cancelIfActive()
//
//        // 开始倒计时
//        countDownJob = viewModelScope.launch {
//
//            countDownTimer.start {
//                _countDownNumber.emit(it)
//            }
//        }
//    }

    override fun refresh() {

    }

}

