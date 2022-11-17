package com.zjl.library_network.converter.kotlin

import kotlinx.serialization.SerializationStrategy
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Converter

/**
 * @author Xiaoc
 * @since 2021-10-13
 *
 * 你不需要了解内部实现，这些都是Kotlin-Serializer的API
 * 脱壳步骤在[KotlinSerializer.FromString]中
 */
internal class SerializationStrategyConverter<T>(
    private val contentType: MediaType,
    private val saver: SerializationStrategy<T>,
    private val kotlinSerializer: KotlinSerializer
) : Converter<T, RequestBody> {
    override fun convert(value: T) = kotlinSerializer.toRequestBody(contentType, saver, value)
}