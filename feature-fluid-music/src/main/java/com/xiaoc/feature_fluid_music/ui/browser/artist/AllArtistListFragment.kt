package com.xiaoc.feature_fluid_music.ui.browser.artist

import android.os.Bundle
import androidx.core.os.bundleOf
import com.gyf.immersionbar.ImmersionBar
import com.xiaoc.feature_fluid_music.databinding.FluidMusicFragmentAllArtistListBinding
import com.xiaoc.feature_fluid_music.ui.browser.artist.adapter.ArtistItemAdapter
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.ui.state.EmptyState
import com.zjl.base.utils.launchAndCollectIn
import com.zy.multistatepage.state.SuccessState

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
        mViewModel.localAllArtistList.launchAndCollectIn(viewLifecycleOwner){
            if(it.isEmpty()){
                uiRootState.show(EmptyState())
                return@launchAndCollectIn
            }
            uiRootState.show(SuccessState())
            artistItemAdapter.setList(it)
        }
    }

    override fun configImmersive(immersionBar: ImmersionBar): ImmersionBar? {
        // 内部Fragment不处理沉浸式，防止被覆盖
        return null
    }
}