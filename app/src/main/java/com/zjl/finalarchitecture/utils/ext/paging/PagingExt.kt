package com.zjl.finalarchitecture.utils.ext.paging

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zjl.base.adapter.DefaultLoadStateAdapter

/**
 * @author Xiaoc
 * @since  2022-05-03
 *
 * 自动处理Paging的Footer状态值
 * @return ConcatAdapter
 **/
fun <T: Any, VH: RecyclerView.ViewHolder> PagingDataAdapter<T, VH>.withLoadState(): ConcatAdapter {
    return withLoadStateFooter(DefaultLoadStateAdapter{
        retry()
    })
}