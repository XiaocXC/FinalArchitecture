package com.zjl.finalarchitecture.module.toolbox.imeAnim.adapter

import android.annotation.SuppressLint
import android.widget.CheckBox
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.RoundedCornerTreatment
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.shape.ShapePath
import com.google.android.material.shape.TriangleEdgeTreatment
import com.zjl.base.utils.ext.dp
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.module.toolbox.imeAnim.data.MessageData

/**
 * @author Xiaoc
 * @since 2022-07-13
 *
 * 消息对话框Adapter
 */
class ImeMessageAdapter: BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(
    mutableListOf()
) {

    /**
     * 是否开启多选
     */
    var currentEnableSelected = false
        private set

    /**
     * 监听开启关闭多选的事件
     */
    var multiSelectListener: MultiSelectListener? = null

    /**
     * 当前已选择的数据集
     */
    var currentSelectSet = mutableSetOf<MultiItemEntity>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            currentSelectSet.clear()
            currentSelectSet.addAll(value)
            notifyDataSetChanged()
        }

    /**
     * 设置选中或取消选中某一项
     * @param item 这一项的ItemData
     * @param selected 是否选中
     * @param position Item的位置
     */
    fun setSelectContent(item: MultiItemEntity, selected: Boolean, position: Int){
        // 如果是选中，将其加入到已选数据集，如果是取消选中，将其从里面移除
        if(selected){
            currentSelectSet.add(item)
        } else {
            currentSelectSet.remove(item)
        }
        // 通知对应Item进行局部更新是否选中状态
        notifyItemChanged(position, selected)
    }

    /**
     * 全选
     */
    @SuppressLint("NotifyDataSetChanged")
    fun selectAll(){
        currentSelectSet.addAll(data)
        notifyDataSetChanged()
    }

    /**
     * 全不选
     */
    @SuppressLint("NotifyDataSetChanged")
    fun unSelectAll(){
        currentSelectSet.clear()
        notifyDataSetChanged()
    }

    init {
        // 自己的气泡
        addItemType(MessageData.MESSAGE_TYPE_TEXT_SELF, R.layout.item_ime_anim_message_bubble_me)
        // 别人的气泡
        addItemType(MessageData.MESSAGE_TYPE_TEXT_OTHER, R.layout.item_ime_anim_message_bubble_other)
    }

    override fun convert(holder: BaseViewHolder, item: MultiItemEntity) {
        when(holder.itemViewType){
            MessageData.MESSAGE_TYPE_TEXT_SELF ->{
                fillMessageBySelf(item as MessageData, holder)
            }
            MessageData.MESSAGE_TYPE_TEXT_OTHER ->{
                fillMessageByOther(item as MessageData, holder)
            }
        }
    }

    private fun fillMessageBySelf(messageData: MessageData, holder: BaseViewHolder){
        holder.setText(R.id.bubble_message, messageData.message)

        holder.setVisible(R.id.cbSelect, currentEnableSelected)

        // 判断该Item是否在已选择的数据集中，更新其选择状态
        handleSelectStatus(holder, currentSelectSet.contains(messageData))
    }

    private fun fillMessageByOther(messageData: MessageData, holder: BaseViewHolder){
        holder.setText(R.id.bubble_message, messageData.message)

        holder.setGone(R.id.cbSelect, !currentEnableSelected)

        // 判断该Item是否在已选择的数据集中，更新其选择状态
        handleSelectStatus(holder, currentSelectSet.contains(messageData))

    }


    /**
     * 局部更新，
     * 当调用 notifyItemChanged(pos, payloads) 方法时会调用到此处
     * 我们将payloads取出然后无损更新视图
     * @param payloads 额外参数值
     */
    override fun convert(holder: BaseViewHolder, item: MultiItemEntity, payloads: List<Any>) {
        if(payloads.isEmpty()){
            return
        }
        payloads.forEach {
            // 如果参数是Boolean，代表是更新
            if(it is Boolean){
                handleSelectStatus(holder, it)
            }
        }
    }

    private fun handleSelectStatus(holder: BaseViewHolder, selected: Boolean){
        val cbSelect = holder.getView<CheckBox>(R.id.cbSelect)
        cbSelect.isChecked = selected
    }

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        val cbSelect = viewHolder.getView<CheckBox>(R.id.cbSelect)
        // 禁用掉CheckBox自己的点击效果
        cbSelect.isClickable = false

        // 设置对话气泡样式，根据是自己的还是别人的气泡，设置尖头左右
        if(viewType == MessageData.MESSAGE_TYPE_TEXT_SELF){
            val messageBubble = viewHolder.getView<MaterialCardView>(R.id.messageBubble)
            val shapePathModel = ShapeAppearanceModel.builder()
                .setAllCorners(RoundedCornerTreatment())
                .setAllCornerSizes(12.dp.toFloat())
                .setRightEdge(object : TriangleEdgeTreatment(4.dp.toFloat(), false) {
                    override fun getEdgePath(
                        length: Float,
                        center: Float,
                        interpolation: Float,
                        shapePath: ShapePath
                    ) {
                        super.getEdgePath(length, 4.dp.toFloat(), interpolation, shapePath)
                    }
                })
                .build()
            messageBubble.shapeAppearanceModel = shapePathModel
        } else {
            val messageBubble = viewHolder.getView<MaterialCardView>(R.id.messageBubble)
            val shapePathModel = ShapeAppearanceModel.builder()
                .setAllCorners(RoundedCornerTreatment())
                .setAllCornerSizes(12.dp.toFloat())
                .setLeftEdge(object : TriangleEdgeTreatment(4.dp.toFloat(), false) {
                    override fun getEdgePath(
                        length: Float,
                        center: Float,
                        interpolation: Float,
                        shapePath: ShapePath
                    ) {
                        super.getEdgePath(length, 4.dp.toFloat(), interpolation, shapePath)
                    }
                })
                .build()
            messageBubble.shapeAppearanceModel = shapePathModel
        }


    }

    /**
     * 开启或关闭多选功能
     */
    fun toggleSelect(){
        if(currentEnableSelected){
            disableSelect()
        } else {
            enableSelect()
        }
    }

    /**
     * 开启多选
     */
    @SuppressLint("NotifyDataSetChanged")
    fun enableSelect(){
        currentEnableSelected = true
        notifyDataSetChanged()

        multiSelectListener?.selectStatusChanged(true)
    }

    /**
     * 关闭多选
     */
    @SuppressLint("NotifyDataSetChanged")
    fun disableSelect(){
        currentEnableSelected = false
        // 关闭多选清除多选的内容
        currentSelectSet.clear()
        notifyDataSetChanged()

        multiSelectListener?.selectStatusChanged(false)
    }

    /**
     * 监听开启多选关闭多选的监听
     */
    interface MultiSelectListener{
        fun selectStatusChanged(enable: Boolean)
    }
}