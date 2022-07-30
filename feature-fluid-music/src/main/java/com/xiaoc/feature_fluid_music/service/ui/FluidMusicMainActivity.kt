package com.xiaoc.feature_fluid_music.service.ui

import android.Manifest
import android.os.Bundle
import android.view.View
import com.permissionx.guolindev.PermissionX
import com.xiaoc.feature_fluid_music.R
import com.xiaoc.feature_fluid_music.databinding.FluidMusicActivityMainBinding
import com.xiaoc.feature_fluid_music.service.ui.browser.MusicBrowserFragment
import com.zjl.base.activity.BaseActivity

/**
 * @author Xiaoc
 * @since  2022-07-30
 *
 * FluidMusic主页面
 **/
class FluidMusicMainActivity: BaseActivity<FluidMusicActivityMainBinding, FluidMusicMainViewModel>() {

    override fun initViewAndEvent(savedInstanceState: Bundle?) {
        // 判断是否读取外置存储权限
        if(PermissionX.isGranted(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            mBinding.tvPermission.visibility = View.GONE
            showMusicBrowser()
        } else {
            mBinding.tvPermission.visibility = View.VISIBLE
        }

        mBinding.tvPermission.setOnClickListener {
            requestReadStorage()
        }
    }

    override fun createObserver() {

    }

    private fun requestReadStorage(){
        PermissionX.init(this)
            .permissions(Manifest.permission.READ_EXTERNAL_STORAGE)
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(deniedList, getString(R.string.fluid_music_permission_go_to_setting), "确定", "取消")
            }
            .request { allGranted, _, _ ->
                if (allGranted) {
                    mBinding.tvPermission.visibility = View.GONE

                    // 显示媒体浏览界面
                    showMusicBrowser()

                } else {
                    mBinding.tvPermission.visibility = View.VISIBLE
                }
            }
    }

    private fun showMusicBrowser(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_music, MusicBrowserFragment())
            .commit()
    }
}