package com.zjl.base.ui.state

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.zjl.base.R
import com.zjl.base.databinding.BaseUiStateErrorEmptyViewBinding
import com.zy.multistatepage.MultiState
import com.zy.multistatepage.MultiStateContainer

/**
 * @author Xiaoc
 * @since 2022-04-12
 *
 * 错误状态
 */
class ErrorState: MultiState() {

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
        binding.ivState.setImageResource(R.drawable.base_ui_ic_unknown_error)
        binding.btnRetry.setOnClickListener {
            retryListener?.retry()
        }
    }

    fun setErrorMsg(message: String?){
        binding.tvErrorInfo.text = message
    }

    fun setErrorIcon(@DrawableRes iconRes: Int){
        binding.ivState.setImageResource(iconRes)
    }

    fun retry(retryListener: OnRetryClickListener?){
        this.retryListener = retryListener
    }

    fun setErrorTextColor(@ColorInt color: Int){
        binding.tvErrorInfo.setTextColor(color)
    }

    fun interface OnRetryClickListener {
        fun retry()
    }
}