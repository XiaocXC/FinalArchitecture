package com.zjl.base.ui


sealed class PagerState<T>(
    val endOfPaginationReached: Boolean
)

class AppendState<T>(
    endOfPaginationReached: Boolean,
    val append: List<T>,
): PagerState<T>(endOfPaginationReached)

class RefreshState<T>(
    endOfPaginationReached: Boolean,
    val data: List<T>
): PagerState<T>(endOfPaginationReached)

class LoadingState<T>: PagerState<T>(false)

data class ErrorState<T>(
    val error: Throwable
): PagerState<T>(false)
