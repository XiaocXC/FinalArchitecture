package com.zjl.finalarchitecture.test

import android.util.Log
import android.util.Log.e
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

/**
 * @description:
 * @author: zhou
 * @date : 2022/3/11 11:18
 */
fun main() {

    val list =
        listOf<String>("it", "word", "abc", "shuai", "wudi", "nihaoa", "xuelunya", "niubi666")
    val result = list
        .filter { it.length > 3 }
        .map { it.length }
        .take(4)
//    print(result.toString())

    fun testLambda(callback: (a: Int) -> Int): String {
        val s = callback(5)
        println("lambda传入的值：$s")
        return "${s + 1}"
    }

    val t = testLambda {
        println("lambda传递的值:$it")
        it + 1
    }
    println("testLambda高级函数返回的值：$t")


//    val price:String = "1.22"
//
//    val split: Array<String> = price.split(".").toTypedArray()
//
//    println(split.size)

    val list1: ArrayList<String> = arrayListOf()
    val list2: ArrayList<String> = ArrayList<String>()

    list2.add("C")
    println(
        "" + list2.add("Z")
    )
    println(list2.size)
    println(
        "${list2.add("M")}"
    )
    println(list2.size)
//    println(split.contentToString())


    //    val coldFlow = flow {
//        emit(1)
//        emit(2)
//        emit(2)
//        emit(2)
//        emit(2)
//        emit(2)
//        emit(2)
//    }.flowOn(Dispatchers.IO) // 以上内容在IO线程执行
//        .map {
//
//        }.map {
//
//        }.flowOn(Dispatchers.Default)  // 以上Map内容在Default线程执行
//
//    val hotFlow = MutableStateFlow(2)
//
//    fun test(){
//        viewModelScope.launch {
//            coldFlow.collect {
//                // 打印
//                // 1  2
//            }
//
//            coldFlow.collect {
//                // 打印
//                // 1  2
//            }
//
//            hotFlow.collect {
//                // 打印
//                // 2
//
//                // 执行下面的内容后
//                // 5
//            }
//
//            hotFlow.value = 5
//        }
//    }

//    private val _plazaListFlow = MutableStateFlow<MutableList<ArticleListVO>>(mutableListOf())
//    val mPlazaListFlow: StateFlow<MutableList<ArticleListVO>> = _plazaListFlow
//
//    // BannerUI
//    private val _addPlazaListFlow = MutableStateFlow<List<ArticleListVO>>(emptyList())
//    val mAddPlazaListFlow: StateFlow<List<ArticleListVO>> = _addPlazaListFlow
//
//    private var currentPage = 0

}