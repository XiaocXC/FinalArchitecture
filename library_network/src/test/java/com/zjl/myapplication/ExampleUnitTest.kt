package com.zjl.myapplication

import com.zjl.library_network.utils.globalJson
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.serializer
import org.junit.Test

import org.junit.Assert.*
import kotlin.reflect.typeOf

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Serializable
    data class Test2<T>(
        val msg: String,
        val code: Int,
        val data: T
    )

    @Serializable
    data class TTT(
        val str: String
    )

    @Test
    fun addition_isCorrect() {
        // 序列化成数据
        val test = Test2("error", 20, TTT("123"))
        val jsonStr = globalJson.encodeToString(test)
//        assertEquals(4, 2 + 2)
        "{\"msg\":\"error\",\"code\":20,\"data\":{\"str\":\"123\"}}"
        val loader = globalJson.serializersModule.serializer(typeOf<TTT>())
        val jsonObject = globalJson.parseToJsonElement(jsonStr).jsonObject
        val dataElement = jsonObject["data"]

        println(dataElement)
    }
}