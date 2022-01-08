package com.zjl.module_domain.model.banner

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @Description:
 * @author lei
 * @date 2021/12/2 4:42 下午
 */

//    "desc": "一起来做个App吧",
//    "id": 10,
//    "imagePath": "https://www.wanandroid.com/blogimgs/50c115c2-cf6c-4802-aa7b-a4334de444cd.png",
//    "isVisible": 1,
//    "order": 1,
//    "title": "一起来做个App吧",
//    "type": 0,
//    "url": "https://www.wanandroid.com/blog/show/2"

@Serializable
data class BannerVO(
    @SerialName("id")
    val id: Long,
    @SerialName("desc")
    val desc: String? = "",
    @SerialName("imagePath")
    val imgUrl: String? = "",
    @SerialName("isVisible")
    val isVisible: Int? = -1,
    @SerialName("order")
    val order: Int? = -1,
    @SerialName("title")
    val title: String? = "",
    @SerialName("type")
    val type: Int? = -1,
    @SerialName("url")
    val linkUrl: String? = ""
)