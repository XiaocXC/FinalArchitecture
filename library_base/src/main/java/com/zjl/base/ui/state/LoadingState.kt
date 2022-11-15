package com.zjl.base.ui.state

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import com.zjl.lib_base.R
import com.zjl.lib_base.databinding.BaseUiStateLoadingViewBinding
import com.zy.multistatepage.MultiState
import com.zy.multistatepage.MultiStateContainer

/**
 * @author Xiaoc
 * @since 2022-04-12
 *
 * 加载中状态
 */
class LoadingState: MultiState() {

    private lateinit var binding: BaseUiStateLoadingViewBinding

    override fun onCreateMultiStateView(
        context: Context,
        inflater: LayoutInflater,
        container: MultiStateContainer
    ): View {
        binding = BaseUiStateLoadingViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onMultiStateViewCreate(view: View) {
        binding.tvLoadingMsg.setText(R.string.base_ui_description_status_view_loading)
    }

    fun setLoadingMsg(text: String?){
        binding.tvLoadingMsg.text = text
    }

    fun setLoadingTextColor(@ColorInt color: Int){
        binding.tvLoadingMsg.setTextColor(color)
    }
}