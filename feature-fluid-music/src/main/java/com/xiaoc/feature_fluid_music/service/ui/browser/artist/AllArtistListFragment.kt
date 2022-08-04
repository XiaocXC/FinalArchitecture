package com.xiaoc.feature_fluid_music.service.ui.browser.artist

import android.os.Bundle
import androidx.core.os.bundleOf
import com.xiaoc.feature_fluid_music.databinding.FluidMusicFragmentAllArtistListBinding
import com.xiaoc.feature_fluid_music.service.ui.browser.artist.adapter.ArtistItemAdapter
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.ui.state.EmptyState
import com.zjl.base.utils.launchAndRepeatWithViewLifecycle
import com.zy.multistatepage.state.SuccessState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @author Xiaoc
 * @since  2022-07-30
 *
 * 所有歌手 展示列表界面
 **/
class AllArtistListFragment: BaseFragment<FluidMusicFragmentAllArtistListBinding, AllArtistListViewModel>() {

    companion object {

        fun newInstance(parentId: String): AllArtistListFragment{
            return AllArtistListFragment().apply {
                arguments = bundleOf(
                    "parentId" to parentId
                )
            }
        }
    }

    private lateinit var artistItemAdapter: ArtistItemAdapter

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        artistItemAdapter = ArtistItemAdapter()

        artistItemAdapter.setOnItemClickListener { _, _, _ ->
            // TODO 进入歌手详情
        }

        mBinding.rvArtist.adapter = artistItemAdapter
    }

    override fun createObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                mViewModel.localAllArtistList.collectLatest {
                    if(it.isEmpty()){
                        uiRootState.show(EmptyState())
                        return@collectLatest
                    }
                    uiRootState.show(SuccessState())
                    artistItemAdapter.setList(it)
                }
            }
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