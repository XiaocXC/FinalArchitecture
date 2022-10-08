package com.xiaoc.feature_fluid_music.ui.browser.music

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.DiffUtil
import com.gyf.immersionbar.ImmersionBar
import com.xiaoc.feature_fluid_music.databinding.FluidMusicFragmentAllMusicListBinding
import com.xiaoc.feature_fluid_music.ui.bean.UIMediaData
import com.xiaoc.feature_fluid_music.ui.browser.music.adapter.MusicItemAdapter
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.ui.state.EmptyState
import com.zjl.base.utils.launchAndCollectIn
import com.zy.multistatepage.state.SuccessState

/**
 * @author Xiaoc
 * @since  2022-07-30
 *
 * 所有音乐 展示列表界面
 **/
class AllMusicListFragment: BaseFragment<FluidMusicFragmentAllMusicListBinding, AllMusicListViewModel>() {

    companion object {

        fun newInstance(parentId: String): AllMusicListFragment{
            return AllMusicListFragment().apply {
                arguments = bundleOf(
                    "parentId" to parentId
                )
            }
        }
    }

    private lateinit var musicItemAdapter: MusicItemAdapter

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        musicItemAdapter = MusicItemAdapter()
        musicItemAdapter.setDiffCallback(object: DiffUtil.ItemCallback<UIMediaData>() {
            override fun areItemsTheSame(oldItem: UIMediaData, newItem: UIMediaData): Boolean {
                return oldItem.mediaItem == newItem.mediaItem
            }

            override fun areContentsTheSame(oldItem: UIMediaData, newItem: UIMediaData): Boolean {
                return oldItem.isPlaying == newItem.isPlaying
            }

            override fun getChangePayload(oldItem: UIMediaData, newItem: UIMediaData): Any? {
                return newItem.isPlaying
            }
        })

        musicItemAdapter.setOnItemClickListener { _, _, position ->
            mViewModel.playByList(position)
        }

        mBinding.rvMusic.adapter = musicItemAdapter
    }

    override fun createObserver() {
        mViewModel.localAllMusicList.launchAndCollectIn(viewLifecycleOwner){
            if(it.isEmpty()){
                uiRootState.show(EmptyState())
                return@launchAndCollectIn
            }
            uiRootState.show(SuccessState())
            musicItemAdapter.setDiffNewData(it.toMutableList())
        }
    }

    override fun configImmersive(immersionBar: ImmersionBar): ImmersionBar? {
        // 内部Fragment不处理沉浸式，防止被覆盖
        return null
    }
}