package com.zjl.library_skin.provider

import android.content.Context
import android.util.Log
import android.view.View

/**
 * @author Xiaoc
 * @since 2023-01-16
 *
 * 皮肤提供器接口
 * 它提供了给你自定义获取资源数据的方法
 * 你可以通过自定义这些方法，来达到自定义资源内容的目的
 */
abstract class SkinProvider {

    /**
     * 是否支持代替该条资源数据的样式
     * 这里可以判断当前主题是否需要替换资源
     *
     * @param context 上下文
     * @return true 代表要代替，false 代表保持原内容
     */
    abstract fun support(context: Context): Boolean

    /**
     * 当 [support] 返回true 时调用此方法
     * 返回一个字符串用来寻找代替后的资源数据内容
     * @param context 上下文
     * @param resName 资源名
     * @param resType 资源类型
     * @return 代替后的资源名
     */
    abstract fun replaceResIdPrefix(context: Context, resName: String, resType: String): String

    open fun resetResIdIfNeed(context: Context?, resId: Int): Int{
        if(context == null){
            return resId
        }
        if(!support(context)){
            return resId
        }
        if(resId == View.NO_ID){
            return resId
        }

        var newResId = resId
        val res = context.resources
        try {
            val resPkg = res.getResourcePackageName(resId)
            // 非本包名下的资源则无需替换
            if(context.packageName != resPkg){
                return newResId
            }
            val resName = res.getResourceEntryName(resId)
            val resType = res.getResourceTypeName(resId)
            Log.i("SkinProvider", "当前查阅的resName:$resName -- resType:$resType")
            // 获取对应皮肤的资源 id
            val newResName = replaceResIdPrefix(context, resName, resType)
            val id = res.getIdentifier(newResName, resType, resPkg)
            if(id != 0){
                newResId = id
            }
        } catch (e: Exception){
            e.printStackTrace()
        } finally { }
        return newResId
    }
}