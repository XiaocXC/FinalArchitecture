package com.zjl.finalarchitecture.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * @author Xiaoc
 * @since 2022-02-02
 *
 * 文章列表项的数据VO
 */
@Serializable
@Parcelize
data class ArticleListVO(
    val adminAdd: Boolean,
    val apkLink: String,
    val audit: Int,
    /* 文章作者 */
    val author: String,
    val canEdit: Boolean,
    val chapterId: Int,
    val chapterName: String,
    val collect: Boolean,
    val courseId: Int,
    val desc: String,
    val descMd: String,
    val envelopePic: String,
    val fresh: Boolean,
    val host: String,
    /* 文章ID */
    val id: Int,
    /* 文章连接 */
    val link: String,
    val niceDate: String,
    val niceShareDate: String,
    val origin: String,
    val prefix: String,
    val projectLink: String,
    val publishTime: Long? = 0L,
    val realSuperChapterId: Int,
    val selfVisible: Int,
    val shareDate: Long? = 0L,
    val shareUser: String,
    val superChapterId: Int,
    val superChapterName: String,
    val tags: List<TagsVO>,
    /* 文章标题 */
    val title: String,
    val type: Int,
    val userId: Int,
    val visible: Int,
    val zan: Int,
    /* 本地字段是否是 置顶文章 */
    var top: Boolean = false
) : Parcelable