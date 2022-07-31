package com.xiaoc.feature_fluid_music.service.ui.browser

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.xiaoc.feature_fluid_music.databinding.FluidMusicFragmentMusicBrowserListBinding
import com.xiaoc.feature_fluid_music.service.ui.browser.music.AllMusicListFragment
import com.zjl.base.fragment.BaseFragment
import com.zjl.base.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collectLatest

/**
 * @author Xiaoc
 * @since  2022-07-30
 *
 * 音乐浏览列表
 **/
class MusicBrowserFragment: BaseFragment<FluidMusicFragmentMusicBrowserListBinding, MusicBrowserViewModel>() {

    private lateinit var mediaTypeAdapter: MusicTypeTabViewPagerAdapter

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        mediaTypeAdapter = MusicTypeTabViewPagerAdapter(emptyList(), this@MusicBrowserFragment)
        mBinding.vpMediaType.adapter = mediaTypeAdapter

        // 与TabLayout关联
        TabLayoutMediator(mBinding.tabMediaType, mBinding.vpMediaType){ tab, position ->
            tab.text = mViewModel.mediaTypes.value[position].mediaMetadata.title
        }.attach()
    }

    override fun createObserver() {
        launchAndRepeatWithViewLifecycle {
            // 更新媒体分类类型的Tab栏与对应的Fragment
            mViewModel.mediaTypes.collectLatest {
                mediaTypeAdapter.mediaTypes = it
                mediaTypeAdapter.notifyDataSetChanged()
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

    private class MusicTypeTabViewPagerAdapter(
        var mediaTypes: List<MediaItem>,
        fragment: Fragment
    ): FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int {
            return mediaTypes.size
        }

        override fun createFragment(position: Int): Fragment {
            return AllMusicListFragment.newInstance(mediaTypes[position].mediaId)
        }
    }
}