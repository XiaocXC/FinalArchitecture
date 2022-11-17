package com.zjl.finalarchitecture.api.converter.kotlin

import com.zjl.library_network.converter.kotlin.KotlinSerializer
import com.zjl.library_network.utils.globalJson
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.capturedKClass
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.jsonObject
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody

/**
 * @author Xiaoc
 * @since 2021-10-13
 *
 * 玩安卓的序列化器
 * 注意：该序列化器创建的Retrofit序列化处理器会自动脱壳[ResultVO]
 */
class WanAndroidSerializer(override val format: StringFormat) : KotlinSerializer() {

    /**
     * 将后台返回的数据序列化
     */
    override fun <T> fromResponseBody(loader: DeserializationStrategy<T>, body: ResponseBody): T {
        val string = body.string()
        val jsonObject = globalJson.parseToJsonElement(string).jsonObject

        return try {
            // ----- 注意 -----
            // 可能我们有这种需求，例如一个提交接口，后台data什么也不返回，而我们需要使用ResultVO的Msg字段
            // 所以我们这里当业务成功时，而data为null时，我们序列化message的内容，这样可以更加灵活
            // 你只需要把你的泛型改成对应msg的类型即可
            val dataElement = jsonObject["data"]
            if(dataElement == JsonNull){
                // 如果data内容为null，我们尝试获取errorMsg作为内容
                val messageElement = jsonObject["errorMsg"]
                return format.decodeFromString(loader, globalJson.encodeToString(messageElement))
            }
            // 脱壳ResultVO
            // 这里由于拦截器里做了正误判断，我们直接脱壳就行，取data字段里的数据，进行序列化
            format.decodeFromString(loader, globalJson.encodeToString(dataElement))
        } catch (e: Throwable){
            // 如果脱壳失败，可能是固定格式丢失，我们尝试用原始内容进行一次序列化
            format.decodeFromString(loader, string)
        } finally {
            body.close()
        }

    }

    /**
     * 将请求的参数数据序列化
     */
    override fun <T> toRequestBody(contentType: MediaType, saver: SerializationStrategy<T>, value: T): RequestBody {
        val string = format.encodeToString(saver, value)
        return string.toRequestBody(contentType)
    }
}