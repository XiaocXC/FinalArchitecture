package com.zjl.finalarchitecture.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * @description:教程接口返回参数
 * @author: zhou
 * @date : 2022/11/16 17:28
 */
@Serializable
@Parcelize
data class TutorialVO(
    val articleList: List<String> = emptyList(),
    val author: String,
    val children: List<String> = emptyList(),
    val courseId: Int,
    val cover: String,
    val desc: String,
    val id: Int,
    val lisense: String,
    val lisenseLink: String,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val type: Int,
    val userControlSetTop: Boolean,
    val visible: Int
) : Parcelable