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

}