package com.zjl.finalarchitecture.module.mine.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.data.model.CoinRecordVO

class IntegralAdapter : BaseQuickAdapter<CoinRecordVO, BaseViewHolder>(
    R.layout.item_integral
) {
    override fun convert(holder: BaseViewHolder, item: CoinRecordVO) {
        holder.setText(R.id.txtCoinCount, "+" + item.coinCount)
        holder.setText(R.id.txtTitle, item.reason)
        holder.setText(R.id.txtTime, item.desc)
    }

}