package com.zjl.finalarchitecture.module.splash.ui.adapter
import android.widget.ImageView
import android.widget.TextView
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder
import com.zjl.finalarchitecture.R
import com.zjl.finalarchitecture.databinding.BannerItemWelcomeBinding

/**
 * @description: 欢迎页 Adapter
 * @author: zhou
 * @date : 2022/1/14 15:42
 */
class WelcomeBannerAdapter : BaseBannerAdapter<String>() {

    override fun bindData(holder: BaseViewHolder<String>?, data: String?, position: Int, pageSize: Int) {
        val txt: TextView = holder!!.findViewById(R.id.txtBannerItem)
        txt.text = data
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.banner_item_welcome
    }


}