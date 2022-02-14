package com.zjl.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.zjl.lib_base.databinding.BaseItemDefaultLoadStateBinding

/**
 * @author Xiaoc
 * @since 2022-02-11
 *
 * 一个默认的分页状态Adapter
 */
class DefaultLoadStateAdapter(
    /**
     * 是否展示已经到底了提示
     */
    private val shouldShowEndReached: Boolean = false,

    /**
     * 重试回调
     */
    private val retry: () -> Unit
): LoadStateAdapter<ViewBoundViewHolder<LoadState, BaseItemDefaultLoadStateBinding>>() {

    override fun onBindViewHolder(
        holder: ViewBoundViewHolder<LoadState, BaseItemDefaultLoadStateBinding>,
        loadState: LoadState
    ) {
        loadState.endOfPaginationReached
        // 如果是错误状态
        if(loadState is LoadState.Error){
            holder.binding.tvStateErrorEmpty.text = loadState.error.message
        }

        holder.binding.progressLoading.isVisible = loadState is LoadState.Loading
        holder.binding.btnStateRetry.isVisible = loadState is LoadState.Error
        holder.binding.tvStateErrorEmpty.isVisible = loadState is LoadState.Error
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): ViewBoundViewHolder<LoadState, BaseItemDefaultLoadStateBinding> {
        return ViewBoundViewHolder<LoadState, BaseItemDefaultLoadStateBinding>(
            BaseItemDefaultLoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ).apply {
            // 设置重试监听
            binding.btnStateRetry.setOnClickListener {
                retry()
            }
        }
    }
}