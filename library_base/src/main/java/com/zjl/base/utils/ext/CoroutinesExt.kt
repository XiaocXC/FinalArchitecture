package com.zjl.base.utils.ext

import kotlinx.coroutines.Job

/**
 * 如果活跃则取消当前协程工作
 */
fun Job?.cancelIfActive(){
    if(this?.isActive == true){
        cancel()
    }
}