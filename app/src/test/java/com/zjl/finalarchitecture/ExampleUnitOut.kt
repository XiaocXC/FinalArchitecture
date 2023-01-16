package com.zjl.finalarchitecture

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitOut {
    @Test
    fun addition_isCorrect() {
        val test = "0"

        val match = test.matches(Regex("[^0]"))
        println("符合？:$match")

        val testFlow: Flow<Int> = flow {
            emit(1)
            emit(2)
            emit(3)
            emit(4)
        }

        runBlocking {
            // 观察者1
            testFlow.collect {
                println("观察者1：$it")
            }
            // 观察者2
            testFlow.collect {
                println("观察者2：$it")
            }
        }

        val testStateFlow = MutableStateFlow(1)

        runBlocking {
            // 观察者1
            testStateFlow.collect {
                println("观察者1：$it")
            }
            // 观察者2
            testStateFlow.collect {
                println("观察者2：$it")
            }
        }
        // 更改数据为1
        testStateFlow.value = 1
        // 更改数据为2
        testStateFlow.value = 2
        // 更改数据为3
        testStateFlow.value = 3
        // 更改数据为4
        testStateFlow.value = 4
    }

    fun secondToHour(second: String?): String {
        return if (second.isNullOrEmpty()) {
            "计算中"
        } else try {
            val secondInteger = second.toInt()
            var hour = 0
            var minute = 0
            var seconds = 0
            if(secondInteger <= 0){
                return "0秒"
            } else {
                minute = secondInteger / 60
                if(minute < 60){
                    seconds = secondInteger % 60

                } else {
                    hour = minute / 60
                    if(hour > 99){
                        return "59小时59分59秒"
                    }
                    minute %= 60
                    seconds = secondInteger - hour * 3600 - minute * 60
                }
                if(minute <= 0 && hour <= 0){
                    return "${seconds}秒"
                } else if(hour <= 0){
                    if(seconds <= 0){
                        return "${minute}分钟"
                    } else {
                        return "${minute}分${seconds}秒"
                    }
                } else {
                    return "${hour}小时${minute}分${seconds}秒"
                }
            }
        } catch (e: Exception) {
            "时间错误"
        }
    }
}