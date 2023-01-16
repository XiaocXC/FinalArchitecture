package com.zjl.library_skin.wrapper

import android.content.res.Resources
import androidx.appcompat.view.ContextThemeWrapper
import com.zjl.library_skin.hepler.WebViewResourceHelper
import com.zjl.library_skin.provider.SkinProvider
import com.zjl.library_skin.resource.SkinResources

/**
 * @author Xiaoc
 * @since 2023-01-16
 *
 * 皮肤换肤专用Resource
 * 它会根据当前所选的皮肤，获取正确的皮肤资源数据对象
 */
class SkinContextThemeWrapper(
    private val provider: SkinProvider
): ContextThemeWrapper() {

    private var mResources: Resources? = null

    override fun getResources(): Resources {
        if(mResources == null){
            WebViewResourceHelper.addChromeResourceIfNeeded(this)
            mResources = SkinResources(provider, this, super.getResources())
        }
        return mResources!!
    }
}