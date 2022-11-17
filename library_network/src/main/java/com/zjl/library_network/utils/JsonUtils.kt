package com.zjl.library_network.utils

import kotlinx.serialization.json.Json

/**
 * 全局的Json序列化处理
 */
val globalJson = Json {
    // 取消严格模式，如果实体类没有包含Json字符串中的字段时不报错
    ignoreUnknownKeys = true
    isLenient = true
}