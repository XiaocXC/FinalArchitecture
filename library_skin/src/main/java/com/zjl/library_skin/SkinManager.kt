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
    private val application: Application,
    provider: SkinProvider
) {

    companion object {

        @Volatile
        private var mSkinManager: SkinManager? = null

        fun init(application: Application, provider: SkinProvider): SkinManager{
            if(mSkinManager == null){
                synchronized(SkinManager::class){
                    mSkinManager = SkinManager(application, provider)
                }
            }
            return mSkinManager!!
        }

        fun getInstance(): SkinManager{
            return mSkinManager ?: throw IllegalStateException("没有初始化SkinManager！")
        }
    }

    lateinit var provider: SkinProvider

    val skinViewInflaters = mutableListOf<SkinViewInflater>()

    fun addSkinViewInflater(skinViewInflater: SkinViewInflater){
        skinViewInflaters.add(skinViewInflater)
    }

    fun addSkinViewInflaters(skinViewInflaterList: List<SkinViewInflater>){
        skinViewInflaters.addAll(skinViewInflaterList)
    }
}