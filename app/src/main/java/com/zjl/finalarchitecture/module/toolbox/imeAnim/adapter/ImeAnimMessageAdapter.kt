package com.zjl.finalarchitecture.module.toolbox.imeAnim.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zjl.finalarchitecture.R

/**
 * @author Xiaoc
 * @since 2022-07-13
 *
 * 消息对话框Adapter
 * 这里只是做一个示例，所以并没有初始化数据集
 * 而是直接默认30条数据然后一条自己的，一条别人的
 */
class ImeAnimMessageAdapter: BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(
    mutableListOf()
) {

    init {
        // 自己的气泡
        addItemType(0, R.layout.item_ime_anim_message_bubble_me)
        // 别人的气泡
        addItemType(1, R.layout.item_ime_anim_message_bubble_other)
    }

    override fun convert(holder: BaseViewHolder, item: MultiItemEntity) {}

    override fun getItem(position: Int): MultiItemEntity {
        return object: MultiItemEntity{
            override val itemType: Int
                get() = position % 2
        }
    }

    override fun getItemCount(): Int {
        return 30
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).itemType
    }
}