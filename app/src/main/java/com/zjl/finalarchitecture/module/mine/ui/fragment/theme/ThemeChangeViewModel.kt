package com.zjl.finalarchitecture.module.mine.ui.fragment.theme

import com.zjl.base.viewmodel.BaseViewModel
import com.zjl.base.viewmodel.requestScope
import com.zjl.finalarchitecture.theme.FinalTheme
import com.zjl.finalarchitecture.theme.ThemeManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

class ThemeChangeViewModel: BaseViewModel() {

    private val _themeList = MutableStateFlow<List<ThemeUIState>>(emptyList())
    val themeList: StateFlow<List<ThemeUIState>> = _themeList

    init {

        // 监听当前主题变化，一旦发生变化更新主题列表信息
        requestScope {
            ThemeManager.currentTheme.collectLatest { currentTheme ->
                // 获取APP内置主题
                _themeList.value = FinalTheme.values().map { theme ->
                    ThemeUIState(theme.tag, "", theme.tag == currentTheme.tag)
                }
            }
        }
    }
}