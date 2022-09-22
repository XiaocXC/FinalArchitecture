package com.zjl.finalarchitecture.module.toolbox.progressList.timer

import android.os.CountDownTimer
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zjl.finalarchitecture.R
import timber.log.Timber

/**
 * @author Xiaoc
 * @since 2022-09-20
 *
 * 倒计时列表 ITEM Adapter
 */
class TimerProgressAdapter: BaseQuickAdapter<TimerProgressData, BaseViewHolder>(
    R.layout.item_progress_timer
) {

    /**
     * Timer倒计时缓存
     * 每一个Item对应的Id分别有一个倒计时实例
     */
    private val timerMap = mutableMapOf<Int, CountDownTimer>()

    override fun convert(holder: BaseViewHolder, item: TimerProgressData) {
        holder.itemView.tag = item

        holder.setText(R.id.tv_title, item.title)
            .setText(R.id.tv_subtitle, item.subTitle)

        val currentTime = System.currentTimeMillis()
        val lastTime = item.endTime - currentTime

        // 更新倒计时文本
        updateLastTime(holder, lastTime)
    }

    override fun convert(holder: BaseViewHolder, item: TimerProgressData, payloads: List<Any>) {
        if(payloads.isEmpty()){
            return
        }

        // 获得剩余时间
        val currentTime = System.currentTimeMillis()
        val lastTime = item.endTime - currentTime

        payloads.forEach { _ ->
            updateLastTime(holder, lastTime)
        }
    }

    private fun updateLastTime(holder: BaseViewHolder, lastTime: Long){
        if(lastTime >= 0){
            holder.setText(R.id.tv_count_down, "${(lastTime / 1000)}秒")
        } else {
            holder.setText(R.id.tv_count_down, "已结束")
        }

    }

    /**
     * 当View附着到视图后调用
     * 我们此时才会把计时器启动
     */
    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        super.onViewAttachedToWindow(holder)
        // 对应Item附着后，我们创建对应Timer
        val item = (holder.itemView.tag as TimerProgressData)
        val timer = timerMap[item.id]

        val currentTime = System.currentTimeMillis()
        val lastTime = item.endTime - currentTime
        // 如果Timer为空且需要倒计时，我们创建并开启倒计时
        if(timer == null && lastTime >= 0){
            // 创建一个到指定时间停止，每隔1s更新一次的Timer定时器
            val createTimer = object: CountDownTimer(lastTime,1000L){

                override fun onTick(millisUntilFinished: Long) {
                    // 更新数据
                    notifyItemChanged(holder.absoluteAdapterPosition, item)
                }

                override fun onFinish() {
                    // 更新数据
                    notifyItemChanged(holder.absoluteAdapterPosition, item)
                }

            }
            // 启动倒计时
            createTimer.start()
            timerMap[item.id] = createTimer
        }
    }

    /**
     * 当视图在界面中移除后调用此方法
     * 此时我们把定时器移除，防止过多定时器
     */
    override fun onViewDetachedFromWindow(holder: BaseViewHolder) {
        super.onViewDetachedFromWindow(holder)
        // 对应Item销毁后，我们取消对应Timer，防止太多Timer启动
        val id = (holder.itemView.tag as TimerProgressData).id
        Timber.i("视图消失：${id}")
        val timer = timerMap[id] ?: return
        // 停止倒计时
        timer.cancel()
        // 移除对应多的Timer
        timerMap.remove(id)
    }
}

data class TimerProgressData(
    val id: Int,

    val title: String,

    val subTitle: String,

    /**
     * 结束时间（真实的时间），单位为ms
     */
    val endTime: Long
)