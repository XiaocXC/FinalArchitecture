package com.zjl.finalarchitecture.utils.ext.smartrefresh

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.zjl.base.ui.PagingUiModel

/**
 * 处理[SmartRefreshLayout]与Paging3相关状态联动
 * 该扩展方法可以控制细颗粒度的状态
 */
fun SmartRefreshLayout.handleWithPaging3(
    combinedLoadStates: CombinedLoadStates,
    /**
     * 是否处理刷新，默认处理
     */
    isRefresh: Boolean = true,

    /**
     * 是否处理加载更多，默认不处理
     */
    isLoadMore: Boolean = false
){

    if(isRefresh){
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

    if(isLoadMore){
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

/**
 * 处理[SmartRefreshLayout]与分页相关状态联动
 * 该扩展方法可以控制细颗粒度的状态
 */
fun SmartRefreshLayout.handleWithPaging(
    pagingUiModel: PagingUiModel<*>,
){
    when(pagingUiModel){
        is PagingUiModel.Success ->{
            if(pagingUiModel.refresh){
                finishRefresh()
            } else {
                finishLoadMore()
            }
        }
        is PagingUiModel.Loading ->{
            if(pagingUiModel.refresh){
                autoRefresh()
            } else {
                autoLoadMore()
            }
        }
        is PagingUiModel.Error ->{
            if(pagingUiModel.refresh){
                finishRefresh(false)
            } else {
                finishLoadMore(false)
            }
        }
    }
}