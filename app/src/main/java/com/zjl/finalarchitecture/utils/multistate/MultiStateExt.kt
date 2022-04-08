package com.zjl.finalarchitecture.utils.multistate

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.zjl.base.viewmodel.BaseViewModel
import com.zy.multistatepage.MultiStateContainer
import com.zy.multistatepage.state.EmptyState
import com.zy.multistatepage.state.ErrorState
import com.zy.multistatepage.state.LoadingState
import com.zy.multistatepage.state.SuccessState

/**
 * @author Xiaoc
 * @since 2022-02-14
 *
 * 处理[MultiStateContainer]整个视图页面与Paging3相关状态联动
 * 该扩展方法可以控制细颗粒度的状态
 * 因为有些界面可能与Paging相关状态相关联来控制整个视图
 * 所以[BaseViewModel]中的rootState倒显得不那么重要
 */
fun MultiStateContainer.handleWithPaging3(
    combinedLoadStates: CombinedLoadStates,
    /**
     * 是否处理除加载完成状态的其他状态内容
     */
    enabledHandle: Boolean,
    /**
     * 重试回调
     */
    retry: () -> Unit
){
    when(val refresh = combinedLoadStates.refresh){
        is LoadState.Loading ->{
            if(!enabledHandle){
                return
            }
            show(LoadingState())
        }
        is LoadState.Error ->{
            if(!enabledHandle){
                return
            }
            show<ErrorState> {
                it.setErrorMsg(refresh.error.message ?: "")
                it.retry(retry)
            }
        }
        is LoadState.NotLoading ->{
            // 如果enableHandle为true，则说明是空状态，我们显示空布局
            if(enabledHandle){
                show(EmptyState())
            } else {
                // enabledHandle为false传给show方法作为不展示渐变动画的选择
                show(SuccessState(), enabledHandle)
            }
        }
    }
}