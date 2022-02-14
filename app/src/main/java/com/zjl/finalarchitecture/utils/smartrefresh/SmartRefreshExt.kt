package com.zjl.finalarchitecture.utils.smartrefresh

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * 处理[SmartRefreshLayout]与Paging3相关状态联动
 * 该扩展方法可以控制细颗粒度的状态
 */
fun SmartRefreshLayout.handleWithPaging3(
    combinedLoadStates: CombinedLoadStates,
    /**
     * 是否处理刷新，默认处理
     */
    enableHandleRefresh: Boolean = true,
    /**
     * 是否处理加载更多，默认不处理
     */
    enableHandleLoadMore: Boolean = false
){

    if(enableHandleRefresh){
        when(combinedLoadStates.refresh){
            is LoadState.Loading ->{
                autoRefreshAnimationOnly()
            }
            is LoadState.Error ->{
                finishRefresh(false)
            }
            is LoadState.NotLoading ->{
                finishRefresh()
            }
        }
    }

    if(enableHandleLoadMore){
        when(combinedLoadStates.append){
            is LoadState.Loading ->{
                autoLoadMoreAnimationOnly()
            }
            is LoadState.Error ->{
                finishLoadMore(false)
            }
            is LoadState.NotLoading ->{
                finishLoadMore()
            }
        }
    }
}