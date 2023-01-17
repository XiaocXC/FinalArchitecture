package com.zjl.finalarchitecture.theme

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * @author Xiaoc
 * @since 2023-01-17
 *
 * Final的主题管理器
 */
object ThemeManager {

    private val _currentTheme = MutableStateFlow<FinalTheme>(FinalTheme.GREEN)
    val currentTheme: StateFlow<FinalTheme> = _currentTheme

    enum class FinalTheme{
        DEFAULT, GREEN
    }

    fun changeTheme(finalTheme: FinalTheme){
        _currentTheme.value = finalTheme
    }
}