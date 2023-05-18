package com.xiaoc.feature_fluid_music.ui

import android.Manifest
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import coil.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.ktx.immersionBar
import com.permissionx.guolindev.PermissionX
import com.xiaoc.feature_fluid_music.R
import com.xiaoc.feature_fluid_music.databinding.FluidMusicActivityMainBinding
import com.zjl.base.activity.BaseActivity
import com.zjl.base.utils.ext.getAttrColor
import com.zjl.base.utils.ext.isNightMode
import com.zjl.base.utils.launchAndCollectIn
import timber.log.Timber
import kotlin.math.min

/**
 * @author Xiaoc
 * @since  2022-07-30
 *
 * FluidMusic主页面
 **/
class FluidMusicMainActivity: BaseActivity<FluidMusicActivityMainBinding, FluidMusicMainViewModel>() {

    private lateinit var bottomPlayerBehavior: BottomSheetBehavior<View>

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        // 判断是否读取外置存储权限
        if(PermissionX.isGranted(this, getAudioStoragePermissionString())){
            mBinding.tvPermission.visibility = View.GONE
        } else {
            mBinding.tvPermission.visibility = View.VISIBLE
        }

        mBinding.tvPermission.setOnClickListener {
            requestReadStorage()
        }

        mBinding.layoutBottomPlayer.containerDetailPlayer.alpha = 0f

        bottomPlayerBehavior = BottomSheetBehavior.from(mBinding.layoutBottomPlayer.root)
        bottomPlayerBehavior.addBottomSheetCallback(object:
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if(newState == BottomSheetBehavior.STATE_COLLAPSED){
                    // 折叠后做一些UI处理
                    immersionBar {
                        transparentBar()
                        statusBarDarkFont(!resources.isNightMode())
                        navigationBarDarkIcon(!resources.isNightMode())
                    }
//                    mBinding.layoutBottomPlayer.containerDetailPlayer.visibility = View.GONE
                } else if(newState == BottomSheetBehavior.STATE_EXPANDED){
                    // 展开后做一些UI处理
                    immersionBar {
                        transparentBar()
                        statusBarDarkFont(!resources.isNightMode())
                        navigationBarDarkIcon(!resources.isNightMode())
                        statusBarDarkFont(false)
                        navigationBarDarkIcon(false)
                    }
                    // 折叠时我们把详情页的内容给隐藏掉，防止资源过度消耗
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Timber.i("滑动:$slideOffset")
                // 滑动到0.1左右后我们就隐藏掉底部的播放栏
                val alpha = 1 - min(slideOffset, 0.1f) / 0.1f
                val showAlpha = min(slideOffset, 0.1f) / 0.1f
                Timber.i("滑动透明度:$alpha")
                mBinding.layoutBottomPlayer.containerBottomPlayer.alpha = alpha
                mBinding.layoutBottomPlayer.containerDetailPlayer.alpha = showAlpha
            }

        })

        mBinding.layoutBottomPlayer.containerBottomPlayer.setOnClickListener {
            if(bottomPlayerBehavior.state == BottomSheetBehavior.STATE_EXPANDED){
                return@setOnClickListener
            }
            bottomPlayerBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        // 配置自定义返回
        onBackPressedDispatcher.addCallback {
            if(bottomPlayerBehavior.state == BottomSheetBehavior.STATE_EXPANDED){
                bottomPlayerBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            } else {
                finish()
            }
        }
    }

    override fun createObserver() {
        // 当前播放信息
        mViewModel.currentPlayInfo.launchAndCollectIn(this){ mediaItem ->
            mBinding.layoutBottomPlayer.tvArtistTitle.text = mediaItem?.mediaMetadata?.title
            mBinding.layoutBottomPlayer.ivAlbumArt.load(mediaItem?.mediaMetadata?.artworkUri)
        }

        // 播放状态
        mViewModel.currentPlaybackState.launchAndCollectIn(this){
            updateCurrentPlayerState(it.isPlaying)
        }

        // 颜色
        mViewModel.musicColor.launchAndCollectIn(this){
            if(it == null){
                mBinding.layoutBottomPlayer.containerBottomPlayer.setCardBackgroundColor(getAttrColor(R.attr.colorPrimary))
                mBinding.layoutBottomPlayer.tvArtistTitle.setTextColor(Color.WHITE)
                mBinding.layoutBottomPlayer.btnPauseResume.iconTint = ColorStateList.valueOf(Color.WHITE)
            } else {
                mBinding.layoutBottomPlayer.containerBottomPlayer.setCardBackgroundColor(it.primaryColor)
                mBinding.layoutBottomPlayer.tvArtistTitle.setTextColor(it.onPrimaryColor)
                mBinding.layoutBottomPlayer.btnPauseResume.iconTint = ColorStateList.valueOf(it.onPrimaryColor)
            }
        }
    }

    private fun updateCurrentPlayerState(isPlaying: Boolean){
        if(isPlaying){
            mBinding.layoutBottomPlayer.btnPauseResume.setIconResource(R.drawable.fluid_music_player_pause)
        } else {
            mBinding.layoutBottomPlayer.btnPauseResume.setIconResource(R.drawable.fluid_music_player_play)
        }
    }

    private fun getAudioStoragePermissionString(): String{
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            Manifest.permission.READ_MEDIA_AUDIO
        } else{
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }

    private fun requestReadStorage(){
        val permissionList = mutableListOf(getAudioStoragePermissionString())

        PermissionX.init(this)
            .permissions(permissionList)
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(deniedList, getString(R.string.fluid_music_permission_go_to_setting), "确定", "取消")
            }
            .request { allGranted, _, _ ->
                if (allGranted) {
                    mBinding.tvPermission.visibility = View.GONE

                } else {
                    mBinding.tvPermission.visibility = View.VISIBLE
                }
            }
    }
}