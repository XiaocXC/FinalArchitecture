package com.zjl.finalarchitecture.module.toolbox.progressList.download

import com.zjl.base.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * @author Xiaoc
 * @since 2022-09-20
 *
 * 下载进度列表案例 ViewModel
 */
class DownloadProgressListViewModel: BaseViewModel() {

    private val _downloadList = MutableStateFlow<List<DownloadProgressData>>(emptyList())
    val downloadList: StateFlow<List<DownloadProgressData>> = _downloadList

    override fun refresh() {

    }
}