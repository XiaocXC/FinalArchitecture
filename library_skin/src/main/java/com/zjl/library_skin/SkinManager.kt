package com.zjl.library_skin

import android.app.Application
import com.zjl.library_skin.inflater.SkinViewInflater
import com.zjl.library_skin.provider.SkinProvider

/**
 * @author Xiaoc
 * @since 2023-01-16
 *
 * 皮肤管理器
 * 可以在application中初始化
 */
class SkinManager(
    private val application: Application
) {

    companion object {

        @Volatile
        private var mSkinManager: SkinManager? = null

        fun init(application: Application): SkinManager{
            if(mSkinManager == null){
                synchronized(SkinManager::class){
                    mSkinManager = SkinManager(application)
                }
            }
            return mSkinManager!!
        }

        fun getInstance(): SkinManager{
            return mSkinManager ?: throw IllegalStateException("没有初始化SkinManager！")
        }
    }

    var provider: SkinProvider? = null

    val skinViewInflaters = mutableListOf<SkinViewInflater>()

    fun setSkinProvider(skinProvider: SkinProvider): SkinManager{
        this.provider = skinProvider
        return this
    }

    fun addSkinViewInflater(skinViewInflater: SkinViewInflater): SkinManager{
        skinViewInflaters.add(skinViewInflater)
        return this
    }

    fun addSkinViewInflaters(skinViewInflaterList: List<SkinViewInflater>): SkinManager{
        skinViewInflaters.addAll(skinViewInflaterList)
        return this
    }
}