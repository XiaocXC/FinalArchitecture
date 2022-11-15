package com.zjl.base.ui.state

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.zjl.lib_base.R
import com.zjl.lib_base.databinding.BaseUiStateErrorEmptyViewBinding
import com.zy.multistatepage.MultiState
import com.zy.multistatepage.MultiStateContainer

/**
 * @author Xiaoc
 * @since 2022-04-12
 *
 * 空状态
 */
class EmptyState: MultiState() {

    private lateinit var binding: BaseUiStateErrorEmptyViewBinding

    private var retryListener: OnRetryClickListener? = null

    override fun onCreateMultiStateView(
        context: Context,
        inflater: LayoutInflater,
        container: MultiStateContainer
    ): View {
        binding = BaseUiStateErrorEmptyViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onMultiStateViewCreate(view: View) {
        binding.tvErrorInfo.setText(R.string.base_ui_description_status_view_empty)
        binding.ivState.setImageResource(R.drawable.base_ui_ic_empty)
        binding.btnRetry.setOnClickListener {
            retryListener?.retry()
        }
    }

    fun setEmptyMsg(message: String?){
        binding.tvErrorInfo.text = message
    }

    fun setEmptyIcon(@DrawableRes iconRes: Int){
        binding.ivState.setImageResource(iconRes)
    }

    fun setEmptyTextColor(@ColorInt color: Int){
        binding.tvErrorInfo.setTextColor(color)
    }

    fun retry(retryListener: OnRetryClickListener?){
        this.retryListener = retryListener
    }

    fun interface OnRetryClickListener {
        fun retry()
    }
}