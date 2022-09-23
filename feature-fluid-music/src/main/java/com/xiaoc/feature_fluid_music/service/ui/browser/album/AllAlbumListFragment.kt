package com.xiaoc.feature_fluid_music.service.ui.browser.album

import android.os.Bundle
import androidx.core.os.bundleOf
import com.xiaoc.feature_fluid_music.databinding.FluidMusicFragmentAllAlbumListBinding
import com.xiaoc.feature_fluid_music.service.ui.browser.album.adapter.AlbumItemAdapter
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.ui.state.EmptyState
import com.zjl.base.utils.launchAndCollectIn
import com.zy.multistatepage.state.SuccessState

/**
 * @author Xiaoc
 * @since  2022-07-30
 *
 * 所有专辑 展示列表界面
 **/
class AllAlbumListFragment: BaseFragment<FluidMusicFragmentAllAlbumListBinding, AllAlbumListViewModel>() {

    companion object {

        fun newInstance(parentId: String): AllAlbumListFragment{
            return AllAlbumListFragment().apply {
                arguments = bundleOf(
                    "parentId" to parentId
                )
            }
        }
    }

    private lateinit var albumItemAdapter: AlbumItemAdapter

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        albumItemAdapter = AlbumItemAdapter()

        albumItemAdapter.setOnItemClickListener { _, _, _ ->
            // TODO 进入专辑详情
        }

        mBinding.rvAlbum.adapter = albumItemAdapter
    }

    override fun createObserver() {
        mViewModel.localAllAlbumList.launchAndCollectIn(viewLifecycleOwner){
            if(it.isEmpty()){
                uiRootState.show(EmptyState())
                return@launchAndCollectIn
            }
            uiRootState.show(SuccessState())
            albumItemAdapter.setList(it)
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