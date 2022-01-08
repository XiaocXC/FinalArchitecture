package com.zjl.library_network.converter

import com.zjl.library_network.utils.globalJson
import com.zjl.library_network.utils.toJsonString
import kotlinx.serialization.*
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import java.lang.reflect.Type

/**
 * @author Xiaoc
 * @since 2021-10-13
 *
 * 序列化器
 * 注意：该序列化器创建的Retrofit序列化处理器会自动脱壳[ResultVO]
 */
internal sealed class Serializer {
    abstract fun <T> fromResponseBody(loader: DeserializationStrategy<T>, body: ResponseBody): T
    abstract fun <T> toRequestBody(contentType: MediaType, saver: SerializationStrategy<T>, value: T): RequestBody

    protected abstract val format: SerialFormat

    @ExperimentalSerializationApi
    fun serializer(type: Type): KSerializer<Any> = format.serializersModule.serializer(type)

    class FromString(override val format: StringFormat) : Serializer() {
        override fun <T> fromResponseBody(loader: DeserializationStrategy<T>, body: ResponseBody): T {
            val string = body.string()
            // 脱壳ResultVO
            val jsonObject = globalJson.parseToJsonElement(string).jsonObject

            // 这里由于拦截器里做了正误判断，我们直接脱壳就行
            val dataJson = jsonObject.getValue("data")
            return format.decodeFromString(loader, dataJson.toJsonString())
        }

        override fun <T> toRequestBody(contentType: MediaType, saver: SerializationStrategy<T>, value: T): RequestBody {
            val string = format.encodeToString(saver, value)
            return string.toRequestBody(contentType)
        }
    }

    class FromBytes(override val format: BinaryFormat) : Serializer() {
        override fun <T> fromResponseBody(loader: DeserializationStrategy<T>, body: ResponseBody): T {
            val bytes = body.bytes()
            return format.decodeFromByteArray(loader, bytes)
        }

        override fun <T> toRequestBody(contentType: MediaType, saver: SerializationStrategy<T>, value: T): RequestBody {
            val bytes = format.encodeToByteArray(saver, value)
            return bytes.toRequestBody(contentType)
        }
    }
}