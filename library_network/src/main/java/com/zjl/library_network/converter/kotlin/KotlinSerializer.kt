package com.zjl.library_network.converter.kotlin

import com.zjl.library_network.utils.globalJson
import kotlinx.serialization.*
import kotlinx.serialization.json.jsonObject
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import java.lang.reflect.Type

/**
 * @author Xiaoc
 * @since 2021-10-13
 *
 * Kotlin序列化器，你需要继承该类来序列化你的内容
 */
abstract class KotlinSerializer {
    abstract fun <T> fromResponseBody(loader: DeserializationStrategy<T>, body: ResponseBody): T
    abstract fun <T> toRequestBody(contentType: MediaType, saver: SerializationStrategy<T>, value: T): RequestBody

    protected abstract val format: SerialFormat

    @ExperimentalSerializationApi
    fun serializer(type: Type): KSerializer<Any> = format.serializersModule.serializer(type)

    class DefaultFromString(override val format: StringFormat) : KotlinSerializer() {

        /**
         * 将后台返回的数据序列化
         */
        override fun <T> fromResponseBody(loader: DeserializationStrategy<T>, body: ResponseBody): T {
            val string = body.string()
            return format.decodeFromString(loader, string)
        }

        /**
         * 将请求的参数数据序列化
         */
        override fun <T> toRequestBody(contentType: MediaType, saver: SerializationStrategy<T>, value: T): RequestBody {
            val string = format.encodeToString(saver, value)
            return string.toRequestBody(contentType)
        }
    }

    class DefaultFromBytes(override val format: BinaryFormat) : KotlinSerializer() {
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