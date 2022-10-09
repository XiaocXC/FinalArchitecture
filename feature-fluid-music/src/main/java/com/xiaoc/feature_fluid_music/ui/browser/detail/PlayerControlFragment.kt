package com.xiaoc.feature_fluid_music.ui.browser.detail

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.format.DateUtils
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import coil.load
import com.google.android.material.slider.Slider
import com.gyf.immersionbar.ImmersionBar
import com.xiaoc.feature_fluid_music.R
import com.xiaoc.feature_fluid_music.databinding.FluidMusicFragmentPlayerControlBinding
import com.xiaoc.feature_fluid_music.service.tree.MediaItemTree
import com.xiaoc.feature_fluid_music.ui.FluidMusicMainViewModel
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.ext.getAttrColor
import com.zjl.base.utils.launchAndCollectIn
import com.zjl.base.utils.materialcolor.PaletteUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author Xiaoc
 * @since  2022-09-02
 *
 * 播放控制页面
 **/
class PlayerControlFragment: BaseFragment<FluidMusicFragmentPlayerControlBinding, PlayerDetailViewModel>() {

    companion object {
        /**
         * 用于position字符串更新的缓冲
         */
        private val positionStringBuilder = StringBuilder(6)
    }

    private val fluidMusicMainViewModel by activityViewModels<FluidMusicMainViewModel>()

    private var sliderJob: Job? = null

    override fun createViewModel(): PlayerDetailViewModel {
        val viewModel by viewModels<PlayerDetailViewModel>(ownerProducer = {
            parentFragment ?: this
        })
        return viewModel
    }

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        // 单独给顶部滑块设置marginTop
        ImmersionBar.setTitleBarMarginTop(this, mBinding.viewTip)

        // 播放暂停
        mBinding.btnPauseResume.setOnClickListener {
            mViewModel.pauseOrResume()
        }

        // 下一首
        mBinding.btnPauseNext.setOnClickListener {
            mViewModel.next()
        }

        // 上一首
        mBinding.btnPausePrevious.setOnClickListener {
            mViewModel.previous()
        }

        // Slider进度条滑动事件
        mBinding.seekbarProgress.addOnSliderTouchListener(object: Slider.OnSliderTouchListener{
            override fun onStartTrackingTouch(slider: Slider) {
                sliderJob?.cancel()
            }

            override fun onStopTrackingTouch(slider: Slider) {
                // 停止滑动后跳转到指定位置
                mViewModel.seekTo(slider.value.toLong())
                startUpdateSlider()
            }

        })

        // Slider进度改变的监听
        mBinding.seekbarProgress.addOnChangeListener { _, value, _ ->
            mBinding.tvCurrentPosition.text = DateUtils.formatElapsedTime(positionStringBuilder,value.toLong() / 1000L)
        }

        // 设置滑动指示器格式
        mBinding.seekbarProgress.setLabelFormatter {
            DateUtils.formatElapsedTime(positionStringBuilder,it.toLong() / 1000L)
        }
    }

    override fun createObserver() {
        // 当前播放信息
        mViewModel.currentPlayInfo.launchAndCollectIn(viewLifecycleOwner){
            mBinding.seekbarProgress.value = 0f
            updateCurrentMediaInfo(it)
        }

        // 当前播放状态
        mViewModel.currentPlaybackState.launchAndCollectIn(viewLifecycleOwner){
            updateCurrentPlayerState(it.isPlaying)
            val duration = it.duration.toFloat()
            mBinding.seekbarProgress.valueTo = if(duration <= 0f){
                0.01f
            } else {
                duration
            }

            sliderJob?.cancel()
            if(it.isPlaying){
                startUpdateSlider()
            }

        }

        fluidMusicMainViewModel.musicColor.launchAndCollectIn(viewLifecycleOwner){
            if(it == null){
                // 重置颜色
                val whiteWith80 = handleAlpha80Color(Color.WHITE)

                mBinding.root.setBackgroundColor(requireContext().getAttrColor(R.attr.colorPrimary))
                mBinding.tvMusicTitle.setTextColor(Color.WHITE)
                mBinding.tvMusicSubtitle.setTextColor(whiteWith80)
                mBinding.btnPauseResume.iconTint = ColorStateList.valueOf(Color.WHITE)
                mBinding.btnPauseNext.iconTint = ColorStateList.valueOf(Color.WHITE)
                mBinding.btnPausePrevious.iconTint = ColorStateList.valueOf(Color.WHITE)
                mBinding.seekbarProgress.thumbTintList = ColorStateList.valueOf(Color.WHITE)
                mBinding.tvDuration.setTextColor(Color.WHITE)
                mBinding.tvCurrentPosition.setTextColor(Color.WHITE)
            } else {
                val primaryColorWith80 = handleAlpha80Color(it.onPrimaryColor)

                mBinding.root.setBackgroundColor(it.primaryColor)
                mBinding.tvMusicTitle.setTextColor(it.onPrimaryColor)
                mBinding.tvMusicSubtitle.setTextColor(primaryColorWith80)
                mBinding.btnPauseResume.iconTint = ColorStateList.valueOf(it.onPrimaryColor)
                mBinding.btnPauseNext.iconTint = ColorStateList.valueOf(it.onPrimaryColor)
                mBinding.btnPausePrevious.iconTint = ColorStateList.valueOf(it.onPrimaryColor)
                mBinding.seekbarProgress.thumbTintList = ColorStateList.valueOf(it.onPrimaryColor)
                mBinding.tvDuration.setTextColor(it.onPrimaryColor)
                mBinding.tvCurrentPosition.setTextColor(it.onPrimaryColor)
            }
        }

    }

    private fun startUpdateSlider(){
        sliderJob = viewLifecycleOwner.lifecycleScope.launch {
            while (true){
                mBinding.seekbarProgress.value = mViewModel.getCurrentPosition().toFloat()
                delay(500L)
            }
        }
    }

    private fun handleAlpha80Color(color: Int): Int{
        return PaletteUtils.rgb2Argb(color, 80)
    }

    private fun updateCurrentMediaInfo(mediaItem: MediaItem?){
        mBinding.tvMusicTitle.text = mediaItem?.mediaMetadata?.title
        mBinding.tvMusicSubtitle.text = mediaItem?.mediaMetadata?.artist
        mBinding.ivAlbumArt.load(mediaItem?.mediaMetadata?.artworkUri)

        val duration = mediaItem?.mediaMetadata?.extras?.getLong(MediaItemTree.KEY_METADATA_DURATION) ?: 0L
        mBinding.tvDuration.text = DateUtils.formatElapsedTime(positionStringBuilder, duration / 1000L)
    }

    private fun updateCurrentPlayerState(isPlaying: Boolean){
        if(isPlaying){
            mBinding.btnPauseResume.setIconResource(R.drawable.fluid_music_player_pause)
        } else {
            mBinding.btnPauseResume.setIconResource(R.drawable.fluid_music_player_play)
        }
    }
}