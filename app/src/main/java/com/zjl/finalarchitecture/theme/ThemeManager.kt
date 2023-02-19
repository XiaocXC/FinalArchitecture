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

    private val _currentTheme = MutableStateFlow(FinalTheme.DEFAULT)
    val currentTheme: StateFlow<FinalTheme> = _currentTheme

    fun changeTheme(finalTheme: FinalTheme){
        _currentTheme.value = finalTheme
    }
}

enum class FinalTheme(
    val tag: String
){
    DEFAULT("default"), GREEN("green")
}