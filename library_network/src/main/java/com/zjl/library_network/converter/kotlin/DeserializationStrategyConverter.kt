package com.zjl.library_network.converter.kotlin

import kotlinx.serialization.DeserializationStrategy
import okhttp3.ResponseBody
import retrofit2.Converter

/**
 * @author Xiaoc
 * @since 2021-10-13
 *
 * 你不需要了解内部实现，这些都是Kotlin-Serializer的API
 * 脱壳步骤在[KotlinSerializer.FromString]中
 */
internal class DeserializationStrategyConverter<T>(
    private val loader: DeserializationStrategy<T>,
    private val kotlinSerializer: KotlinSerializer
) : Converter<ResponseBody, T> {
    override fun convert(value: ResponseBody) = kotlinSerializer.fromResponseBody(loader, value)
}