package com.zjl.finalarchitecture.module.toolbox.progressList.download

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.zjl.finalarchitecture.R

/**
 * @author Xiaoc
 * @since 2022-09-20
 *
 * 下载进度列表 ITEM Adapter
 */
class DownloadProgressAdapter: BaseQuickAdapter<DownloadProgressData, BaseViewHolder>(
    R.layout.item_progress_download
) {

    override fun convert(holder: BaseViewHolder, item: DownloadProgressData) {
        holder.itemView.tag = item

        holder.setText(R.id.tv_title, item.title)
            .setText(R.id.tv_subtitle, item.subTitle)

        updateProgress(holder, item.currentProgress, false)
    }

    override fun convert(holder: BaseViewHolder, item: DownloadProgressData, payloads: List<Any>) {
        if(payloads.isEmpty()){
            return
        }

        payloads.forEach { _ ->
            updateProgress(holder, item.currentProgress)
        }
    }

    private fun updateProgress(holder: BaseViewHolder, progress: Int, showAnim: Boolean = true){
        val progressDownload = holder.getView<LinearProgressIndicator>(R.id.progress_download)
        progressDownload.setProgress(progress, showAnim)
    }
}

data class DownloadProgressData(
    val id: Int,

    val title: String,

    val subTitle: String,

    val currentProgress: Int
)