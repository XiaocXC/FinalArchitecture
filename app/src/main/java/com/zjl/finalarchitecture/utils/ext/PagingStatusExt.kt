package com.zjl.finalarchitecture.utils.ext

import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.zjl.base.ui.PagingUiModel
import com.zjl.base.ui.state.LoadingState
import com.zy.multistatepage.MultiStateContainer
import com.zy.multistatepage.state.EmptyState
import com.zy.multistatepage.state.ErrorState
import com.zy.multistatepage.state.SuccessState

/**
 * @author Xiaoc
 * @since  2022-07-04
 *
 * PagingUiModel分页数据与视图状态的处理
 *
 * 该扩展方法可以根据 PagingUiModel 的状态适配SmartRefreshLayout、BaseAdapter等内容的分页状态
 * 并根据具体情况加载更多
 * 该扩展处理了使用Navigation时出现的切换夜间模式或界面时造成的显示数据缺失的问题
 * 注意数据丢失会出现在满足以下条件的情况下：
 * 1.你的分页请求初始化是放在ViewModel的初始化中，而不是Fragment中的
 * 2.你使用了LiveData、StateFlow之类的观察类
 * 3.你没有使用Paging库进行分页，而是使用adapter、smartRefresh等界面库的分页操作
 * 以上条件均满足，当切换界面重建则会出现数据丢失的情况
 *
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
                    // 设置数据到Adapter中，这里分两种情况
                    // 如果当前adapter中没有数据，我们填充所有数据
                    // 否则我们填充当前数据
                    if(adapter.data.isEmpty()){
                        adapter.setList(this.totalList)
                    } else {
                        adapter.setList(this.data)
                    }

                    // 否则显示成功布局（即正常视图）
                    stateContainer?.show(SuccessState())
                }
                // 结束下拉刷新
                refreshLayout?.finishRefresh(500)
            } else {
                // 如果不是重新刷新，将数据加入到Adapter中，这里分两种情况
                // 如果当前adapter中没有数据，我们填充所有数据
                // 否则我们添加当前数据
                if (adapter.data.isEmpty()){
                    adapter.setList(this.totalList)
                } else {
                    adapter.addData(this.data)
                }
                // 结束加载更多
                refreshLayout?.finishLoadMore(500)
            }
            // 如果SmartRefresh为空，我们才使用Adapter的加载更多状态
            if(refreshLayout == null){
                // 是否加载更多
                if(this.noMore){
                    adapter.loadMoreModule.loadMoreEnd()
                } else {
                    adapter.loadMoreModule.loadMoreComplete()
                }
            }
        }

        // 如果是失败
        is PagingUiModel.Error -> {
            // 判断一下，如果当前adapter是空数据，可能是出现了界面重绘，我们填充一下所有数据
            if (adapter.data.isEmpty()) {
                adapter.setList(this.totalList)
            }
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
                // 如果SmartRefresh为空，我们才使用Adapter的加载状态
                if(refreshLayout == null){
                    // 加载失败
                    adapter.loadMoreModule.loadMoreFail()
                }
            }
        }

        // 如果是加载中
        is PagingUiModel.Loading -> {
            // 判断一下，如果当前adapter是空数据，可能是出现了界面重绘，我们填充一下所有数据
            if (adapter.data.isEmpty()){
                adapter.setList(this.totalList)
            }
            if (this.refresh) {
                refreshLayout?.autoRefresh()
                // 如果adapter不存在数据，我们展示加载状态框
                if (adapter.itemCount <= 0) {
                    stateContainer?.show(LoadingState())
                }
            } else {
                refreshLayout?.autoLoadMore()
                // 如果SmartRefresh为空，我们才使用Adapter的加载状态
                if(refreshLayout == null){
                    // 加载失败
                    adapter.loadMoreModule.loadMoreToLoading()
                }
            }
        }
    }
}