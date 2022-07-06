package com.zjl.finalarchitecture.utils.ext

import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.zjl.base.ui.PagingUiModel
import com.zy.multistatepage.MultiStateContainer
import com.zy.multistatepage.state.EmptyState
import com.zy.multistatepage.state.ErrorState
import com.zy.multistatepage.state.LoadingState
import com.zy.multistatepage.state.SuccessState

/**
 * @author Xiaoc
 * @since  2022-07-04
 *
 * PagingUiModel分页数据与视图状态的处理
 * @param adapter 列表适配器
 * @param stateContainer 状态容器，为null则不处理
 * @param refreshLayout smartRefresh，为null则不处理
 * @param retry 重试回调
 **/
fun <T> PagingUiModel<T>.handlePagingStatus(
    adapter: BaseQuickAdapter<T, *>,
    stateContainer: MultiStateContainer? = null,
    refreshLayout: SmartRefreshLayout? = null,
    retry: () -> Unit = {},
) {

    when (this) {
        // 如果是成功
        is PagingUiModel.Success -> {
            // 如果是重新刷新
            if (this.refresh) {
                // 如果数据集为空，显示空布局
                if (this.data.isEmpty()) {
                    stateContainer?.show(EmptyState())
                } else {
                    // 设置数据到Adapter中
                    adapter.setList(this.data)
                    // 否则显示成功布局（即正常视图）
                    stateContainer?.show(SuccessState())
                }
                // 结束下拉刷新
                refreshLayout?.finishRefresh(500)
            } else {
                // 如果不是重新刷新，将数据加入到Adapter中
                adapter.addData(this.data)
                // 结束加载更多
                refreshLayout?.finishLoadMore(500)
            }
//            // 是否加载更多
//            if(this.hasMore){
//                adapter.loadMoreModule.loadMoreComplete()
//            } else {
//                adapter.loadMoreModule.loadMoreEnd()
//            }
        }

        // 如果是失败
        is PagingUiModel.Error -> {
            // 如果是重新刷新
            if (this.refresh) {
                refreshLayout?.finishRefresh(false)
                // 显示错误布局
                stateContainer?.show<ErrorState> {
                    it.setErrorMsg(this.error.message ?: "")
                    it.retry(retry)
                }
            } else {
                refreshLayout?.finishLoadMore(false)
//                adapter.loadMoreModule.loadMoreFail()
            }
        }

        // 如果是加载中
        is PagingUiModel.Loading -> {

            if (this.refresh) {
                refreshLayout?.autoRefresh()
                if (adapter.itemCount <= 0) {
                    stateContainer?.show(LoadingState())
                }
            } else {
                refreshLayout?.autoLoadMore()
            }
        }
    }
}