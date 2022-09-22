package com.zjl.finalarchitecture.module.toolbox.progressList.download

import androidx.lifecycle.viewModelScope
import com.zjl.base.viewmodel.BaseViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * @author Xiaoc
 * @since 2022-09-20
 *
 * 下载进度列表案例 ViewModel
 */
class DownloadProgressListViewModel: BaseViewModel() {

    private val _updateProgress = MutableSharedFlow<DownloadProgressData>()
    val updateProgress: SharedFlow<DownloadProgressData> = _updateProgress

    private val _downloadList = MutableStateFlow<List<DownloadProgressData>>(emptyList())
    val downloadList: StateFlow<List<DownloadProgressData>> = _downloadList

    init {
        initData()
    }

    override fun refresh() {
        viewModelScope.launch {
            val downloads = buildList {
                add(DownloadProgressData(1, "标题1", "这是副标题1", 0))
                add(DownloadProgressData(2, "标题2", "这是副标题2", 0))
                add(DownloadProgressData(3, "标题3", "这是副标题3", 0))
                add(DownloadProgressData(4, "标题4", "这是副标题4", 0))
                add(DownloadProgressData(5, "标题5", "这是副标题5", 0))
                add(DownloadProgressData(6, "标题6", "这是副标题6", 0))
                add(DownloadProgressData(7, "标题7", "这是副标题7", 0))
                add(DownloadProgressData(8, "标题8", "这是副标题8", 0))
                add(DownloadProgressData(9, "标题9", "这是副标题9", 0))
                add(DownloadProgressData(10, "标题10", "这是副标题10", 0))
                add(DownloadProgressData(11, "标题11", "这是副标题11", 0))
                add(DownloadProgressData(12, "标题12", "这是副标题12", 0))
                add(DownloadProgressData(13, "标题13", "这是副标题13", 0))
                add(DownloadProgressData(14, "标题14", "这是副标题14", 0))
                add(DownloadProgressData(15, "标题15", "这是副标题15", 0))
            }
            _downloadList.value = downloads

            // 模拟下载
            downloads.forEach { data ->
                viewModelScope.launch {
                    var progress = data.currentProgress
                    // 随机一个更新间隙
                    val randomInternal = Random.nextLong(1000L, 3000L)
                    // 随机一个更新增量
                    val randomAdd = Random.nextInt(5, 20)
                    while (progress < 100){
                        delay(randomInternal)
                        progress += randomAdd
                        _updateProgress.emit(data.copy(currentProgress = progress))
                    }
                }
            }
        }
    }
}