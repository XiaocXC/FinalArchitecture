package com.zjl.library_skin.wrapper

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
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
    context: Context?,
    private val provider: SkinProvider
): ContextWrapper(context) {

    private var mResources: Resources? = null

    override fun getResources(): Resources {
        if(mResources == null){
            // 防止Android 7以上WebView的选择菜单出现闪退
            WebViewResourceHelper.addChromeResourceIfNeeded(this)
            // 替换Resources的为SkinResource，这样我们使用context调用getColor等方法时能够根据皮肤来返回对应内容
            mResources = SkinResources(provider, this, super.getResources())
        }
        return mResources!!
    }
}