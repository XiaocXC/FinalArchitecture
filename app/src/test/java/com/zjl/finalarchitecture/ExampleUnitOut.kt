package com.zjl.finalarchitecture

import android.text.TextUtils
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitOut {
    @Test
    fun addition_isCorrect() {

        println(secondToHour("0"))
        println(secondToHour("60"))
        println(secondToHour("111"))
        println(secondToHour("3700"))
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