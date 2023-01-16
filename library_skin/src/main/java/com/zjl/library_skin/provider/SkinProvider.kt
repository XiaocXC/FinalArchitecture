package com.zjl.library_skin.provider

import android.content.Context

/**
 * @author Xiaoc
 * @since 2023-01-16
 *
 * 皮肤提供器接口
 * 它提供了给你自定义获取资源数据的方法
 * 你可以通过自定义这些方法，来达到自定义资源内容的目的
 */
interface SkinProvider {

    /**
     * 是否开启代替该条资源数据的样式
     * @param resId 资源ID
     * @param context 上下文
     * @return true 代表要代替，false 代表保持原内容
     */
    fun enabledReplaceResId(context: Context, resId: Int): Boolean

    /**
     * 当 [enabledReplaceResId] 返回true 时调用此方法
     * 返回一个字符串用来寻找代替后的资源数据内容
     * @param context 上下文
     * @param resName 资源名
     * @param resType 资源类型
     * @return 代替后的资源名
     */
    fun replaceResIdPrefix(context: Context, resName: String, resType: String): String
}