package com.xiaoc.feature_fluid_music.service.ui.browser.detail

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.media3.common.MediaItem
import coil.load
import com.xiaoc.feature_fluid_music.R
import com.xiaoc.feature_fluid_music.databinding.FluidMusicFragmentPlayerControlBinding
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @author Xiaoc
 * @since  2022-09-02
 **/
class PlayerControlFragment: BaseFragment<FluidMusicFragmentPlayerControlBinding, PlayerDetailViewModel>() {

    override fun createViewModel(): PlayerDetailViewModel {
        val viewModel by viewModels<PlayerDetailViewModel>(ownerProducer = {
            parentFragment ?: this
        })
        return viewModel
    }

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        mBinding.btnPauseResume.setOnClickListener {
            mViewModel.pauseOrResume()
        }
    }

    override fun createObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                mViewModel.currentPlayInfo.collectLatest {
                    updateCurrentMediaInfo(it)
                }
            }

            launch {
                mViewModel.currentIsPlaying.collectLatest {
                    updateCurrentPlayerState(it)
                }
            }
        }

    }

    private fun updateCurrentMediaInfo(mediaItem: MediaItem?){
        mBinding.tvMusicTitle.text = mediaItem?.mediaMetadata?.title
        mBinding.tvMusicSubtitle.text = mediaItem?.mediaMetadata?.subtitle
        mBinding.ivAlbumArt.load(mediaItem?.mediaMetadata?.artworkUri)
    }

    private fun updateCurrentPlayerState(isPlaying: Boolean){
        if(isPlaying){
            mBinding.btnPauseResume.setIconResource(R.drawable.fluid_music_player_pause)
        } else {
            mBinding.btnPauseResume.setIconResource(R.drawable.fluid_music_player_play)
        }
    }

    override fun onStart() {
        super.onStart()
        mViewModel.initializeBrowser(requireContext())
    }

    override fun onStop() {
        super.onStop()
        mViewModel.releaseBrowser()
    }
}