package com.zjl.finalarchitecture.module.toolbox.progressList.timer

import androidx.lifecycle.viewModelScope
import com.zjl.base.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * @author Xiaoc
 * @since 2022-09-20
 *
 * 倒计时列表案例 ViewModel
 */
class TimerProgressListViewModel: BaseViewModel() {

    private val _timerList = MutableStateFlow<List<TimerProgressData>>(emptyList())
    val timerList: StateFlow<List<TimerProgressData>> = _timerList

    init {
        initData()
    }

    override fun refresh() {
        viewModelScope.launch {
            // 生成假数据
            val currentTime = System.currentTimeMillis()
            val result = buildList<TimerProgressData> {
                add(
                    TimerProgressData(1, "1", "这是1", currentTime + 60 * 1000)
                )

                add(
                    TimerProgressData(2, "2", "这是2", currentTime + 30 * 1000)
                )

                add(
                    TimerProgressData(3, "3", "这是3", currentTime + 100 * 1000)
                )

                add(
                    TimerProgressData(4, "4", "这是4", currentTime + 90 * 1000)
                )

                add(
                    TimerProgressData(5, "5", "这是5", currentTime + 20 * 1000)
                )

                add(
                    TimerProgressData(6, "6", "这是6", currentTime + 40 * 1000)
                )

                add(
                    TimerProgressData(7, "7", "这是7", currentTime + 10 * 1000)
                )

                add(
                    TimerProgressData(8, "8", "这是8", currentTime + 80 * 1000)
                )
            }

            _timerList.value = result
        }
    }
}