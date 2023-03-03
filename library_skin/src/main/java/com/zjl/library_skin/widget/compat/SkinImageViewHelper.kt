package com.zjl.library_skin.widget.compat

import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.zjl.library_skin.R
import com.zjl.library_skin.SkinManager

class SkinImageViewHelper(
    private val view: ImageView
) {

    private var srcResId: Int = View.NO_ID

    fun loadFromAttributes(attr: AttributeSet?, defStyleAttr: Int){
        val ta: TypedArray = view.context.obtainStyledAttributes(
            attr, R.styleable.AppCompatImageView,
            defStyleAttr, 0
        )
        ta.use {
            srcResId = ta.getResourceId(R.styleable.AppCompatImageView_android_src, View.NO_ID)
            val srcCompatResId = ta.getResourceId(R.styleable.AppCompatImageView_srcCompat, View.NO_ID)
            if(srcCompatResId != View.NO_ID){
                srcResId = srcCompatResId
            }
        }
        applySkin()
    }

    fun onSetImageResource(resId: Int){
        srcResId = resId
        applySkin()
    }

    fun applySkin(){
        val provider = SkinManager.getInstance().provider ?: return
        val replaceSrcResId = provider.resetResIdIfNeed(view.context, srcResId)
        if(replaceSrcResId != View.NO_ID){
            view.setImageDrawable(ContextCompat.getDrawable(view.context, replaceSrcResId))
        }
    }
}