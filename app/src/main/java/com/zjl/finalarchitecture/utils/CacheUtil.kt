package com.zjl.finalarchitecture.utils

import com.tencent.mmkv.MMKV
import com.zjl.base.utils.globalJson
import com.zjl.base.utils.toJsonString
import kotlinx.serialization.decodeFromString

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

    /**
     * 获取搜索历史缓存数据
     */
    fun getSearchHistoryData(): List<String> {
        val kv = MMKV.mmkvWithID("cache")
        val searchCacheStr =  kv.decodeString("history")
        if (!searchCacheStr.isNullOrEmpty()) {
            return globalJson.decodeFromString(searchCacheStr)
        }
        return arrayListOf()
    }

    fun setSearchHistoryData(searchResponseStr: String) {
        val kv = MMKV.mmkvWithID("cache")
        kv.encode("history",searchResponseStr)
    }

}