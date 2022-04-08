package com.zjl.finalarchitecture.module.home.model.system

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * 体系项目分类VO
 */
@Serializable
@Parcelize
data class ClassifyVO(
    val id: String,
    val courseId: Int,
    val name: String,
    val order: Int = 0,
    val parentChapterId: Int = 0,
    val userControlSetTop: Boolean = false,
    val visible: Int,
    val children: List<String> = emptyList()
): Parcelable