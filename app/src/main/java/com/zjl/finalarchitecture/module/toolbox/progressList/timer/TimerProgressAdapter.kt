package com.zjl.finalarchitecture.module.toolbox.progressList.timer

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zjl.finalarchitecture.R

/**
 * @author Xiaoc
 * @since 2022-09-20
 *
 * 倒计时列表 ITEM Adapter
 */
class TimerProgressAdapter: BaseQuickAdapter<TimerProgressData, BaseViewHolder>(
    R.layout.item_progress_timer
) {

    override fun convert(holder: BaseViewHolder, item: TimerProgressData) {
        holder.setText(R.id.tv_title, item.title)
            .setText(R.id.tv_subtitle, item.subTitle)
    }

    override fun convert(holder: BaseViewHolder, item: TimerProgressData, payloads: List<Any>) {

    }
}

data class TimerProgressData(
    val id: Int,

    val title: String,

    val subTitle: String,

    /**
     * 结束时间（真实的时间），单位为ms
     */
    val endTime: Long,

    /**
     * 剩余倒计时的时间，此内容会动态更新，单位为ms
     */
    val lastTime: Long
)