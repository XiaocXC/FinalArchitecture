package com.zjl.base.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.zjl.lib_base.R
import com.zjl.lib_base.databinding.BaseItemDefaultLoadStateBinding
import timber.log.Timber

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
    private val shouldShowEndReached: Boolean = true,

    /**
     * 重试回调
     */
    private val retry: () -> Unit

): LoadStateAdapter<ViewBoundViewHolder<LoadState, BaseItemDefaultLoadStateBinding>>() {

    override fun onBindViewHolder(
        holder: ViewBoundViewHolder<LoadState, BaseItemDefaultLoadStateBinding>,
        loadState: LoadState
    ) {


        holder.binding.progressLoading.isVisible = loadState is LoadState.Loading
        holder.binding.btnStateRetry.isVisible = loadState is LoadState.Error
        // 文字在错误或者到达底部时显示
        holder.binding.tvStateErrorEmpty.isVisible = loadState is LoadState.Error || loadState.endOfPaginationReached

        if (loadState is LoadState.Error) {
            // 如果是错误状态
            holder.binding.tvStateErrorEmpty.text = loadState.error.message
        } else if(loadState.endOfPaginationReached && loadState is LoadState.NotLoading){
            // 如果是滑到底部
            holder.binding.tvStateErrorEmpty.text = holder.itemView.context.getString(R.string.base_load_end)
        }
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

    override fun displayLoadStateAsItem(loadState: LoadState): Boolean {
        // 这里控制什么时候显示首尾状态，源代码只在加载或错误时显示
        // 我们加入：当滑到底部时，我们仍然会显示对应状态
        return super.displayLoadStateAsItem(loadState)
                || (loadState is LoadState.NotLoading && loadState.endOfPaginationReached && shouldShowEndReached)
    }
}