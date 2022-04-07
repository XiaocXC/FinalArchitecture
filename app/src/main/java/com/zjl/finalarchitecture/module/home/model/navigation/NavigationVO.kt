package com.zjl.finalarchitecture.module.home.model.navigation

import android.os.Parcelable
import com.zjl.finalarchitecture.module.home.model.ArticleListVO
import kotlinx.android.parcel.Parcelize
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
