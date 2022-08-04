package com.xiaoc.feature_fluid_music.service.ui.browser.album

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.DiffUtil
import com.xiaoc.feature_fluid_music.databinding.FluidMusicFragmentAllAlbumListBinding
import com.xiaoc.feature_fluid_music.service.bean.UIMediaData
import com.xiaoc.feature_fluid_music.service.ui.browser.album.adapter.AlbumItemAdapter
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
        launchAndRepeatWithViewLifecycle {
            launch {
                mViewModel.localAllAlbumList.collectLatest {
                    if(it.isEmpty()){
                        uiRootState.show(EmptyState())
                        return@collectLatest
                    }
                    uiRootState.show(SuccessState())
                    albumItemAdapter.setList(it)
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