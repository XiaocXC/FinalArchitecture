package com.zjl.finalarchitecture.module.home.model.system

import kotlinx.serialization.Serializable

/**
 * 体系VO
 */
@Serializable
data class SystemVO(
    val id: String,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int,
    val children: List<ClassifyVO>
)