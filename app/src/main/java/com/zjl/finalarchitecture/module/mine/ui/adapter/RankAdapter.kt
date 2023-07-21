package com.zjl.finalarchitecture.module.mine.ui.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import coil.load
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.data.model.CoinRecordVO

class RankAdapter : BaseQuickAdapter<CoinRecordVO, BaseViewHolder>(
    R.layout.item_rank
) {
    override fun convert(holder: BaseViewHolder, item: CoinRecordVO) {

        val index: Int = holder.adapterPosition + 1
        holder.setText(R.id.txtIndex, "$index")
        holder.setText(R.id.txtUserName, item.username)
        holder.setText(R.id.txtCoinCount, item.coinCount)

        when (index) {
            1 -> {
                holder.getView<ImageView>(R.id.imageIndex).load(R.mipmap.ic_rank_1)
            }
            2 -> {
                holder.getView<ImageView>(R.id.imageIndex).load(R.mipmap.ic_rank_2)
            }
            3 -> {
                holder.getView<ImageView>(R.id.imageIndex).load(R.mipmap.ic_rank_3)
            }
            else -> {
                holder.getView<ImageView>(R.id.imageIndex).load(ColorDrawable(Color.TRANSPARENT))
            }
        }

    }

}