package com.zjl.base.utils

import com.tencent.mmkv.MMKV

/**
 * @description:
 * @author: zhou
 * @date : 2022/1/13 22:55
 */
object CacheUtil {

    /**
     * 是否第一次进入APP
     */
    fun isFirstEnterApp(): Boolean {
        val kv = MMKV.mmkvWithID("app")
        return kv.decodeBool("firstEnter",true)
    }

    fun setFirstEnterApp(firstEnter: Boolean) : Boolean{
        val kv =  MMKV.mmkvWithID("app")
        return kv.encode("firstEnter",firstEnter)
    }

    /**
     * 首页是否开启获取置顶文章
     */
    fun isNeedTop(): Boolean {
        val kv = MMKV.mmkvWithID("app")
        return kv.decodeBool("top", true)
    }
    /**
     * 设置首页是否开启获取置顶文章
     */
    fun setIsNeedTop(isNeedTop:Boolean): Boolean {
        val kv = MMKV.mmkvWithID("app")
        return kv.encode("top", isNeedTop)
    }

}