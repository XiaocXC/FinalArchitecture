package com.zjl.library_network.converter.kotlin

import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * @author Xiaoc
 * @since 2021-10-13
 *
 * Kotlin-serialization序列化工具创建工厂
 * 你不需要了解内部实现，这些都是Kotlin-Serializer的API
 * 脱壳步骤在[KotlinSerializer.FromString]中
 *
 * 用于Retrofit相关的序列化处理
 * 注意：该工厂创建的Retrofit序列化处理器会自动脱壳[ResultVO]，详情见[KotlinSerializer]
 */
@ExperimentalSerializationApi
class SerializerFactory(
    private val contentType: MediaType,
    private val kotlinSerializer: KotlinSerializer
) : Converter.Factory() {

    @Suppress("RedundantNullableReturnType")
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        val loader = kotlinSerializer.serializer(type)
        return DeserializationStrategyConverter(loader, kotlinSerializer)
    }

    @Suppress("RedundantNullableReturnType")
    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        val saver = kotlinSerializer.serializer(type)
        return SerializationStrategyConverter(contentType, saver, kotlinSerializer)
    }
}