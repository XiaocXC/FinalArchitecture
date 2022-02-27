package com.zjl.finalarchitecture

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
//        assertEquals(4, 2 + 2)

//        val doubleValue = 1.00
//
//        println(doubleValue.toString())
//
        val testFlow = flow<Int> {
            for (index in 0..10) {
                delay(500)
                emit(index)
            }
        }.flowOn(Dispatchers.IO).filter {
            return@filter it%2 == 0
        }.map {
            it.toString()+"love"
        }


        runBlocking {
            testFlow.collect {
                println(it)
            }
        }

//        val hotFlow : MutableStateFlow<String> = MutableStateFlow("1")
//        hotFlow.value = "55"
//        runBlocking {
//            hotFlow.collect {
//                println(it)
//            }
//        }

    }
}