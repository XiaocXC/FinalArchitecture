package com.zjl.library_skin.inflater

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.zjl.library_skin.widget.compat.SkinView

class CompatViewInflater: SkinViewInflater {

    override fun createView(context: Context, name: String?, attributeSet: AttributeSet?): View? {
        return when(name){
            "View" ->{
                SkinView(context, attributeSet)
            }
            else ->{
                null
            }
        }
    }
}