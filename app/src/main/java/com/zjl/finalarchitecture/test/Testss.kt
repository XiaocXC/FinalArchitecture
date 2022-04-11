package com.zjl.finalarchitecture.test

import android.util.Log
import java.util.*

/**
 * @description:
 * @author: zhou
 * @date : 2022/3/11 11:18
 */
fun main(){

    val list = listOf<String>("it","word","abc","shuai","wudi","nihaoa","xuelunya","niubi666")
    val result = list
        .filter { it.length >3 }
        .map { it.length }
        .take(4)
//    print(result.toString())

    fun testLambda(callback: (a:Int) -> Int) : String{
        val s = callback(5)
        println("lambda返回过来的值：$s")
        return "${s+1}"
    }

    val t = testLambda {
        println("lambda传递的值:$it")
        it + 1
    }
    println("testLambda高级函数返回的值：$t")


    val price:String = "1.22"

    val split: Array<String> = price.split(".").toTypedArray()

    println(split.size)

}