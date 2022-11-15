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

    fun initData() {
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

                add(
                    TimerProgressData(9, "9", "这是9", currentTime + 10 * 1000)
                )

                add(
                    TimerProgressData(10, "10", "这是10", currentTime + 5 * 1000)
                )

                add(
                    TimerProgressData(11, "11", "这是11", currentTime + 55 * 1000)
                )

                add(
                    TimerProgressData(12, "12", "这是12", currentTime + 32 * 1000)
                )

                add(
                    TimerProgressData(13, "13", "这是13", currentTime + 54 * 1000)
                )

                add(
                    TimerProgressData(14, "14", "这是14", currentTime + 14 * 1000)
                )

                add(
                    TimerProgressData(15, "15", "这是15", currentTime + 87 * 1000)
                )
            }

            _timerList.value = result
        }
    }
}