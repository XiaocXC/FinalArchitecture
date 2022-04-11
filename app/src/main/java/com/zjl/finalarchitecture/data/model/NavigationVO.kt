package com.zjl.finalarchitecture.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * @author Xiaoc
 * @since 2022-04-07
 *
 * 导航VO
 */
@Serializable
@Parcelize
data class NavigationVO(
    val articles: List<ArticleListVO>,
    val cid: Int,
    val name: String
): Parcelable