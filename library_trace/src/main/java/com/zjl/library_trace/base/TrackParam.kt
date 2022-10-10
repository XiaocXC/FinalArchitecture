package com.zjl.library_trace.base

import java.io.Serializable

/**
 * 埋点数据参数
 */
open class TrackParams : Iterable<Any?>, Serializable {

    /**
     * 数据，是一个HashMap
     */
    private val data = HashMap<String, String?>()

    operator fun set(key: String, value: Any?): TrackParams {
        data[key] = value?.toString()
        return this
    }

    operator fun get(key: String): String? = data[key]

    /**
     * 如果对应Key的值为null
     * 则设置它的值
     */
    fun setIfNull(key: String, value: Any?): TrackParams {
        val oldValue = data[key]
        if (null == oldValue) {
            data[key] = value?.toString()
        }
        return this
    }

    fun get(key: String, default: String?): String? = data[key] ?: default

    /**
     * 合并其他的TrackParams的参数
     */
    fun merge(other: TrackParams?): TrackParams {
        if (null != other) {
            for ((key, value) in other) {
                setIfNull(key, value)
            }
        }
        return this
    }

    override fun iterator() = data.iterator()

    override fun toString(): String {
        return StringBuilder().apply {
            append("[")
            for ((key, value) in data) {
                append(" $key = $value ,")
            }
            deleteCharAt(this.length - 1)
            append("]")
        }.toString()
    }
}