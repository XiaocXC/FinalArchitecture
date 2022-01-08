package com.zjl.library_network.converter

import kotlinx.serialization.SerializationStrategy
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Converter

/**
 * @author Xiaoc
 * @since 2021-10-13
 */
internal class SerializationStrategyConverter<T>(
    private val contentType: MediaType,
    private val saver: SerializationStrategy<T>,
    private val serializer: Serializer
) : Converter<T, RequestBody> {
    override fun convert(value: T) = serializer.toRequestBody(contentType, saver, value)
}