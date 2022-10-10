package com.zjl.library_trace.ext

import android.app.Activity
import android.content.ContextWrapper
import android.view.View


internal fun getActivityFromView(view: View): Activity? {
    var context = view.context
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    return null
}